package fractal.visaapp;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class VisaStatusRequest extends StringRequest {
    private static final String REQUEST_URL = "http://server1.miteshshah.com/api/set/visaStatus";
    private Map<String, String> params;

    public VisaStatusRequest(String empCode, String accessLevel, String action, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, REQUEST_URL, listener, errorListener);
        params = new HashMap<>();
        params.put("emp_code", empCode);
        params.put("access_level", accessLevel);
        params.put("action", action);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}