import { displayError } from '../../util/error/error-handler.js';

export function validateUsername(username) {
    const USERNAME_REGEX = /^[a-zA-Z0-9_]{3,20}$/;
    if (!username) {
        displayError('username', 'Username cannot be empty');
        return false;
    }
    if (!USERNAME_REGEX.test(username)) {
        displayError('username', 'Username must be 3-20 characters long and can only contain letters, digits, and underscores.');
        return false;
    }
    return true;
}

export function validateEmail(email) {
    const EMAIL_REGEX = /^[A-Za-z0-9_.-]+@gmail\.com$/;
    if (!email) {
        displayError('email', 'Email cannot be empty');
        return false;
    }
    if (!EMAIL_REGEX.test(email)) {
        displayError('email', 'Email should be valid with @gmail.com domain');
        return false;
    }
    return true;
}

export function validateName(name) {
    const NAME_REGEX = /^([А-ЗЙ-ЩЮЯЇІЄ][а-щьюяїієґ']+(-[А-ЗЙ-ЩЮЯЇІЄ][а-щьюяїієґ']+)?)$/;
    if (!name) {
        displayError('name', 'Name cannot be empty');
        return false;
    }
    if (!NAME_REGEX.test(name)) {
        displayError('name', 'Name must start with a Ukrainian capital letter and can include lowercase letters and apostrophes.');
        return false;
    }
    return true;
}

export function validatePassword(password) {
    if (!password) {
        displayError('passwordInput', 'Password cannot be empty');
        return false;
    }
    if (password.length < 8 || password.length > 30) {
        displayError('passwordInput', 'Password must be between 8 and 30 characters long');
        return false;
    }
    if (!password.match(/.*[A-Z].*/)) {
        displayError('passwordInput', 'Password must contain at least one uppercase letter');
        return false;
    }
    if (!password.match(/.*[a-z].*/)) {
        displayError('passwordInput', 'Password must contain at least one lowercase letter');
        return false;
    }
    if (!password.match(/.*\d.*/)) {
        displayError('passwordInput', 'Password must contain at least one digit');
        return false;
    }
    if (!password.match(/.*[!@#$%^&*(),.?:{}|<>].*/)) {
        displayError('passwordInput', 'Password must contain at least one special character');
        return false;
    }
    return true;
}

export function validateConfirmPassword(password, confirmPassword) {
    if (password !== confirmPassword) {
        displayError('confirmPasswordInput', 'Passwords do not match');
        return false;
    }
    return true;
}