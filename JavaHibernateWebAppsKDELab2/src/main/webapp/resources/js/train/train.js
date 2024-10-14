document.addEventListener('DOMContentLoaded', () => {
    const contextPath = document.querySelector('meta[name="context-path"]').getAttribute('content');
    const trainId = window.location.pathname.split('/').pop();
    const updateTrainForm = document.getElementById('updateTrainForm');
    const deleteTrainBtn = document.getElementById('deleteTrainBtn');
    const deleteTrainForeverBtn = document.getElementById('deleteTrainForeverBtn');
    const cancelBtn = document.getElementById('cancelBtn');
    const deleteError = document.getElementById('deleteError');

    function fetchTrainDetails() {
        fetch(`${contextPath}/trains/${trainId}`, {
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

    function clearErrors() {
        const errorElements = document.querySelectorAll('.text-danger');
        errorElements.forEach(el => el.remove());
    }

    function displayError(elementId, message) {
        const errorElement = document.createElement('div');
        errorElement.className = 'text-danger mt-2';
        errorElement.textContent = message;
        document.getElementById(elementId).parentNode.appendChild(errorElement);
    }

    function displayFormError(message) {
        const formErrorElement = document.getElementById('formError');
        formErrorElement.textContent = message;
        formErrorElement.classList.remove('d-none');
    }

    function formatErrorMessage(errorMessage) {
        const regex = /-(.*)$/;
        const match = errorMessage.match(regex);
        if (match) {
            const fieldName = match[1].trim();
            const formattedFieldName = fieldName.replace(/([a-z])([A-Z])/g, '$1 $2').replace(/\b\w/g, char => char.toUpperCase());
            return errorMessage.replace(fieldName, formattedFieldName);
        }
        return errorMessage;
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

        fetch(`${contextPath}/trains/${trainId}`, {
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
                        // document.body.innerHTML = html;
                    });
                }

                throw new Error(`Unexpected response status: ${response.status}`);
            })
            .catch(error => {
                console.error('Error updating train:', error);
            });
    });

    function validateTrainNumber(number) {
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

    function validateStationName(station, elementId) {
        const STATION_REGEX = /^(([А-ЗЙ-ЩЮЯЇІЄ][А-ЩЮЯЇІЄҐ]+|[А-ЗЙ-ЩЮЯЇІЄ][а-щьюяїієґ\']+[.]?)(?:[ -](?:[А-ЗЙ-ЩЮЯЇІЄ][А-ЩЮЯЇІЄҐ]+[0-9]*|[А-ЗЙ-ЩЮЯЇІЄ]?[а-щьюяїієґ\']+[.|0-9]*|[0-9]+)|[(][А-ЗЙ-ЩЮЯЇІЄ]?[а-щьюяїієґ\']+[.]?[)])*){1,100}$/;
        if (!station) {
            displayError(elementId, 'Station name cannot be empty');
            return false;
        }

        station = station.trim();

        if (station.charAt(0).toLowerCase() === station.charAt(0)) {
            station = station.charAt(0).toUpperCase() + station.slice(1);
        }

        console.log(station);

        if (!STATION_REGEX.test(station)) {
            displayError(elementId, 'Station name can include ukrainian letters, spaces, hyphens, numbers, and parentheses.');
            return false;
        }

        return true;
    }

    deleteTrainBtn.addEventListener('click', () => {
        if (confirm('Are you sure you want to delete this train?')) {
            fetch(`${contextPath}/trains/${trainId}`, {
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

    function displayDeleteError(message) {
        deleteError.textContent = message;
        deleteError.classList.remove('d-none');
    }

    deleteTrainForeverBtn.addEventListener('click', () => {
        if (confirm('Are you sure you want to delete this train forever?')) {
            fetch(`${contextPath}/trains/${trainId}`, {
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
});