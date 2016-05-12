package fractal.visaapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EmployeeProfile extends AppCompatActivity {

    private static final String TAG = "EmployeeProfile";
    RequestQueue queue;

    //String empCode;
    Intent intent;
    String empCode;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_profile);

        intent = getIntent();
        empCode = intent.getStringExtra("empCode");
        EmployeeProfile.context = getApplicationContext();
        final TextView tv = (TextView) findViewById(R.id.textView);
        final TextView tem = (TextView) findViewById(R.id.tem);
        final TextView tec = (TextView) findViewById(R.id.tec);
        final TextView dn = (TextView) findViewById(R.id.dn);
        final TextView email = (TextView) findViewById(R.id.email);
        final TextView pn = (TextView) findViewById(R.id.pn);
        final TextView pc = (TextView) findViewById(R.id.pc);
        final TextView v1 = (TextView) findViewById(R.id.vid);
        final TextView v2 = (TextView) findViewById(R.id.vtp);
        final TextView v3 = (TextView) findViewById(R.id.dc);
        final TextView v4 = (TextView) findViewById(R.id.pno);
       // final TextView v5 = (TextView) findViewById(R.id.pid);
        //final TextView v6 = (TextView) findViewById(R.id.ped);
        final TextView v0 = (TextView) findViewById(R.id.tv);


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    // tv.append("\t \t \t \t  Employee Deatils \n ");
                    tec.append(jsonResponse.get("emp_code") +"");
                    tem.append(jsonResponse.get("emp_name") + "");
                    pc.append(jsonResponse.get("proj_code") + "");
                    email.append(jsonResponse.get("email_id") + "");
                    pn.append(jsonResponse.get("phone_no") + "");
                    dn.append(jsonResponse.get("department") + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.Listener<String> responseListener2 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Test");
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    int success = jsonResponse.getInt("success");
                    if(success != 0){
                        //tv.append(" \t \t \t VISA DETAILS \t\n");
                        v1.append(jsonResponse.get("visa_id") +"");
                        v2.append(jsonResponse.get("visa_type")+"");
                        v3.append(jsonResponse.get("country")+"");
                        v4.append(jsonResponse.get("passport_no")+"");
                       // v5.append(jsonResponse.get("Passport_issue")+"");
                       // v6.append(jsonResponse.get("Passport_expiry")+"");
                    } else {
                        v0.append("\nYou have not entered visa details!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EmployeeProfile.this);
                System.out.println(error);
                builder.setMessage("Response error from server!")
                        .setNegativeButton("Retry", null)
                        .create()
                        .show();
            }
        };

        EmployeeProfileRequest epRequest = new EmployeeProfileRequest(empCode, responseListener, errorListener);
        epRequest.setTag(TAG);
        VisaDetailsRequest vdRequest = new VisaDetailsRequest(empCode, responseListener2, errorListener);
        vdRequest.setTag(TAG);

        queue = RequestSingleton.getInstance(EmployeeProfile.this.getApplicationContext()).getRequestQueue();
        RequestSingleton.getInstance(EmployeeProfile.this).getRequestQueue().add(epRequest);
        RequestSingleton.getInstance(EmployeeProfile.this).getRequestQueue().add(vdRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();
        EmployeeProfile.context = getApplicationContext();
    }

    @Override
    protected void onStop () {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }

    public void gotoVIew(View v)
    {
        Intent ilog=new Intent(this, ViewFile.class);
        ilog.putExtra("empcode", empCode);
        startActivity(ilog);
    }

    public static Context getAppContext() {
        return EmployeeProfile.context;
    }

}