import {displayError} from '../../util/error/error-handler.js';

export function validateCarriageNumber(carriage, elementId) {
    if (!carriage) {
        displayError(elementId, 'Please enter carriage number');
        return false;
    }

    if (carriage < 1) {
        displayError(elementId, 'Carriage number must be greater than or equal to 1');
        return false;
    }

    return true;
}

export function validateSeatNumber(seat, elementId) {
    if (!seat) {
        displayError(elementId, 'Please enter seat number');
        return false;
    }

    if (seat < 1) {
        displayError(elementId, 'Seat number must be greater than or equal to 1');
        return false;
    }

    return true;
}

export function validatePassengerSurname(surname, elementId) {
    const PASSENGER_SURNAME_REGEX = /^[А-ЗЙ-ЩЮЯЇІЄҐ]?[а-щьюяїієґ\']{2,}(-[А-ЗЙ-ЩЮЯЇІЄҐ]?[а-щьюяїієґ']{2,})?$/;
    if (!surname) {
        displayError(elementId, 'Please enter passenger surname');
        return false;
    }
    if (!PASSENGER_SURNAME_REGEX.test(surname)) {
        displayError(elementId, 'Passenger surname must be in Ukrainian and may include a hyphen for double surnames and must be at least 3 letters long');
        return false;
    }
    return true;
}

export function validatePassportNumber(passport, elementId) {
    const PASSPORT_NUMBER_REGEX = /^([А-ЗЙ-ЩЮЯЇІЄҐ]{2}[0-9]{6}|[0-9]{9})$/;
    if (!passport) {
        displayError(elementId, 'Please enter passport number');
        return false;
    }
    if (!PASSPORT_NUMBER_REGEX.test(passport)) {
        displayError(elementId, 'Passport number must be either 9 digits (ID-card) or 2 Ukrainian letters followed by 6 digits');
        return false;
    }
    return true;
}

export async function validateDepartureDate(departureDate, trainNumber, contextPath) {
    return fetch(`${contextPath}/api/trains?qNumber=${trainNumber}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            if (data.length === 0) {
                displayError('trainNumber', 'Train not found');
                return false;
            }

            const train = data[0];
            const currentDate = new Date();
            const departureDateObj = new Date(departureDate);

            if (departureDateObj < currentDate) {
                displayError('departureDate', 'Departure date cannot be in the past');
                return false;
            }

            const trainDepartureTime = new Date();
            const [hours, minutes] = train.departureTime.split(':');
            trainDepartureTime.setHours(hours, minutes, 0, 0);

            if (departureDateObj.toDateString() === currentDate.toDateString() && currentDate > trainDepartureTime) {
                displayError('departureDate', 'Cannot create ticket for a train that has already departed today');
                return false;
            }

            const movementType = train.movementType;
            const dayOfMonth = departureDateObj.getDate();

            if (movementType === 'ODD_DAYS' && dayOfMonth % 2 === 0) {
                displayError('departureDate', 'Cannot create ticket for a train that only runs on odd days');
                return false;
            }

            if (movementType === 'EVEN_DAYS' && dayOfMonth % 2 !== 0) {
                displayError('departureDate', 'Cannot create ticket for a train that only runs on even days');
                return false;
            }

            return true;
        })
        .catch(error => {
            console.error('Error fetching train details:', error);
            displayError('trainNumber', 'Error fetching train details');
            return false;
        });
}