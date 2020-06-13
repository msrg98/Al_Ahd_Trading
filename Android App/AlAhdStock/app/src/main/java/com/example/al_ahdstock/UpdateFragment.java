package com.example.al_ahdstock;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText name, specification, count;
    private Spinner category, categorySearch, itemSearch;
    private RequestQueue queue;
    private Button imageButton,addButton;
    private FrameLayout loading;
    private ImageView image;
    private ProgressBar p1,p2;
    private LinearLayout body;
    boolean imageChangeFlag = false;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    String myLog = "myLog";
    View view;
    Bitmap bitmap;
    int id;


    // TODO: Rename and change types of parameters

    public UpdateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UpdateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateFragment newInstance() {
        UpdateFragment fragment = new UpdateFragment();
        Bundle args = new Bundle();
        //    args.putString(ARG_PARAM1, param1);
        //    args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //        mParam1 = getArguments().getString(ARG_PARAM1);
            //        mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_update, container, false);
        categorySearch = (Spinner) view.findViewById(R.id.cateSearchSpinner);
        itemSearch = (Spinner) view.findViewById(R.id.itemSearchSpinner);
        name = (EditText) view.findViewById(R.id.nameText);
        specification = (EditText) view.findViewById(R.id.specText);
        count = (EditText) view.findViewById(R.id.countText);
        category = (Spinner) view.findViewById(R.id.cateSpinner);
        addButton = (Button) view.findViewById(R.id.addButton);
        loading = (FrameLayout) view.findViewById(R.id.progressBarHolder);
        imageButton = (Button) view.findViewById(R.id.imageButton);
        image = (ImageView) view.findViewById(R.id.image);
        p1 = (ProgressBar) view.findViewById(R.id.progressBar1);
        p2 = (ProgressBar) view.findViewById(R.id.progressBar2);
        body = (LinearLayout) view.findViewById(R.id.body);
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        sendGetCategoriesRequest();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 111);
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdateFragment.MyTask(id).execute();
            }
        });

        categorySearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0)
                    sendGetItemsRequest(parent.getItemAtPosition(position).toString());
                else
                    itemSearch.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                itemSearch.setVisibility(View.INVISIBLE);
            }
        });

        itemSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0)
                    sendGetItemDetailsRequest(parent.getItemAtPosition(position).toString());
                else
                    body.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                body.setVisibility(View.GONE);
            }
        });

        return view;
    }

    private String getName(){
        return name.getText().toString();
    }

    private String getSpecifcation(){
        return specification.getText().toString();
    }

    private String getCount(){
        return count.getText().toString();
    }

    private String getCategory(){
        return category.getSelectedItem().toString();
    }

    private void clearInputs() {
        name.setText("");
        specification.setText("");
        count.setText("");
        category.setSelection(0);
        image.setImageResource(R.drawable.item);
        itemSearch.setSelection(0);
        categorySearch.setSelection(0);
        body.setVisibility(View.GONE);
        itemSearch.setVisibility(View.INVISIBLE);
    }

    private void sendGetCategoriesRequest() {
        StringRequest stringRequest = new StringRequest
                (Request.Method.GET, "http://192.168.1.10:8000/bx6yjsa8v/get_categories", new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            ArrayList<String> cats = new ArrayList<>();
                            JSONArray arr = new JSONArray(response);
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                cats.add(obj.getString("name"));
                            }

                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, cats);
                            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            category.setAdapter(arrayAdapter);
                            categorySearch.setAdapter((arrayAdapter));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Snackbar snackbar1 = Snackbar.make(view, "No server response", Snackbar.LENGTH_SHORT);
                        snackbar1.show();
                    }
                }) {
        };
        queue.add(stringRequest);
    }

    private void sendGetItemsRequest(String category) {
        p1.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest
                (Request.Method.GET, "http://192.168.1.10:8000/bx6yjsa8v/get_category_items?category="+category, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            p1.setVisibility(View.GONE);
                            itemSearch.setVisibility(View.VISIBLE);
                            ArrayList<String> items = new ArrayList<>();
                            JSONArray arr = new JSONArray(response);
                            items.add("");
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                items.add(obj.getString("name"));
                            }
                            Collections.sort(items);

                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, items);
                            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            itemSearch.setAdapter((arrayAdapter));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        p1.setVisibility(View.GONE);
                        itemSearch.setVisibility(View.INVISIBLE);
                        System.out.println(error.getMessage());
                        Snackbar snackbar1 = Snackbar.make(view, "No server response", Snackbar.LENGTH_SHORT);
                        snackbar1.show();
                    }
                }) {
        };
        queue.add(stringRequest);
    }

    private void sendGetItemDetailsRequest(String itemName) {
        p2.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest
                (Request.Method.GET, "http://192.168.1.10:8000/bx6yjsa8v/get_item_details?name="+itemName, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            p2.setVisibility(View.GONE);
                            body.setVisibility(View.VISIBLE);
                            JSONObject item = new JSONObject(response);
                            name.setText(item.getString("name"));
                            specification.setText(item.getString("specifications"));
                            count.setText(item.getString("count"));
                            category.setSelection(item.getInt("category_id"));
                            id = Integer.parseInt(item.getString("id"));
                            if (item.getBoolean("image"))
                                new DownloadImageTask(item.getString("id")).execute();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        p2.setVisibility(View.GONE);
                        body.setVisibility(View.GONE);
                        Snackbar snackbar1 = Snackbar.make(view, "No server response", Snackbar.LENGTH_SHORT);
                        snackbar1.show();
                    }
                }) {
        };
        queue.add(stringRequest);
    }

    private void uploadImage(final String id) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.1.10:8000/bx6yjsa8v/upload_image", new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                outAnimation = new AlphaAnimation(1f, 0f);
                outAnimation.setDuration(200);
                loading.setAnimation(outAnimation);
                loading.setVisibility(View.GONE);
                addButton.setEnabled(true);
                imageButton.setEnabled(true);
                Snackbar snackbar1 = Snackbar.make(view, "Item updated", Snackbar.LENGTH_SHORT);
                snackbar1.show();
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                outAnimation = new AlphaAnimation(1f, 0f);
                outAnimation.setDuration(200);
                loading.setAnimation(outAnimation);
                loading.setVisibility(View.GONE);
                addButton.setEnabled(true);
                imageButton.setEnabled(true);
                System.out.println(error);
                error.printStackTrace();
                Snackbar snackbar1 = Snackbar.make(view, "Image uploading failed", Snackbar.LENGTH_SHORT);
                snackbar1.show();
            }
        }) {
            //adding parameters to send
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("image", imageString);
                parameters.put("id", id);
                parameters.put("directory", "items");
                return parameters;
            }
        };

        queue.add(request);
    }

    private void sendPostRequest(final String id) {
        StringRequest stringRequest = new StringRequest
                (Request.Method.POST, "http://192.168.1.10:8000/bx6yjsa8v/update_item", new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        if (imageChangeFlag) {
                            uploadImage(id);
                            imageChangeFlag = false;
                        }
                        else {
                            outAnimation = new AlphaAnimation(1f, 0f);
                            outAnimation.setDuration(200);
                            loading.setAnimation(outAnimation);
                            loading.setVisibility(View.GONE);
                            addButton.setEnabled(true);
                            imageButton.setEnabled(true);
                            System.out.println(response);
                            Snackbar snackbar1 = Snackbar.make(view, "Item updated", Snackbar.LENGTH_SHORT);
                            snackbar1.show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        outAnimation = new AlphaAnimation(1f, 0f);
                        outAnimation.setDuration(200);
                        loading.setAnimation(outAnimation);
                        loading.setVisibility(View.GONE);
                        addButton.setEnabled(true);
                        imageButton.setEnabled(true);
                        System.out.println(error);
                        Snackbar snackbar1 = Snackbar.make(view, "No response from server", Snackbar.LENGTH_SHORT);
                        snackbar1.show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name.getText().toString());
                params.put("specification", specification.getText().toString());
                params.put("count", count.getText().toString());
                params.put("category", category.getSelectedItem() != null ? ""+category.getSelectedItem().toString() : "");
                params.put("id",id);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        int id;

        public MyTask(int itemId) {
            id = itemId;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (getName().equals("") || getCount().equals("") || getCategory().equals("")) {
                Snackbar snackbar1 = Snackbar.make(view, "Please fill all fields", Snackbar.LENGTH_SHORT);
                snackbar1.show();
                cancel(true);
            }
            else {
                addButton.setEnabled(false);
                imageButton.setEnabled(false);
                inAnimation = new AlphaAnimation(0f, 1f);
                inAnimation.setDuration(200);
                loading.setAnimation(inAnimation);
                loading.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            clearInputs();
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (!isCancelled()) {
                sendPostRequest(""+this.id);
            }
            return null;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 111 && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            try {
                //getting image from gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), filePath);

                //Setting image to ImageView
                image.setImageBitmap(bitmap);
                imageChangeFlag = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private Bitmap DownloadImage(String URL) {
        Bitmap bitmap = null;
        InputStream in;
        try {
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return bitmap;
    }


    private InputStream OpenHttpConnection(String urlString) throws IOException {
        InputStream in = null;

        int response;

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");

        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            response = httpConn.getResponseCode();
            if (response == 200)
                in = httpConn.getInputStream();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return in;
    }



    class DownloadImageTask extends AsyncTask<Void, Integer, Void> {
        Bitmap bitmap = null;
        String imageName;
        public DownloadImageTask(String name) {
            imageName = name;
        }
        protected Void doInBackground(Void... urls) {
            bitmap = DownloadImage("http://192.168.1.10:8000/storage/items/"+this.imageName+".jpg");
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            if(bitmap != null) {
                image.setImageBitmap(bitmap);
            }
        }
    }

}
