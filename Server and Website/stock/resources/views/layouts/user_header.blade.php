<html class="h-100">
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<script type="text/javascript" src="{!!url('https://code.jquery.com/jquery-3.4.1.min.js')!!}"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery-form-validator/2.3.26/jquery.form-validator.min.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.css">
<link rel="stylesheet" href="{{asset('/css/header.css')}}">
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
<script type="text/javascript" src="{{asset('/js/header.js')}}"></script>

<body>
<div class="overlay h-100" id="overlay"></div>
<nav class="bg-orange navbar fixed-top" id="navbar">
    <button class="navbar-toggler h-100 p-3" type="button" id="toggler" onclick="show_sidebar()">
        <img class="h-100" src={{asset('/img/toggler.png')}}></button>
</nav>
<div class="sidebar h-100 fixed-top p-0" id="sidebar">
    <button class="back-btn" onclick="hide_sidebar()"><img src="{{asset('/img/left.png')}}"></button>
    <div class="logo d-flex justify-content-center align-items-center">
        <img class="w-75" src="{{asset('/img/account.png')}}">
    </div>
    <div class="mb-auto">
{{--        <button class="sidebar-btn" onclick="window.location='/'"><img src="{{asset('/img/account.png')}}">Account</button>--}}
{{--        <button class="sidebar-btn" onclick="window.location='/categories'"><img src="{{asset('/img/search.png')}}">Search</button>--}}
    </div>
</div>
