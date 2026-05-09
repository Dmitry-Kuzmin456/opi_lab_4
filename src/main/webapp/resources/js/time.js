document.addEventListener('DOMContentLoaded', function() {

    function updateTime() {
        const timeElement = document.getElementById('clock');
        if (timeElement) {
            const now = new Date();
            const timeString = now.toLocaleDateString('ru-RU', {
                day: '2-digit',
                month: '2-digit',
                year: 'numeric'
            }) + ' ' + now.toLocaleTimeString('ru-RU');

            timeElement.textContent = timeString;
        }
    }

    updateTime();

    setInterval(updateTime, 11000);
});
