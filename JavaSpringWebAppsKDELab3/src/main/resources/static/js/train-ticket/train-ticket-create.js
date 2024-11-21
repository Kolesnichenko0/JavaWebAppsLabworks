import {validateTrainNumber, validateStationName} from '../train/util/train-utils.js';
import {
    validateCarriageNumber,
    validatePassportNumber,
    validatePassengerSurname,
    validateDepartureDate
} from './util/train-ticket-utils.js';
import {
    handleError,
    clearErrors,
    displayError,
    displayFormError,
    formatErrorMessage
} from '../util/error/error-handler.js';

document.addEventListener('DOMContentLoaded', () => {
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    const contextPath = document.querySelector('base').href.replace(/\/$/, '');
    const createTicketForm = document.getElementById('createTicketForm');
    const cancelBtn = document.getElementById('cancelBtn');

    createTicketForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        clearErrors();

        const trainNumber = document.getElementById('trainNumber').value;
        const passengerSurname = document.getElementById('passengerSurname').value;
        const passportNumber = document.getElementById('passportNumber').value;
        const carriageNumber = document.getElementById('carriageNumber').value;
        const seatNumber = document.getElementById('seatNumber').value;
        const departureDate = document.getElementById('departureDate').value;

        if (!validateTrainNumber(trainNumber) || !validatePassengerSurname(passengerSurname, "passengerSurname") || !validatePassportNumber(passportNumber, "passportNumber") || !validateCarriageNumber(carriageNumber)) {
            return;
        }

        fetch(`${contextPath}/api/trains?qNumber=${trainNumber}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.text();
            })
            .then(text => {
                if (!text) {
                    displayError('trainNumber', 'Train not found');
                    throw new Error('Empty response');
                }
                return JSON.parse(text);
            })
            .then(data => {
                if (data.length === 0) {
                    displayError('trainNumber', 'Train not found');
                    return false;
                }

                const train = data[0];

                const trainId = train.id;
                const ticketData = {
                    trainId: trainId,
                    passengerSurname: passengerSurname,
                    passportNumber: passportNumber,
                    carriageNumber: carriageNumber,
                    seatNumber: seatNumber,
                    departureDate: departureDate
                };

                validateDepartureDate(departureDate, trainNumber, contextPath)
                    .then(isDepartureDateValid => {
                        if (!isDepartureDateValid) {
                            return;
                        }
                        fetch(`${contextPath}/api/train-tickets`, {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json',
                                [csrfHeader]: csrfToken
                            },
                            body: JSON.stringify(ticketData)
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
                                        window.location.href = `${contextPath}/train-tickets`;
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

                                if (response.status === 500 || response.status === 403) {
                                    handleError(contextPath, errorMessage, response.status);
                                    return;
                                }

                                throw new Error(`Unexpected response status: ${response.status}`);
                            })
                            .catch(error => {
                                console.error('Error creating ticket:', error);
                            });
                    })
                    .catch(error => {
                        console.error('Error fetching train:', error);
                    });
            });
    });

    cancelBtn.addEventListener('click', () => {
        window.location.href = `${contextPath}/train-tickets`;
    });
});