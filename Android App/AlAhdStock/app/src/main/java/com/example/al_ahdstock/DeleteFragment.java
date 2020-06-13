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
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeleteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeleteFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Spinner itemCategory, item, category;
    private RequestQueue queue;
    private Button deleteItemButton, deleteCategoryButton;
    private FrameLayout loading;
    private ProgressBar p1;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    View view;



    // TODO: Rename and change types of parameters

    public DeleteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DeleteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeleteFragment newInstance() {
        DeleteFragment fragment = new DeleteFragment();
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
        view = inflater.inflate(R.layout.fragment_delete, container, false);
        category = (Spinner) view.findViewById(R.id.cateSpinner);
        itemCategory = (Spinner) view.findViewById(R.id.cateSearchSpinner);
        item = (Spinner) view.findViewById(R.id.itemSearchSpinner);
        deleteCategoryButton = (Button) view.findViewById(R.id.deleteCategoryButton);
        loading = (FrameLayout) view.findViewById(R.id.progressBarHolder);
        deleteItemButton = (Button) view.findViewById(R.id.deleteItemButton);
        p1 = (ProgressBar) view.findViewById(R.id.progressBar);
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        sendGetCategoriesRequest();

        deleteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyTask(MyTask.ITEM).execute();
            }
        });
        deleteCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyTask(MyTask.CATEGORY).execute();
            }
        });

        itemCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0)
                    sendGetItemsRequest(parent.getItemAtPosition(position).toString());
                else
                    item.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                item.setVisibility(View.INVISIBLE);
            }
        });

        return view;
    }


    private String getCategory() {
        return category.getSelectedItem().toString();
    }

    private String getItem() {
        return item.getSelectedItem().toString();
    }


    private void clearInputs() {
        category.setSelection(0);
        itemCategory.setSelection(0);
        item.setSelection(0);
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
                            itemCategory.setAdapter((arrayAdapter));
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
                            item.setVisibility(View.VISIBLE);
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
                            item.setAdapter((arrayAdapter));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        p1.setVisibility(View.GONE);
                        item.setVisibility(View.INVISIBLE);
                        System.out.println(error.getMessage());
                        Snackbar snackbar1 = Snackbar.make(view, "No server response", Snackbar.LENGTH_SHORT);
                        snackbar1.show();
                    }
                }) {
        };
        queue.add(stringRequest);
    }

    private void sendDeleteItemRequest() {
        StringRequest stringRequest = new StringRequest
                (Request.Method.POST, "http://192.168.1.10:8000/bx6yjsa8v/delete_item", new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        outAnimation = new AlphaAnimation(1f, 0f);
                        outAnimation.setDuration(200);
                        loading.setAnimation(outAnimation);
                        loading.setVisibility(View.GONE);
                        deleteCategoryButton.setEnabled(true);
                        deleteItemButton.setEnabled(true);
                        Snackbar snackbar1 = Snackbar.make(view, response, Snackbar.LENGTH_SHORT);
                        snackbar1.show();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        outAnimation = new AlphaAnimation(1f, 0f);
                        outAnimation.setDuration(200);
                        loading.setAnimation(outAnimation);
                        loading.setVisibility(View.GONE);
                        deleteCategoryButton.setEnabled(true);
                        deleteItemButton.setEnabled(true);
                        System.out.println(error);
                        Snackbar snackbar1 = Snackbar.make(view, "No response from server", Snackbar.LENGTH_SHORT);
                        snackbar1.show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", item.getSelectedItem().toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void sendDeleteCategoryRequest() {
        StringRequest stringRequest = new StringRequest
                (Request.Method.POST, "http://192.168.1.10:8000/bx6yjsa8v/delete_category", new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        outAnimation = new AlphaAnimation(1f, 0f);
                        outAnimation.setDuration(200);
                        loading.setAnimation(outAnimation);
                        loading.setVisibility(View.GONE);
                        deleteCategoryButton.setEnabled(true);
                        deleteItemButton.setEnabled(true);
                        Snackbar snackbar1 = Snackbar.make(view, response, Snackbar.LENGTH_SHORT);
                        snackbar1.show();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        outAnimation = new AlphaAnimation(1f, 0f);
                        outAnimation.setDuration(200);
                        loading.setAnimation(outAnimation);
                        loading.setVisibility(View.GONE);
                        deleteCategoryButton.setEnabled(true);
                        deleteItemButton.setEnabled(true);
                        System.out.println(error);
                        Snackbar snackbar1 = Snackbar.make(view, "No response from server", Snackbar.LENGTH_SHORT);
                        snackbar1.show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", category.getSelectedItem().toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        public static final int ITEM = 0;
        public static final int CATEGORY = 1;
        int toDelete;

        public MyTask(int mode) {
            toDelete = mode;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (toDelete == ITEM) {
                if (getItem().equals("")) {
                    Snackbar snackbar1 = Snackbar.make(view, "Please choose item to delete", Snackbar.LENGTH_SHORT);
                    snackbar1.show();
                    cancel(true);
                } else {
                    deleteCategoryButton.setEnabled(false);
                    deleteItemButton.setEnabled(false);
                    inAnimation = new AlphaAnimation(0f, 1f);
                    inAnimation.setDuration(200);
                    loading.setAnimation(inAnimation);
                    loading.setVisibility(View.VISIBLE);
                }
            }
            else if (toDelete == CATEGORY) {
                if (getCategory().equals("")) {
                    Snackbar snackbar1 = Snackbar.make(view, "Please choose category to delete", Snackbar.LENGTH_SHORT);
                    snackbar1.show();
                    cancel(true);
                } else {
                    deleteCategoryButton.setEnabled(false);
                    deleteItemButton.setEnabled(false);
                    inAnimation = new AlphaAnimation(0f, 1f);
                    inAnimation.setDuration(200);
                    loading.setAnimation(inAnimation);
                    loading.setVisibility(View.VISIBLE);
                }
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
                switch (toDelete) {
                    case ITEM :
                        sendDeleteItemRequest();
                        break;
                    case CATEGORY:
                        sendDeleteCategoryRequest();
                        break;
                }
            }
            return null;
        }
    }


}
