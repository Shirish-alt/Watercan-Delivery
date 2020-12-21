package com.login.logindesign;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    TextView signup;
    EditText username, password;
    Button login;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseUser user;
    public static List<UserInformation> userData;  //static variable can be access from all class by LoginActivity.userData
    // final String httpurl="http://192.168.43.249/watercan/ClientDeliveryboylogin.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        signup = findViewById( R.id.signup );
        username = findViewById( R.id.username );
        password = findViewById( R.id.password );
        login = findViewById( R.id.login );

        auth = FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();


        signup.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( LoginActivity.this, Account.class );
                startActivity( intent );
            }
        } );
        login.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String musername = username.getText().toString().trim();
                final String mpassword = password.getText().toString().trim();
                if (TextUtils.isEmpty( musername )) {
                    Toast.makeText( getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT ).show();
                    return;
                }

                if (TextUtils.isEmpty( mpassword )) {
                    Toast.makeText( getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT ).show();
                    return;
                }
                auth.createUserWithEmailAndPassword( musername,mpassword )
                        .addOnCompleteListener( LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                if (mpassword.length() < 6) {
                                    password.setError("Password too short, enter minimum 6 characters!");
                                } else {
                                    Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                }
                            }
                            else {
                                Log.e("error","msg");
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                user = FirebaseAuth.getInstance().getCurrentUser();
                                final String loggedinUserid = user.getEmail();
                                Query query = ref.child  ("Register").orderByChild( "Username" ).equalTo( loggedinUserid ) ;
                                query.addListenerForSingleValueEvent( new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@androidx.annotation.NonNull DataSnapshot dataSnapshot) {

                                        LoginModel loginModel = dataSnapshot.getValue(LoginModel.class);

                                        for(DataSnapshot postDatasnap : dataSnapshot.getChildren()){
                                           // model = postDatasnap.getValue(Model.class);
                                            //  Log.e( "users", " "+));

                                            if(postDatasnap.child( "UserType" ).getValue().equals( "d" )){
                                                startActivity( new Intent( LoginActivity.this,Deliveryboy.class) );
                                                Toast.makeText( LoginActivity.this, "Helloooo", Toast.LENGTH_SHORT ).show();
                                            }
                                            else if(postDatasnap.child( "UserType" ).getValue().equals( "c" )){
                                             //   userData.add( new LoginModel( jsonobj.getString("id")............. ) );

                                                startActivity( new Intent( LoginActivity.this, Client.class) );



                                            }
                                        }





                                    }

                                    @Override
                                    public void onCancelled(@androidx.annotation.NonNull DatabaseError databaseError) {}
                                });


                            }//else end..








                            }
                        } );

            }
        } );

    }
}
