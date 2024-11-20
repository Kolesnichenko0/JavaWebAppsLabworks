document.addEventListener('DOMContentLoaded', () => {
    const logoutLink = document.getElementById('logoutLink');
    logoutLink.addEventListener('click', (event) => {
        event.preventDefault();
        if (confirm('Are you sure you want to logout?')) {
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = '/logout';
            document.body.appendChild(form);
            form.submit();
        }
    });
});