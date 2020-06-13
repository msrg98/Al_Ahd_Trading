@extends('layouts.user_header')

<div class="bodydiv">
    <div class="w-100 mb-5">
        <div class="justify-content-between d-flex">
            <h1 class="orange mr-auto">{{$category->name}}</h1>
        </div>
    </div>
    <div class="row p-3 orange">
        @php $count = 0; @endphp
        @foreach($products as $item)
            <div class="col-lg-3 col-md-4 col-sm-6 p-3">
                <div class="card">
                    @if(file_exists(storage_path('app/public/img/items/' . $item->id.'.jpg')))
                    <img class="card-img-top" src="storage/items/{{$item->id}}.jpg"  alt="Card image cap">
                    @else
                    <img class="card-img-top" src="/img/trolley.png"  alt="Card image cap">
                    @endif
                    <div class="card-body">
                        <h5 class="card-title">{{$item->name}}</h5>
                    </div>
                        <p class="card-text px-4">{{$item->specifications}}</p>
                    <div class="card-footer">
                        <h5 class=""><b>{{$item->count}} pieces</b></h5>
                    </div>
                </div>
            </div>
            @php $count++; @endphp
            @if($count === 4)
                @php $count =0; @endphp
    </div>
    <div class="row p-3 orange">
        @endif
        @endforeach
    </div>

</div>
<script type="text/javascript" src="{!!url('https://code.jquery.com/jquery-3.4.1.min.js')!!}"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery-form-validator/2.3.26/jquery.form-validator.min.js"></script>
<script type="text/javascript" src="{{asset('/js/validate.js')}}"></script>
