package com.example.alsihadb;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.example.alsihadb.JSON.CustomRequest;
import com.example.alsihadb.JSON.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class AddPatient extends AppCompatActivity {


    Toolbar toolbar;
    RecyclerView recyclerView;
    AddPatientAdpater adapter;
    List<PatientItem> patientItems=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        toolbar=findViewById(R.id.toolbar);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getIntent().getStringExtra("activity").equals("add"))
        {
            getSupportActionBar().setTitle("Add Patient");
        }
        else
            getSupportActionBar().setTitle("Remove Patient");


        recyclerView=findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        SharedPreferences sharedPreferences = getSharedPreferences("userdata", MODE_PRIVATE);

        if(getIntent().getStringExtra("activity").equals("add"))
        {
            getPatient(sharedPreferences.getString("username","null"));
        }
        else
        PatientList(sharedPreferences.getString("username","null"));

    }

    public  void getData(final String username,String patid, final String action) {

        final android.app.AlertDialog loading = new ProgressDialog(AddPatient.this);
        loading.setMessage("Adding...");
        loading.show();
        //Creating json array request

        String url=null;

        Map<String, String> params = new Hashtable<String, String>();
        params.put("username", username);
        params.put("patid",patid);
        if(action.equals("add"))
        {
            url="http://alseha.martoflahore.com/addPatient.php";
        }
        else
        {
            url="http://alseha.martoflahore.com/removePatient.php";
        }

        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                loading.dismiss();


                parseData(response,action);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();

            }
        });


        jsonRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonRequest);
    }

    private void parseData(JSONObject response,String action) {
        JSONArray students = null;
        try {
            students = response.getJSONArray("data");
        } catch (JSONException e) {
        }

        assert students != null;
        patientItems.clear();
        if(adapter!=null)
        {
            adapter.notifyDataSetChanged();
        }


        for (int i = 0; i < students.length(); i++) {
            PatientItem item=new PatientItem();
            try {
                JSONObject student = students.getJSONObject(i);

                {


                    item.setId(student.getString("id"));
                    item.setUsername(student.getString("username"));

                }


            } catch (JSONException e) {

            }

            patientItems.add(item);
        }

        adapter=new AddPatientAdpater(patientItems,action,AddPatient.this);

        recyclerView.setAdapter(adapter);

        ///////////
    }



    public  void getPatient(final String username) {

        final android.app.AlertDialog loading = new ProgressDialog(AddPatient.this);
        loading.setMessage("Getting Data...");
        loading.show();
        //Creating json array request



        Map<String, String> params = new Hashtable<String, String>();
        params.put("username", username);


        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, "http://alseha.martoflahore.com/getPatient.php", params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                loading.dismiss();


                parseData2(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();

            }
        });


        jsonRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonRequest);
    }

    private void parseData2(JSONObject response) {
        JSONArray students = null;
        try {
            students = response.getJSONArray("data");
        } catch (JSONException e) {
        }

        assert students != null;

        patientItems.clear();

        for (int i = 0; i < students.length(); i++) {
            PatientItem item=new PatientItem();
            try {
                JSONObject student = students.getJSONObject(i);

                {


                    item.setId(student.getString("id"));
                    item.setUsername(student.getString("username"));

                }


            } catch (JSONException e) {

            }

            patientItems.add(item);
        }

        adapter=new AddPatientAdpater(patientItems,"add",AddPatient.this);
        recyclerView.setAdapter(adapter);

        ///////////
    }

    ///////////remove

    public  void PatientList(final String username) {

        final android.app.AlertDialog loading = new ProgressDialog(AddPatient.this);
        loading.setMessage("Getting Data...");
        loading.show();
        //Creating json array request



        Map<String, String> params = new Hashtable<String, String>();
        params.put("username", username);


        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, "http://alseha.martoflahore.com/myPatient.php", params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                loading.dismiss();


                parseData3(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();

            }
        });


        jsonRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonRequest);
    }

    private void parseData3(JSONObject response) {
        JSONArray students = null;
        try {
            students = response.getJSONArray("data");
        } catch (JSONException e) {
        }

        assert students != null;

        patientItems.clear();

        for (int i = 0; i < students.length(); i++) {
            PatientItem item=new PatientItem();
            try {
                JSONObject student = students.getJSONObject(i);

                {


                    item.setId(student.getString("id"));
                    item.setUsername(student.getString("username"));

                }


            } catch (JSONException e) {

            }

            patientItems.add(item);
        }

        adapter=new AddPatientAdpater(patientItems,"remove",AddPatient.this);
        recyclerView.setAdapter(adapter);

        ///////////
    }


}
