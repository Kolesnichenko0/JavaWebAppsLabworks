import {
    clearErrors,
    displayFormError,
    formatErrorMessage,
    handleError
} from '../util/error/error-handler.js';
import {
    validateCarriageNumber,
    validatePassportNumber,
    validatePassengerSurname,
    validateSeatNumber
} from './util/train-ticket-utils.js';

document.addEventListener('DOMContentLoaded', () => {
    const contextPath = document.querySelector('base').href.replace(/\/$/, '');
    const ticketId = window.location.pathname.split('/').pop();
    let trainId;

    const updatePassengerSurname = document.getElementById('updatePassengerSurname');
    const updatePassportNumber = document.getElementById('updatePassportNumber');
    const updateCarriageNumber = document.getElementById('updateCarriageNumber');
    const updateSeatNumber = document.getElementById('updateSeatNumber');
    const updateTicketForm = document.getElementById('updateTicketForm');
    const deleteTicketBtn = document.getElementById('deleteTicketBtn');
    const goToTrainBtn = document.getElementById('goToTrainBtn');
    const deleteError = document.getElementById('deleteError');
    const cancelBtn = document.getElementById('cancelBtn');

    function fetchTicketDetails(ticketId) {
        fetch(`${contextPath}/api/train-tickets/${ticketId}`, {
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
                        trainId = data.trainId;
                        populateTicketDetails(data);
                    });
                }

                if (response.status === 404 || response.status === 500) {
                    handleError(contextPath, errorMessage, response.status);
                    return;
                }

                throw new Error(`Unexpected response status: ${response.status}`);
            })
            .catch(error => {
                console.error('Error fetching ticket details:', error);
            });
    }

    function populateTicketDetails(data) {
        const formattedDepartureDate = new Intl.DateTimeFormat('uk-UA', {
            day: '2-digit',
            month: 'short',
            year: 'numeric'
        }).format(new Date(data.departureDate));

        document.getElementById('ticketDetails').innerHTML = `
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">Ticket Number: ${data.trainNumber}</h5>
                <p class="card-text"><strong>Passenger Surname:</strong> ${data.passengerSurname}</p>
                <p class="card-text"><strong>Passport Number:</strong> ${data.passportNumber}</p>
                <p class="card-text"><strong>Departure Date:</strong> ${formattedDepartureDate}</p>
                <p class="card-text"><strong>Carriage Number:</strong> ${data.carriageNumber}</p>
                <p class="card-text"><strong>Seat Number:</strong> ${data.seatNumber}</p>
            </div>
        </div>
    `;

        updatePassengerSurname.value = data.passengerSurname;
        updatePassportNumber.value = data.passportNumber;
        updateCarriageNumber.value = data.carriageNumber;
        updateSeatNumber.value = data.seatNumber;

        goToTrainBtn.addEventListener('click', () => {
            window.location.href = `${contextPath}/trains/${data.trainId}`;
        });
        cancelBtn.addEventListener('click', () => {
            window.location.href = `${contextPath}/trains/${trainId}/tickets`;
        });
    }

    updateTicketForm.addEventListener('submit', (event) => {
        if (confirm('Are you sure you want to update this ticket?')) {
            event.preventDefault();
            clearErrors();

            const passengerSurname = updatePassengerSurname.value;
            const passportNumber = updatePassportNumber.value;
            const carriageNumber = updateCarriageNumber.value;
            const seatNumber = updateSeatNumber.value;

            if (!validatePassengerSurname(passengerSurname, 'updatePassengerSurname') ||
                !validatePassportNumber(passportNumber, 'updatePassportNumber') ||
                !validateCarriageNumber(carriageNumber, 'updateCarriageNumber') ||
                !validateSeatNumber(seatNumber, 'updateSeatNumber')) {
                return;
            }

            const ticketData = {
                passengerSurname: passengerSurname,
                passportNumber: passportNumber,
                carriageNumber: carriageNumber,
                seatNumber: seatNumber
            };

            console.log(ticketData);

            fetch(`${contextPath}/api/train-tickets/${ticketId}`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(ticketData)
            })
                .then(response => {
                    const errorMessage = response.headers.get('Error-Message');
                    if (response.status === 200) {
                        window.location.reload();
                        return;
                    }
                    if (response.status === 400) {
                        displayFormError(formatErrorMessage(errorMessage));
                        return;
                    }

                    if (response.status === 409) {
                        displayFormError(formatErrorMessage(errorMessage));
                        return;
                    }

                    if (response.status === 422) {
                        displayFormError(formatErrorMessage(errorMessage));
                        return;
                    }
                    if (response.status === 500) {
                        handleError(contextPath, errorMessage, response.status);
                        return;
                    }
                    throw new Error(`Unexpected response status: ${response.status}`);
                })
                .catch(error => {
                    console.error('Error updating ticket:', error);
                });
        }
    });

    deleteTicketBtn.addEventListener('click', () => {
        if (confirm('Are you sure you want to delete this ticket forever?')) {
            fetch(`${contextPath}/api/train-tickets/${ticketId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(handleDeleteResponse)
                .catch(error => {
                    console.error('Error deleting ticket forever:', error);
                });
        }
    });

    function handleDeleteResponse(response) {
        const errorMessage = response.headers.get('Error-Message');
        const successMessage = response.headers.get('Success-Message');

        if (response.ok) {
            console.log(successMessage);
            window.location.href = `${contextPath}/trains/${trainId}/tickets`;
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

    fetchTicketDetails(ticketId);
});