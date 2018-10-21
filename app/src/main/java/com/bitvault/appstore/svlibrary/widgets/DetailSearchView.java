package com.bitvault.appstore.svlibrary.widgets;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bitvault.appstore.R;
import com.bitvault.appstore.app.searchapp.OnBackPressedListener;
import com.bitvault.appstore.custom.CustomEditText;
import com.bitvault.appstore.custom.FontTextView;
import com.bitvault.appstore.preference.AppPreferences;
import com.bitvault.appstore.svlibrary.interfaces.OnSearchActionsListener;
import com.bitvault.appstore.svlibrary.interfaces.OnSearchListener;
import com.bitvault.appstore.svlibrary.interfaces.OnSimpleSearchActionsListener;
import com.bitvault.appstore.svlibrary.utils.Util;
import com.bitvault.appstore.utils.AndroidAppUtils;
import com.bitvault.appstore.webservice.response.AppList;
import com.bitvault.appstore.webservice.response.SearchResponse;


/**
 * Created by Dheeraj Bansal on 2/05/2017.
 * version 1.0.0
 * material search view
 */

public class DetailSearchView extends FrameLayout implements View.OnClickListener, OnSearchActionsListener, View.OnFocusChangeListener {

    final Animation fade_in;
    final Animation fade_out;
    private CustomEditText mSearchEditText;
    private FontTextView mClearSearch;
    private OnSearchListener mOnSearchListener;
    private View lineDivider;
    private CardView cardLayout;
    private RelativeLayout searchLayout;
    private FontTextView backArrowImg;
    private ListView mFrameLayout;
    private Context mContext;
    private SearchViewResults searchViewResults;
    private OnSimpleSearchActionsListener searchListener;
    private OnBackPressedListener onBackPressedListener;
    private boolean isShowAnimation = false;


    public DetailSearchView(final Context context) {
        this(context, null);
    }

