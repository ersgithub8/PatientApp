package com.example.alsihadb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    Toolbar toolbar;
    EditText fname,lname,email,pass,cpass,username;
    Button btn_nxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fname=findViewById(R.id.fname);
        lname=findViewById(R.id.lname);
        email=findViewById(R.id.email);
        pass=findViewById(R.id.pass);
        cpass=findViewById(R.id.cfrm);
        username=findViewById(R.id.username);
        btn_nxt=findViewById(R.id.btn_nxt);

        btn_nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fname.getText().toString().equals("")&&lname.getText().toString().equals("")&&email.getText().toString().equals("")
                        &&pass.getText().toString().equals("")&&cpass.getText().toString().equals("")&&username.getText().toString().equals(""))
                {
                    Toast.makeText(Register.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
                }
                else if(!pass.getText().toString().equals(cpass.getText().toString()))
                {
                    Toast.makeText(Register.this, "Password not match", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    Intent i=new Intent(Register.this,RegisterFinal.class);
                    i.putExtra("fname",fname.getText().toString());
                    i.putExtra("lname",lname.getText().toString());
                    i.putExtra("email",email.getText().toString());
                    i.putExtra("pass",pass.getText().toString());
                    i.putExtra("username",username.getText().toString());
                    startActivity(i);

                }
            }
        });


    }

}
