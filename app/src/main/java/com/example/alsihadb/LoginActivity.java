package com.example.alsihadb;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Hashtable;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class LoginActivity extends AppCompatActivity {

    EditText emial,pass;
    TextView reg;
    Button login;
    SessionManagment managment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        managment=new SessionManagment();
        emial=findViewById(R.id.et_mail);
        pass=findViewById(R.id.et_pass);
        reg=findViewById(R.id.tv_reg);
        login=findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail,password;
                mail=emial.getText().toString();
                password=pass.getText().toString();

                if(mail.equals("") && password.equals(""))
                {
                    Toast.makeText(LoginActivity.this, "Fill the fields", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    if(getIntent().getStringExtra("login").equals("pat"))
                    {
                        getData(mail,password);
                    }
                    else
                    {
                        getData2(mail,password);
                    }

                }
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getIntent().getStringExtra("login").equals("pat")) {
                    Intent i = new Intent(LoginActivity.this, Register.class);
                    startActivity(i);
                }
                else
                {
                    Intent i = new Intent(LoginActivity.this, DocRegistration.class);
                    startActivity(i);
                }
            }
        });

        handleSSLHandshake();


    }

    private void getData(final String email, String pass) {

        final android.app.AlertDialog loading = new ProgressDialog(LoginActivity.this);
        loading.setMessage("Checking...");
        loading.show();
        //Creating json array request



        Map<String, String> params = new Hashtable<String, String>();
        params.put("email", email);
        params.put("password", pass);
        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, "http://alseha.martoflahore.com/Login.php", params, new Response.Listener<JSONObject>() {
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

        if (students.length() == 0) {
            Toast.makeText(this, "wrong email and password", Toast.LENGTH_SHORT).show();
        }
        for (int i = 0; i < students.length(); i++) {

            try {
                JSONObject student = students.getJSONObject(i);

                {
                    SharedPreferences sharedPreferences = getSharedPreferences("userdata", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", student.getString("email"));
                    editor.putString("id", student.getString("id"));
                    editor.putString("username",student.getString("username"));
                    editor.putString("first",student.getString("first"));
                    editor.putString("login","pat");
                    editor.apply();

                    managment.setPreferences(LoginActivity.this, "status", "1");

                    if(student.getString("first").equals("yes"))
                    {
                        Intent i1 = new Intent(LoginActivity.this, AdditionInfo.class);
                        i1.putExtra("username",student.getString("username"));
                        i1.putExtra("id",student.getString("id"));
                        i1.putExtra("from","log");
                        startActivity(i1);
                    }
                    else
                    {
                        Intent i1 = new Intent(LoginActivity.this, PateintMain.class);
                        i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i1);
                    }

                }


            } catch (JSONException e) {

            }

        }

        ///////////
    }

    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }

    private void getData2(final String email, String pass) {

        final android.app.AlertDialog loading = new ProgressDialog(LoginActivity.this);
        loading.setMessage("Checking...");
        loading.show();
        //Creating json array request



        Map<String, String> params = new Hashtable<String, String>();
        params.put("email", email);
        params.put("password", pass);
        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, "http://alseha.martoflahore.com/DocLogin.php", params, new Response.Listener<JSONObject>() {
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

        if (students.length() == 0) {
            Toast.makeText(this, "wrong email and password", Toast.LENGTH_SHORT).show();
        }
        for (int i = 0; i < students.length(); i++) {

            try {
                JSONObject student = students.getJSONObject(i);

                {
                    SharedPreferences sharedPreferences = getSharedPreferences("userdata", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", student.getString("email"));
                    editor.putString("id", student.getString("id"));
                    editor.putString("login","doc");
                    editor.putString("username",student.getString("username"));
                    editor.apply();

                    managment.setPreferences(LoginActivity.this, "status", "1");


                    {
                        Intent i1 = new Intent(LoginActivity.this, DocMain.class);
                        i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i1);
                    }

                }


            } catch (JSONException e) {

            }

        }

        ///////////
    }

}
