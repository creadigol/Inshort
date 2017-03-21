package com.creadigol.inshort.Gcm;

/**
 * Created by Vj on 2/8/2017.
 */

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.creadigol.inshort.Utils.AppUrl;
import com.creadigol.inshort.Utils.MyApplication;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


//Class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private int countTimeOut = 3;
    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.e(TAG, "Refreshed token: " + refreshedToken);

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String number = telephonyManager.getDeviceId();

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken, number);
    }

    private void sendRegistrationToServer(final String token, final String number) {
        //You can implement this method to store the token on your server
        //Not required for current project
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, AppUrl.URL_SETGCM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e(TAG, "Response: " + response);
                    JSONObject jsonObject = new JSONObject(response);
                    int statusCode = jsonObject.optInt("status_code");
                    String massage = jsonObject.optString("massage");
                    if (statusCode == 1) {
                    } else {
                    }
                } catch (JSONException e) {

                }
                //mProgressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (countTimeOut >= 3) {
                    countTimeOut = 0;
                } else {
                    countTimeOut++;
                    sendRegistrationToServer(token, number);
                }
                //mProgressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("gcmtoken", token);
                params.put("imei", number);
                Log.e(TAG, "reqSetGcmId params: " + params.toString());
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest, TAG);
    }

}