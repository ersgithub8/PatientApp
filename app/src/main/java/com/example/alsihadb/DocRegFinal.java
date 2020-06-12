package com.example.alsihadb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.alsihadb.JSON.MySingleton;

import java.util.HashMap;
import java.util.Map;

public class DocRegFinal extends AppCompatActivity {


    EditText mblnmbr,hname,hnumbr;
    Button reg;
    CheckBox terms,newsletter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_reg_final);

        mblnmbr=findViewById(R.id.mbl);

        hname=findViewById(R.id.hname);
        hnumbr=findViewById(R.id.hnumb);

        reg=findViewById(R.id.btn_reg);
        terms=findViewById(R.id.checkBox);
        newsletter=findViewById(R.id.checkBox2);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(terms.isChecked())
                {

                    if(mblnmbr.getText().toString().equals("") && hname.getText().toString().equals("")&& hnumbr.getText().toString().equals(""))
                    {
                        Toast.makeText(DocRegFinal.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
                    }


                    else
                    {
                        sendIdToServer(getIntent().getStringExtra("fname"),getIntent().getStringExtra("lname"),getIntent().getStringExtra("pass"),getIntent().getStringExtra("email"),getIntent().getStringExtra("username"),
                                mblnmbr.getText().toString(),hname.getText().toString(),hnumbr.getText().toString());

                    }
                }
                else
                {
                    Toast.makeText(DocRegFinal.this, "Accept terms and condition", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void sendIdToServer(final String fname, final String lname, final String password, final String email, final String username, final String mbl, final String hname, final String hnumb) {
//Creating a progress dialog to show while it is storing the data on server
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering...");
        progressDialog.show();


        //getting the email entered

        //Creating a string request
        StringRequest req = new StringRequest(Request.Method.POST, "http://alseha.martoflahore.com/DocRegistration.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //dismissing the progress dialog
                        progressDialog.dismiss();
                        if(response.trim().equalsIgnoreCase("success"))
                        {
                            Toast.makeText(DocRegFinal.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(DocRegFinal.this,LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.putExtra("login","doc");
                            startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(DocRegFinal.this, response.trim(), Toast.LENGTH_SHORT).show();
                        }

                        //

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.dismiss();
                        error.printStackTrace();
                        Toast.makeText(DocRegFinal.this, "Something went wrong try again"+error, Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //adding parameters to post request as we need to send firebase id and email
                params.put("fname",fname);
                params.put("lname",lname);
                params.put("password", password);
                params.put("email", email);
                params.put("username",username);
                params.put("mbl",mbl);
                params.put("hname",hname);
                params.put("hnumb",hnumb);


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
}
