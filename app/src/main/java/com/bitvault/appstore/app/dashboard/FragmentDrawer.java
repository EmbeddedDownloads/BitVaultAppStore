package com.bitvault.appstore.app.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.bitvault.appstore.R;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Dheeraj Bansal on 13/2/17.
 * version 1.0.0
 */

/**
 * fragment for slider menu
 */
public class FragmentDrawer extends Fragment {


    private String[] titles = null;
    private String[] type = null;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View containerView;
    private FragmentDrawerListener drawerListener;
    private int selectIndex = 0;

    private DrawerAdapter adapter;

    public int getSelectIndex() {
        return selectIndex;
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    /**
     * set drawer item list
     * @return
     */
    private List<DrawerItem> getData() {
        List<DrawerItem> data = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            data.add(new DrawerItem(type[i], titles[i]));
        }
        return data;
    }

    /**
     * set drawer item listener
     * @param listener
     */
    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titles = getActivity().getResources().getStringArray(R.array.array_drawer_name);
        type = getActivity().getResources().getStringArray(R.array.array_drawer_icon);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_drawer, container, false);
        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.id_drawer_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<DrawerItem> mListDrawerItems = getData();
        adapter = new DrawerAdapter(this, mListDrawerItems);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return layout;
    }

    /**
     * upadate UI of drawer after click
     */
    public void updateAdapter(){
        adapter.notifyDataSetChanged();
    }

    /**
     *  setup drawer toggle
     * @param fragmentId
     * @param drawerLayout
     * @param toolbar
     * @param isToggleAdd
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar, boolean isToggleAdd) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        if (isToggleAdd) {
            mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.string_drawer_open,
                    R.string.string_drawer_close) {
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);

                    getActivity().invalidateOptionsMenu();
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                    getActivity().invalidateOptionsMenu();
                }

                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    super.onDrawerSlide(drawerView, slideOffset);
                    toolbar.setAlpha(1 - slideOffset / 2);
                }
            };

            mDrawerLayout.addDrawerListener(mDrawerToggle);
            mDrawerLayout.post(new Runnable() {
                @Override
                public void run() {
                    mDrawerToggle.syncState();
                }
            });
            mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    /**
     * disable drawer toggle
     */
    public void setDisableDrawerToggle() {
        if(mDrawerToggle!=null) {
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_back);
            mDrawerToggle.syncState();
        }
    }

    /**
     * enable drawer toggle
     */
    public void setEnableDrawerToggle() {
        if(mDrawerToggle!=null) {
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            mDrawerToggle.setHomeAsUpIndicator(null);
            mDrawerToggle.syncState();
        }
    }

    /**
     * lock drawer
     */
    public void lockDrawer() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    /**
     * unlock drawer
     */
    public void unlockDrawer() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    /**
     * check drawer open or not
     * @return
     */
    public boolean isDrawerOpen() {
        return mDrawerLayout.isDrawerOpen(containerView);
    }

    /**
     * close the drawer
     */
    public void closeDrawer() {
        mDrawerLayout.closeDrawer(containerView);
    }

    /**
     * open drawer
     */
    public void openDrawer() {
        mDrawerLayout.openDrawer(containerView);
    }


    /**
     * drawer item listener
     */
    public interface FragmentDrawerListener {
        void onDrawerItemSelected(View view, int position);
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }

    }


}