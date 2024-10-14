document.addEventListener('DOMContentLoaded', () => {
    const trainId = window.location.pathname.split('/').pop();
    const contextPath = document.querySelector('meta[name="context-path"]').getAttribute('content');
    const restoreByNumberBtn = document.getElementById('restoreByNumberBtn');
    const trainNumberInput = document.getElementById('trainNumber');
    const formError = document.getElementById('formError');
    const deletedTrainTableBody = document.getElementById('deletedTrainTableBody');

    restoreByNumberBtn.addEventListener('click', () => {
        clearErrors();
        const trainNumber = trainNumberInput.value.trim();
        if (!validateTrainNumber(trainNumber)) {
            return;
        }

        fetch(`${contextPath}/trains/id?number=${trainNumber}`, {
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
                            fetch(`${contextPath}/trains/${data.id}`, {
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

    function displayError(elementId, message) {
        const element = document.getElementById(elementId);
        if (!element) {
            console.error(`Element with ID ${elementId} not found.`);
            return;
        }
        const errorElement = document.createElement('div');
        errorElement.className = 'text-danger mt-2';
        errorElement.textContent = message;
        element.parentNode.appendChild(errorElement);
    }

    function displayFormError(message) {
        formError.textContent = message;
        formError.classList.remove('d-none');
    }

    function clearErrors() {
        const errorElements = document.querySelectorAll('.text-danger');
        errorElements.forEach(el => el.remove());
        formError.classList.add('d-none');
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

    function fetchDeletedTrains() {
        fetch(`${contextPath}/trains/deleted`, {
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
                                    <td colspan="6" class="text-center text-muted no-trains-message">
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
                            <td colspan="6" class="text-center text-muted no-trains-message">
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