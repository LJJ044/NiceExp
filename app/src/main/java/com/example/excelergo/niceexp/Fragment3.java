package com.example.excelergo.niceexp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment3 extends Fragment {
private List<Fragment> list;
private ViewPager viewPager;
private TabLayout tabLayout;
private MyFragmentAdapter adapter;
private NewsBean newsBean;
private String[] titles={"头条","社会","国内","国际","娱乐","体育","军事","科技","财经","时尚"};
    public Fragment3() {

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment3,container,false);
        viewPager=view.findViewById(R.id.vp);
        tabLayout=view.findViewById(R.id.tl);
        addFragments();
        adapter=new MyFragmentAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(10);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab ta) {
            for(int i=0;i<tabLayout.getTabCount();i++){
                if(ta==tabLayout.getTabAt(i)){
                viewPager.setCurrentItem(i);

                }
            }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                tabLayout.getTabAt(i).select();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        return view;
    }
    private void addFragments(){
        list=new ArrayList<>();
        list.add(new NewsFragment1());
        list.add(new NewsFragment2());
        list.add(new NewsFragment3());
        list.add(new NewsFragment4());
        list.add(new NewsFragment5());
        list.add(new NewsFragment6());
        list.add(new NewsFragment7());
        list.add(new NewsFragment8());
        list.add(new NewsFragment9());
        list.add(new NewsFragment10());
    }
    class MyFragmentAdapter extends FragmentPagerAdapter{

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return list.get(i);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
