package fractal.visaapp;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;


import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    RequestQueue queue;
    public static final String TAG = "LoginActivity";
    String empCode;
    String password;
    String empCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar_login);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Fractal Visa Centre");

        final EditText etEmpCode = (EditText) findViewById(R.id.etEmpCode);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button tvRegisterLink = (Button) findViewById(R.id.tvRegisterLink);
        final Button bLogin = (Button) findViewById(R.id.bSignIn);
        final Spinner spinner = (Spinner)findViewById(R.id.empSpinner);

//        spinner.setVisibility(View.INVISIBLE);
//        etEmpCode.setVisibility(View.INVISIBLE);
//        etPassword.setVisibility(View.INVISIBLE);
//        tvRegisterLink.setVisibility(View.INVISIBLE);
//        bLogin.setVisibility(View.INVISIBLE);
//
//        iv.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                spinner.setVisibility(View.VISIBLE);
//                etEmpCode.setVisibility(View.VISIBLE);
//                etPassword.setVisibility(View.VISIBLE);
//                tvRegisterLink.setVisibility(View.VISIBLE);
//                bLogin.setVisibility(View.VISIBLE);
//                iv.bringToFront();
//                iv.setVisibility(View.GONE);
//            }
//        },1500);
        if (tvRegisterLink != null) {
            tvRegisterLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                    LoginActivity.this.startActivity(registerIntent);
                }
            });
        }

        if (bLogin != null) {
            bLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        empCode = etEmpCode.getText().toString();
                        password = etPassword.getText().toString();
                        empCategory = spinner.getSelectedItem().toString();
                    } catch (Exception e){
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage("Fill all field!!")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                    // Response received from the server
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                int success = jsonResponse.getInt("success");

                                if (success == 1) {
                                    if (empCategory.toLowerCase().equals("employee")){
                                        System.out.println(empCode);
                                        Intent intent = new Intent(LoginActivity.this, EmployeeHome.class);
                                        intent.putExtra("empCode", empCode);
                                        LoginActivity.this.startActivity(intent);
                                    } else if (empCategory.toLowerCase().equals("manager")){
                                        Intent intent = new Intent(LoginActivity.this, ManagerHome.class);
                                        intent.putExtra("empCode", empCode);
                                        LoginActivity.this.startActivity(intent);
                                    } else if (empCategory.toLowerCase().equals("admin")){
                                        Intent intent = new Intent(LoginActivity.this, AdminHome.class);
                                        intent.putExtra("empCode", empCode);
                                        LoginActivity.this.startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(LoginActivity.this, LawyerHome.class);
                                        intent.putExtra("empCode", empCode);
                                        LoginActivity.this.startActivity(intent);
                                    }
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setTitle("Error!")
                                            .setMessage("Login Failed")
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            System.out.println(error);
                            builder.setMessage("Response error")
                                    .setNegativeButton("Retry", null)
                                    .create()
                                    .show();
                        }
                    };

                    LoginRequest loginRequest = new LoginRequest(empCode, password, empCategory, responseListener, errorListener);
                    loginRequest.setTag(TAG);
                    queue = RequestSingleton.getInstance(LoginActivity.this.getApplicationContext()).getRequestQueue();
                    RequestSingleton.getInstance(LoginActivity.this).addToRequestQueue(loginRequest);
                }
            });
        }
    }


    @Override
    protected void onStop () {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }
}