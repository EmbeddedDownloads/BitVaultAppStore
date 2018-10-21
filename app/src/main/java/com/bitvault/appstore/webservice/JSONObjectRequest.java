package com.bitvault.appstore.webservice;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bitvault.appstore.R;
import com.bitvault.appstore.application.AppStoreApplication;
import com.bitvault.appstore.utils.AndroidAppUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dheeraj Bansal on 7/2/17.
 * version 1.0.0
 * JSONObject request to API
 */

public class JSONObjectRequest {

    private static final String TAG = JSONObjectRequest.class.getName();

    private Dialog dialog;

    /**
     * JSON get request API
     * @param listener
     * @param url
     * @param method
     * @param dialog
     */
    public void getRequest(final ObjectResponseListener listener, String url, int method, final ProgressDialog dialog) {

        dialog.show();
        JsonObjectRequest request = new JsonObjectRequest(method, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.hide();
                listener.onObjectResponseSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.hide();
                listener.onObjectResponseFailure(error);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppStoreApplication.getInstance().addToRequestQueue(request);
    }

    /**
     * JSON post request API
     * @param listener
     * @param url
     * @param params
     * @param header
     * @param context
     */
    public void postRequest(final ObjectResponseListener listener, String url, final Map<String, String> params, final Map<String, String> header, Context context) {

        showDialog(context);
        JSONObject jsonObject = new JSONObject(params);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideDialog();
                listener.onObjectResponseSuccess(response);
                AndroidAppUtils.showLogD(TAG, response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                listener.onObjectResponseFailure(error);
                AndroidAppUtils.showLogE(TAG, error.toString());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                if (header != null && !header.isEmpty()) {
                    headers = header;
                }
                headers.put("Content-Type", "application/json");

                return headers;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppStoreApplication.getInstance().addToRequestQueue(request);
    }

    /**
     * JSON delete request api
     * @param listener
     * @param url
     * @param params
     * @param header
     * @param context
     */
    public void deleteRequest(final ObjectResponseListener listener, String url, final Map<String, String> params, final Map<String, String> header, Context context) {

        showDialog(context);
        JSONObject jsonObject = new JSONObject(params);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideDialog();
                listener.onObjectResponseSuccess(response);
                AndroidAppUtils.showLogD(TAG, response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                listener.onObjectResponseFailure(error);
                AndroidAppUtils.showLogE(TAG, error.toString());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                if (header != null && !header.isEmpty()) {
                    headers = header;
                }
                headers.put("Content-Type", "application/json");

                return headers;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppStoreApplication.getInstance().addToRequestQueue(request);
    }


    /**
     * loading progress bar when API hit
     * @param context
     */
    private void showDialog(Context context) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
    }


    private void hideDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }


    public interface ObjectResponseListener {
        void onObjectResponseSuccess(JSONObject object);

        void onObjectResponseFailure(VolleyError error);
    }

}
