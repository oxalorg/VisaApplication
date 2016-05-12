package fractal.visaapp;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdminBarActivity extends AppCompatActivity {

    public final String TAG = "AdminBarActivity";
    public final String statType = "visa_type";
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_bar);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    int h1Count=(jsonResponse.getInt("h1"));
                    int l1Count=(jsonResponse.getInt("l1"));
                    int b1Count=(jsonResponse.getInt("b1"));
                    int studentCount=(jsonResponse.getInt("student"));
                    int businessCount=(jsonResponse.getInt("business"));
                    BarChart chart = (BarChart) findViewById(R.id.chart);
                    BarData data = new BarData(getXAxisValues(), getDataSet(h1Count, l1Count, b1Count, studentCount, businessCount));
                    chart.setData(data);
                    chart.setDescription("VISA type frequency");
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
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminBarActivity.this);
                System.out.println(error);
                builder.setMessage("Response error from server!")
                        .setNegativeButton("Retry", null)
                        .create()
                        .show();
            }
        };

        AdminStatisticsRequest abRequest = new AdminStatisticsRequest(statType,responseListener, errorListener);
        abRequest.setTag(TAG);
        queue = RequestSingleton.getInstance(AdminBarActivity.this.getApplicationContext()).getRequestQueue();
        RequestSingleton.getInstance(AdminBarActivity.this).getRequestQueue().add(abRequest);
        ArrayList<BarDataSet> dataSets = null;

    }

    public BarDataSet getDataSet(int h1Count, int l1Count, int b1Count, int studentCount, int businessCount) {
        /* Some default values. */;

        ArrayList<BarEntry> entries = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(h1Count, 0); // h1
        entries.add(v1e1);
        BarEntry v1e2 = new BarEntry(l1Count, 1); // l1
        entries.add(v1e2);
        BarEntry v1e3 = new BarEntry(b1Count, 2); // b1
        entries.add(v1e3);
        BarEntry v1e4 = new BarEntry(studentCount, 3); // student
        entries.add(v1e4);
        BarEntry v1e5 = new BarEntry(businessCount, 4); // business
        entries.add(v1e5);

        BarDataSet barDataSet1 = new BarDataSet(entries, "");
        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);

        return barDataSet1;
    }

    public ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("H1");
        xAxis.add("L1");
        xAxis.add("B1");
        xAxis.add("Student");
        xAxis.add("Business");
        return xAxis;
    }
}
