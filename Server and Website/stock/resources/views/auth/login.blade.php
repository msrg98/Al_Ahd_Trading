<html>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="{{asset('/css/bootstrap.min.css')}}">
<link rel="stylesheet" href="{{asset('/css/bootstrap.css')}}">
<script src="{{asset('/js/popper.min.js')}}"></script>
<script src="{{asset('/js/bootstrap.min.js')}}"></script>
<link rel="stylesheet" href="{{asset('/css/login.css')}}">
<script type="text/javascript" src="{{asset('/js/jquery-3.4.1.min.js')}}"></script>
<script src="{{asset('/js/jquery.form-validator.min.js')}}"></script>

<body class="p-1">
<div class="shadow my-rounded bg-white p-5">
    <form class="container" method="post" action="/login">
        {{csrf_field()}}
        <p>
            <label for="email">Email:</label>
            <br>
            <input class="form-control" type="text" id="email" name="email" data-validation="required email"
                   autofocus>
        </p>
        <p>
            <label class=" mt-5" for="password">Password</label>
            <br>
            <input class="form-control" type="password" id="password" name="password" data-validation="required">
        </p>
        <input class="my-btn mt-5" type="submit" value="Log in">
        <br>
        <br>
        Don't have an account?<a href="/register"> Sign up here</a>
    </form>
</div>
</body>
</html>
<script type="text/javascript" src="{{asset('/js/validate.js')}}"></script>
