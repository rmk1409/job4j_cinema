<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm"
          crossorigin="anonymous">

    <title>Cinema</title>
</head>
<body>
<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
        integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"
        integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q"
        crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"
        integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
        crossorigin="anonymous"></script>

<div class="container">
    <div class="row pt-3">
        <h3>
            Вы выбрали ряд <span id="row"></span> место <span id="cell"></span>,
            Сумма : 500 рублей.
        </h3>
    </div>
    <div class="row">
        <form>
            <div class="form-group">
                <label for="username">ФИО</label>
                <input type="text" class="form-control" id="username"
                       placeholder="ФИО">
            </div>
            <div class="form-group">
                <label for="phone">Номер телефона</label>
                <input type="text" class="form-control" id="phone"
                       placeholder="Номер телефона">
            </div>
            <button type="button" class="btn btn-success"
                    onclick="return tryToBuy()">Оплатить
            </button>
        </form>
    </div>
</div>
<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
<script>
    let row;
    let cell
    $(document).ready(() => {
        const place = new URL(window.location.href).searchParams.get(`place`);
        row = place.charAt(0);
        $('#row').text(row);
        cell = place.charAt(1);
        $('#cell').text(cell);
    });

    const tryToBuy = () => {
        const phoneNumber = $('#phone').val();
        const username = $('#username').val();
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/cinema/tickets',
            data: {phoneNumber, username, row, cell}
        }).done((_) => {
            alert(`Congrats! You've bought the ticket!`);
        }).fail((xhr) => {
            alert($.parseHTML(xhr.responseText)[5].innerText);
        });
    }
</script>
</body>
</html>
