package com.example.al_ahdstock;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText name, specification, count;
    private Spinner category;
    private RequestQueue queue;
    private Button imageButton,addButton;
    private FrameLayout loading;
    private ImageView image;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    String myLog = "myLog";
    View view;
    Bitmap bitmap;
    boolean imageFlag = false;


    // TODO: Rename and change types of parameters

    public ItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemFragment newInstance() {
        ItemFragment fragment = new ItemFragment();
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
        view = inflater.inflate(R.layout.fragment_item, container, false);
        name = (EditText) view.findViewById(R.id.nameText);
        specification = (EditText) view.findViewById(R.id.specText);
        count = (EditText) view.findViewById(R.id.countText);
        category = (Spinner) view.findViewById(R.id.cateSpinner);
        addButton = (Button) view.findViewById(R.id.addButton);
        loading = (FrameLayout) view.findViewById(R.id.progressBarHolder);
        imageButton = (Button) view.findViewById(R.id.imageButton);
        image = (ImageView) view.findViewById(R.id.image);
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        sendGetRequest();

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
                new MyTask().execute();
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
    }

    private void sendGetRequest() {
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
                Snackbar snackbar1 = Snackbar.make(view, "Item added", Snackbar.LENGTH_SHORT);
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

    private void sendPostRequest() {
        StringRequest stringRequest = new StringRequest
                (Request.Method.POST, "http://192.168.1.10:8000/bx6yjsa8v/add_item", new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        if (!response.equals("failed"))
                            if (imageFlag) {
                                uploadImage(response);
                                imageFlag = false;
                            }
                        else {
                            outAnimation = new AlphaAnimation(1f, 0f);
                            outAnimation.setDuration(200);
                            loading.setAnimation(outAnimation);
                            loading.setVisibility(View.GONE);
                            addButton.setEnabled(true);
                            imageButton.setEnabled(true);
                            Snackbar snackbar1 = Snackbar.make(view, "Item already exists", Snackbar.LENGTH_SHORT);
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
                params.put("category", category.getSelectedItem() != null ? category.getSelectedItem().toString() : "");
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

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
        //    clearInputs();
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (!isCancelled()) {
                sendPostRequest();
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
                imageFlag = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}
