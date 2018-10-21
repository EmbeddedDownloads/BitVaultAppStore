package com.bitvault.appstore.app.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bitvault.appstore.R;
import com.bitvault.appstore.app.appdetail.ProductDetailFragment;
import com.bitvault.appstore.app.myapp.MyAppFragment;
import com.bitvault.appstore.app.searchapp.OnBackPressedListener;
import com.bitvault.appstore.app.searchapp.SearchFragment;
import com.bitvault.appstore.app.helpandsupport.HelpAndSupportActivity;
import com.bitvault.appstore.app.setting.InternetConnectionFragment;
import com.bitvault.appstore.app.setting.SettingActivity;
import com.bitvault.appstore.application.AppStoreApplication;
import com.bitvault.appstore.custom.FontTextView;
import com.bitvault.appstore.svlibrary.interfaces.OnSearchListener;
import com.bitvault.appstore.svlibrary.interfaces.OnSimpleSearchActionsListener;
import com.bitvault.appstore.svlibrary.widgets.DetailSearchView;
import com.bitvault.appstore.utils.KeyboardUtils;
import com.bitvault.appstore.utils.Utils;
import com.bitvault.appstore.webservice.response.AppList;
import com.bitvault.appstore.webservice.response.SearchResponse;

import pl.tajchert.nammu.Nammu;


/**
 * Dashboard activity
 */
