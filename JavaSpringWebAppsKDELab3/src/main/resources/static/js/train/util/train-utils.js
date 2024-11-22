import {displayError} from "../../util/error/error-handler.js";

export function validateTrainNumber(number) {
    if (!number) {
        displayError('trainNumber', 'The number cannot be empty');
        return false;
    }

    number = number.trim().toUpperCase();

    let numericPart = number.replaceAll(/[^0-9]/g, "");

    while (numericPart.length < 3) {
        number = "0" + number;
        numericPart = "0" + numericPart;
    }

    const NUMBER_REGEX = /^((?:00[1-9]|0[1-9][0-9]|1[0-4][0-9]|150|1(?:5[1-9]|[6-9][0-9])|2[0-8][0-9]|29[0-8]|30[1-9]|3[1-9][0-9]|4[0-4][0-9]|450|4(?:5[1-9]|[6-9][0-9])|5[0-8][0-9]|59[0-8]|60[1-9]|6[1-9][0-9]|70[1-9]|7[1-4][0-9]|750|75[1-9]|7[6-8][0-8])(?:ІС[+]?|РЕ|Р|НЕ|НШ|НП))$/;
    if (!number.match(NUMBER_REGEX)) {
        displayError('trainNumber', 'The number should consist of 1 to 3 digits and {ІС+, ІС, РЕ, P, НЕ, НШ, НП}');
        return false;
    }

    return true;
}

export function validateStationName(station, elementId) {
    const STATION_REGEX = /^(([А-ЗЙ-ЩЮЯЇІЄ][А-ЩЮЯЇІЄҐ]+|[А-ЗЙ-ЩЮЯЇІЄ][а-щьюяїієґ\']+[.]?)(?:[ -](?:[А-ЗЙ-ЩЮЯЇІЄ][А-ЩЮЯЇІЄҐ]+[0-9]*|[А-ЗЙ-ЩЮЯЇІЄ]?[а-щьюяїієґ\']+[.|0-9]*|[0-9]+)|[(][А-ЗЙ-ЩЮЯЇІЄ]?[а-щьюяїієґ\']+[.]?[)])*){1,100}$/;
    if (!station) {
        displayError(elementId, 'Station name cannot be empty');
        return false;
    }

    station = station.trim();

    if (station.charAt(0).toLowerCase() === station.charAt(0)) {
        station = station.charAt(0).toUpperCase() + station.slice(1);
    }

    if (!STATION_REGEX.test(station)) {
        displayError(elementId, 'Station name can include ukrainian letters, spaces, hyphens, numbers, and parentheses.');
        return false;
    }

    return true;
}

export function calculateArrivalTime(departureTime, duration) {
    const [hours, minutes] = departureTime.split(':').map(Number);
    const durationHours = duration.hours;
    const durationMinutes = duration.minutes;
    const totalMinutes = durationHours * 60 + durationMinutes;

    const totalDepartureMinutes = hours * 60 + minutes + totalMinutes;
    const arrivalHours = Math.floor(totalDepartureMinutes / 60) % 24;
    const arrivalMinutes = totalDepartureMinutes % 60;

    return `${String(arrivalHours).padStart(2, '0')}:${String(arrivalMinutes).padStart(2, '0')}`;
}

