package com.bitvault.appstore.app.rating;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bitvault.appstore.R;
import com.bitvault.appstore.application.APIs;
import com.bitvault.appstore.databinding.ActivityAllReviewsBinding;
import com.bitvault.appstore.databinding.ActivityAllReviewsLandBinding;
import com.bitvault.appstore.utils.AndroidAppUtils;
import com.bitvault.appstore.utils.AppConstants;
import com.bitvault.appstore.utils.OnLoadMoreListener;
import com.bitvault.appstore.webservice.STRINGRequest;
import com.bitvault.appstore.webservice.response.AppDetailResponse;
import com.bitvault.appstore.webservice.response.AppReviewsModel;
import com.bitvault.appstore.webservice.response.ReviewModel;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by anuj on 4/5/17.
 * version 1.0.0
 */

/**
 * Display List of all reviews for particular apps
 */
public class AllReviewsActivity extends AppCompatActivity {

    private static final String TAG = AllReviewsActivity.class.getCanonicalName();
    private int pageNo = 1;

    Intent intent;
    AppDetailResponse.Result.Star star;
    private ActivityAllReviewsBinding activityAllReviewsBinding;
    private ActivityAllReviewsLandBinding activityAllReviewsLandBinding;
    private int orientation;
    private StringRequest request;
    private AllReviewsAdapter allReviewsAdapter;
    private ArrayList<ReviewModel> reviewModelsList;
    private int reviewHeaderDy;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        intent = getIntent();
        orientation = getResources().getConfiguration().orientation;

