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

    function clearErrors() {
        const errorElements = document.querySelectorAll('.text-danger');
        errorElements.forEach(el => el.remove());
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
});