public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener,
        OnSearchListener,
        OnSimpleSearchActionsListener, OnBackPressedListener, View.OnClickListener {

    //code for check after setResult() request check , used also in other classes
    public static final int ACTIVITY_CODE = 101;
    private int MY_APP_FRAGMENT_SELECTED = 1;
    public DetailSearchView mSearchView;

    private FragmentDrawer mFragmentDrawer;

    public Fragment mFragment;

    public RelativeLayout rlHeader;
    private FontTextView headerMenuBtn;
    private TextView headerSearchTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Nammu.init(getApplicationContext());
        mFragmentDrawer = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        mFragmentDrawer.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.id_drawer_layout), null, false);
        mFragmentDrawer.setDrawerListener(this);
        headerMenuBtn = (FontTextView) findViewById(R.id.id_header_menu_btn);
        headerSearchTxt = (TextView) findViewById(R.id.id_header_text_search);
        rlHeader = (RelativeLayout) findViewById(R.id.id_header);

        mSearchView = (DetailSearchView) findViewById(R.id.id_detail_search_view);

        mSearchView.setOnSearchListener(this);
        mSearchView.setSearchResultsListener(this);
        mSearchView.setOnBackPressedListener(this);
        mSearchView.getSearchView().setListener(this);
        mSearchView.setShowAnimation(false);
        mSearchView.setHintText(getString(R.string.search_hint));

        setListener();
        Intent intent = getIntent();
        if(intent != null && intent.getStringExtra(getString(R.string.destination_screen))  != null
                && intent.getStringExtra(getString(R.string.destination_screen)).equals(getString(R.string.my_apps))) {
            replaceFragment(new HomeFragment(), null, Utils.TAG_HOME_FRAGMENT, true);
            showMyAppFragment();
            mFragmentDrawer.setSelectIndex(MY_APP_FRAGMENT_SELECTED);

        } else if (intent != null && intent.getStringExtra(getString(R.string.destination_screen)) != null
                && intent.getStringExtra(getString(R.string.destination_screen)).equals(getString(R.string.app_detail))) {

            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.appId), String.valueOf(intent.getLongExtra(getString(R.string.appId), 1)));
            replaceFragment(new ProductDetailFragment(), bundle, Utils.TAG_PRODUCT_DETAIL_FRAGMENT, true);

        } else {
                replaceFragment(new HomeFragment(), null, Utils.TAG_HOME_FRAGMENT, true);
        }

    }

    /**
     * Visible search view
     *
     * @param visibility
     */
    public void setHeaderVisibility(int visibility) {
        rlHeader.setVisibility(visibility);

    }

    /**
     * Set Listener on view
     */
    private void setListener() {

        headerMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!mFragmentDrawer.isDrawerOpen()) {
                    mFragmentDrawer.openDrawer();
                    return;
                }

            }
        });
        headerSearchTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                displaySearchBox();

            }
        });
    }

    /**
     * Display search box for search app
     */
    public void displaySearchBox() {

        // Focus the field.
        mSearchView.getSearchView().requestFocus();
        mSearchView.display();

    }

    /**
     * set Position of header on scroll
     * @param dy
     */
    public void setHeader(int dy) {
        rlHeader.setY(dy);

    }
    /**
     * show search view on scroll of scrollbar
     */
    public void showViews() {
        mSearchView.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        rlHeader.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }

    /**
     * hide search view on scroll of scrollbar
     */
    public void hideViews() {
        mSearchView.animate().translationY(-(int) getResources().getDimension(R.dimen.dimen66)).setInterpolator(new AccelerateInterpolator(2));
        rlHeader.animate().translationY(-(int) getResources().getDimension(R.dimen.dimen66)).setInterpolator(new AccelerateInterpolator(2));
    }

    /**
     * call after drawer item click
     *
     * @param view
     * @param position
     */
    @Override
    public void onDrawerItemSelected(View view, int position) {
        mFragmentDrawer.setSelectIndex(position);
        switch (position) {
            case 0:
                if (!(mFragment instanceof HomeFragment)) {
                    FragmentManager fm = getSupportFragmentManager();
                    for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                        fm.popBackStackImmediate();
                    }
                }

                mFragment = getCurrentFragment();
                mFragmentDrawer.updateAdapter();
                rlHeader.setVisibility(View.VISIBLE);
                mSearchView.setPadding((int) getResources().getDimension(R.dimen.dimen4),
                        (int) getResources().getDimension(R.dimen.dimen5), (int) getResources().getDimension(R.dimen.dimen4),
                        (int) getResources().getDimension(R.dimen.dimen5));
                break;
            case 1:
                if (!(mFragment instanceof MyAppFragment)) {
                    showMyAppFragment();
                }
                break;
            case 2:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case 3:
               startActivity(new Intent(this, HelpAndSupportActivity.class));

                //remove later
//                NotificationReceiver notificationReceiver = new NotificationReceiver();
//                notificationReceiver.getAppsUpdatesInfo(getApplicationContext());

                break;

        }
    }

    /**
     * method open MyAppFragment
     */
    private void showMyAppFragment() {
        replaceFragment(new MyAppFragment(), null, Utils.TAG_MY_APP_FRAGMENT, true);
        mFragmentDrawer.updateAdapter();
        rlHeader.setVisibility(View.GONE);
    }

    /**
     * search call
     */
    @Override
    public void onSearch(String query) {
        mSearchView.onQuery(query);
    }

    /**
     * search view open
     */
    @Override
    public void searchViewOpened() {
        mSearchView.onQuery("");
        if(mSearchView.getSearchViewResults() != null) {
            mSearchView.getSearchViewResults().setHistoryItems();
        }
    }

    /**
     * search view close
     */
    @Override
    public void searchViewClosed() {

    }

    /**
     * cancel search
     */
    @Override
    public void onCancelSearch() {

    }

    /**
     * Search item clicked
     *
     * @param item
     */
    @Override
    public void onItemClicked(AppList item) {
        Bundle bundle = new Bundle();

        if(item.getApplicationId() != null) {
            bundle.putString(getResources().getString(R.string.appId), item.getApplicationId());
            replaceFragment(new ProductDetailFragment(), bundle, Utils.TAG_HOME_FRAGMENT, true);
        } else {
            bundle.putString(getResources().getString(R.string.search_query), item.getAppName());
            replaceFragment(new SearchFragment(), bundle, Utils.TAG_SEARCH_FRAGMENT, true);
        }
        clearAdapter();
    }

    /**
     * search view item scroll
     */
    @Override
    public void onScroll() {

    }

    /**
     * search view error after search
     */
    @Override
    public void error(String localizedMessage) {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onBackPressed() {

        if (mFragmentDrawer.isDrawerOpen()) {
            mFragmentDrawer.closeDrawer();
            return;
        }

        onBack();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * back key press event
     */
    @Override
    public void onBackKeyPress() {
        onBack();
    }

    /**
     * on Back Click
     */
    public void onBack() {

        if (mSearchView.getSearchViewResults() != null) {
            if (mSearchView.getSearchViewResults().getAdapter() != null && mSearchView.getSearchViewResults().getAdapter().getCount() > 0) {
                mSearchView.getSearchViewResults().clearAdapter();
                mSearchView.getSearchView().setText("");
                KeyboardUtils.hideSoftKeyboard(this);
            } else {
                if (mSearchView.getSearchView().isFocusable() && mSearchView.getSearchView().isFocused()) {
                    mSearchView.hide();
                } else {
                    onBackEvent();
                }
            }
        } else {
            if (mSearchView.getSearchView().isFocusable() && mSearchView.getSearchView().isFocused()) {
                mSearchView.hide();
            } else {
                onBackEvent();
            }
        }
    }

    /**
     * on Back Click
     */
    public void onBackEvent() {
        if (mFragment instanceof MyAppFragment) {
            mFragmentDrawer.setSelectIndex(0);
            mFragmentDrawer.updateAdapter();

        }

        if (getSupportFragmentManager().getBackStackEntryCount() > 1 || mFragment instanceof InternetConnectionFragment) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 2 && mFragment instanceof InternetConnectionFragment) {
                finish();
            } else {
                if (mFragment instanceof InternetConnectionFragment) {
                    //remove later
                    //getSupportFragmentManager().popBackStack();
                    finish();
                }
                super.onBackPressed();

                if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                    rlHeader.setVisibility(View.VISIBLE);
                    mSearchView.setPadding((int) getResources().getDimension(R.dimen.dimen4),
                            (int) getResources().getDimension(R.dimen.dimen5), (int) getResources().getDimension(R.dimen.dimen4),
                            (int) getResources().getDimension(R.dimen.dimen5));
                }
                mFragment = getCurrentFragment();
            }
            return;
        }

        finish();
    }

    /**
     * back text event
     */
    @Override
    public void onBackImagePress() {
        KeyboardUtils.hideSoftKeyboard(this);
        if (mSearchView.getBackArrowImg().getText().toString().equalsIgnoreCase("h")) {
            mFragmentDrawer.openDrawer();
        } else {
            clearAdapter();
        }
    }

    /**
     * cancel txt event
     */
    @Override
    public void onClearImgPress() {
        KeyboardUtils.hideSoftKeyboard(this);
    }

    /**
     * search button on keyboard
     *
     * @param query
     */
    @Override
    public void onIMESearchPress(String query) {
        Bundle bundle = new Bundle();
        bundle.putString(getResources().getString(R.string.search_query), query);
        replaceFragment(new SearchFragment(), bundle, Utils.TAG_SEARCH_FRAGMENT, true);
        clearAdapter();
    }

    /**
     * clear search result
     */
    public void clearAdapter() {
        if (mSearchView.getSearchViewResults() != null) {
            if (mSearchView.getSearchViewResults().getAdapter() != null && mSearchView.getSearchViewResults().getAdapter().getCount() > 0) {
                mSearchView.getSearchViewResults().clearAdapter();
                mSearchView.getSearchView().setText("");
                KeyboardUtils.hideSoftKeyboard(this);
            }
        }
    }

    /**
     * This method hide search view
     */
    public void hideMainSearchView() {
        mSearchView.hide();
        rlHeader.setVisibility(View.GONE);
        mSearchView.setPadding((int) getResources().getDimension(R.dimen.dimen0),
                (int) getResources().getDimension(R.dimen.dimen0), (int) getResources().getDimension(R.dimen.dimen0),
                (int) getResources().getDimension(R.dimen.dimen0));
        mSearchView.setBackground(null);
    }


    /**
     * Method used to replace fragment on Dashboard AllApps and home dashboard
     * <p>
     * * @param replaceFragment
     */
    public void replaceFragment(Fragment replaceFragment, Bundle bundle, String fragmentTag, boolean addToBackStack) {

        Utils.hideKeyboard(this, this.getCurrentFocus());
        mFragment = replaceFragment;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (bundle != null) {
            replaceFragment.setArguments(bundle);
        }
        AppStoreApplication.getInstance().cancelPendingRequests(AppStoreApplication.getInstance().getRequestQueue());
        fragmentTransaction.replace(R.id.container_body, replaceFragment, fragmentTag);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragmentTag);
        }
        try {
            fragmentTransaction.commit();
        } catch (IllegalStateException mIllegalStateException) {
            fragmentTransaction.commitAllowingStateLoss();
        }


        if (Utils.TAG_HOME_FRAGMENT.equals(fragmentTag)) {
            getSupportFragmentManager().popBackStack(Utils.TAG_HOME_FRAGMENT, 0);
            rlHeader.setVisibility(View.VISIBLE);
            mSearchView.setPadding((int) getResources().getDimension(R.dimen.dimen4),
                    (int) getResources().getDimension(R.dimen.dimen5), (int) getResources().getDimension(R.dimen.dimen4),
                    (int) getResources().getDimension(R.dimen.dimen5));
            mSearchView.setBackground(ContextCompat.getDrawable(this, R.drawable.drw_header));
        }
        showViews();
    }

    /**
     * this method return current Fragment
     *
     * @return
     */
    private Fragment getCurrentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        Fragment currentFragment = fragmentManager.findFragmentByTag(fragmentTag);
        return currentFragment;
    }

    /**
     * get FragmentDrawer object
     *
     * @return
     */
    public FragmentDrawer getmFragmentDrawer() {
        return mFragmentDrawer;
    }
}
