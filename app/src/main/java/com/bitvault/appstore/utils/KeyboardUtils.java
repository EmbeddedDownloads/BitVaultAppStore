package com.bitvault.appstore.utils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Dheeraj Bansal on 7/2/17.
 * version 1.0.0
 * hide keyboard
 */

public class KeyboardUtils {

    /**
     * Hides the soft keyboard
     */
    public static void hideSoftKeyboard(Activity activity) {
        if(activity.getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }


    /**
     * Hide keyboard.
     *
     * <pre>
     * <code>KeyboardUtils.hideKeyboard(getActivity(), searchField);</code>
     * </pre>
     *
     * @param context
     * @param field
     */
    public static void hideKeyboard(Context context, EditText field) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(
                INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(field.getWindowToken(), 0);
    }

    /**
     * Show keyboard with a 100ms delay.
     *
     * <pre>
     * <code>KeyboardUtils.showDelayedKeyboard(getActivity(), searchField);</code>
     * </pre>
     *
     * @param context
     * @param view
     */
    public static void showDelayedKeyboard (Context context, View view) {
        showDelayedKeyboard(context, view, 100);
    }

    /**
     * Show keyboard with a custom delay.
     *
     * <pre>
     * <code>KeyboardUtils.showDelayedKeyboard(getActivity(), searchField, 500);</code>
     * </pre>
     *
     * @param context
     * @param view
     * @param delay
     */
    public static void showDelayedKeyboard (final Context context, final View view, final int delay) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(
                        INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }

        }.execute();
    }
    public static void hideKeyboard(Context context , View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService
                (INPUT_METHOD_SERVICE);
        if (view == null) {
            view = new View(context);
        }
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}
