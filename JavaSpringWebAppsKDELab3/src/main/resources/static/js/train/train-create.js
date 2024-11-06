import {
    validateStationName,
    validateTrainNumber
} from './util/train-utils.js';

import {
    handleError,
    clearErrors,
    displayError,
    displayFormError,
    formatErrorMessage
} from '../util/error/error-handler.js';

document.addEventListener('DOMContentLoaded', () => {
    const contextPath = document.querySelector('base').href.replace(/\/$/, '');
    const createTrainForm = document.getElementById('createTrainForm');
    const cancelBtn = document.getElementById('cancelBtn');

    createTrainForm.addEventListener('submit', (event) => {
        event.preventDefault();
        clearErrors();

        const trainNumber = document.getElementById('trainNumber').value;
        const departureStation = document.getElementById('departureStation').value;
        const arrivalStation = document.getElementById('arrivalStation').value;
        const departureTime = document.getElementById('departureTime').value;
        const movementType = document.getElementById('movementType').value;

        if (!validateTrainNumber(trainNumber) || !validateStationName(departureStation, 'departureStation') || !validateStationName(arrivalStation, 'arrivalStation')) {
            return;
        }

        const durationHours = document.getElementById('durationHours').value;
        const durationMinutes = document.getElementById('durationMinutes').value;
        const totalMinutes = (parseInt(durationHours) * 60) + parseInt(durationMinutes);

        if (totalMinutes > 1440) {
            displayError('durationHours', 'Duration cannot exceed 24 hours.');
            return;
        }

        const duration = `PT${durationHours}H${durationMinutes}M`;

        const trainData = {
            number: trainNumber,
            departureStation: departureStation,
            arrivalStation: arrivalStation,
            movementType: movementType,
            departureTime: departureTime,
            duration: duration
        };

        fetch(`${contextPath}/api/trains`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(trainData)
        })
            .then(response => {
                const errorMessage = response.headers.get('Error-Message');
                const successMessage = response.headers.get('Success-Message');
                const locationHeader = response.headers.get('Location');

                if (response.status === 201) {
                    console.log(successMessage);
                    if (locationHeader) {
                        window.location.href = locationHeader.replace('/api', '');
                    } else {
                        window.location.href = `${contextPath}/trains`;
                    }
                    return;
                }

                if (response.status === 422) {
                    displayFormError(formatErrorMessage(errorMessage));
                    return;
                }

                if (response.status === 409) {
                    displayFormError(formatErrorMessage(errorMessage));
                    return;
                }

                if (response.status === 400) {
                    console.error(errorMessage);
                    return;
                }

                if (response.status === 500) {
                    handleError(contextPath, errorMessage, response.status);
                    return;
                }

                throw new Error(`Unexpected response status: ${response.status}`);
            })
            .catch(error => {
                console.error('Error fetching trains:', error);
            });
    });

    cancelBtn.addEventListener('click', () => {
        window.location.href = `${contextPath}/trains`;
    });
});