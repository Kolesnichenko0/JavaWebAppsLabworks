import { clearErrors, displayError, displayFormError, formatErrorMessage, validateTrainNumber, validateStationName } from './util/train-utils.js';
document.addEventListener('DOMContentLoaded', () => {
    const contextPath = document.querySelector('meta[name="context-path"]').getAttribute('content');
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

        const trainData = {
            number: trainNumber,
            departureStation: departureStation,
            arrivalStation: arrivalStation,
            movementType: movementType,
            departureTime: departureTime,
            duration: totalMinutes
        };

        fetch(`${contextPath}/trains`, {
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
                        window.location.href = locationHeader;
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

                if (response.status === 404) {
                    return response.text().then(html => {
                        console.error(errorMessage);
                        document.body.innerHTML = html;
                    });
                }

                if (response.status === 500) {
                    return response.text().then(html => {
                        console.error(errorMessage);
                        document.body.innerHTML = html;
                    });
                }

                throw new Error(`Unexpected response status: ${response.status}`);
            })
            .catch(error => {
                console.error('Error fetching trains:', error);
            });
    });

    cancelBtn.addEventListener('click', () => {
        const previousPage = document.referrer;
        if (previousPage) {
            window.location.href = previousPage;
        } else {
            window.location.href = `${contextPath}/trains`;
        }
    });
});