package fractal.visaapp;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
public class AdminStatisticsRequest extends StringRequest  {

    private static final String REGISTER_REQUEST_URL = "http://server1.miteshshah.com/api/get/adminStatistics";

    private Map<String, String> params;

    public AdminStatisticsRequest(String statType , Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, REGISTER_REQUEST_URL+"?statType="+statType, listener, errorListener);
        params = new HashMap<>();
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}