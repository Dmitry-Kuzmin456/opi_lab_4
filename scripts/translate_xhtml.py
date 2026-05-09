#!/usr/bin/env python3
import json
import re
import sys
import urllib.parse
import urllib.error
import urllib.request
from pathlib import Path


LIBRE_API_URL = "https://libretranslate.de/translate"
MYMEMORY_API_URL = "https://api.mymemory.translated.net/get"
GOOGLE_API_URL = "https://translate.googleapis.com/translate_a/single"
ATTR_NAMES = ("value", "placeholder", "headerText", "emptyMessage", "title")


def detect_source_lang(text: str) -> str:
    if re.search(r"[А-Яа-яЁё]", text):
        return "ru"
    return "en"


def looks_like_api_error(text: str) -> bool:
    upper = text.upper()
    return (
        "INVALID SOURCE LANGUAGE" in upper
        or "LANGPAIR" in upper
        or "MYMEMORY" in upper and "ERROR" in upper
    )


def should_translate(text: str) -> bool:
    stripped = text.strip()
    if not stripped:
        return False
    if re.fullmatch(r"[XYZRxyzr]:?", stripped):
        return False
    if "#{" in stripped or "${" in stripped:
        return False
    if stripped.startswith("http://") or stripped.startswith("https://"):
        return False
    return bool(re.search(r"[A-Za-zА-Яа-я]", stripped))


def _translate_with_libre(text: str, target_lang: str) -> str:
    payload = json.dumps(
        {
            "q": text,
            "source": "auto",
            "target": target_lang,
            "format": "text",
        }
    ).encode("utf-8")
    request = urllib.request.Request(
        LIBRE_API_URL,
        data=payload,
        headers={"Content-Type": "application/json"},
        method="POST",
    )
    with urllib.request.urlopen(request, timeout=15) as response:
        data = json.loads(response.read().decode("utf-8"))
        translated = data.get("translatedText", "")
        if not translated:
            raise RuntimeError("Empty translation from LibreTranslate")
        return translated


def _translate_with_mymemory(text: str, target_lang: str) -> str:
    source_lang = detect_source_lang(text)
    query = urllib.parse.urlencode({"q": text, "langpair": f"{source_lang}|{target_lang}"})
    url = f"{MYMEMORY_API_URL}?{query}"
    with urllib.request.urlopen(url, timeout=15) as response:
        data = json.loads(response.read().decode("utf-8"))
        translated = data.get("responseData", {}).get("translatedText", "")
        if not translated or looks_like_api_error(translated):
            raise RuntimeError("Empty translation from MyMemory")
        return translated


def _translate_with_google(text: str, target_lang: str) -> str:
    query = urllib.parse.urlencode(
        {
            "client": "gtx",
            "sl": "auto",
            "tl": target_lang,
            "dt": "t",
            "q": text,
        }
    )
    url = f"{GOOGLE_API_URL}?{query}"
    with urllib.request.urlopen(url, timeout=15) as response:
        data = json.loads(response.read().decode("utf-8"))
        # Format: [[["translated","source",...],...],...]
        chunks = data[0]
        translated = "".join(part[0] for part in chunks if part and part[0])
        if not translated:
            raise RuntimeError("Empty translation from Google endpoint")
        return translated


def translate_text(text: str, target_lang: str, cache: dict[str, str]) -> str:
    if text in cache:
        return cache[text]
    try:
        translated = _translate_with_google(text, target_lang)
    except (urllib.error.URLError, TimeoutError, json.JSONDecodeError, RuntimeError):
        try:
            translated = _translate_with_libre(text, target_lang)
        except (urllib.error.URLError, TimeoutError, json.JSONDecodeError, RuntimeError):
            translated = _translate_with_mymemory(text, target_lang)

    if looks_like_api_error(translated):
        raise RuntimeError("Translation API returned error text")
    cache[text] = translated
    return translated


def replace_text_nodes(
    content: str, target_lang: str, cache: dict[str, str], stats: dict[str, int]
) -> str:
    def repl(match: re.Match[str]) -> str:
        original = match.group(1)
        if not should_translate(original):
            return f">{original}<"
        translated = translate_text(original.strip(), target_lang, cache)
        if translated != original.strip():
            stats["changed"] += 1
        return f">{translated}<"

    return re.sub(r">([^<>]+)<", repl, content)


def replace_attr_values(
    content: str, target_lang: str, cache: dict[str, str], stats: dict[str, int]
) -> str:
    attr_pattern = "|".join(ATTR_NAMES)

    def repl(match: re.Match[str]) -> str:
        attr = match.group(1)
        value = match.group(2)
        if not should_translate(value):
            return match.group(0)
        translated = translate_text(value, target_lang, cache)
        if translated != value:
            stats["changed"] += 1
        return f'{attr}="{translated}"'

    return re.sub(rf"\b({attr_pattern})=\"([^\"]+)\"", repl, content)


def main() -> int:
    if len(sys.argv) != 3:
        print("Usage: translate_xhtml.py <faces_dir> <target_lang>")
        return 1

    faces_dir = Path(sys.argv[1])
    target_lang = sys.argv[2].strip().lower()
    if not faces_dir.exists():
        print(f"Faces directory not found: {faces_dir}")
        return 1
    if not target_lang:
        print("Target language is empty.")
        return 1

    cache: dict[str, str] = {}
    files = sorted(faces_dir.glob("*.xhtml"))
    stats = {"changed": 0}
    transformed: list[tuple[Path, str]] = []

    for file_path in files:
        content = file_path.read_text(encoding="utf-8")
        if "PLEASE SELECT TWO DISTINCT LANGUAGES" in content:
            raise RuntimeError(
                f"Corrupted source text detected in {file_path}. Revert pages before translating."
            )
        content = replace_text_nodes(content, target_lang, cache, stats)
        content = replace_attr_values(content, target_lang, cache, stats)
        transformed.append((file_path, content))
        print(f"Processed: {file_path}")

    if stats["changed"] == 0:
        if target_lang in {"ru", "ru-ru"}:
            print("No changes needed: pages are already in Russian.")
            return 0
        print(
            "No text was translated. Check API availability or choose another target language.",
            file=sys.stderr,
        )
        return 2

    for file_path, content in transformed:
        file_path.write_text(content, encoding="utf-8")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
