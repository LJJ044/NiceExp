package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import fragment.Fragment1;
import fragment.Fragment2;
import fragment.Fragment3;
import fragment.Fragment4;

public class MyFragmentViewPagerAdapter extends FragmentPagerAdapter {
    private final int PAGE_COUNT = 4;
    private Fragment1 fragment1 ;
    private Fragment2 fragment2 ;
    private Fragment3 fragment3 ;
    private Fragment4 fragment4 ;

    public MyFragmentViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();
        fragment4 = new Fragment4();
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment=null;//局部变量一定要初始化
        switch (i) {
            case 0:
                fragment=fragment1;
                break;
            case 1:
                fragment=fragment2;
                break;
            case 2:
                fragment=fragment3;
                break;
            case 3:
                fragment=fragment4;
                break;

        }
                return fragment;

        }


        @Override
        public int getCount () {
            return PAGE_COUNT;
        }

    }

