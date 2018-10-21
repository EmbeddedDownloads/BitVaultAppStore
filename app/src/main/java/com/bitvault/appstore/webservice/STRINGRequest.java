package com.bitvault.appstore.webservice;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bitvault.appstore.R;
import com.bitvault.appstore.application.AppStoreApplication;
import com.bitvault.appstore.utils.AndroidAppUtils;

import java.util.Map;

/**
 * Created by Dheeraj Bansal on 7/2/17.
 * version 1.0.0
 * String Request API
 */

public class STRINGRequest {
    private static final String TAG = JSONObjectRequest.class.getName();
    private Dialog dialog;
    /**
     * string request get API
     * @param listener
     * @param url
     * @param method
     */

    public void getRequest(Context context,final StringResponseListener listener, String url, int method, final int requestCode) {

//        showDialog(context);
        StringRequest request = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                hideDialog();
                listener.onResponseSuccess(response, requestCode);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//               hideDialog();
                listener.onResponseFailure(error);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppStoreApplication.getInstance().addToRequestQueue(request);
    }

    /**
     * string request post API
     * @param listener
     * @param params
     * @param url
     * @param method
     */
    public StringRequest postRequest(final Context context, final StringResponseListener listener, final Map<String, String> params, String url, int method, final int requestCode) {

//        showDialog(context);
        StringRequest request = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                hideDialog();
                listener.onResponseSuccess(response, requestCode);
                AndroidAppUtils.showLogD(TAG, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                hideDialog();
                error.printStackTrace();
                listener.onResponseFailure(error);
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

        return request;
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


    /**
     * hide dialog after API response either success of failure
     */
    private void hideDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }


    public interface StringResponseListener {
        void onResponseSuccess(String response, int requestCode);

        void onResponseFailure(VolleyError error);
    }

}
