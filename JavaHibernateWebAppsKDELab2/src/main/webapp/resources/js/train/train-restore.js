import {
    calculateArrivalTime,
    clearErrors,
    displayFormError,
    formatErrorMessage,
    validateTrainNumber
} from './util/train-utils.js';

document.addEventListener('DOMContentLoaded', () => {
    const trainId = window.location.pathname.split('/').pop();
    const contextPath = document.querySelector('meta[name="context-path"]').getAttribute('content');
    const restoreByNumberBtn = document.getElementById('restoreByNumberBtn');
    const trainNumberInput = document.getElementById('trainNumber');
    const deletedTrainTableBody = document.getElementById('deletedTrainTableBody');

    restoreByNumberBtn.addEventListener('click', () => {
        clearErrors();
        const trainNumber = trainNumberInput.value.trim();
        if (!validateTrainNumber(trainNumber)) {
            return;
        }

        fetch(`${contextPath}/trains/id?number=${encodeURIComponent(trainNumber)}&ajax=true`, {
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
                        if (confirm('Are you sure you want to restore this train?')) {
                            fetch(`${contextPath}/trains/${data.id}?ajax=true`, {
                                method: 'PATCH',
                                headers: {
                                    'Content-Type': 'application/json',
                                    'Action': 'restore'
                                }
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
                                        displayFormError(errorMessage);
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
                }

                if (response.status === 204 || response.status === 404) {
                    console.log(successMessage);
                    displayFormError("Train not found in deleted trains. Please enter the correct train number.");
                    return;
                }

                if (response.status === 422) {
                    displayFormError(formatErrorMessage(errorMessage));
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
                console.error('Error fetching train details:', error);
            });
    });

    function fetchDeletedTrains() {
        fetch(`${contextPath}/trains/deleted?ajax=true`, {
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
                        if (data.length === 0) {
                            deletedTrainTableBody.innerHTML = `
                                <tr>
                                    <td colspan="7" class="text-center text-muted no-trains-message">
                                        No deleted trains found.
                                    </td>
                                </tr>`;
                        } else {
                            data.forEach(train => {
                                const row = document.createElement('tr');
                                row.innerHTML = `
                                    <td>${train.number}</td>
                                    <td>${train.departureStation}</td>
                                    <td>${train.arrivalStation}</td>
                                    <td>${train.movementType}</td>
                                    <td>${train.departureTime}</td>
                                    <td>${calculateArrivalTime(train.departureTime, train.duration)}</td>
                                    <td>${train.duration}</td>
                                `;
                                row.addEventListener('click', () => {
                                    document.getElementById('trainNumber').value = train.number;
                                });
                                deletedTrainTableBody.appendChild(row);
                            });
                        }
                    });
                }

                if (response.status === 204) {
                    console.log(successMessage);
                    deletedTrainTableBody.innerHTML = `
                        <tr>
                            <td colspan="7" class="text-center text-muted no-trains-message">
                                No deleted trains found.
                            </td>
                        </tr>`;
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
                console.error('Error fetching deleted trains:', error);
            });
    }

    fetchDeletedTrains();
});