        star = (AppDetailResponse.Result.Star) intent.getSerializableExtra(getString(R.string.star_object));
        long totalStar = 0;
        if (star != null) {
            totalStar = star.getStar1() + star.getStar2() + star.getStar3() + star.getStar4() + star.getStar5();
        }
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            activityAllReviewsBinding = DataBindingUtil.setContentView(this, R.layout.activity_all_reviews);
            activityAllReviewsBinding.idTvTitle.setText(intent.getStringExtra(getString(R.string.intent_app)));
            activityAllReviewsBinding.idTvAverageRating.setText(new DecimalFormat("0.0").format(Float.parseFloat(intent.getStringExtra(getString(R.string.average_rating)))));
            activityAllReviewsBinding.idRatingBar.setRating(Float.parseFloat(intent.getStringExtra(getString(R.string.average_rating))));
            activityAllReviewsBinding.idTvTotalRatingCount.setText(String.valueOf(totalStar));
        } else {
            activityAllReviewsLandBinding = DataBindingUtil.setContentView(this, R.layout.activity_all_reviews_land);
            activityAllReviewsLandBinding.idTvTitle.setText(intent.getStringExtra(getString(R.string.intent_app)));
            activityAllReviewsLandBinding.idTvAverageRating.setText(new DecimalFormat("0.0").format(Float.parseFloat(intent.getStringExtra(getString(R.string.average_rating)))));
            activityAllReviewsLandBinding.idRatingBar.setRating(Float.parseFloat(intent.getStringExtra(getString(R.string.average_rating))));
            activityAllReviewsLandBinding.idTvTotalRatingCount.setText(String.valueOf(totalStar));
        }

        getDataFromServer();

    }


    /**
     * Read all reviews from file
     */
    private void getDataFromServer() {

        //file read remove later
//        try {
//            InputStream inputStream = getResources().openRawResource(R.raw.allreviews);
//            String json = Utils.readTextFile(inputStream);
//            JsonParse(json);
//        } catch (NullPointerException ignored) {
//        }

        Map<String, String> param = new HashMap<>();
        String package_name = intent.getStringExtra(getString(R.string.package_name));
        if (package_name != null) {
            param.put(getString(R.string.package_name), package_name);
        }

        if(pageNo == 1) {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                activityAllReviewsBinding.loadingLout.setVisibility(View.VISIBLE);
            } else {
                activityAllReviewsLandBinding.loadingLout.setVisibility(View.VISIBLE);
            }
        }

        request = new STRINGRequest().postRequest(this, new LoadAllRatingApp(), null, APIs.API_RATE_REVIEW_LIST + getString(R.string.applicationId) + "=" + intent.getStringExtra(getString(R.string.appId)) + "&" + getString(R.string.page) + "=" + pageNo + "&" + getString(R.string.size) + "=15", Request.Method.GET, 1);

    }


    /**
     * parse all review data to JSON and display
     *
     * @param json
     */
    private void jsonParse(String json) {

        Gson gson = new GsonBuilder().create();
        AppReviewsModel appReviewsModel = gson.fromJson(json, AppReviewsModel.class);
        if (appReviewsModel != null) {
            setData(appReviewsModel);
            if(reviewModelsList == null) {
                ArrayList<ReviewModel> reviewModels = appReviewsModel.getApp_reviews();
                if (reviewModels != null && reviewModels.size() > 0) {
                    reviewModelsList = reviewModels;
                    setDataToList();
                }
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    activityAllReviewsBinding.loadingLout.setVisibility(View.GONE);
                } else {
                    activityAllReviewsLandBinding.loadingLout.setVisibility(View.GONE);
                }
            } else {
                allReviewsAdapter.removeItem(reviewModelsList.size() - 1);
                if(appReviewsModel.getApp_reviews() != null && appReviewsModel.getApp_reviews().size() > 0) {
                    reviewModelsList.addAll(appReviewsModel.getApp_reviews());
                    allReviewsAdapter.setLoading(false);
                } else {
                    allReviewsAdapter.setLoading(true);
                }
                allReviewsAdapter.notifyDataSetChanged();
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    activityAllReviewsBinding.loadingLout.setVisibility(View.GONE);
                } else {
                    activityAllReviewsLandBinding.loadingLout.setVisibility(View.GONE);
                }
            }
        }

    }

    /**
     * set all reviews to recycler view adapter
     */
    private void setDataToList() {

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {

                activityAllReviewsBinding.idRvAllReview.setLayoutManager(new LinearLayoutManager(this));
                allReviewsAdapter = new AllReviewsAdapter(this, reviewModelsList, activityAllReviewsBinding.idRvAllReview, TAG);
                activityAllReviewsBinding.idRvAllReview.setAdapter(allReviewsAdapter);
                activityAllReviewsBinding.idRvAllReview.setFocusable(false);
                allReviewsAdapter.setmOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {
                        allReviewsAdapter.addItem(null);
                        pageNo++;
                        getDataFromServer();
                    }
                });

                activityAllReviewsBinding.idRvAllReview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        reviewHeaderDy = reviewHeaderDy + dy;
                        if((reviewHeaderDy - activityAllReviewsBinding.idRvAllReview.computeVerticalScrollOffset()) > 200) {
                            activityAllReviewsBinding.idLlRatingHeader.setY(-reviewHeaderDy);
                        } else {
                            activityAllReviewsBinding.idLlRatingHeader.setY(-activityAllReviewsBinding.idRvAllReview.computeVerticalScrollOffset());
                        }

                    }
                });
        } else {
            activityAllReviewsLandBinding.idRvAllReview.setLayoutManager(new LinearLayoutManager(this));
            allReviewsAdapter = new AllReviewsAdapter(this, reviewModelsList, activityAllReviewsLandBinding.idRvAllReview, TAG);
            activityAllReviewsLandBinding.idRvAllReview.setAdapter(allReviewsAdapter);
            allReviewsAdapter.setmOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    allReviewsAdapter.addItem(null);
                    pageNo++;
                    getDataFromServer();
                }
            });
        }
    }

    /**
     * Display rating star wise
     *
     * @param appReviewsModel
     */
    private void setData(AppReviewsModel appReviewsModel) {

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            activityAllReviewsBinding.setAapReviewModel(appReviewsModel);

            if (star != null) {
                BarData data = new BarData(getXAxisValues(), getDataSet(appReviewsModel));
                data.setValueFormatter(new MyValueFormatter());
                activityAllReviewsBinding.idRatingBarChart.setData(data);
            }
            activityAllReviewsBinding.idRatingBarChart.setDescription("");

            activityAllReviewsBinding.idRatingBarChart.getAxisLeft().setEnabled(false);
            activityAllReviewsBinding.idRatingBarChart.getAxisRight().setEnabled(false);
            activityAllReviewsBinding.idRatingBarChart.getXAxis().setEnabled(false);
            activityAllReviewsBinding.idRatingBarChart.getLegend().setEnabled(false);
            activityAllReviewsBinding.idRatingBarChart.invalidate();
            activityAllReviewsBinding.idRatingBarChart.setDrawGridBackground(false);
            activityAllReviewsBinding.idRatingBarChart.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return false;
                }
            });

        } else {

            activityAllReviewsLandBinding.setAapReviewModel(appReviewsModel);

            if (star != null) {
                BarData data = new BarData(getXAxisValues(), getDataSet(appReviewsModel));
                data.setValueFormatter(new MyValueFormatter());
                activityAllReviewsLandBinding.idRationgBarChart.setData(data);
            }
            activityAllReviewsLandBinding.idRationgBarChart.setDescription("");

            activityAllReviewsLandBinding.idRationgBarChart.getAxisLeft().setEnabled(false);
            activityAllReviewsLandBinding.idRationgBarChart.getAxisRight().setEnabled(false);
            activityAllReviewsLandBinding.idRationgBarChart.getXAxis().setEnabled(false);
            activityAllReviewsLandBinding.idRationgBarChart.getLegend().setEnabled(false);
            activityAllReviewsLandBinding.idRationgBarChart.invalidate();
            activityAllReviewsLandBinding.idRationgBarChart.setDrawGridBackground(false);
            activityAllReviewsLandBinding.idRationgBarChart.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return false;
                }
            });

        }

    }

    /**
     * Display star with color
     *
     * @param appReviewsModel
     * @return
     */

    private ArrayList<BarDataSet> getDataSet(AppReviewsModel appReviewsModel) {
        ArrayList<BarDataSet> dataSets;
        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        BarEntry v2e1 = new BarEntry(star.getStar5(), 4);
        valueSet2.add(v2e1);
        BarEntry v2e2 = new BarEntry(star.getStar4(), 3);
        valueSet2.add(v2e2);
        BarEntry v2e3 = new BarEntry(star.getStar3(), 2);
        valueSet2.add(v2e3);
        BarEntry v2e4 = new BarEntry(star.getStar2(), 1);
        valueSet2.add(v2e4);
        BarEntry v2e5 = new BarEntry(star.getStar1(), 0);
        valueSet2.add(v2e5);


        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "");

        ArrayList<Integer> colorArrayList = new ArrayList<>();
        colorArrayList.add(ContextCompat.getColor(getApplicationContext(), R.color.star5));
        colorArrayList.add(ContextCompat.getColor(getApplicationContext(), R.color.star4));
        colorArrayList.add(ContextCompat.getColor(getApplicationContext(), R.color.star3));
        colorArrayList.add(ContextCompat.getColor(getApplicationContext(), R.color.star2));
        colorArrayList.add(ContextCompat.getColor(getApplicationContext(), R.color.star1));
        barDataSet2.setColors(colorArrayList);
        barDataSet2.setValueTextSize(getResources().getDimension(R.dimen.text4));
        if (orientation != Configuration.ORIENTATION_PORTRAIT) {
            barDataSet2.setBarSpacePercent(0);
        }


        dataSets = new ArrayList<>();
        dataSets.add(barDataSet2);
        return dataSets;
    }

    /**
     * Create color line for star
     *
     * @return
     */

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("");
        xAxis.add("");
        xAxis.add("");
        xAxis.add("");
        xAxis.add("");
        return xAxis;
    }

    /**
     * Activity will finish on Cancel button click
     *
     * @param view
     */
    public void onCancelClicked(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (request != null) {
            request.cancel();
        }
    }

    /**
     * result form start activity
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConstants.ACTIVITY_INTERNET_CONNECTION_RESULT_CODE) {
            getDataFromServer();
        }
    }

    /**
     * Class for set float value to int formate
     */
    public class MyValueFormatter implements ValueFormatter {

        @Override
        public String getFormattedValue(float value) {
            return Math.round(value)+"";
        }
    }

    private class LoadAllRatingApp implements STRINGRequest.StringResponseListener {

        @Override
        public void onResponseSuccess(String response, int requestCode) {

            if(context != null) {
                jsonParse(response);
            }
        }

        @Override
        public void onResponseFailure(VolleyError error) {
            AndroidAppUtils.showLogE(TAG, error.toString());
        }
    }
}
