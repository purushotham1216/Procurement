package com.mine.procurement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.common.Utilities;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.HttpsTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class LoginActivty extends AppCompatActivity {

    private EditText textInputLayoutEmail, textInputLayoutpassword;
    private Button login;
    String emailInput, passwordInput;
    public static String imeiNo = "";
    TelephonyManager telephonyManager;
    String str_rescode;
    String str_result;
    String role_id;
    String resoonce_loan=null;
            JSONObject j = null;
    String user_id,session_id,serice_code,user_name,email_id,mobile_no;
    TextView textViewName;
    private CardView profile_cardId,upload_advt_id,view_advt_id;
    SharedPreferences sharePref;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;

   /* String[] permissionsRequired = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };*/

    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    private String lat = "", longi = "", add = "";
    private String Designation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activty);

        /*permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            check_permissions();*/

        textInputLayoutEmail = findViewById(R.id.login_user_id);
        textInputLayoutpassword = findViewById(R.id.login_password);

        textInputLayoutEmail.setText("9989997698");
        textInputLayoutpassword.setText("Sa@12345");

        /*textInputLayoutEmail.setText("HQ.02");
        textInputLayoutpassword.setText("Test@123");*/
        login = findViewById(R.id.login_btn);

        int imeiI = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE);
        if (imeiI == PackageManager.PERMISSION_GRANTED){
            telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            imeiNo = telephonyManager.getDeviceId();
        }else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},REQUEST_PERMISSION_SETTING);
        }

        System.out.println("email value---------->" + textInputLayoutEmail.getText().toString());


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateEmail();
                validatePassword();
         // new AuthTask().execute().get();
                String resoonce_loan = null;
                try {
                    resoonce_loan = new CheckLogin().execute().get();
                    sharePref = getApplicationContext().getSharedPreferences("procurement", 0);
                    Log.d("response-----",resoonce_loan);
                    //ramesh start
                    try
                    {

                        j = new JSONObject(resoonce_loan);
                        if(j.getInt("SuccessFlag")==1)
                        {
                        JSONObject jsonObject = new JSONObject(String.valueOf(resoonce_loan));

                        JSONArray jsonArray = jsonObject.getJSONArray("Data");
                        for( int i=0; i < jsonArray.length(); i++ )
                        {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            if(jsonObject1.getInt("Role")==32) {
                                SharedPreferences.Editor editor = sharePref.edit();
                                editor.putString("UserId", jsonObject1.getString("UserId").trim());
                               // editor.putString("session_id",session_id);
                                System.out.println("username1------------------>"+jsonObject1.getString("OfficerNAME"));
                                editor.putString("OfficerNAME",jsonObject1.getString("OfficerNAME"));
                                editor.putString("Designation",jsonObject1.getString("Designation"));
                                editor.putString("MobileNo",jsonObject1.getString("MobileNo"));

                                editor.commit();
                                startActivity(new Intent(LoginActivty.this, MainActivity.class));
                            }
                            else if(jsonObject1.getInt("Role")==4) {
                                SharedPreferences.Editor editor = sharePref.edit();
                                editor.putString("UserIdd", jsonObject1.getString("UserId").trim());
                                // editor.putString("session_id",session_id);
                                System.out.println("username1------------------>"+jsonObject1.getString("OfficerNAME"));
                                editor.putString("OfficerNAMEE",jsonObject1.getString("OfficerNAME"));
                                editor.putString("Designation",jsonObject1.getString("Designation"));
                                editor.putString("MobileNo",jsonObject1.getString("MobileNo"));
                                startActivity(new Intent(LoginActivty.this, DaState.class));
                            }
                        }
                        }else  {
                            Utilities.showAlertDialog(LoginActivty.this,"invalid login","Please check the Details",true);
                            return;
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    //ramesh end
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

//                Log.d("resp----------------->",""+resoonce_loan.toString());
            }
        });
    }

    private boolean validateEmail(){
       emailInput = textInputLayoutEmail.getText().toString().trim();

        if (emailInput.isEmpty()){
            textInputLayoutEmail.setError("Email should not be empty");
            return false;
        }else {
            textInputLayoutEmail.setError(null);
            return true;
        }
    }

    private boolean validatePassword(){
        passwordInput = textInputLayoutpassword.getText().toString().trim();
        if (passwordInput.isEmpty()){
            textInputLayoutpassword.setError("Password should be empty");
            return false;
        }else {
            textInputLayoutpassword.setError(null);
            return true;
        }
    }

    /*public  void check_permissions(){
        if(ActivityCompat.checkSelfPermission(LoginActivty.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(LoginActivty.this, permissionsRequired[1]) !=
                PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(LoginActivty.this,permissionsRequired[0])
                    || ActivityCompat.checkSelfPermission(LoginActivty.this, permissionsRequired[1])
                    != PackageManager.PERMISSION_GRANTED){
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivty.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Internet permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(LoginActivty.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(permissionsRequired[0],false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivty.this);
                builder.setTitle("Need Internet Permissions");
                builder.setMessage("This app needs Internet permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant Internet and Location", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }  else {
                //just request the permission
                ActivityCompat.requestPermissions(LoginActivty.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
            }
            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0],true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
//            if_permission_granted();
        }
    }*/
    public class CheckLogin extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            SoapObject request = new SoapObject(Utilities.NAMESPACE,Utilities.METHODNAME_ValidateLogin);
            request.addProperty("UserName",emailInput);
            request.addProperty("Pwd",passwordInput);
            request.addProperty("IMEI_No",imeiNo);
            request.addProperty("WS_UserName",Utilities.wsusername);
            request.addProperty("WS_Password",Utilities.wspassword);
            request.addProperty("MobileVersion",Utilities.wsmobilever);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(Utilities.URL);
            try {
                transport.call(Utilities.SOAPaCTION_ValidateLogin,envelope);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            SoapObject response = (SoapObject) envelope.bodyIn;
            System.out.println("response---------------------->"+response);
            SoapPrimitive result = (SoapPrimitive) response.getProperty("ValidateLoginResult");
            System.out.println("result---------------------->"+result.toString());
            resoonce_loan = result.toString();
            return result.toString();

        }


    }

}
