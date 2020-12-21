package com.login.logindesign;

import android.app.ProgressDialog;
import android.content.Intent;
import android.opengl.ETC1;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Account extends AppCompatActivity {

    EditText name,username,password,mobile,confirmpassword,address;
    Button register;
   private ProgressBar progressBar;
   // final String url="http://192.168.43.249/watercan/register.php";
   private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_account );
        name = findViewById( R.id.name );
        username = findViewById( R.id.username );
        address = findViewById( R.id.address );

        password = findViewById( R.id.password );
        mobile = findViewById( R.id.mobile );
        confirmpassword = findViewById( R.id.confirmpassword );
        register = findViewById( R.id.register );


        mAuth = FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

//        userid = user.getUid();




        register.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Register();
            }
            private void Register() {
                final String name1 = name.getText().toString().trim();
                final String mobile1 = mobile.getText().toString().trim();
                final String password1 = password.getText().toString().trim();
                final String username1 = username.getText().toString().trim();
                final String address1 = address.getText().toString().trim();
                if (TextUtils.isEmpty(username1)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password1)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password1.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //progressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(username1,password1 )
                        .addOnCompleteListener( Account.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                               // progressBar.setVisibility( View.VISIBLE );
                                if(!task.isSuccessful()){
                                    Toast.makeText(Account.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();


                                }else {
                                  //  progressBar.setVisibility( View.GONE );
                                    DatabaseReference myRef = database.getReference("Register");
                                    String key=myRef.push().getKey();

                                    myRef.child( key ).child("Name" ).setValue( name1 );
                                    myRef.child( key ).child("Username" ).setValue( username1 );
                                    myRef.child( key ).child("Password" ).setValue( password1 );
                                    myRef.child( key ).child( "Mobile" ).setValue( mobile1 );
                                    myRef.child( key ).child( "Address" ).setValue( address1 );
                                    myRef.child( key ).child( "UserType" ).setValue( "d" );

                                    Toast.makeText( Account.this, "Successfully Registered", Toast.LENGTH_SHORT ).show();



                                }



                            }

                        } );






            }


            });

    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        progressBar.setVisibility( View.GONE );
//    }
}

