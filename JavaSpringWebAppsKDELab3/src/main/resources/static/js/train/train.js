import {
    validateTrainNumber,
    validateStationName,
    calculateArrivalTime
} from './util/train-utils.js';

import {
    handleError,
    clearErrors,
    displayError,
    displayFormError,
    formatErrorMessage
} from '../util/error/error-handler.js';

import {parseDuration, formatDuration} from "./util/duration.js";

document.addEventListener('DOMContentLoaded', () => {
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    const contextPath = document.querySelector('base').href.replace(/\/$/, '');
    const trainId = window.location.pathname.split('/').pop();
    const updateTrainForm = document.getElementById('updateTrainForm');
    const deleteTrainBtn = document.getElementById('deleteTrainBtn');
    const viewTicketsBtn = document.getElementById('viewTicketsBtn');
    const cancelBtn = document.getElementById('cancelBtn');
    const deleteError = document.getElementById('deleteError');
    const movementTypeTranslations = {};
    document.querySelectorAll('#movementTypeEnum option').forEach(option => {
        movementTypeTranslations[option.value] = option.textContent;
    });

    function fetchTrainDetails() {
        fetch(`${contextPath}/api/trains/${trainId}`, {
            headers: {
                'Accept': 'application/json'
            }
        })
            .then(response => {
                const errorMessage = response.headers.get('Error-Message');
                const successMessage = response.headers.get('Success-Message');

                if (response.ok) {
                    return response.json().then(data => {
                        console.log(successMessage);
                        populateTrainDetails(data);
                    });
                }

                if (response.status === 404 || response.status === 500) {
                    handleError(contextPath, errorMessage, response.status);
                    return;
                }

                throw new Error(`Unexpected response status: ${response.status}`);
            })
            .catch(error => {
                console.error('Error fetching train details:', error);
            });
    }

    function populateTrainDetails(data) {
        const duration = parseDuration(data.duration);
        const movementTypeUkr = movementTypeTranslations[data.movementType] || data.movementType;

        const [hours, minutes] = data.departureTime.split(':');
        const formattedDepartureTime = `${hours}:${minutes}`;

        document.getElementById('trainDetails').innerHTML = `
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">Train Number: ${data.number}</h5>
                    <p class="card-text"><strong>Departure Station:</strong> ${data.departureStation}</p>
                    <p class="card-text"><strong>Arrival Station:</strong> ${data.arrivalStation}</p>
                    <p class="card-text"><strong>Movement Type:</strong> ${movementTypeUkr}</p>
                    <p class="card-text"><strong>Departure Time:</strong> ${formattedDepartureTime}</p>
                    <p class="card-text"><strong>Arrival Time:</strong> ${calculateArrivalTime(data.departureTime, duration)}</p>
                    <p class="card-text"><strong>Duration:</strong> ${formatDuration(duration)}</p>
                </div>
            </div>
        `;
        if (document.getElementById('trainNumber')) {
            document.getElementById('trainNumber').value = data.number;
            document.getElementById('departureStation').value = data.departureStation;
            document.getElementById('arrivalStation').value = data.arrivalStation;
            document.getElementById('movementType').value = data.movementType;
            document.getElementById('departureTime').value = data.departureTime;
            document.getElementById('durationHours').value = duration.hours;
            document.getElementById('durationMinutes').value = duration.minutes;
        }
    }
    if (updateTrainForm) {
        updateTrainForm.addEventListener('submit', (event) => {
            if (confirm('Are you sure you want to update this train?')) {
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

                fetch(`${contextPath}/api/trains/${trainId}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                        [csrfHeader]: csrfToken
                    },
                    body: JSON.stringify(trainData)
                })
                    .then(response => {
                        const errorMessage = response.headers.get('Error-Message');
                        const successMessage = response.headers.get('Success-Message');
                        const locationHeader = response.headers.get('Location');

                        if (response.ok) {
                            console.log(successMessage);
                            window.location.href = window.location.href;
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

                        if (response.status === 500 || response.status === 403) {
                            handleError(contextPath, errorMessage, response.status);
                            return;
                        }

                        throw new Error(`Unexpected response status: ${response.status}`);
                    })
                    .catch(error => {
                        console.error('Error updating train:', error);
                    });
            }
        });
    }

    viewTicketsBtn.addEventListener('click', () => {
        window.location.href = `${contextPath}/trains/${trainId}/tickets`;
    });
    if (deleteTrainBtn) {
        deleteTrainBtn.addEventListener('click', () => {
            if (confirm('Are you sure you want to delete this train forever?')) {
                fetch(`${contextPath}/api/trains/${trainId}`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json',
                        [csrfHeader]: csrfToken
                    }
                })
                    .then(handleDeleteResponse)
                    .catch(error => {
                        console.error('Error deleting train forever:', error);
                    });
            }
        });
    }

    function handleDeleteResponse(response) {
        const errorMessage = response.headers.get('Error-Message');
        const successMessage = response.headers.get('Success-Message');

        if (response.ok) {
            console.log(successMessage);
            window.location.href = `${contextPath}/trains`;
            return;
        }

        if (response.status === 409) {
            displayDeleteError(errorMessage);
            return;
        }

        if (response.status === 404 || response.status === 500) {
            handleError(contextPath, errorMessage, response.status);
            return;
        }

        throw new Error(`Unexpected response status: ${response.status}`);
    }

    function displayDeleteError(message) {
        deleteError.textContent = message;
        deleteError.classList.remove('d-none');
    }
    if (cancelBtn) {
        cancelBtn.addEventListener('click', () => {
            window.location.href = `${contextPath}/trains`;
        });
    }

    fetchTrainDetails();
});