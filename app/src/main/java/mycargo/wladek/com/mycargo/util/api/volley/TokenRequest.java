package mycargo.wladek.com.mycargo.util.api.volley;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import mycargo.wladek.com.mycargo.util.StringUtils;

/**
 * Created by wladek on 9/2/16.
 */
public class TokenRequest extends JsonObjectRequest {

    private static final String REGISTER_REQUEST_URL = StringUtils.SERVER_BASE_URL + "api/v1/Profile/changePassword";
    private String yourToken;

    public TokenRequest(JSONObject jsonRequest, Response.Listener<JSONObject> listener) {
        super(REGISTER_REQUEST_URL, jsonRequest, listener, null);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("Authorization", "Bearer "+ yourToken);
        return params;
    }

    public void setYourToken(String yourToken){
        this.yourToken = yourToken;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        int status = response.statusCode;

        Log.d(" STATUS CODE " , status+"");

        return super.parseNetworkResponse(response);
    }

    @Override
    public String getBodyContentType() {
        return super.getBodyContentType();
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        super.deliverResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        onErrorResponse(volleyError);
        return super.parseNetworkError(volleyError);
    }

    public void onErrorResponse(VolleyError error) {

        // As of f605da3 the following should work
        NetworkResponse response = error.networkResponse;
        if (error instanceof ServerError && response != null) {
            try {
                String res = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                // Now you can use any deserializer to make sense of data
                JSONObject obj = new JSONObject(res);
            } catch (UnsupportedEncodingException e1) {
                // Couldn't properly decode data to string
                e1.printStackTrace();
            } catch (JSONException e2) {
                // returned data is not JSONObject?
                e2.printStackTrace();
            }
        }
    }
}