package fractal.visaapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = "RegisterActivity";
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button bRegister = (Button) findViewById(R.id.regButton);
        final EditText etName = (EditText) findViewById(R.id.Name);
        final EditText etEmail = (EditText) findViewById(R.id.Email);
        final EditText etPassword = (EditText) findViewById(R.id.editText6);
        final EditText etProjcode = (EditText) findViewById(R.id.editText7);
        final EditText etDepartment = (EditText) findViewById(R.id.editText8);
        final EditText etContact = (EditText) findViewById(R.id.editText9);

        if (bRegister != null) {
            bRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String name = etName.getText().toString();
                        String email = etEmail.getText().toString();
                        String pass = etPassword.getText().toString();
                        String pcode = etProjcode.getText().toString();
                        String dept = etDepartment.getText().toString();
                        String contact = etContact.getText().toString();
                        Spinner spinner = (Spinner) findViewById(R.id.empSpinner2);
                        String empCategory = spinner.getSelectedItem().toString();

                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    int success = jsonResponse.getInt("success");

                                    if (success == 1) {
                                        final Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        Toast.makeText(RegisterActivity.this, "Successfully Registered.", Toast.LENGTH_SHORT).show();
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                        builder.setTitle("Successfully Registered.")
                                                .setMessage("EMPLOYEE CODE will be emailed to you shortly!")
                                                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface arg0, int arg1) {
                                                        RegisterActivity.this.startActivity(intent);
                                                    }
                                                })
                                                .create()
                                                .show();
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                        builder.setMessage("Register Failed")
                                                .setNegativeButton("Retry", null)
                                                .create()
                                                .show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        Response.ErrorListener errorListener = new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                System.out.println(error);
                                builder.setMessage("Response error from server!")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        };

                        RegisterRequest registerRequest = new RegisterRequest(name, email, pass, pcode, dept, contact, empCategory, responseListener, errorListener);
                        registerRequest.setTag(TAG);
                        queue = RequestSingleton.getInstance(RegisterActivity.this.getApplicationContext()).getRequestQueue();
                        RequestSingleton.getInstance(RegisterActivity.this).addToRequestQueue(registerRequest);
                    } catch (NullPointerException e){
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage("Fill all fields!!")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    } catch (Exception e) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage("OOPS! Something went wrong!")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                }
            });
        }
    }
}
