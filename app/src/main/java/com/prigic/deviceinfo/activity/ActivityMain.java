package com.prigic.deviceinfo.activity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.prigic.deviceinfo.R;
import com.prigic.deviceinfo.mainfunction.LoaderData;

import java.util.ArrayList;
import java.util.List;

public class ActivityMain extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        iniComponent();
    }

    private void iniComponent() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mSectionsPagerAdapter.addFragment(new ActivityInfo(), getString(R.string.tab_title_cpu));
        mSectionsPagerAdapter.addFragment(new ActivityInfo(), getString(R.string.tab_title_device));
        mSectionsPagerAdapter.addFragment(new ActivityInfo(), getString(R.string.tab_title_system));
        mSectionsPagerAdapter.addFragment(new ActivityInfo(), getString(R.string.tab_title_battery));
        mSectionsPagerAdapter.addFragment(new ActivityInfo(), getString(R.string.tab_title_sensor));
        mSectionsPagerAdapter.addFragment(new ActivityAbout(), getString(R.string.tab_title_about));

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(6);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.app_name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    private Menu menu;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                new LoaderInfo(this).execute("");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public class LoaderInfo extends AsyncTask<String, String, String> {
        LoaderData cpu = null;
        String status = "failed";
        Context context;

        public LoaderInfo(Activity act) {
            context = act;
            cpu = new LoaderData(act);
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                cpu.loadCpuInfo();
                cpu.loadBateryInfo();
                cpu.loadDeviceInfo();
                cpu.loadSystemInfo();
                cpu.loadSupportInfo();
                status = "succced";
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            setProgressBarIndeterminateVisibility(false);
            if (!status.equals("failed")) {
                Toast.makeText(context, "Refresh complete", Toast.LENGTH_SHORT).show();
                //refresh view
                mSectionsPagerAdapter.notifyDataSetChanged();
            }
            super.onPostExecute(result);
        }

    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = mFragmentList.get(position);
            Bundle args = new Bundle();
            args.putString(ActivityInfo.ARG_SECTION_TITLE, mFragmentTitleList.get(position));
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
