package fractal.visaapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UnapprovedEmployeeRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "http://server1.miteshshah.com/api/get/unapprovedEmployees";

    public UnapprovedEmployeeRequest(String empCode, String approved_by, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, LOGIN_REQUEST_URL+"?emp_code="+empCode+"&approved_by="+approved_by, listener, errorListener);
    }
}
