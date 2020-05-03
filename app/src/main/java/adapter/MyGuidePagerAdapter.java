package adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class MyGuidePagerAdapter extends PagerAdapter {
    private List imglist;
    public MyGuidePagerAdapter(List imgs) {
       this.imglist=imgs;
    }

    @Override
    public int getCount() {
        return imglist.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view=(View) imglist.get(position);
        ((ViewPager)container).addView(view);
        return view;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);

    }
}
