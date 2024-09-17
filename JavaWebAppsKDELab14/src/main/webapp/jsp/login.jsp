<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Registration</title>
    <link rel="icon" href="<%= request.getContextPath() %>/png/favicon.png" sizes="32x32" type="image/png">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
    <style>
        input::placeholder {
            font-size: 16px;
        }
    </style>
</head>
<body>
<section class="vh-100" style="background-color: #f8f9fa;">
    <div class="container py-5 h-100">
        <div class="row d-flex justify-content-center align-items-center h-100">
            <div class="col-12 col-md-8 col-lg-6 col-xl-5">
                <div class="card text-dark" style="border-radius: 1rem; background-color: #e9f7ef;">
                    <div class="card-body p-5 text-center">

                        <div class="mb-md-5 mt-md-4 pb-5">

                            <h2 class="fw-bold mb-2 text-uppercase" style="color: #343a40;">Login</h2>
                            <p class="text-muted mb-5">Please enter your login and password!</p>

                            <div id="error" class="alert alert-danger" style="display: none;"></div>

                            <form id="loginForm" action="<%= request.getContextPath() %>/check-login" method="POST">
                                <div class="form-outline form-dark mb-4">
                                    <input name="username" required minlength="3" maxlength="20"
                                           placeholder="Input username" type="text"
                                           id="typeEmailX"
                                           class="form-control form-control-lg"
                                           style="background-color: #f1f1f1;"/>
                                    <label class="form-label" for="typeEmailX">Username</label>
                                </div>

                                <div class="form-outline form-dark mb-4">
                                    <input name="password" required minlength="3" maxlength="20"
                                           placeholder="Input password" type="password"
                                           id="typePasswordX" class="form-control form-control-lg"
                                           style="background-color: #f1f1f1;"/>
                                    <label class="form-label" for="typePasswordX">Password</label>
                                </div>

                                <button class="btn btn-outline-dark btn-lg px-5" type="submit">Login</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<script>
    document.querySelector('#loginForm').addEventListener('submit', function (event) {
        event.preventDefault();

        let formData = new FormData(this);

        fetch('<%= request.getContextPath() %>/check-login', {
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
</script>
</body>
</html>