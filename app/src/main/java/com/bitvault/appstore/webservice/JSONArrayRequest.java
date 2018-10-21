package com.bitvault.appstore.webservice;

import android.app.ProgressDialog;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bitvault.appstore.application.AppStoreApplication;
import com.bitvault.appstore.utils.AndroidAppUtils;

import org.json.JSONArray;

import java.util.Map;

/**
 * Created by Dheeraj Bansal on 7/2/17.
 * version 1.0.0
 * JSONArray request API
 */

public class JSONArrayRequest {

    private static final String TAG = JSONArrayRequest.class.getName();

    /**
     * JSONArray get Request API
     * @param listener
     * @param url
     * @param method
     * @param dialog
     */
    public void getRequest(final ArrayResponseListener listener, String url, int method, final ProgressDialog dialog) {

        dialog.show();
        JsonArrayRequest request = new JsonArrayRequest(method, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                dialog.hide();
                listener.onArrayResponseSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.hide();
                listener.onArrayResponseFailure(error);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppStoreApplication.getInstance().addToRequestQueue(request);
    }

    /**
     * JSONArray post request API
     * @param listener
     * @param params
     * @param url
     * @param method
     * @param dialog
     */
    public void postRequest(final ArrayResponseListener listener, final Map<String, String> params, String url,
                            int method, final ProgressDialog dialog) {

        dialog.show();
        final JsonArrayRequest request = new JsonArrayRequest(method, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                dialog.hide();
                listener.onArrayResponseSuccess(response);
                AndroidAppUtils.showLogD(TAG, response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.hide();
                listener.onArrayResponseFailure(error);
                AndroidAppUtils.showLogE(TAG, error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppStoreApplication.getInstance().addToRequestQueue(request);
    }


    public interface ArrayResponseListener {
        void onArrayResponseSuccess(JSONArray object);

        void onArrayResponseFailure(VolleyError error);
    }

}
