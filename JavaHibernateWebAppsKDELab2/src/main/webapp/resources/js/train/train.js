import {
    clearErrors,
    displayError,
    displayFormError,
    formatErrorMessage,
    validateTrainNumber,
    validateStationName,
    calculateArrivalTime
} from './util/train-utils.js';

document.addEventListener('DOMContentLoaded', () => {
    const contextPath = document.querySelector('meta[name="context-path"]').getAttribute('content');
    const trainId = window.location.pathname.split('/').pop();
    const updateTrainForm = document.getElementById('updateTrainForm');
    const deleteTrainBtn = document.getElementById('deleteTrainBtn');
    const deleteTrainForeverBtn = document.getElementById('deleteTrainForeverBtn');
    const cancelBtn = document.getElementById('cancelBtn');
    const deleteError = document.getElementById('deleteError');

    function fetchTrainDetails() {
        fetch(`${contextPath}/trains/${trainId}?ajax=true`, {
            headers: {
                'Accept': 'application/json'
            }
        })
            .then(response => {
                const errorMessage = response.headers.get('Error-Message');
                const successMessage = response.headers.get('Success-Message');

                if (response.status === 400) {
                    console.error(errorMessage);
                    return;
                }

                if (response.status === 200) {
                    return response.json().then(data => {
                        console.log(successMessage);
                        document.getElementById('trainDetails').innerHTML = `
                        <div class="card">
                            <div class="card-body">
                                <h5 class="card-title">Train Number: ${data.number}</h5>
                                <p class="card-text"><strong>Departure Station:</strong> ${data.departureStation}</p>
                                <p class="card-text"><strong>Arrival Station:</strong> ${data.arrivalStation}</p>
                                <p class="card-text"><strong>Movement Type:</strong> ${data.movementType}</p>
                                <p class="card-text"><strong>Departure Time:</strong> ${data.departureTime}</p>
                                <p class="card-text"><strong>Arival Time:</strong> ${calculateArrivalTime(data.departureTime, data.duration)}</p>
                                <p class="card-text"><strong>Duration:</strong> ${data.duration}</p>
                            </div>
                        </div>
                    `;
                        document.getElementById('trainNumber').value = data.number;
                        document.getElementById('departureStation').value = data.departureStation;
                        document.getElementById('arrivalStation').value = data.arrivalStation;
                        document.getElementById('movementType').value = data.movementType;
                        document.getElementById('departureTime').value = data.departureTime;
                        const durationString = data.duration;
                        const durationParts = durationString.match(/(\d+)\s*год\.\s*(\d+)\s*хв\./);
                        const durationHours = parseInt(durationParts[1], 10);
                        const durationMinutes = parseInt(durationParts[2], 10);
                        document.getElementById('durationHours').value = durationHours;
                        document.getElementById('durationMinutes').value = durationMinutes;
                    });
                }

                if (response.status === 204) {
                    console.log(successMessage);
                    document.getElementById('trainDetails').innerHTML = `
                    <div class="text-center text-muted no-trains-message">
                        No train details found. Please try again later.
                    </div>`;
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
                console.error('Error fetching train details:', error);
            });
    }

    updateTrainForm.addEventListener('submit', (event) => {
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

        fetch(`${contextPath}/trains/${trainId}?ajax=true`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(trainData)
        })
            .then(response => {
                const errorMessage = response.headers.get('Error-Message');
                const successMessage = response.headers.get('Success-Message');
                const locationHeader = response.headers.get('Location');

                if (response.status === 200) {
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

                if (response.status === 500) {
                    return response.text().then(html => {
                        console.error(errorMessage);
                        document.body.innerHTML = html;
                    });
                }

                throw new Error(`Unexpected response status: ${response.status}`);
            })
            .catch(error => {
                console.error('Error updating train:', error);
            });
    });

    deleteTrainBtn.addEventListener('click', () => {
        if (confirm('Are you sure you want to delete this train?')) {
            fetch(`${contextPath}/trains/${trainId}?ajax=true`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                    'Action': 'delete'
                },
            })
                .then(response => {
                    const errorMessage = response.headers.get('Error-Message');
                    const successMessage = response.headers.get('Success-Message');
                    const locationHeader = response.headers.get('Location');

                    if (response.status === 200) {
                        console.log(successMessage);
                        if (locationHeader) {
                            window.location.href = locationHeader;
                        } else {
                            window.location.href = `${contextPath}/trains`;
                        }
                        return;
                    }

                    if (response.status === 409) {
                        displayDeleteError(errorMessage);
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
                    console.error('Error updating train:', error);
                });
        }
    });

    deleteTrainForeverBtn.addEventListener('click', () => {
        if (confirm('Are you sure you want to delete this train forever?')) {
            fetch(`${contextPath}/trains/${trainId}?ajax=true`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                },
            })
                .then(response => {
                    const errorMessage = response.headers.get('Error-Message');
                    const successMessage = response.headers.get('Success-Message');
                    const locationHeader = response.headers.get('Location');

                    if (response.status === 200) {
                        console.log(successMessage);
                        if (locationHeader) {
                            window.location.href = locationHeader;
                        } else {
                            window.location.href = `${contextPath}/trains`;
                        }
                        return;
                    }

                    if (response.status === 409) {
                        displayDeleteError(errorMessage);
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
                    console.error('Error deleting train forever:', error);
                });
        }
    });

    cancelBtn.addEventListener('click', () => {
        window.location.href = `${contextPath}/trains`;
    });

    fetchTrainDetails();

    function displayDeleteError(message) {
        deleteError.textContent = message;
        deleteError.classList.remove('d-none');
    }
});