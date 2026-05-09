let ctx, p_ctx, X_CANVAS, Y_CANVAS;

document.addEventListener('DOMContentLoaded', function () {
    const graphCanvas = document.getElementById('graph-canvas');
    const pointsCanvas = document.getElementById('points-canvas');
    if (!graphCanvas || !pointsCanvas) return;

    ctx = graphCanvas.getContext('2d');
    p_ctx = pointsCanvas.getContext('2d');
    X_CANVAS = graphCanvas.width;
    Y_CANVAS = graphCanvas.height;

    graphCanvas.addEventListener('click', handleCanvasClick);

    drawGraph();
});


window.drawGraph = function () {
    if (!ctx) return;

    const rRadio = document.querySelector('input[name="main-form:r-radio"]:checked');
    if (!rRadio) return;

    const R = parseFloat(rRadio.value);

    const statusElement = document.getElementById('main-form:status');
    if (statusElement) {
        statusElement.value = "";
    }
    hideNotification();

    ctx.clearRect(0, 0, X_CANVAS, Y_CANVAS);
    const scale = (X_CANVAS / 2) * 0.8;
    const r_scaled = (scale / 5) * R;

    ctx.fillStyle = 'rgba(95, 158, 160, 0.5)';
    ctx.strokeStyle = '#333';
    ctx.lineWidth = 1;

    ctx.beginPath();
    ctx.rect(X_CANVAS / 2, Y_CANVAS / 2 - r_scaled, r_scaled, r_scaled);
    ctx.closePath();
    ctx.fill();

    // треугольник
    ctx.beginPath();
    ctx.moveTo(X_CANVAS / 2, Y_CANVAS / 2);
    ctx.lineTo(X_CANVAS / 2 - r_scaled, Y_CANVAS / 2);
    ctx.lineTo(X_CANVAS / 2, Y_CANVAS / 2 + r_scaled / 2);
    ctx.closePath();
    ctx.fill();

    // четверть круга
    ctx.beginPath();
    ctx.moveTo(X_CANVAS / 2, Y_CANVAS / 2);
    ctx.arc(X_CANVAS / 2, Y_CANVAS / 2, r_scaled / 2, 0, Math.PI / 2);
    ctx.closePath();
    ctx.fill();

    ctx.strokeStyle = '#000';
    ctx.lineWidth = 2;
    ctx.beginPath();
    ctx.moveTo(0, Y_CANVAS / 2);
    ctx.lineTo(X_CANVAS, Y_CANVAS / 2);
    ctx.moveTo(X_CANVAS / 2, 0);
    ctx.lineTo(X_CANVAS / 2, Y_CANVAS);
    ctx.stroke();

    ctx.font = '12px Arial';
    ctx.fillStyle = '#000';
    ctx.textAlign = 'center';

    ctx.fillText('-R', X_CANVAS / 2 - r_scaled, Y_CANVAS / 2 + 15);
    ctx.fillText('-R/2', X_CANVAS / 2 - r_scaled / 2, Y_CANVAS / 2 + 15);
    ctx.fillText('R/2', X_CANVAS / 2 + r_scaled / 2, Y_CANVAS / 2 + 15);
    ctx.fillText('R', X_CANVAS / 2 + r_scaled, Y_CANVAS / 2 + 15);

    ctx.textAlign = 'right';
    ctx.fillText('R', X_CANVAS / 2 - 10, Y_CANVAS / 2 - r_scaled);
    ctx.fillText('R/2', X_CANVAS / 2 - 10, Y_CANVAS / 2 - r_scaled / 2);
    ctx.fillText('-R/2', X_CANVAS / 2 - 10, Y_CANVAS / 2 + r_scaled / 2);
    ctx.fillText('-R', X_CANVAS / 2 - 10, Y_CANVAS / 2 + r_scaled);

    ctx.fillText('0', X_CANVAS / 2 - 10, Y_CANVAS / 2 + 15);

    drawNewPoint();
};



window.drawPoint = function (x, y, r, hit) {
    if (!p_ctx) return;

    const scale = (X_CANVAS / 2) * 0.8;
    const plotX = (x / 5) * scale + X_CANVAS / 2;
    const plotY = (-y / 5) * scale + Y_CANVAS / 2;

    p_ctx.beginPath();
    p_ctx.arc(plotX, plotY, 4, 0, 2 * Math.PI);
    p_ctx.fillStyle = hit ? 'green' : 'red';
    p_ctx.fill();
};

window.drawNewPoint = function () {
    if (!p_ctx) return;
    p_ctx.clearRect(0, 0, X_CANVAS, Y_CANVAS);

    const statusElement = document.getElementById('main-form:status');
    if (!statusElement) return;

    const status = statusElement.value;

    if (status && status !== 'Success') {
        showNotification(status);
        return;
    }

    const x = parseFloat(document.getElementById('main-form:lastX').value);
    const y = parseFloat(document.getElementById('main-form:lastY').value);
    const r = parseFloat(document.getElementById('main-form:lastR').value);
    const hit = document.getElementById('main-form:lastHit').value === 'true';

    if (isNaN(x) || isNaN(y) || isNaN(r)) return;

    drawPoint(x, y, r, hit);
};


window.showNotification = function (message) {
    const container = document.getElementById('notification-container');
    const messageElement = document.getElementById('notification-message');
    if (container && messageElement) {
        messageElement.textContent = message;
        container.style.display = 'flex';
    } else {
        console.error('Notification elements not found');
    }
};

window.hideNotification = function () {
    const container = document.getElementById('notification-container');
    if (container) {
        container.style.display = 'none';
    }
};

window.handleCanvasClick = function (event) {
    const rRadio = document.querySelector('input[name="main-form:r-radio"]:checked');
    if (!rRadio) {
        showNotification('Пожалуйста, выберите значение R.');
        return;
    }

    const R = parseFloat(rRadio.value);
    const rect = event.target.getBoundingClientRect();
    const clickX = event.clientX - rect.left;
    const clickY = event.clientY - rect.top;

    const scale = (X_CANVAS / 2) * 0.8;
    const xValue = ((clickX - X_CANVAS / 2) / scale) * 5;
    const yValue = (-(clickY - Y_CANVAS / 2) / scale) * 5;

    const yInput = document.getElementById('main-form:y-input');
    const xSpinner = document.getElementById('main-form:x-spinner_input');
    if (xSpinner && yInput) {
        xSpinner.value = xValue.toFixed(3);
        yInput.value = yValue.toFixed(3);
    }

    document.getElementById('main-form:submit-button').click();
};

if (window.jsf && jsf.ajax) {
    jsf.ajax.addOnEvent(function (data) {
        if (data.status === 'success') {
            setTimeout(drawNewPoint, 50);
        }
    });
}