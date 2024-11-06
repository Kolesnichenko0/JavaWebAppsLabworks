export function clearErrors() {
    const errorElements = document.querySelectorAll('.text-danger');
    errorElements.forEach(el => el.remove());
    const formError = document.getElementById('formError');
    if (formError) {
        formError.classList.add('d-none');
    }
}

export function displayError(elementId, message) {
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

export function displayFormError(message) {
    const formErrorElement = document.getElementById('formError');
    formErrorElement.textContent = message;
    formErrorElement.classList.remove('d-none');
}

export function formatErrorMessage(errorMessage) {
    const regex = /-(.*)$/;
    const match = errorMessage.match(regex);
    if (match) {
        const fieldName = match[1].trim();
        const formattedFieldName = fieldName.replace(/([a-z])([A-Z])/g, '$1 $2').replace(/\b\w/g, char => char.toUpperCase());
        return errorMessage.replace(fieldName, formattedFieldName);
    }
    return errorMessage;
}

export function handleError(contextPath, errorMessage, statusCode) {
    fetch(`${contextPath}/error/${statusCode}`)
        .then(errorResponse => errorResponse.text())
        .then(html => {
            console.error(errorMessage);
            document.body.innerHTML = html;
        })
        .catch(error => {
            console.error('Error fetching the error page:', error);
        });
}