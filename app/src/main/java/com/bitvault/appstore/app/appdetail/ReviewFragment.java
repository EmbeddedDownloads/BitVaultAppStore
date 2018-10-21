package com.bitvault.appstore.app.appdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bitvault.appstore.R;
import com.bitvault.appstore.utils.KeyboardUtils;

/**
 * Created Dheeraj Bansal root on 16/5/17.
 * version 1.0.0
 * review submit of an app
 */

public class ReviewFragment extends Fragment {

    private EditText editText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_pager_review_item, container, false);

        editText=(EditText)view.findViewById(R.id.id_vp_edt);

        TextView textViewFinish=(TextView)view.findViewById(R.id.id_vp_finish_txt);

        textViewFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyboardUtils.hideSoftKeyboard(getActivity());
                ((ProductDetailFragment)getParentFragment()).reviewSubmit(editText.getText().toString());
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        clearData();
        super.onResume();
    }

    /**
     * This method clear data
     */
    private void clearData() {
        editText.setText("");
    }
}