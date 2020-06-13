<?php

namespace App\Http\Controllers;

use App\Category;
use App\Product;
use Illuminate\Database\QueryException;
use Illuminate\Foundation\Auth\Access\AuthorizesRequests;
use Illuminate\Foundation\Bus\DispatchesJobs;
use Illuminate\Foundation\Validation\ValidatesRequests;
use Illuminate\Http\Request;
use Illuminate\Routing\Controller as BaseController;
use Illuminate\Support\Facades\File;

class Controller extends BaseController
{
    use AuthorizesRequests, DispatchesJobs, ValidatesRequests;

    public function addProduct(Request $request)
    {
        $p = new Product;
        $p->name = $request->name;
        $p->specifications = $request->specification;
        $p->count = $request->count;
        $category = Category::where("name","=",$request->category)->first();
        $p->category_id = $category->id;
        try {
            $p->save();
            echo $p->id;
        }
        catch (QueryException $e) {
            echo "failed";
        }

    }

    public function addCategory(Request $request)
    {
        $c = new Category();
        $c->name = ucwords($request->name);
        try {
            $c->save();
            echo $c->id;
        } catch (QueryException $e) {
            echo "failed";
        }
    }

    public function uploadImage(Request $request)
    {
        $image = $request->image;
        $image = str_replace('data:image/png;base64,', '', $image);
        $image = str_replace(' ', '+', $image);


        switch ($request->directory) {
            case "items":
                \File::put(storage_path() . '/app/public/img/items/' . $request->id . ".jpg", base64_decode($image));
                break;
            case "categories":
                \File::put(storage_path() . '/app/public/img/categories/' . $request->id . ".jpg", base64_decode($image));
                break;
        }

        echo "success";
    }

    public function getCategories(Request $request)
    {
        $categories = Category::all();
        echo json_encode($categories);
    }

    public function getCategoryItems(Request $request)
    {
        $category = Category::where("name","=",$request->input("category"))->first();
        $items = $category->products;
        echo json_encode($items);
    }

    public function getItemDetails(Request $request){
        $item = Product::where("name","=",$request->input("name"))->first();
        $path = storage_path('app/public/img/items/' . $item->id.'.jpg');
        if (File::exists($path)) {
            $item->image = true;
        }
        else
            $item->image = false;
        echo json_encode($item);
    }

    public function updateItem(Request $request){
        $item = Product::find(intval($request->id));
        $item->name = $request->name;
        $item->specifications = $request->specification;
        $item->count = intval($request->count);
        $item->category_id = Category::where("name","=",$request->category)->first()->select("id");
        $item->save();
        echo "success";
    }

    public function deleteItem(Request $request) {
        $product = Product::where("name","=",$request->name)->first();
        if ($product === null) {
            echo "Item doesn't exist";
        }
        else {
            $path = storage_path('app/public/img/items/' . $product->id.'.jpg');
            if (file_exists($path))
                File::delete($path);
            $product->delete();
            echo "Item deleted";
        }
    }

    public function deleteCategory(Request $request) {
        $category = Category::where("name","=",$request->name)->first();
        if ($category === null) {
            echo "Category doesn't exist";
        }
        else {
            $items = $category->products;
            foreach ($items as $item) {
                $item->category_id = 0;
                $item->save();
            }
            $path = storage_path('app/public/img/categories/' . $category->id.'.jpg');
            if (file_exists($path))
                File::delete($path);
            $category->delete();
            echo "Category deleted";
        }
    }

    public function show_categories()
    {
        $categories = Category::all();
        foreach ($categories as $cat) {
            $prods = $cat->products;
            $cat->products_count = count($prods, COUNT_NORMAL);
        }
        return view('home',[
            'categories' => $categories,
        ]);
    }

    public function show_products(Request $request)
    {
        $category_id = $request->input('category_id');
        $category = Category::find($category_id);
        $products = $category->products;
        return view('products',[
            'products' => $products,
            'category' => $category,
        ]);
    }

}
