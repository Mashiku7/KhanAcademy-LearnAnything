package com.andrewpham.android.khanacademy_learnanything;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.andrewpham.android.khanacademy_learnanything.adapter.TabsPagerAdapter;
import com.andrewpham.android.khanacademy_learnanything.ui_model.NavDrawerItem;

import java.util.ArrayList;

public class SubjectActivity extends FragmentActivity
        implements ActionBar.TabListener {

    public static final String TAG = "SubjectActivity";

    private ActionBar mActionBar;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;

    private CharSequence mTitle;

    private String[] mTopics;
    private TypedArray mIcons;

    private ArrayList<NavDrawerItem> mNavDrawerItems;
    private NavDrawerListAdapter mNavDrawerListAdapter;

    private ArrayList<String> mTranslatedTitles;
    private ArrayList<String> mNodeSlugs;

    private ViewPager mViewPager;
    private TabsPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getIntent().getExtras();
        mTranslatedTitles = bundle.getStringArrayList(HomeActivity.EXTRA_TRANSLATED_TITLES);
        Log.d(TAG, mTranslatedTitles.toString());
        mNodeSlugs = bundle.getStringArrayList(HomeActivity.EXTRA_NODE_SLUGS);
        Log.d(TAG, mNodeSlugs.toString());
        setContentView(R.layout.activity_subject);

        mTitle = mDrawerTitle = getTitle();
        mTopics = mTranslatedTitles.toArray(new String[mTranslatedTitles.size()]);
        mIcons = getResources().obtainTypedArray(R.array.home_nav_drawer_icons);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerList = (ListView) findViewById(R.id.drawerListView);
        mDrawerList.setOnItemClickListener(new SlidingMenuClickListener());

        mNavDrawerItems = new ArrayList<NavDrawerItem>();

        for (int i = 0; i < mTopics.length; i++) {
            mNavDrawerItems.add(new NavDrawerItem(mTopics[i], mIcons.getResourceId(0, -1)));
        }

        // Recycle the typed array
        mIcons.recycle();

        mNavDrawerListAdapter = new NavDrawerListAdapter(getApplicationContext(),
                mNavDrawerItems);
        mDrawerList.setAdapter(mNavDrawerListAdapter);

        // Initialization
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mActionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mActionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), mTranslatedTitles.size());

        // Initializations with ViewPager
        mViewPager.setAdapter(mAdapter);
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mActionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar_background)));
        mActionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar_tab_background)));

        // Initializations with Navigation Drawer
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(false);

        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.app_name,
                R.string.app_name) {
            public void onDrawerClosed(View view) {
                mActionBar.setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                mActionBar.setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Adding tabs
        for (String tab : mTopics) {
            mActionBar.addTab(mActionBar.newTab().setText(tab)
                    .setTabListener(this));
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    /**
     * Sliding menu item click listener
     */
    private class SlidingMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mViewPager.setCurrentItem(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}