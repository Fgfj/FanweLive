package com.fanwe.auction.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.fanwe.hybrid.fragment.BaseFragment;
import com.fanwe.live.R;

import java.util.ArrayList;

public class ActivityTabViewPage extends BaseFragment {



    private TabLayout tl_coupon;
    private ViewPager vp_coupon;


    @Override
    protected int onCreateContentView() {
        return R.layout.activity_tab_view_page;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {

        tl_coupon= (TabLayout) findViewById(R.id.tl_coupon);
        vp_coupon= (ViewPager) findViewById(R.id.vp_coupon);

        ArrayList<String> titleDatas=new ArrayList<>();
        titleDatas.add("钻石充值");
        titleDatas.add("VIP充值");
        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
//        fragmentList.add(new HJXCRechargeView(this));
//        fragmentList.add(new LiveRechargeVipActivity(this));
//
//        tl_coupon.setTabMode(TabLayout.MODE_FIXED);
//        tl_coupon.addTab(tl_coupon.newTab().setText(titleDatas.get(0)));
//        tl_coupon.addTab(tl_coupon.newTab().setText(titleDatas.get(1)));
//
//        final MyViewPageAdapter adapter=new MyViewPageAdapter(getSupportFragmentManager(),titleDatas,fragmentList);
//        tl_coupon.setTabsFromPagerAdapter(adapter);
//        vp_coupon.setAdapter(adapter);
//        tl_coupon.setupWithViewPager(vp_coupon, true);


        tl_coupon.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp_coupon.setCurrentItem(tl_coupon.getSelectedTabPosition());
//                adapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }



    class MyViewPageAdapter extends FragmentPagerAdapter {
        private ArrayList<String> titleList;
        private ArrayList<Fragment> fragmentList;
        public MyViewPageAdapter(FragmentManager fm, ArrayList<String> titleList, ArrayList<Fragment> fragmentList) {
            super(fm);
            this.titleList = titleList;
            this.fragmentList = fragmentList;

            notifyDataSetChanged();
        }
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }
        @Override
        public int getCount() {
            return fragmentList.size();
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }
}
