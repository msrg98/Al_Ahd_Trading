@extends('layouts.user_header')

<div class="bodydiv">
    <div class="w-100 mb-5">
        <div class="justify-content-between d-flex">
            <h1 class="orange mr-auto">Categories</h1>
        </div>
    </div>
    <div class="row p-3 orange">
        @php $count = 0; @endphp
        @foreach($categories as $category)
            @if($category->id != 0)
            <div class="col-lg-3 col-md-4 col-sm-6 p-3">
                <div class="card">
                    @if(file_exists(storage_path('app/public/img/categories/' . $category->id.'.jpg')))
                        <img class="card-img-top" src="storage/categories/{{$category->id}}.jpg" onclick="window.location = '/products?category_id={{$category->id}}'" alt="Card image cap">
                    @else
                        <img class="card-img-top" src="/img/unknown-tag.png" onclick="window.location = '/products?category_id={{$category->id}}'" alt="Card image cap">
                    @endif
                    <div class="card-body">
                        <h5 class="card-title">{{$category->name}}</h5>
                    </div>
                    <div class="card-footer">
                        <h5 class=""><b>{{$category->products_count}}</b></h5>
                    </div>
                </div>
            </div>
            @php $count++; @endphp
            @if($count === 4)
                @php $count =0; @endphp
    </div>
    <div class="row p-3 orange">
        @endif
        @endif
        @endforeach
    </div>

</div>
<script type="text/javascript" src="{!!url('https://code.jquery.com/jquery-3.4.1.min.js')!!}"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery-form-validator/2.3.26/jquery.form-validator.min.js"></script>
<script type="text/javascript" src="{{asset('/js/validate.js')}}"></script>
