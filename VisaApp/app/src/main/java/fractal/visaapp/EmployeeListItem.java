package fractal.visaapp;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public class EmployeeListItem extends AppCompatActivity {
    private static final String TAG = "EmployeeListItem";
    RequestQueue queue;
    //Intent intent;
    //String empCode,accessLevel;
    TextView tv;
    Intent i;
    String empCode ;
    String accessLevel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list_item);
        i=getIntent();
        empCode = i.getStringExtra("empCode");
        accessLevel = i.getStringExtra("accessLevel");



        tv = (TextView) findViewById(R.id.textView2);
        Button acceptApplication = (Button) findViewById(R.id.acceptApplication);
        Button rejectApplication = (Button) findViewById(R.id.rejectApplication);
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
      //  final TextView v5 = (TextView) findViewById(R.id.pid);
        //final TextView v6 = (TextView) findViewById(R.id.ped);
        final TextView v0 = (TextView) findViewById(R.id.tv);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    //tv.append(" \t \t \t EMPLOYEE PROFILE DETAILS \t\n");
                    tec.append(jsonResponse.get("emp_code")+"");
                    tem.append(jsonResponse.get("emp_name")+ "");
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
                        v1.append(jsonResponse.get("visa_id") +"");
                        v2.append(jsonResponse.get("visa_type")+"");
                        v3.append(jsonResponse.get("country")+"");
                        v4.append(jsonResponse.get("passport_no")+"");
                      //  v5.append(jsonResponse.get("Passport_issue")+"");
                       // v6.append(jsonResponse.get("Passport_expiry")+"");
                    } else {
                        v0.append("You have not entered visa details!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };



        final Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EmployeeListItem.this);
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
        queue = RequestSingleton.getInstance(EmployeeListItem.this.getApplicationContext()).getRequestQueue();
        RequestSingleton.getInstance(EmployeeListItem.this).getRequestQueue().add(epRequest);
        RequestSingleton.getInstance(EmployeeListItem.this).getRequestQueue().add(vdRequest);

        final Response.Listener<String> submitListener= new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    int success = jsonResponse.getInt("success");
                    if(success == 1){
                        Toast.makeText(EmployeeListItem.this, "The operation was successfully recorded.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(EmployeeListItem.this, "The operation FAILED!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        if (acceptApplication != null) {
            acceptApplication.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String action = "accept";
                    VisaStatusRequest vsRequest = new VisaStatusRequest(empCode, accessLevel, action, submitListener, errorListener);
                    RequestSingleton.getInstance(EmployeeListItem.this).getRequestQueue().add(vsRequest);
                }
            });
        }

        if (rejectApplication != null) {
            rejectApplication.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String action = "reject";
                    VisaStatusRequest vsRequest = new VisaStatusRequest(empCode, accessLevel, action, submitListener, errorListener);
                    RequestSingleton.getInstance(EmployeeListItem.this).getRequestQueue().add(vsRequest);
                }
            });
        }
    }

    public void gotoVIew(View v)
    {
        Intent ilog=new Intent(this, ViewFile.class);
        ilog.putExtra("empcode", empCode);
        startActivity(ilog);
    }

    @Override
    protected void onStop () {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }
}