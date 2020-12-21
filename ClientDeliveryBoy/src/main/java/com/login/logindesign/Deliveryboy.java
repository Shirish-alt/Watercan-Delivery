package com.login.logindesign;

import android.app.Dialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Deliveryboy extends AppCompatActivity {

    ImageView imageView;
    Button scanner,submit;
    final String url="http://192.168.43.249/demo/update.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_deliveryboy );
        imageView=findViewById( R.id.qrimage );
        scanner=findViewById( R.id.scanner );
        submit=findViewById( R.id.submit );


        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator intentIntegrator=new IntentIntegrator(Deliveryboy.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.setCameraId(0);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setPrompt("Scanning");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setBarcodeImageEnabled(true);
                intentIntegrator.initiateScan();
            }
        });
        submit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        } );

    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        final IntentResult intentResult=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(intentResult!=null && intentResult.getContents()!=null){


            Log.e("asjdbad",intentResult.getContents());
            //Here whatever want to show after scanning...
           ViewGroup viewGroup = findViewById(android.R.id.content);

           //then we will inflate the custom alert dialog xml that we created
          View dialogView = LayoutInflater.from(this).inflate(R.layout.my_dialog, viewGroup, false);

            TextView b =dialogView.findViewById( R.id.et );
            TextView c=dialogView.findViewById( R.id.et2 );
           //Now we need an
            AlertDialog.Builder objectAlertDialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
           //setting the view of the builder to our custom view that we already inflated
           builder.setView(dialogView);


//            //finally creating the alert dialog and displaying it
           AlertDialog alertDialog = builder.create();
            final String a[] = intentResult.getContents().split( "," );
            b.setText( a[0] );
            c.setText( a[1] );
            //   alertDialog.show();

//            Intent intent=new Intent( Deliveryboy.this, Display.class );
//            intent.putExtra( "Data",intentResult.getContents() );
//            startActivity( intent );
//
            final TextView info=findViewById( R.id.info );
            info.setText( intentResult.getContents());
            submit.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    String sr=info.getText().toString().trim();
//                   Intent intent=new Intent( Deliveryboy.this, Display.class );
//            intent.putExtra( "Data",sr);
//            startActivity( intent );
                    Toast.makeText( Deliveryboy.this, "Successfullyy Submitted", Toast.LENGTH_SHORT ).show();

                    StringRequest stringRequest = new StringRequest( Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.e("responce",response);
                                    try {
                                        JSONObject jsonObject=new JSONObject(response);
                                        String success=jsonObject.getString("success");
                                        if(success.equals("1")){
                                            Toast.makeText(Deliveryboy.this, "Scanned Successfully", Toast.LENGTH_SHORT).show();

//                                            Intent intent=new Intent(Deliveryboy.this,LoginActivity.class);
//                                            startActivity(intent);

                                        }else {


                                            Toast.makeText(Deliveryboy.this,"Something is wrong"+response.toString(),Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(Deliveryboy.this, "Failed"+e.toString(), Toast.LENGTH_SHORT).show();

                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Deliveryboy.this,"Scanned Error"+error.toString(),Toast.LENGTH_SHORT).show();


                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("Id", a[0]);

                            return params;
                        }
                    };
                    RequestQueue requestQueue= Volley.newRequestQueue( Deliveryboy.this ) ;
                    requestQueue.add(stringRequest);



                }
            } );







       }
        super.onActivityResult(requestCode, resultCode, data);
    }



}

