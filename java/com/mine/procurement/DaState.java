package com.mine.procurement;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class DaState extends AppCompatActivity {

    String response_initial;
    JSONObject json = null;
    TextView mda_today_entries,mda_total_entries,mda_insu_docs_upload,mda_insu_docs_upload_pend,
            mda_grounded_units,mda_arrival_cert_pend,mda_sent_insurance,mda_num_policies_generated;
    TextView mda_username;
    String user_name,user_id;

    String mda_today_entries_str,mda_total_entries_str,mda_insu_docs_upload_str,mda_insu_docs_upload_pend_str,
            mda_grounded_units_str,mda_arrival_cert_pend_str,mda_sent_insurance_str,mda_num_policies_generated_str;

    SharedPreferences sharePref;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_da_state);

        mda_today_entries = findViewById(R.id.da_today_entries);
        mda_total_entries = findViewById(R.id.da_total_entries);
        mda_insu_docs_upload = findViewById(R.id.da_insu_docs_upload);
        mda_insu_docs_upload_pend = findViewById(R.id.da_insu_docs_upload_pend);
        mda_grounded_units = findViewById(R.id.da_grounded_units);
        mda_arrival_cert_pend = findViewById(R.id.da_arrival_cert_pend);
        mda_sent_insurance = findViewById(R.id.da_sent_insurance);
        mda_num_policies_generated = findViewById(R.id.da_num_policies_generated);


        mda_username = findViewById(R.id.da_username);

        sharePref = getApplicationContext().getSharedPreferences("procurement", 0);
        user_id = sharePref.getString("UserIdd", "");
        user_name=sharePref.getString("OfficerNAMEE","");
        mda_username.setText(user_name);

        try {
            response_initial = new DaStateDash().execute().get();
            json = new JSONObject(response_initial);
            JSONArray jsonArray = json.getJSONArray("Data");
            for( int i=0; i < jsonArray.length(); i++ )
            {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                mda_today_entries_str = mda_today_entries.getText().toString();
                mda_total_entries_str = mda_total_entries.getText().toString();
                mda_insu_docs_upload_str = mda_insu_docs_upload.getText().toString();
                mda_insu_docs_upload_pend_str = mda_insu_docs_upload_pend.getText().toString();
                mda_grounded_units_str = mda_grounded_units.getText().toString();
                mda_arrival_cert_pend_str = mda_arrival_cert_pend.getText().toString();
                mda_sent_insurance_str = mda_sent_insurance.getText().toString();
                mda_num_policies_generated_str = mda_num_policies_generated.getText().toString();


                mda_today_entries.setText(jsonObject1.getString("TotalTagnoTodayentry"));
                mda_total_entries.setText(jsonObject1.getString("TotalTagnoentry"));
                mda_insu_docs_upload.setText(jsonObject1.getString("TotalValiluvationupd"));
                mda_insu_docs_upload_pend.setText(jsonObject1.getString("TotalValiluvationpend"));
                mda_sent_insurance.setText(jsonObject1.getString("TotalPolicysendforInsurance"));
                mda_grounded_units.setText(jsonObject1.getString("Totalatvasforarrival"));
                mda_arrival_cert_pend.setText(jsonObject1.getString("TotalPolicyGenPending"));
                mda_num_policies_generated.setText(jsonObject1.getString("TotalPolicyGen"));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public class DaStateDash extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {

            SoapObject request = new SoapObject(Utilities.NAMESPACE,Utilities.METHODNAME_GetCPTGroundedDetails);

            request.addProperty("Userid",user_id);
            request.addProperty("distcode","distcode");
            request.addProperty("mandalcd","mandalcd");
            request.addProperty("WS_UserName",Utilities.wsusername);
            request.addProperty("WS_Password",Utilities.wspassword);
            request.addProperty("MobileVersion",Utilities.wsmobilever);
            request.addProperty("Action",Utilities.DA_Action);

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