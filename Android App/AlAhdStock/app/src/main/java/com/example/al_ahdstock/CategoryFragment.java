package com.example.al_ahdstock;

import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText name;
    private FrameLayout loading;
    private ImageView image;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    private Button imageButton,addButton;
    private RequestQueue queue;
    View view;
    Bitmap bitmap;
    boolean imageFlag = false;

    // TODO: Rename and change types of parameters


    public CategoryFragment() {
        // Required empty public constructor
    }
    private String getName(){
        return name.getText().toString();
    }
    private void clearInputs() {
        name.setText("");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
   //     args.putString(ARG_PARAM1, param1);
   //     args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
   //         mParam1 = getArguments().getString(ARG_PARAM1);
   //         mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_category, container, false);
        name = (EditText) view.findViewById(R.id.nameText2);
        addButton = (Button) view.findViewById(R.id.addButton2);
        loading = (FrameLayout) view.findViewById(R.id.progressBarHolder2);
        imageButton = (Button) view.findViewById(R.id.imageButton2);
        System.out.println(imageButton.getText());
        image = (ImageView) view.findViewById(R.id.image2);
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("hi");
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 112);
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CategoryFragment.MyTask().execute();
            }
        });

        return view;
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
                parameters.put("directory", "categories");
                return parameters;
            }
        };

        queue.add(request);
    }

    private void sendPostRequest() {
        StringRequest stringRequest = new StringRequest
                (Request.Method.POST, "http://192.168.1.10:8000/bx6yjsa8v/add_category", new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        if (!response.equals("failed")) {
                            uploadImage(response);
                            imageFlag = false;
                        }
                        else
                        {
                            outAnimation = new AlphaAnimation(1f, 0f);
                            outAnimation.setDuration(200);
                            loading.setAnimation(outAnimation);
                            loading.setVisibility(View.GONE);
                            addButton.setEnabled(true);
                            imageButton.setEnabled(true);
                            Snackbar snackbar1 = Snackbar.make(view, "Category already exists", Snackbar.LENGTH_SHORT);
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
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (getName().equals("") || !imageFlag) {
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

        if (requestCode == 112 && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
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
