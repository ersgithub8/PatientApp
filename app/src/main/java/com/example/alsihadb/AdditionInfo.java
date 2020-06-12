package com.example.alsihadb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.alsihadb.JSON.CustomRequest;
import com.example.alsihadb.JSON.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class AdditionInfo extends AppCompatActivity {

    EditText name,age,height,weight,dname,dnum,contact1,contact2;
    Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addition_info);
        name=findViewById(R.id.nam);
        age=findViewById(R.id.age);
        height=findViewById(R.id.height);
        weight=findViewById(R.id.weight);
        dname=findViewById(R.id.dname);
        dnum=findViewById(R.id.dnum);
        contact1=findViewById(R.id.contact1);
        contact2=findViewById(R.id.contact2);
        save=findViewById(R.id.save);
        if(getIntent().getStringExtra("from").equals("main"))
        {
            save.setVisibility(View.GONE);
            age.setFocusable(false);
            height.setFocusable(false);
            weight.setFocusable(false);
            dname.setFocusable(false);
            dnum.setFocusable(false);
            contact1.setFocusable(false);
            contact2.setFocusable(false);
            getData();


        }
        else
        {
            name.setText(getIntent().getStringExtra("username"));
        }



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(age.getText().toString().equals("") && height.getText().toString().equals("") && weight.getText().toString().equals("")
                        && dname.getText().toString().equals("") && dname.getText().toString().equals("") && contact1.getText().toString().equals("") && contact2.getText().toString().equals(""))
                {
                    Toast.makeText(AdditionInfo.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    sendIdToServer(getIntent().getStringExtra("id"),age.getText().toString(),height.getText().toString(),weight.getText().toString(),
                            dname.getText().toString(),dnum.getText().toString(),contact1.getText().toString(),contact2.getText().toString());

                }
            }
        });
    }

    private void sendIdToServer(final String id, final String age, final String height, final String weight, final String dname, final String dnum, final String con1, final String con2) {
//Creating a progress dialog to show while it is storing the data on server
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating...");
        progressDialog.show();


        //getting the email entered

        //Creating a string request
        StringRequest req = new StringRequest(Request.Method.POST, "http://alseha.martoflahore.com/updateReg.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //dismissing the progress dialog
                        Toast.makeText(AdditionInfo.this, response, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        if(response.trim().equalsIgnoreCase("success"))
                        {
                            Toast.makeText(AdditionInfo.this, "Update successfully", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(AdditionInfo.this,PateintMain.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }

                        //

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.dismiss();
                        error.printStackTrace();
                        Toast.makeText(AdditionInfo.this, "Something went wrong try again"+error, Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //adding parameters to post request as we need to send firebase id and email
                params.put("id",id);
                params.put("age",age);
                params.put("height", height);
                params.put("weight", weight);
                params.put("dname",dname);
                params.put("dnum",dname);
                params.put("con1",con1);
                params.put("con2",con2);


                return params;
            }
        };

        req.setRetryPolicy(new RetryPolicy() {
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
        MySingleton.getInstance(this).addToRequestQueue(req);
    }



    private void getData() {

        final android.app.AlertDialog loading = new ProgressDialog(AdditionInfo.this);
        loading.setMessage("Checking...");
        loading.show();
        //Creating json array request


        SharedPreferences sharedPreferences = getSharedPreferences("userdata", MODE_PRIVATE);
        Map<String, String> params = new Hashtable<String, String>();
        params.put("id", sharedPreferences.getString("id","0"));
        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, "http://alseha.martoflahore.com/getData.php", params, new Response.Listener<JSONObject>() {
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

            try {
                JSONObject student = students.getJSONObject(i);

                {


                    name.setText(student.getString("username"));
                    age.setText(student.getString("age"));
                    height.setText(student.getString("height"));
                    weight.setText(student.getString("weight"));
                    dname.setText(student.getString("dname"));
                    dnum.setText(student.getString("dnumber"));
                    contact1.setText(student.getString("contact1"));
                    contact2.setText(student.getString("contact2"));


                }


            } catch (JSONException e) {

            }

        }

        ///////////
    }

}
