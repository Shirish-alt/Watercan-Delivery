package com.login.logindesign;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Client extends AppCompatActivity {
    Button inc,dec,qrcode,placeorder;
    TextView count,itemprise,deliverycharge,finalprise,datetextview,name,address,mobile;
    int incdecCount=0,itempriseCount=0,deliverychargeCount=0,finalpriseCount=0,mm,dd,yy;
    ImageView date,qrimage;
    RadioGroup radioGroup;
    EditText cash,paytm;
    RadioButton radioButton1,radioButton2;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    String id;
    //final String httpurl="http://192.168.43.249/watercan/ClientDeliveryInfo.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_client );




        cash=findViewById( R.id.cash );
        paytm=findViewById( R.id.paytm );
        placeorder=findViewById( R.id.placeorder );
        inc=findViewById( R.id.inc );
        qrimage=findViewById( R.id.qrimage );
        datetextview=findViewById( R.id.datetextview );
        date=findViewById( R.id.date );
        qrcode=findViewById( R.id.qrcode );
        mobile=findViewById( R.id.mobile );


        name=findViewById( R.id.name );
       name.setText( LoginActivity.userData.get( 0 ).getName() );
        address=findViewById( R.id.address );
    // address.setText(LoginActivity.userData.get( 0 ).getAddress() );
       dec=findViewById( R.id.dec );
        count=findViewById( R.id.count );
        itemprise=findViewById( R.id.itemprice );
        deliverycharge=findViewById( R.id.deliverycharge );
        finalprise=findViewById( R.id.finalprice );
        radioGroup=findViewById( R.id.radiogroup );
        radioButton1=findViewById( R.id.radiobutton1 );
        radioButton2=findViewById( R.id.radiobutton2 );


        inc.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incdecCount++;
                count.setText(String.valueOf( incdecCount ) );
                itempriseCount+=30;
                itemprise.setText( String.valueOf( itempriseCount ) );
                deliverychargeCount+=10;
                deliverycharge.setText( String.valueOf( deliverychargeCount ) );

                double num=Double.parseDouble( itemprise.getText().toString() );
                double num1=Double.parseDouble( deliverycharge.getText().toString() );

                double sum=num+num1;
                finalprise.setText( Double.toString( sum ) );







            }
        } );
        dec.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incdecCount--;
                count.setText(String.valueOf( incdecCount ) );
                itempriseCount-=30;
                itemprise.setText( String.valueOf( itempriseCount ) );
                deliverychargeCount-=10;
                deliverycharge.setText( String.valueOf( deliverychargeCount ) );

                double num=Double.parseDouble( itemprise.getText().toString() );
                double num1=Double.parseDouble( deliverycharge.getText().toString() );

                double sum=num+num1;
                finalprise.setText( Double.toString( sum ) );





            }
        } );
        date.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                yy = calendar.get(Calendar.YEAR);
                mm = calendar.get(Calendar.MONTH);
                dd = calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Client.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet
                                    (DatePicker view, int year, int month, int dayOfMonth) {

                                datetextview.setText( (year  + "/" + (month+1) + "/" + dayOfMonth));

                            }

                        }, yy, mm, dd);
                //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);

                datePickerDialog.show();


            }
        } );

        radioGroup.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
               // textViewChoice.setText("You Selected " + rb.getText());

               switch (i){
                   case R.id.radiobutton1:
                       cash.setVisibility( View.VISIBLE );
                       paytm.setVisibility( View.GONE );
                       break;
                   case R.id.radiobutton2:
                       cash.setVisibility( View.GONE );
                       paytm.setVisibility( View.VISIBLE );

                       break;
               }

            }
        } );


       placeorder.setOnClickListener( new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               StoreData();
           }
       } );

    }

    private void createQR(String id){
        Log.e("create ","create");
        String v=name.getText().toString();
        // String id = LoginActivity.userData.get( 0 ).getId();
   //     address.setText( LoginActivity.userData.get( 0 ).getAddress() );
        String a=address.getText().toString();
     //   String q=LoginActivity.userData.get( 0 ).getMobile();
        String Cash1=cash.getText().toString().trim();
        String data=id+","+"Name:"+v+","+"Address :"+a+","+"Moblie :"+","+"Amount Paid :"+","+Cash1;
        Toast.makeText( Client.this, ""+data, Toast.LENGTH_SHORT ).show();

        qrimage.setVisibility( View.VISIBLE );
        if(data!=null && !data.isEmpty()){
            try{
                MultiFormatWriter multiFormatWriter=new MultiFormatWriter();
                BitMatrix bitMatrix=multiFormatWriter.encode(data, BarcodeFormat.QR_CODE,500,500);
                BarcodeEncoder barcodeEncoder=new BarcodeEncoder();
                Bitmap bitmap=barcodeEncoder.createBitmap(bitMatrix);
                qrimage.setImageBitmap(bitmap);


            }catch (WriterException e) {
                e.printStackTrace();
            }
        }

    }
    private void StoreData(){

        final String NoOfCans=this.count.getText().toString().trim();
        final String FinalPrise=this.finalprise.getText().toString().trim();
         final String Date=datetextview.getText().toString().trim();
         final String Cash=cash.getText().toString().trim();
       //  final   String q=LoginActivity.userData.get( 0 ).getMobile();


        String abc="NooofCans"+NoOfCans+"\n"+FinalPrise+"\n"+Date;
        Toast.makeText( this, "INFO:"+abc, Toast.LENGTH_SHORT ).show();

        StringRequest stringRequest = new StringRequest( Request.Method.POST, httpurl,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e( "responce",response );
//                            JSONObject jsonObject = new JSONObject( response );
                            // Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                            //String success=jsonObject.getString("success");
                            if(!response.equals( "error" )){
                                Toast.makeText( Client.this, "SuccessFully Inserted", Toast.LENGTH_SHORT ).show();
                                id = response;
                                createQR( id );
                            }else {
                                Toast.makeText( Client.this, "Something went wrong..", Toast.LENGTH_SHORT ).show();
                            }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),String.valueOf(error), Toast.LENGTH_LONG).show();
                        Log.e(String.valueOf(error),"error");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
              //  params.put( "clientid",LoginActivity.userData.get( 0 ).getId() );
                params.put("NoOfCans",NoOfCans);
                params.put("FinalPrise",FinalPrise);
                params.put("Date",Date);
                params.put( "AmountType",Cash );
               // params.put("Mobile",q);
                return params;
            }
        };
        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);
   }



}
