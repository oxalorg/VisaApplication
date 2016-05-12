package fractal.visaapp;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdminPieActivity extends AppCompatActivity {
    public final String TAG = "AdminPieActivity";
    public final String statType = "visa_status";
    int noOfVisaApproved;
    int noOfVisaApplicants;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pie);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    noOfVisaApproved =jsonResponse.getInt("visa_approved");
                    noOfVisaApplicants =jsonResponse.getInt("visa_id");
                    PieChart chart = (PieChart) findViewById(R.id.pieChart);
                    PieData data = new PieData(getXAxisValues(), getDataSet());
                    System.out.println(data);
                    chart.setDescription("Applicant Visa statistics");
                    chart.setData(data);
                    chart.animateXY(2000, 2000);
                    chart.invalidate();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminPieActivity.this);
                System.out.println(error);
                builder.setMessage("Response error from server!")
                        .setNegativeButton("Retry", null)
                        .create()
                        .show();
            }
        };

        AdminStatisticsRequest apRequest = new AdminStatisticsRequest(statType, responseListener, errorListener);
        apRequest.setTag(TAG);
        queue = RequestSingleton.getInstance(AdminPieActivity.this.getApplicationContext()).getRequestQueue();
        RequestSingleton.getInstance(AdminPieActivity.this).getRequestQueue().add(apRequest);

    }

    public PieDataSet getDataSet() {
        int noOfVisaDenied = noOfVisaApplicants - noOfVisaApproved;
        float approved = (float) noOfVisaApproved;
        float denied = (float) noOfVisaDenied;
        ArrayList<Entry> entries = new ArrayList<>();
        Entry valueApproved = new Entry(approved, 0); // approved
        entries.add(valueApproved);
        Entry valueDenied = new Entry(denied, 1); // denied
        entries.add(valueDenied);

        PieDataSet pieDataSet = new PieDataSet(entries, "(applicants)");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        System.out.println(pieDataSet);
        return pieDataSet;
    }

    public ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("approved");
        xAxis.add("denied");
        return xAxis;
    }
}
