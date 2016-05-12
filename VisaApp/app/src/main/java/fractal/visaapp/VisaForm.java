package fractal.visaapp;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class VisaForm extends AppCompatActivity {

    Intent intent;
    String empCode;
    RequestQueue queue;
    RadioGroup rg;
    int r;
    RadioButton rb;
    String visa_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visa_form);
        intent = getIntent();
        Button bvs=(Button)findViewById(R.id.submit);
        empCode = intent.getStringExtra("empCode");
        rg =(RadioGroup) findViewById(R.id.type);
    }

    public void onClickVisaForm(View v)
    {
        try {
            r = rg.getCheckedRadioButtonId();
            rb = (RadioButton) findViewById(r);
            visa_type = rb.getText().toString();
            final EditText pno = (EditText) findViewById(R.id.epn);
            final EditText pi = (EditText) findViewById(R.id.editText);
            final EditText pe = (EditText) findViewById(R.id.editText2);
            final EditText coun = (EditText) findViewById(R.id.editText3);

            final String passport_no = pno.getText().toString();
            final String passport_issue = pi.getText().toString();
            final String passport_expiry = pe.getText().toString();
            final String country = coun.getText().toString();

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        int success = jsonResponse.getInt("success");
                        if (success == 1) {
                            Intent intent = new Intent(VisaForm.this, EmployeeHome.class);
                            Toast.makeText(VisaForm.this, "Successfully uploaded visa form.", Toast.LENGTH_LONG).show();
                            VisaForm.this.startActivity(intent);
                        } else if (success == 2) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(VisaForm.this);
                            builder.setTitle("Error").setMessage("You have already submitted your visa application.")
                                    .setNegativeButton("Confirm.", null)
                                    .create()
                                    .show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(VisaForm.this);
                            builder.setTitle("Error").setMessage("Upload failed.")
                                    .setNegativeButton("Retry.", null)
                                    .create()
                                    .show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            VisaRequest visaReq = new VisaRequest(empCode, country, visa_type, passport_no, passport_issue, passport_expiry, responseListener);
            queue = RequestSingleton.getInstance(VisaForm.this).getRequestQueue();
            RequestSingleton.getInstance(VisaForm.this).addToRequestQueue(visaReq);
        }catch (NullPointerException e){
            AlertDialog.Builder builder = new AlertDialog.Builder(VisaForm.this);
            builder.setMessage("Fill all fields!!")
                    .setNegativeButton("Retry", null)
                    .create()
                    .show();
        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(VisaForm.this);
            builder.setMessage("OOPS! Something went wrong!")
                    .setNegativeButton("Retry", null)
                    .create()
                    .show();
        }
    }
}
