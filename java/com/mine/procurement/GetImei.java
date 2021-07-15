package com.mine.procurement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.telephony.TelephonyManager.CellInfoCallback;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GetImei extends AppCompatActivity {

    TextView imei_number;
    Button get_imei;
    String IMEI_Number_Holder;
    TelephonyManager telephonyManager;

    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_imei);

        imei_number = (TextView) findViewById(R.id.textView);
        get_imei = (Button) findViewById(R.id.button);

        int imeiI = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE);
        if (imeiI == PackageManager.PERMISSION_GRANTED){
            telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            IMEI_Number_Holder = telephonyManager.getDeviceId();
        }else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},REQUEST_CODE);
        }
//        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        get_imei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imei_number.setText(IMEI_Number_Holder);
                }
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    IMEI_Number_Holder = telephonyManager.getImei();
                }*/


        });
    }

    }



    /*String getDeviceID(TelephonyManager phonyManager) {

        String id = phonyManager.getDeviceId();
        if (id == null) {
            id = "not available";
        }
        int phoneType = phonyManager.getPhoneType();
        switch(phoneType){
            case TelephonyManager.PHONE_TYPE_NONE:
                return "NONE: " + id;

            case TelephonyManager.PHONE_TYPE_GSM:
                return "GSM: IMEI=" + id;

            case TelephonyManager.PHONE_TYPE_CDMA:
                return "CDMA: MEID/ESN=" + id;

            *//*
             *  for API Level 11 or above
             *  case TelephonyManager.PHONE_TYPE_SIP:
             *   return "SIP";
             *//*

            default:
                return "UNKNOWN: ID=" + id;
        }
    }*/
