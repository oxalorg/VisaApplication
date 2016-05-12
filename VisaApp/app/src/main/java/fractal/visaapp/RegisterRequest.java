package fractal.visaapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "http://server1.miteshshah.com/api/register";

    private Map<String, String> params;

    public RegisterRequest(String emp_name, String email_id,String password, String proj_code,String department,String phone_no, String empCategory, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, errorListener);
        params = new HashMap<>();
        params.put("emp_name", emp_name);
        params.put("email_id", email_id);
        params.put("password", password);
        params.put("proj_code", proj_code);
        params.put("department", department);
        params.put("phone_no", phone_no);
        params.put("emp_category", empCategory);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
