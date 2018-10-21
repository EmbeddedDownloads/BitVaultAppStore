package com.bitvault.appstore.app.appdetail;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.bitvault.appstore.R;
import com.bitvault.appstore.app.category.CategoryActivity;
import com.bitvault.appstore.app.dashboard.HomeFragment;
import com.bitvault.appstore.app.rating.AllReviewsActivity;
import com.bitvault.appstore.app.searchapp.OnBackPressedListener;
import com.bitvault.appstore.app.searchapp.SearchActivity;
import com.bitvault.appstore.app.setting.InternetConnectionActivity;
import com.bitvault.appstore.application.APIs;
import com.bitvault.appstore.application.AppStoreApplication;
import com.bitvault.appstore.custom.FontTextView;
import com.bitvault.appstore.dialog.PermissionAcceptDialog;
import com.bitvault.appstore.dialog.PermissionDialog;
import com.bitvault.appstore.svlibrary.interfaces.OnSearchListener;
import com.bitvault.appstore.svlibrary.interfaces.OnSimpleSearchActionsListener;
import com.bitvault.appstore.svlibrary.widgets.DetailSearchView;
import com.bitvault.appstore.utils.AndroidAppUtils;
import com.bitvault.appstore.utils.AppConstants;
import com.bitvault.appstore.utils.HorizontalDividerItemDecoration;
import com.bitvault.appstore.utils.KeyboardUtils;
import com.bitvault.appstore.utils.NetworkConnection;
import com.bitvault.appstore.utils.OnLoadMoreListener;
import com.bitvault.appstore.utils.Utils;
import com.bitvault.appstore.webservice.STRINGRequest;
import com.bitvault.appstore.webservice.response.AppDetailResponse;
import com.bitvault.appstore.webservice.response.SearchResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import java.io.InputStream;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/*
 * Created Dheeraj Bansal root on 5/5/17.
 * version 1.0.0
 */

/**
 * Display all details about an application
 */

