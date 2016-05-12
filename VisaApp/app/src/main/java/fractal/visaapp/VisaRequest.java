package fractal.visaapp;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class VisaRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "http://server1.miteshshah.com/api/set/visaForm";

    private Map<String, String> params;

    public VisaRequest(String emp_code, String country, String visa_type, String Passport_no, String Passport_issue, String Passport_expiry, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("emp_code", emp_code);
        params.put("country", country);
        params.put("visa_type", visa_type);
        params.put("passport_no", Passport_no);
        params.put("passport_issue", Passport_issue);
        params.put("passport_expiry", Passport_expiry);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }


}
