package com.example.alsihadb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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

public class DocMain extends AppCompatActivity {

    Button logout,add,remove;
    SessionManagment managment;
    RecyclerView recyclerView;
    listAdapter adapter;
    List<PatientItem> patientItems=new ArrayList<>();
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_main);

        add=findViewById(R.id.add);
        remove=findViewById(R.id.remove);
        recyclerView=findViewById(R.id.re);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Alsiha");


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(DocMain.this,AddPatient.class);
                i.putExtra("activity","add");
                startActivity(i);
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(DocMain.this,AddPatient.class);
                i.putExtra("activity","remove");
                startActivity(i);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        managment=new SessionManagment();
        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                managment.setPreferences(DocMain.this, "status", "0");
                Intent i1 = new Intent(DocMain.this, ChooseType.class);
                i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i1);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences=getSharedPreferences("userdata",MODE_PRIVATE);

        if(patientItems!=null)
        {
            patientItems.clear();
            getData(sharedPreferences.getString("username","null"));
        }
        else
        {
            getData(sharedPreferences.getString("username","null"));
        }

    }

    private void getData(final String username) {

        final android.app.AlertDialog loading = new ProgressDialog(DocMain.this);
        loading.setMessage("Getting Data...");
        loading.show();
        //Creating json array request



        Map<String, String> params = new Hashtable<String, String>();
        params.put("username", username);

        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, "http://alseha.martoflahore.com/myPatient.php", params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                loading.dismiss();


                parseData(response);
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

    private void parseData(JSONObject response) {
        JSONArray students = null;
        try {
            students = response.getJSONArray("data");
        } catch (JSONException e) {
        }

        assert students != null;


        for (int i = 0; i < students.length(); i++) {
            PatientItem item=new PatientItem();
            try {
                JSONObject student = students.getJSONObject(i);

                {


                    item.setId(student.getString("id"));
                    item.setUsername(student.getString("username"));
                    item.setEmail(student.getString("email"));
                }


            } catch (JSONException e) {

            }

            patientItems.add(item);
        }

        adapter=new listAdapter(patientItems,getApplicationContext());
        recyclerView.setAdapter(adapter);

        ///////////
    }

}
