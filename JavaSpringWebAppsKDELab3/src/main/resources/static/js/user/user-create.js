import {
    validateUsername,
    validateEmail,
    validateName,
    validatePassword,
    validateConfirmPassword
} from './util/user-utils.js';

import {
    handleError,
    clearErrors,
    displayFormError,
    formatErrorMessage
} from '../util/error/error-handler.js';

document.addEventListener('DOMContentLoaded', () => {
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    const contextPath = document.querySelector('base').href.replace(/\/$/, '');
    const createUserForm = document.getElementById('createUserForm');
    const cancelBtn = document.getElementById('cancelBtn');
    const togglePassword = document.getElementById('togglePassword');
    const password = document.getElementById('password');
    const passwordIcon = document.getElementById('passwordIcon');
    const toggleConfirmPassword = document.getElementById('toggleConfirmPassword');
    const confirmPassword = document.getElementById('confirmPassword');
    const confirmPasswordIcon = document.getElementById('confirmPasswordIcon');

    createUserForm.addEventListener('submit', (event) => {
        event.preventDefault();
        clearErrors();

        const username = document.getElementById('username').value;
        const email = document.getElementById('email').value;
        const name = document.getElementById('name').value;
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        const role = document.getElementById('role').value;

        if (!validateUsername(username) || !validateEmail(email) || !validateName(name) || !validatePassword(password) || !validateConfirmPassword(password, confirmPassword)) {
            return;
        }

        const userData = {
            username: username,
            email: email,
            name: name,
            password: password,
            role: role
        };

        fetch(`${contextPath}/api/users`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify(userData)
        })
            .then(response => {
                const errorMessage = response.headers.get('Error-Message');
                const successMessage = response.headers.get('Success-Message');

                if (response.status === 201) {
                    console.log(successMessage);
                    window.location.href = `${contextPath}/home?successMessage=employee-added`;
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
                console.error('Error creating user:', error);
            });
    });

    cancelBtn.addEventListener('click', () => {
        window.location.href = `${contextPath}/home`;
    });

    togglePassword.addEventListener('click', () => {
        const type = password.getAttribute('type') === 'password' ? 'text' : 'password';
        password.setAttribute('type', type);
        passwordIcon.classList.toggle('bi-eye');
        passwordIcon.classList.toggle('bi-eye-slash');
    });

    toggleConfirmPassword.addEventListener('click', () => {
        const type = confirmPassword.getAttribute('type') === 'password' ? 'text' : 'password';
        confirmPassword.setAttribute('type', type);
        confirmPasswordIcon.classList.toggle('bi-eye');
        confirmPasswordIcon.classList.toggle('bi-eye-slash');
    });
});