    public DetailSearchView(final Context context, final AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DetailSearchView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        final LayoutInflater factory = LayoutInflater.from(context);
        factory.inflate(R.layout.toolbar_searchview, this);
        mContext = context;
        cardLayout = (CardView) findViewById(R.id.card_search);
        mFrameLayout = (ListView) findViewById(R.id.material_search_container);
        searchLayout = (RelativeLayout) findViewById(R.id.view_search);
        lineDivider = findViewById(R.id.line_divider);
        mSearchEditText = (CustomEditText) findViewById(R.id.edit_text_search);
        backArrowImg = (FontTextView) findViewById(R.id.image_search_back);
        mClearSearch = (FontTextView) findViewById(R.id.clearSearch);
        mSearchEditText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        fade_in = AnimationUtils.loadAnimation(getContext().getApplicationContext(), android.R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(getContext().getApplicationContext(), android.R.anim.fade_out);

        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mOnSearchListener != null) {
                    mOnSearchListener.onSearch(getSearchQuery());
                    onQuery(getSearchQuery());
                }
                toggleClearSearchButton(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mSearchEditText.setOnClickListener(this);
        mSearchEditText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        mSearchEditText.setOnFocusChangeListener(this);

        mSearchEditText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER ||
                                keyCode == KeyEvent.KEYCODE_SEARCH)) {
                    final String query = getSearchQuery();
                    if(query.length() > 1) {
                        AppPreferences.getInstance(context).setSearchBarKeywords(query);
                    }
                    if (!TextUtils.isEmpty(query) && mOnSearchListener != null) {
                        onBackPressedListener.onIMESearchPress(query);
                    }
                    return true;
                }
                return false;
            }
        });

        backArrowImg.setOnClickListener(this);
        mClearSearch.setOnClickListener(this);
        setVisibility(View.GONE);
        clearAnimation();
    }


    /**
     * return back image txt
     *
     * @return
     */
    public FontTextView getBackArrowImg() {
        return backArrowImg;
    }

    /**
     * set txt to back img
     *
     * @param text
     */
    public void setBackArrowImg(String text) {
        backArrowImg.setText(text);
    }

    /**
     * return search result
     *
     * @return
     */
    public SearchViewResults getSearchViewResults() {
        return searchViewResults;
    }

    /**
     * set animation will show or not
     *
     * @param showAnimation
     */
    public void setShowAnimation(boolean showAnimation) {
        isShowAnimation = showAnimation;
    }

    /**
     * set edit text listener for back key handle
     */
    public void setEditTextListener() {
        mSearchEditText.setListener(onBackPressedListener);
    }

    /**
     * set text for hint
     *
     * @param hint
     */
    public void setHintText(String hint) {
        mSearchEditText.setHint(hint);
    }

    /**
     * return card layout
     *
     * @return
     */
    public CardView getCardLayout() {
        return cardLayout;
    }

    /**
     * return list viewe
     *
     * @return
     */
    public ListView getListview() {
        return mFrameLayout;
    }

    /**
     * return line divider
     *
     * @return
     */
    public View getLineDivider() {
        return lineDivider;
    }

    @Override
    public void onItemClicked(AppList item) {
        this.searchListener.onItemClicked(item);
    }

    /**
     * show progress while searching
     *
     * @param show
     */
    @Override
    public void showProgress(boolean show) {

    }

    /**
     * after search list empty
     */
    @Override
    public void listEmpty() {

    }

    /**
     * after search list scroll
     */
    @Override
    public void onScroll() {
        this.searchListener.onScroll();
    }

    /**
     * no result based on string
     *
     * @param localizedMessage
     */
    @Override
    public void error(String localizedMessage) {
        this.searchListener.error(localizedMessage);
    }

    public void setOnSearchListener(final OnSearchListener l) {
        mOnSearchListener = l;
    }

    public void setSearchResultsListener(OnSimpleSearchActionsListener listener) {
        this.searchListener = listener;
    }

    public void setOnBackPressedListener(OnBackPressedListener listener) {
        this.onBackPressedListener = listener;
    }

    public String getSearchQuery() {
        return mSearchEditText.getText() != null ? mSearchEditText.getText().toString() : "";
    }

    /**
     * set search query
     *
     * @param query
     */
    public void setSearchQuery(final String query) {
        mSearchEditText.setText(query);
        toggleClearSearchButton(query);
    }

    /**
     * is search view visible or not
     *
     * @return
     */
    public boolean isSearchViewVisible() {
        return getVisibility() == View.VISIBLE;
    }

    // Show the SearchView
    public void display() {
        if (isSearchViewVisible()) {
            return;
        }
        setVisibility(View.VISIBLE);
        mOnSearchListener.searchViewOpened();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isShowAnimation) {
            final Animator animator = ViewAnimationUtils.createCircularReveal(cardLayout,
                    cardLayout.getWidth() - Util.dpToPx(getContext(), 56),
                    Util.dpToPx(getContext(), 23),
                    0,
                    (float) Math.hypot(cardLayout.getWidth(), cardLayout.getHeight()));
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    searchLayout.setVisibility(View.VISIBLE);
                    searchLayout.startAnimation(fade_in);
                    if (isShowAnimation) {
                        ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).
                                toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            cardLayout.setVisibility(View.VISIBLE);
            if (cardLayout.getVisibility() == View.VISIBLE) {
                animator.setDuration(300);
                animator.start();
                cardLayout.setEnabled(true);
            }
            fade_in.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    mFrameLayout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } else {
            cardLayout.setVisibility(View.VISIBLE);
            cardLayout.setEnabled(true);

            mFrameLayout.setVisibility(View.VISIBLE);
            if(mSearchEditText.isFocused()) {
                ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).
                        toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }

    }

    /**
     * after enter text for search
     *
     * @param query
     */
    public void onQuery(String query) {
        String trim = query.trim();

        if (searchViewResults != null) {
            searchViewResults.updateSequence(trim);
        } else {
            searchViewResults = new SearchViewResults(mContext, trim);
            searchViewResults.setListView(mFrameLayout);
            searchViewResults.setSearchProvidersListener(this);
        }
    }

    /**
     * hide searchview
     */
    public void hide() {
        if (!isSearchViewVisible()) {
            return;
        }
        mOnSearchListener.searchViewClosed();
        //remove later
//        if (!isShowAnimation)
//            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isShowAnimation) {
            final Animator animatorHide = ViewAnimationUtils.createCircularReveal(cardLayout,
                    cardLayout.getWidth() - Util.dpToPx(getContext(), 56),
                    Util.dpToPx(getContext(), 23),
                    (float) Math.hypot(cardLayout.getWidth(), cardLayout.getHeight()),
                    0);
            animatorHide.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    searchLayout.startAnimation(fade_out);
                    searchLayout.setVisibility(View.INVISIBLE);
                    cardLayout.setVisibility(View.GONE);
                    ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(searchLayout.getWindowToken(), 0);

                    mFrameLayout.setVisibility(View.GONE);
                    clearSearch();
                    setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animatorHide.setDuration(300);
            animatorHide.start();
        } else {
            ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(searchLayout.getWindowToken(), 0);
            searchLayout.setVisibility(View.INVISIBLE);
            cardLayout.setVisibility(View.GONE);
            clearSearch();
            setVisibility(View.GONE);
        }
    }

    public CustomEditText getSearchView() {
        return mSearchEditText;
    }

    /**
     * toggle search button
     *
     * @param query
     */
    private void toggleClearSearchButton(final CharSequence query) {
        mClearSearch.setVisibility(!TextUtils.isEmpty(query) ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * clear search edit text
     */
    private void clearSearch() {
        mSearchEditText.setText("");
        mClearSearch.setVisibility(View.INVISIBLE);
    }

    /**
     * after search cancel
     */
    private void onCancelSearch() {
        if (mOnSearchListener != null) {
            mOnSearchListener.onCancelSearch();
        } else {
            hide();
        }
    }

    /**
     * back  button event
     *
     * @param event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(final KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP &&
                event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            onCancelSearch();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.edit_text_search) {

        } else if (id == R.id.image_search_back) {
            onCancelSearch();
            hide();
            onBackPressedListener.onBackImagePress();

        } else if (id == R.id.clearSearch) {
            clearSearch();
            if(onBackPressedListener != null) {
                onBackPressedListener.onClearImgPress();
            }
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {

    }

}