public class ProductDetailActivity extends AppCompatActivity implements OnSearchListener,
        OnSimpleSearchActionsListener, View.OnClickListener,
        OnBackPressedListener, OnPermissionAcceptListener {

    private final static String TAG = ProductDetailActivity.class.getCanonicalName();

    //header
    private Intent intent;
    private DetailSearchView mSearchView;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private MenuItem menuItemSearch;
    private FontTextView textViewBack;
    private NetworkImageView bannerImg;
    private NetworkImageView appIconImg;
    private TextView appNameTxt;
    private TextView companyTxt;
    private LinearLayout btnLayout;
    private TextView uninstallTxt;
    private TextView installTxt;
    private LinearLayout downloadLayout;
    private TextView downloadTxt;
    private TextView progressTxt;
    private ProgressBar downloadPB;
    private FontTextView crossTxt, ftvSearch;
    private TextView installingTxt;
    private TextView totalDownloadTxt;
    private TextView downloadUnitTxt;
    private RelativeLayout ratingLayout;
    private RatingBar smallRatingBar;
    private TextView ratingSmallTxt;
    private TextView totalRatingTxt;
    private NetworkImageView categoryImg;
    private FontTextView similarTxt;
    private TextView shortDescTxt;
    private TextView readMoreTxt;
    private ViewPager screenShotPager;
    private ViewPager reviewPager;
    private InkPageIndicator inkPageIndicator;
    private TextView rateThreeDotTxt;
    private TextView rateOnTxt;
    private TextView rateOnReviewTxt;
    private RatingBar rateonRatingBar;
    private TextView largeRatingTxt;
    private RatingBar largeRatingBar;
    private TextView totalRatingLargeTxt;
    private TextView star5Txt;
    private TextView star4Txt;
    private TextView star3Txt;
    private TextView star2Txt;
    private TextView star1Txt;
    private RatingBar reviewRatingBar;
    private TextView dateTxt;
    private TextView reviewTxt;
    private TextView threeDotTxt;
    private TextView allReviewTxt;
    private RelativeLayout similarAppLayout;
    private RecyclerView similarAppRecyclerView;
    private AppDetailResponse appDetailResponse;
    private NestedScrollView svNestedScrollView;
    private int lastTopValue = 0;
    private RelativeLayout rlHeader;
    private ReviewPagerAdapter reviewPagerAdapter;

    private int mScreenWidth;
    private int mScreenHeight;

    private float currentRating;

    private AppAdapter appAdapter;
    private RelativeLayout rlLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            mScreenWidth = displayMetrics.widthPixels;
            mScreenHeight = displayMetrics.heightPixels;

            textViewBack = (FontTextView) findViewById(R.id.id_detail_back);
            mSearchView = (DetailSearchView) findViewById(R.id.id_detail_search_view);
            mSearchView.setOnSearchListener(this);
            mSearchView.setSearchResultsListener(this);
            mSearchView.setOnBackPressedListener(this);
            mSearchView.setShowAnimation(true);
            mSearchView.getSearchView().setListener(this);
            mSearchView.setHintText(getString(R.string.search_hint));

            init();
            setListener();

                try {
              // These line will be remove later

//            InputStream inputStream = getResources().openRawResource(R.raw.appdetail);
//            String data = Utils.readTextFile(inputStream);
//            Gson gson = new GsonBuilder().create();
//            appDetailResponse = gson.fromJson(data, AppDetailResponse.class);
//            if (appDetailResponse != null) {
//                jsonParse(appDetailResponse);
//            }

                    InputStream inputStream = getResources().openRawResource(R.raw.search);
                    String data = Utils.readTextFile(inputStream);
                    Gson gson = new GsonBuilder().create();
                    SearchResponse response = gson.fromJson(data, SearchResponse.class);
                    if (response != null) {
                        setCategoryApp(response);
                    }
                } catch (NullPointerException e) {
                    AndroidAppUtils.showLogE(TAG, e.toString());
                }

        getDataFromServer();
    }

    /**
     * get apps data from Server
     */
    public void getDataFromServer() {
        if(NetworkConnection.isNetworkAvailable(this)) {
            //call api later use
            Map<String,String> param=new HashMap<>();
            String packageName = intent.getStringExtra(getString(R.string.package_name));
            if(packageName != null) {
                param.put(getString(R.string.package_name), packageName);
            }
            new STRINGRequest().postRequest(this,new LoadProductDataListener(),param, APIs.API_APP_DETAIL, Request.Method.POST);
        }
        else{
            Intent intent = new Intent(this, InternetConnectionActivity.class);
            startActivityForResult(intent, AppConstants.ACTIVITY_INTERNET_CONNECTION_RESULT_CODE);
        }
    }

    /**
     * Method used to replace fragment
     *
     * * @param replaceFragment
     */
    public void replaceFragment(Fragment replaceFragment, boolean addToBackStack) {

        Utils.hideKeyboard(this, this.getCurrentFocus());
        Fragment mFragment = replaceFragment;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (addToBackStack) {
            ft.replace(R.id.main_content, replaceFragment)
                    //.addToBackStack(replaceFragment.getClass().getName())
                    .commit();
        } else {
            ft.replace(R.id.main_content, replaceFragment)
                    .commit();
        }

    }


    /**
     * set rating pager item from fragment
     *
     * @param position
     */
    public void setCurrentPagerItem(int position, float currentRating) {
        this.currentRating = currentRating;
        reviewPager.setCurrentItem(position);
    }

    /**
     * after review submit
     * @param review
     */
    public void reviewSubmit(String review) {
        rateThreeDotTxt.setVisibility(View.VISIBLE);
        rateOnTxt.setVisibility(View.VISIBLE);
        if(review!=null) {
            rateOnReviewTxt.setText(review);
            rateOnReviewTxt.setVisibility(View.VISIBLE);
        }
        rateonRatingBar.setVisibility(View.VISIBLE);
        rateonRatingBar.setNumStars((int) currentRating);
        rateonRatingBar.setRating(currentRating);
        rateOnTxt.setText(String.format(getString(R.string.rate_on),
                Utils.getCurrentDateAndTime(getString(R.string.date_formate))));
        reviewPager.setVisibility(View.GONE);
        inkPageIndicator.setVisibility(View.GONE);
    }

    /**
     * set list of apps based on category
     *
     * @param response
     */
    private void setCategoryApp(SearchResponse response) {
        appAdapter=new AppAdapter(this, response.getAppList(),similarAppRecyclerView);
        similarAppRecyclerView.setAdapter(appAdapter);
        appAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

            }
        });
    }

    /**
     * display data into controls
     *
     * @param response
     */
    private void jsonParse(AppDetailResponse response) {
        AppDetailResponse.app_detail app_detail;

        if (response.getApp_detail() != null) {
            rlLoading.setVisibility(View.GONE);
            app_detail = response.getApp_detail();
            if (app_detail.getApp_banner_url() != null) {
                bannerImg.setImageUrl(app_detail.getApp_banner_url(), AppStoreApplication.getInstance().getImageLoader());
            }
            if (app_detail.getApp_icon_url() != null) {
                appIconImg.setDefaultImageResId(R.mipmap.ic_launcher);
                appIconImg.setErrorImageResId(R.mipmap.ic_launcher);
                appIconImg.setImageUrl(app_detail.getApp_icon_url(), AppStoreApplication.getInstance().getImageLoader());
            }
            if (app_detail.getApp_name() != null) {
                appNameTxt.setText(app_detail.getApp_name());
            }
            if (app_detail.getCompany_name() != null) {
                companyTxt.setText(app_detail.getCompany_name());
            }
            if (app_detail.getDownloads() == 0) {
                findViewById(R.id.id_dt_download_head_lout).setVisibility(View.GONE);
            } else {
                totalDownloadTxt.setText(String.valueOf(app_detail.getDownloads()));
                if(app_detail.getDownload_unit()==null){
                    downloadUnitTxt.setVisibility(View.GONE);
                }
                else {
                    downloadUnitTxt.setText(app_detail.getDownload_unit());
                }
            }
            if (app_detail.getCategory_icon() != null) {
                categoryImg.setImageUrl(app_detail.getCategory_icon(), AppStoreApplication.getInstance().getImageLoader());
            }
            AppDetailResponse.app_detail.star star = app_detail.getStar();
            if (star == null) {
                findViewById(R.id.id_dt_rating_head_lout).setVisibility(View.GONE);
            } else {
                long totalStar = star.getStar1() + star.getStar2() + star.getStar3() + star.getStar4() + star.getStar5();

                if (totalStar == 0) {
                    findViewById(R.id.id_dt_rating_sml_head_lout).setVisibility(View.GONE);
                } else {
                    ratingSmallTxt.setText(String.valueOf(app_detail.getRating()));
                    smallRatingBar.setRating((float) app_detail.getRating());
                    totalRatingTxt.setText(NumberFormat.getNumberInstance(Locale.US).format(totalStar));
                }
                shortDescTxt.setText(app_detail.getShort_description());
                screenShotPager.setAdapter(new ScreenShotAdapter(this, app_detail.getScreenshots()));

                largeRatingTxt.setText(String.valueOf(app_detail.getRating()));
                largeRatingBar.setRating((float) app_detail.getRating());
                totalRatingLargeTxt.setText(NumberFormat.getNumberInstance(Locale.US).format(totalStar));

                float width = (mScreenWidth / 2 - getResources().getDimension(R.dimen.dimen30));
                setStarWidth((star.getStar5() * width / totalStar), star5Txt);
                setStarWidth((star.getStar4() * width / totalStar), star4Txt);
                setStarWidth((star.getStar3() * width / totalStar), star3Txt);
                setStarWidth((star.getStar2() * width / totalStar), star2Txt);
                setStarWidth((star.getStar1() * width / totalStar), star1Txt);

            }
            List<AppDetailResponse.app_detail.review> list = app_detail.getReview();
            if (list == null || list.size() == 0) {
                findViewById(R.id.id_dt_review_head_lout).setVisibility(View.GONE);
            } else {
                reviewRatingBar.setRating((float) list.get(0).getRating());
                dateTxt.setText(list.get(0).getCreated_at());
                reviewTxt.setText(list.get(0).getReview());
            }

            if (Utils.isPackageInstalled(app_detail.getPackage_name(), this)) {
                installTxt.setText(getString(R.string.open));
                uninstallTxt.setVisibility(View.VISIBLE);
            } else {
                uninstallTxt.setVisibility(View.INVISIBLE);
            }

            if (Utils.getVersionNumber(app_detail.getPackage_name(), this) != null
                    && Utils.getVersionNumber(app_detail.getPackage_name(), this)
                    .equalsIgnoreCase(app_detail.getApp_version())) {
                installTxt.setText(getString(R.string.update));
                uninstallTxt.setVisibility(View.VISIBLE);
            } else {
                uninstallTxt.setVisibility(View.INVISIBLE);
            }

            downloadLayout.setVisibility(View.GONE);
            crossTxt.setVisibility(View.GONE);
            installingTxt.setVisibility(View.GONE);
        }

    }

    /**
     * Initialize all controls to set data and events
     */
    private void init() {
        intent = getIntent();
        ftvSearch = (FontTextView) findViewById(R.id.id_search);
        svNestedScrollView = (NestedScrollView) findViewById(R.id.id_sv_nested_scroll);
        rlHeader = (RelativeLayout) findViewById(R.id.id_rl_header);
        bannerImg = (NetworkImageView) findViewById(R.id.id_dt_appbanner);
        appIconImg = (NetworkImageView) findViewById(R.id.id_dt_app_icon);
        appNameTxt = (TextView) findViewById(R.id.id_dt_app_name);
        companyTxt = (TextView) findViewById(R.id.id_dt_company_name);

        btnLayout = (LinearLayout) findViewById(R.id.id_dt_button_lout);
        uninstallTxt = (TextView) findViewById(R.id.id_dt_uninstall);
        installTxt = (TextView) findViewById(R.id.id_dt_install);

        downloadLayout = (LinearLayout) findViewById(R.id.id_dt_download_lout);
        downloadTxt = (TextView) findViewById(R.id.id_dt_download_txt);
        progressTxt = (TextView) findViewById(R.id.id_dt_download_progress);
        crossTxt = (FontTextView) findViewById(R.id.id_dt_cross_txt);
        downloadPB = (ProgressBar) findViewById(R.id.id_dt_progressBar);
        installingTxt = (TextView) findViewById(R.id.id_dt_installing_txt);

        totalDownloadTxt = (TextView) findViewById(R.id.id_dt_total_downloads);
        downloadUnitTxt = (TextView) findViewById(R.id.id_dt_download_unit);
        ratingLayout = (RelativeLayout) findViewById(R.id.id_dt_rating_lout);
        ratingSmallTxt = (TextView) findViewById(R.id.id_dt_small_rating);
        totalRatingTxt = (TextView) findViewById(R.id.id_dt_total_rate);
        smallRatingBar = (RatingBar) findViewById(R.id.id_dt_small_ratingBar);
        categoryImg = (NetworkImageView) findViewById(R.id.id_dt_category_img);
        similarTxt = (FontTextView) findViewById(R.id.id_dt_similar_txt);

        shortDescTxt = (TextView) findViewById(R.id.id_dt_short_txt);
        readMoreTxt = (TextView) findViewById(R.id.id_dt_read_more_txt);

        screenShotPager = (ViewPager) findViewById(R.id.id_pager_screenshot);
        reviewPager = (ViewPager) findViewById(R.id.id_dt_viewpager);
        inkPageIndicator = (InkPageIndicator) findViewById(R.id.id_dt_indicator);
        rateThreeDotTxt = (TextView) findViewById(R.id.id_dt_rate_three_dot);
        rateOnTxt = (TextView) findViewById(R.id.id_dt_rateon_txt);
        rateOnReviewTxt = (TextView) findViewById(R.id.id_dt_rateon_review);
        rateonRatingBar = (RatingBar) findViewById(R.id.id_dt_rateon_ratebar);


        largeRatingTxt = (TextView) findViewById(R.id.id_dt_rating_txt);
        totalRatingLargeTxt = (TextView) findViewById(R.id.id_totallarge_rating_txt);
        largeRatingBar = (RatingBar) findViewById(R.id.id_dt_ratinbar);
        star5Txt = (TextView) findViewById(R.id.id_dt_star5_txt);
        star4Txt = (TextView) findViewById(R.id.id_dt_star4_txt);
        star3Txt = (TextView) findViewById(R.id.id_dt_star3_txt);
        star2Txt = (TextView) findViewById(R.id.id_dt_star2_txt);
        star1Txt = (TextView) findViewById(R.id.id_dt_star1_txt);

        reviewRatingBar = (RatingBar) findViewById(R.id.id_dt_review_ratingbar);
        dateTxt = (TextView) findViewById(R.id.id_dt_review_date_txt);
        threeDotTxt = (TextView) findViewById(R.id.id_dt_three_dot);
        reviewTxt = (TextView) findViewById(R.id.id_dt_review_txt);

        allReviewTxt = (TextView) findViewById(R.id.id_dt_all_review_txt);
        similarAppLayout = (RelativeLayout) findViewById(R.id.id_category_rlout);
        similarAppRecyclerView = (RecyclerView) findViewById(R.id.id_home_app_recycler);
        similarAppRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration(
                (int) getResources().getDimension(R.dimen.app_item_margin)));
        similarAppRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        rlLoading = (RelativeLayout) findViewById(R.id.loadingLout);

        reviewPagerAdapter = new ReviewPagerAdapter(this, getSupportFragmentManager());
        reviewPager.setAdapter(reviewPagerAdapter);
        reviewPager.setOffscreenPageLimit(2);
        inkPageIndicator.setViewPager(reviewPager);
        reviewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                return currentRating<1;
            }
        });
    }

    /**
     * set click event on textviews and layouts
     */
    private void setListener() {
        ftvSearch.setOnClickListener(this);
        appIconImg.setOnClickListener(this);
        uninstallTxt.setOnClickListener(this);
        installTxt.setOnClickListener(this);
        crossTxt.setOnClickListener(this);
        ratingLayout.setOnClickListener(this);
        similarTxt.setOnClickListener(this);
        categoryImg.setOnClickListener(this);
        readMoreTxt.setOnClickListener(this);
        threeDotTxt.setOnClickListener(this);
        allReviewTxt.setOnClickListener(this);
        similarAppLayout.setOnClickListener(this);
        rateThreeDotTxt.setOnClickListener(this);
        findViewById(R.id.id_detail_back).setOnClickListener(this);
        findViewById(R.id.id_dt_web_lout).setOnClickListener(this);
        findViewById(R.id.id_dt_email_lout).setOnClickListener(this);
        findViewById(R.id.id_dt_privacy_lout).setOnClickListener(this);
        findViewById(R.id.id_dt_permission_lout).setOnClickListener(this);

        //Scroll for Parallax effect
        svNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                Rect rect = new Rect();
                v.getLocalVisibleRect(rect);
                if (lastTopValue != rect.top) {
                    lastTopValue = rect.top;

                    // Taking -rect.top for Parallax effect on app Header Image and 3 for image animation
                    bannerImg.setY((float) (- rect.top / 3));
                }

                if(scrollY < oldScrollY) {
                    //Scroll Down

                    showViews();

                } else {

                    //Scroll Up

                    hideViews();

                }

            }
        });

    }

    /**
     * show search view on scroll down
     */
    private void showViews() {
        rlHeader.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }

    /**
     * hide search view on scroll down
     */
    private void hideViews() {
        rlHeader.animate().translationY(-(int) getResources().getDimension(R.dimen.dimen56)).setInterpolator(new AccelerateInterpolator(2));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search_view, menu);
        menuItemSearch = menu.findItem(R.id.action_search);
        menuItemSearch.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mSearchView.display();
                textViewBack.setVisibility(View.GONE);
                menuItemSearch.setVisible(false);
                return true;
            }
        });
        return true;
    }


    /**
     * after search
     *
     * @param query
     */
    @Override
    public void onSearch(String query) {
        mSearchView.onQuery(query);
    }

    @Override
    public void searchViewOpened() {

    }


    /**
     * call after search view closed
     */
    @Override
    public void searchViewClosed() {

    }

    /**
     * clear search on search view
     */
    @Override
    public void onCancelSearch() {
        textViewBack.setVisibility(View.VISIBLE);
        //menuItemSearch.setVisible(true);
    }

    /**
     * click on search item
     *
     * @param item
     */
    @Override
    public void onItemClicked(SearchResponse.appList item) {
        startActivity(new Intent(this, SearchActivity.class));
    }

    @Override
    public void onScroll() {

    }

    /**
     * call after search error
     *
     * @param localizedMessage
     */
    @Override
    public void error(String localizedMessage) {

    }

    /**
     * show star width based on rating
     *
     * @param width
     * @param textView
     */
    private void setStarWidth(float width, TextView textView) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.width = (int) width;
        params.height = (int) getResources().getDimension(R.dimen.dimen10);
        textView.setLayoutParams(params);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_search:
                mSearchView.display();
                textViewBack.setVisibility(View.GONE);
                //menuItemSearch.setVisible(false);
                break;
            case R.id.id_detail_back:
                finish();
                break;
            case R.id.id_dt_app_icon:
                Intent intent = new Intent(this, AppIconActivity.class);
                intent.putExtra(getString(R.string.intent_url), appDetailResponse.getApp_detail().getApp_icon_url());
                startActivity(intent);
                break;
            case R.id.id_dt_uninstall:
                break;
            case R.id.id_dt_install:
                if (appDetailResponse.getApp_detail().getPermissions() != null && appDetailResponse.getApp_detail().getPermissions().size() > 0) {
                    Dialog dialog = new PermissionAcceptDialog(this, this, appDetailResponse.getApp_detail().getApp_icon_url(),
                            appDetailResponse.getApp_detail().getApp_name(), appDetailResponse.getApp_detail().getPermissions());
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    Window window = dialog.getWindow();
                    lp.copyFrom(window.getAttributes());
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        lp.width = mScreenWidth;
                    } else {
                        lp.width = mScreenHeight;
                    }
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    window.setAttributes(lp);
                    dialog.show();
                } else {
                    startAppDownload();
                }
                break;
            case R.id.id_dt_cross_txt:
                downloadLayout.setVisibility(View.GONE);
                installTxt.setVisibility(View.VISIBLE);
                btnLayout.setVisibility(View.VISIBLE);
                if (Utils.isPackageInstalled(appDetailResponse.getApp_detail().getPackage_name(), this)) {
                    uninstallTxt.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.id_dt_rating_lout:
                intent = new Intent(this, AllReviewsActivity.class);
                intent.putExtra(getString(R.string.intent_app), appDetailResponse.getApp_detail().getApp_name());
                intent.putExtra(getString(R.string.package_name), appDetailResponse.getApp_detail().getPackage_name());
                startActivity(intent);
                break;
            case R.id.id_dt_category_img:
                startActivity(new Intent(this,SearchActivity.class));
                break;
            case R.id.id_dt_similar_txt:
                intent = new Intent(this,CategoryActivity.class);
                intent.putExtra(getString(R.string.header_title), getString(R.string.similar));
                intent.putExtra(getString(R.string.category_id), appDetailResponse.getApp_detail().getCategory_id());
                startActivity(intent);
                break;
            case R.id.id_dt_read_more_txt:
                intent = new Intent(this, FullDescriptionActivity.class);
                intent.putExtra(getString(R.string.package_name), appDetailResponse.getApp_detail().getPackage_name());
                startActivity(intent);
                break;
            case R.id.id_dt_three_dot:
                break;
            case R.id.id_dt_rate_three_dot:
                PopupMenu popup = new PopupMenu(this, view);
                popup.getMenuInflater().inflate(R.menu.popup_rating, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.id_action_edit:
                            rateThreeDotTxt.setVisibility(View.GONE);
                            rateOnTxt.setVisibility(View.GONE);
                            rateOnReviewTxt.setVisibility(View.GONE);
                            rateonRatingBar.setVisibility(View.GONE);
                            reviewPager.setVisibility(View.VISIBLE);
                            inkPageIndicator.setVisibility(View.VISIBLE);
                            reviewPager.setCurrentItem(0);
                                break;

                            case R.id.id_action_delete:
                                reviewPager.setCurrentItem(0);
                                recreate();
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
                break;
            case R.id.id_dt_all_review_txt:
                intent = new Intent(this, AllReviewsActivity.class);
                intent.putExtra(getString(R.string.intent_app), appDetailResponse.getApp_detail().getApp_name());
                intent.putExtra(getString(R.string.package_name), appDetailResponse.getApp_detail().getPackage_name());
                startActivity(intent);
                break;
            case R.id.id_category_rlout:
                intent = new Intent(this,CategoryActivity.class);
                intent.putExtra(getString(R.string.header_title), getString(R.string.similar));
                intent.putExtra(getString(R.string.category_id), appDetailResponse.getApp_detail().getCategory_id());
                startActivity(intent);
                break;
            case R.id.id_dt_web_lout:
                try {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(getString(R.string.web_url)));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, R.string.install_web_browser_txt,  Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                break;
            case R.id.id_dt_email_lout:
                String[] TO = {getString(R.string.mail_to)};
                String[] CC = {getString(R.string.mail_to_cc)};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse(getString(R.string.mail_to_txt)));
                emailIntent.setType(getString(R.string.mail_type));

                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_CC, CC);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, appDetailResponse.getApp_detail().getApp_name());
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                final PackageManager pm = getPackageManager();
                final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
                ResolveInfo best = null;
                for(final ResolveInfo info : matches)
                    if (info.activityInfo.packageName.endsWith(getString(R.string.gm)) || info.activityInfo.name.toLowerCase().contains(getString(R.string.gmail)))
                        best = info;
                if (best != null)
                    emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);

                try {
                    startActivity(emailIntent);
                    finish();

                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this,
                            R.string.email_client_txt, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.id_dt_privacy_lout:
                try {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getString(R.string.privacy_url)));
                startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.id_dt_permission_lout:
                try {
                    if (appDetailResponse.getApp_detail().getPermissions() != null && appDetailResponse.getApp_detail().getPermissions().size() > 0) {
                        Dialog dialog = new PermissionDialog(this, appDetailResponse.getApp_detail().getApp_icon_url(), appDetailResponse.getApp_detail().getApp_name(),
                                appDetailResponse.getApp_detail().getApp_version(), appDetailResponse.getApp_detail().getPermissions());
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        Window window = dialog.getWindow();
                        lp.copyFrom(window.getAttributes());
                        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                            lp.width = mScreenWidth;
                        } else {
                            lp.width = mScreenHeight;
                        }
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        window.setAttributes(lp);
                        dialog.show();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    /**
     * back key press
     */
    @Override
    public void onBackKeyPress() {

        if (mSearchView.getSearchViewResults() != null) {
            if (mSearchView.getSearchViewResults().getAdapter() != null && mSearchView.getSearchViewResults().getAdapter().getCount() > 0) {
                mSearchView.getSearchViewResults().clearAdapter();
                mSearchView.hide();
                //menuItemSearch.setVisible(true);
                textViewBack.setVisibility(View.VISIBLE);
            } else {
                if (mSearchView.getSearchView().isFocused() || mSearchView.getSearchView().isFocusable()) {
                    KeyboardUtils.hideSoftKeyboard(this);
                    mSearchView.hide();
                    //menuItemSearch.setVisible(true);
                    textViewBack.setVisibility(View.VISIBLE);
                } else {
                    finish();
                }
            }
        } else {
            if (mSearchView.getSearchView().isFocused() || mSearchView.getSearchView().isFocusable()) {
                KeyboardUtils.hideSoftKeyboard(this);
            } else {
                finish();
            }
        }

    }

    /**
     * click on back text on search view
     */
    @Override
    public void onBackImagePress() {
        mSearchView.hide();
        //menuItemSearch.setVisible(true);
        textViewBack.setVisibility(View.VISIBLE);
    }

    /**
     * click on cancel text on search view
     */
    @Override
    public void onClearImgPress() {

    }


    /**
     * click on search icon on keyboard
     */
    @Override
    public void onIMESearchPress(String query) {

    }

    /**
     * clear search result and hide keyboard
     */
    private void clearAdapter() {
        if (mSearchView.getSearchViewResults() != null) {
            if (mSearchView.getSearchViewResults().getAdapter() != null && mSearchView.getSearchViewResults().getAdapter().getCount() > 0) {
                mSearchView.getSearchViewResults().clearAdapter();
                KeyboardUtils.hideSoftKeyboard(this);
            }
        }
    }


    /**
     * after permission accept before install
     */
    @Override
    public void onPermissionAccept() {

        startAppDownload();
    }

    /**
     * This method download apk
     */
    public void startAppDownload() {

        downloadLayout.setVisibility(View.VISIBLE);
        crossTxt.setVisibility(View.VISIBLE);
        btnLayout.setVisibility(View.GONE);

        //use later
//        Map<String,String> param=new HashMap<>();
//        String packageName = intent.getStringExtra(getString(R.string.package_name));
//        if(packageName != null) {
//            param.put(getString(R.string.appId), getString(R.string.one));
//            param.put(getString(R.string.publicAdd), getString(R.string.anuj));
//        }
//        new STRINGRequest().postRequest(this,new LoadProductDataListener(),param, APIs.API_BITVAULT_APK_URL, Request.Method.GET);

        Intent i;
        PackageManager manager = getPackageManager();
        try {
            i = manager.getLaunchIntentForPackage(getString(R.string.apk_package_name));
            if (i == null)
                throw new PackageManager.NameNotFoundException();
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            InstallAPK downloadAndInstall = new InstallAPK();
            downloadAndInstall.setContext(getApplicationContext(), downloadPB);
            downloadAndInstall.execute(APIs.API_DOWNLOAD_APK);


        }

    }

    /**
     * for api call
     */
    private class LoadProductDataListener implements STRINGRequest.StringResponseListener {

        @Override
        public void onResponseSuccess(String response) {
            Gson gson = new GsonBuilder().create();
            appDetailResponse = gson.fromJson(response, AppDetailResponse.class);
            if (appDetailResponse != null) {
                jsonParse(appDetailResponse);
            }
        }

        @Override
        public void onResponseFailure(VolleyError error) {
            AndroidAppUtils.showLogE(TAG, error.toString());
        }
    }

    /**
     * result form start activity
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
}
