package fractal.visaapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "http://server1.miteshshah.com/api/login";
    private Map<String, String> params;

    public LoginRequest(String username, String password, String category, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, LOGIN_REQUEST_URL, listener, errorListener);
        params = new HashMap<>();
        params.put("id", username);
        params.put("password", password);
        params.put("category", category);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
