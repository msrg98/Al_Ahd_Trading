<?php

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/


use Illuminate\Support\Facades\File;
use Illuminate\Support\Facades\Response;
use function foo\func;

Route::get('/', 'Controller@show_categories');
Route::get('/products', 'Controller@show_products');


Route::prefix("/bx6yjsa8v")->group(function () {
    Route::post("/add_item","Controller@addProduct");
    Route::post("/add_category","Controller@addCategory");
    Route::post("/upload_image","Controller@uploadImage");
    Route::post("/update_item","Controller@updateItem");
    Route::post("/delete_item","Controller@deleteItem");
    Route::post("/delete_category","Controller@deleteCategory");
    Route::get("/get_categories","Controller@getCategories");
    Route::get("/get_category_items","Controller@getCategoryItems");
    Route::get("/get_item_details","Controller@getItemDetails");
});
Route::get("storage/items/{name}",function ($name){
    $path = storage_path('app/public/img/items/' . $name);

    if (!File::exists($path)) {
        abort(404);
    }

    $file = File::get($path);
    $type = File::mimeType($path);

    $response = Response::make($file, 200);
    $response->header("Content-Type", $type);

    return $response;
});

Route::get("storage/categories/{name}",function ($name){
    $path = storage_path('app/public/img/categories/' . $name);

    if (!File::exists($path)) {
        abort(404);
    }

    $file = File::get($path);
    $type = File::mimeType($path);

    $response = Response::make($file, 200);
    $response->header("Content-Type", $type);

    return $response;
});

