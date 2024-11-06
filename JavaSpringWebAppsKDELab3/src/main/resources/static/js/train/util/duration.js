export function updateDurationLabels() {
    const minDuration = document.getElementById('minDuration').value;
    const maxDuration = document.getElementById('maxDuration').value;
    const minHours = Math.floor(minDuration / 60);
    const minMinutes = minDuration % 60;
    const maxHours = Math.floor(maxDuration / 60);
    const maxMinutes = maxDuration % 60;
    document.getElementById('durationLabels').textContent = `${minHours}h ${minMinutes}m - ${maxHours}h ${maxMinutes}m`;
}

export function parseDuration(duration) {
    const match = duration.match(/PT(\d+H)?(\d+M)?/);
    const hours = match[1] ? parseInt(match[1].replace('H', '')) : 0;
    const minutes = match[2] ? parseInt(match[2].replace('M', '')) : 0;
    return {hours, minutes};
}

export function formatDuration(duration) {
    return `${duration.hours}год. ${duration.minutes}хв.`;
}