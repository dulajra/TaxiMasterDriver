package com.innocept.taximasterdriver.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.innocept.taximasterdriver.R;
import com.innocept.taximasterdriver.presenter.OrderListPresenter;
import com.innocept.taximasterdriver.ui.fragment.FinishedOrderFragment;
import com.innocept.taximasterdriver.ui.fragment.OnGoingOrderFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dulaj on 17-Apr-16.
 */

/**
 * New orders are placed by this activity.
 */
public class OrderListActivity extends AppCompatActivity {

    private final String DEBUG_TAG = OrderListActivity.class.getSimpleName();

    public OrderListPresenter orderListPresenter;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public OnGoingOrderFragment onGoingOrderFragment;
    public FinishedOrderFragment finishedOrderFragment;

    boolean isTouchable = true;
    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        if (orderListPresenter == null) {
            orderListPresenter = OrderListPresenter.getInstance();
        }
        orderListPresenter.setView(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                submit();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    public void submit() {
        orderListPresenter.getOrderList(tabLayout.getSelectedTabPosition());
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        onGoingOrderFragment = new OnGoingOrderFragment();
        finishedOrderFragment = new FinishedOrderFragment();
        adapter.addFragment(onGoingOrderFragment, "ON GOING");
        adapter.addFragment(finishedOrderFragment, "FINISHED");
        viewPager.setAdapter(adapter);
    }

    public void openDriverStateActivity(View view) {
        startActivity(new Intent(OrderListActivity.this, DriverStateActivity.class));
        this.finish();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
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
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void lockUI(){
        this.isTouchable = false;
    }

    public void releaseUI(){
        this.isTouchable = true;
    }

    @Override

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(isTouchable){
            return super.dispatchTouchEvent(ev);
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT)
                .show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
