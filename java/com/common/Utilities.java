package com.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.mine.procurement.MainActivity;
import com.mine.procurement.R;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utilities {
    // 0-show, 1-not show
    public static final int showLogs
            = 0;

    public static final String URL = "https://elaabh.telangana.gov.in/sheepgrounding.asmx?WSDL";
    public static final String NAMESPACE = "http://tempuri.org/";
    public static final String CPTD_Action = "CPTD";
    public static final String DA_Action = "SDA";

    public static final String METHODNAME_ValidateLogin = "ValidateLogin";
    public static final String SOAPaCTION_ValidateLogin = NAMESPACE + METHODNAME_ValidateLogin;

    public static final String METHODNAME_GetCPTGroundedDetails = "GetCPTGroundedDetails";
    public static final String SOAP_ACTION_GetCPTGroundedDetails = NAMESPACE + METHODNAME_GetCPTGroundedDetails;

    public static final String METHODNAME_GetSheepGroundState = "GetSheepGroundState";
    public static final String SOAP_ACTION_GetSheepGroundState = NAMESPACE + METHODNAME_GetSheepGroundState;


    public static final String wsusername = "SFED";
    public static final String wspassword = "SFED$Admin@123";
    public static final String wsmobilever = "1.0.1";
    //				= 1;
    public static void Error_Msg(Context context, String Message) {
        // TODO Auto-generated method stub

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Warning");
        builder.setIcon(R.drawable.warning);
        builder.setMessage(Message).setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // //Log.i("alert id : ", "" + id);
                        dialog.dismiss();

                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    static String versionNameOrCodeStr = "";

    public static String getVersionNameCode(Context _context) {

        try {
            PackageInfo pinfo = _context.getPackageManager().getPackageInfo(
                    _context.getPackageName(), 0);

            if (Utilities.showLogs == 0) {
                Log.d("pinfoCode", "" + pinfo.versionCode);
                Log.d("pinfoName", pinfo.versionName);
            }

            // versionCodeStr=String.valueOf(pinfo.versionCode);
            versionNameOrCodeStr = pinfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return versionNameOrCodeStr;
    }
    public static void showAlertDialog(Context context, String title,
                                       String message, Boolean status) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);

        if (status == true) {
            builder.setIcon(R.drawable.success);
        } else {
            builder.setIcon(R.drawable.fail);
        }

        builder.setMessage(message).setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                    }
                });
        final AlertDialog alert = builder.create();

        alert.show();


    }

    public static String sha512(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA-512");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        String sha256Hash = sb.toString();
        return sha256Hash;
    }
    public static String md5(String passwd) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(passwd.getBytes());
        byte[] digest = md.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            passwd = Integer.toHexString(0xFF & digest[i]);
            if (passwd.length() < 2) {
                passwd = "0" + passwd;
            }
            hexString.append(passwd);
        }
        String md5Hash = hexString.toString();
        return md5Hash;
    }
    public static String getBase64Str(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] data = baos.toByteArray();
        String strBase64 = Base64.encodeToString(data, Base64.DEFAULT);

        return strBase64;
    }
}
