package com.bitvault.appstore.app.appdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bitvault.appstore.R;

/**
 * Created Dheeraj Bansal root on 16/5/17.
 * version 1.0.0
 * Submit rating of an app
 */

public class RatingFragment extends Fragment implements RatingBar.OnRatingBarChangeListener {

    private TextView msgTxt;
    private TextView submitTxt;
    private RatingBar ratingBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_pager_rating_item, container, false);

        ratingBar = (RatingBar) view.findViewById(R.id.id_vp_ratingbar);
        msgTxt = (TextView) view.findViewById(R.id.id_vp_msg_txt);
        submitTxt = (TextView) view.findViewById(R.id.id_vp_submit_txt);
        submitTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ratingBar.getRating()>=1){
                    ((ProductDetailFragment)getParentFragment()).setCurrentPagerItem(1,ratingBar.getRating());
                }
            }
        });
        submitTxt.setEnabled(false);
        msgTxt.setText("");
        ratingBar.setOnRatingBarChangeListener(this);

        return view;
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
        String[] ratingMsg = getActivity().getResources().getStringArray(R.array.array_rating);

        if ( b ) {
            ratingBar.setRating( (float) Math.ceil(v) );
        }

        if(ratingBar.getRating()<1){
            ratingBar.setRating(0);
            submitTxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.color_dark_gray));
            submitTxt.setEnabled(false);
            msgTxt.setText("");
            ((ProductDetailFragment)getParentFragment()).setCurrentRating(ratingBar.getRating());
        } else {
            msgTxt.setText(ratingMsg[(int) ratingBar.getRating() - 1]);
            submitTxt.setEnabled(true);
            submitTxt.setTextColor(ContextCompat.getColor(getContext(), R.color.color_black));
        }
    }

    @Override
    public void onResume() {
        clearRating();
        super.onResume();

    }

    /**
     * clear rating
     */
    public void clearRating() {
        submitTxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.color_dark_gray));
        submitTxt.setEnabled(false);
        msgTxt.setText("");
        ratingBar.setRating(0);
    }

}
