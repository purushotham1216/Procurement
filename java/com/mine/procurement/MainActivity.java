package com.mine.procurement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.common.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    TextView minsurance_uploaded_pend,minsurance_uploaded,mtotal_entries_on_sheep,mtoday_entries_text;
    String response_initial;
    String mTotalTagnoentry,mTotalTagnoTodayentry,mTotalValiluvationupd,mTotalValiluvationpend;

    JSONObject json = null;
    String user_id,session_id,serice_code,user_name,email_id,mobile_no;
    TextView textViewName;
    private CardView profile_cardId,upload_advt_id,view_advt_id;
    SharedPreferences sharePref;

    private String userId = "9989997698";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mtoday_entries_text = findViewById(R.id.today_entries_text);
        mtotal_entries_on_sheep = findViewById(R.id.total_entries_on_sheep);
        minsurance_uploaded = findViewById(R.id.insurance_uploaded);
        minsurance_uploaded_pend = findViewById(R.id.insurance_uploaded_pend);
        textViewName=findViewById(R.id.textViewName);

//---------------------
        sharePref = getApplicationContext().getSharedPreferences("procurement", 0);
        user_id = sharePref.getString("UserId", "");
        //session_id=sharePref.getString("session_id","");
        user_name=sharePref.getString("OfficerNAME","");
        System.out.println("user_name-------->"+user_name);
        mobile_no=sharePref.getString("MobileNo","");
        textViewName.setText(user_name);


        try {
            response_initial = new CPTDDash().execute().get();
            json = new JSONObject(response_initial);
            JSONArray jsonArray = json.getJSONArray("Data");
            for( int i=0; i < jsonArray.length(); i++ )
            {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                mTotalTagnoTodayentry = mtoday_entries_text.getText().toString();
                mTotalTagnoentry = mtotal_entries_on_sheep.getText().toString();
                mTotalValiluvationupd = minsurance_uploaded.getText().toString();
                mTotalValiluvationpend = minsurance_uploaded_pend.getText().toString();


                mtoday_entries_text.setText(jsonObject1.getString("TotalTagnoTodayentry"));
                mtotal_entries_on_sheep.setText(jsonObject1.getString("TotalTagnoentry"));
                minsurance_uploaded.setText(jsonObject1.getString("TotalValiluvationupd"));
                minsurance_uploaded_pend.setText(jsonObject1.getString("TotalValiluvationpend"));
            }



        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public class CPTDDash extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(Utilities.NAMESPACE,Utilities.METHODNAME_GetCPTGroundedDetails);

            request.addProperty("Userid",user_id);
            request.addProperty("distcode","distcode");
            request.addProperty("mandalcd","mandalcd");
            request.addProperty("WS_UserName",Utilities.wsusername);
            request.addProperty("WS_Password",Utilities.wspassword);
            request.addProperty("MobileVersion",Utilities.wsmobilever);
            request.addProperty("Action",Utilities.CPTD_Action);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(Utilities.URL);
            try {
                transport.call(Utilities.SOAP_ACTION_GetCPTGroundedDetails,envelope);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            SoapObject response = (SoapObject) envelope.bodyIn;
            SoapPrimitive result = (SoapPrimitive) response.getProperty("GetCPTGroundedDetailsResult");
            Log.d("result_dash",""+result);
            response_initial = result.toString();
            return result.toString();

        }
    }
}
