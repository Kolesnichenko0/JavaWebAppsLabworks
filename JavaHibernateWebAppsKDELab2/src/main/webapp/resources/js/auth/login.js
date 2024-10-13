const contextPath = document.querySelector('meta[name="context-path"]').getAttribute('content');

document.querySelector('#loginForm').addEventListener('submit', function (event) {
    event.preventDefault();

    let formData = new FormData(this);

    fetch(`${contextPath}/check-login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams(formData).toString()
    })
        .then(function (response) {
            if (response.status === 401) {
                return response.json().then(function (data) {
                    throw new Error(data.error);
                });
            }
            return response.json();
        })
        .then(function (data) {
            if (data.redirect) {
                window.location.href = data.redirect;
            }
        })
        .catch(function (error) {
            document.getElementById('error').innerText = error.message;
            document.getElementById('error').style.display = 'block';
        });
});