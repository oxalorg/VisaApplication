package fractal.visaapp;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LawyerHome extends AppCompatActivity {

    private static final String TAG = "LawyerHome";
    RequestQueue queue;
    public ListView list;
    String empCode;
    Button refreshList;

    final String accessLevel = "lawyer";
    final String approved_by = "admin";
    //    android:theme="@style/AppTheme">
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_home);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar2);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Lawyer Dashboard");


        Intent intent = getIntent();
        empCode = intent.getStringExtra("empCode");
        list = (ListView) findViewById(R.id.lawyerListView);
        refreshList = (Button) findViewById(R.id.lawyerRefreshButton);
        if (refreshList != null){
            refreshList.setOnClickListener(refListOnClickListener);
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String listItem = list.getItemAtPosition(position).toString();
                Intent listIntent = new Intent(LawyerHome.this, EmployeeListItem.class);
                listIntent.putExtra("empCode", listItem);
                listIntent.putExtra("accessLevel", accessLevel);
                LawyerHome.this.startActivity(listIntent);
            }
        });
        // Simulate click to get data when activity is created.
        refreshList.performClick();
    }

    public View.OnClickListener refListOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonResponse = new JSONArray(response);
                        JSONObject jsonData;
                        int success = 0;
                        //int success = jsonResponse.getInt("success");

                        ArrayList<String> items = new ArrayList<String>();
                        for(int i=0; i < jsonResponse.length() ; i++) {

                            jsonData = jsonResponse.getJSONObject(i);
                            int id=jsonData.getInt("emp_code");
                            String emp_code = Integer.toString(id);
//                            String name=jsonData.getString("emp_name");
                            items.add(emp_code);
                        }

                        ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(LawyerHome.this, android.R.layout.simple_expandable_list_item_1, items);
                        list.setAdapter(mArrayAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            ArrayList<String> items = new ArrayList<String>();
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LawyerHome.this);
                    System.out.println(error);
                    builder.setTitle("Error").setMessage("Oops! Looks like our server is angry!")
                            .setNegativeButton("Retry.", null)
                            .create()
                            .show();
                }
            };

            //Request here
            UnapprovedEmployeeRequest ueRequest = new UnapprovedEmployeeRequest(empCode, approved_by,responseListener,errorListener);
            ueRequest.setTag(TAG);
            queue = RequestSingleton.getInstance(LawyerHome.this).getRequestQueue();
            RequestSingleton.getInstance(LawyerHome.this).addToRequestQueue(ueRequest);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (refreshList != null) {
            refreshList.performClick();
        }
    }

    @Override
    protected void onStop () {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_logout:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent ilog=new Intent(this, LoginActivity.class);
                ilog.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ilog);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
