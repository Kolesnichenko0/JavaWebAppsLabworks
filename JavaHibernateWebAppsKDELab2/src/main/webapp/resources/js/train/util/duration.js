export function updateDurationLabels() {
    const minDuration = document.getElementById('minDuration').value;
    const maxDuration = document.getElementById('maxDuration').value;
    const minHours = Math.floor(minDuration / 60);
    const minMinutes = minDuration % 60;
    const maxHours = Math.floor(maxDuration / 60);
    const maxMinutes = maxDuration % 60;
    document.getElementById('durationLabels').textContent = `${minHours}h ${minMinutes}m - ${maxHours}h ${maxMinutes}m`;
}