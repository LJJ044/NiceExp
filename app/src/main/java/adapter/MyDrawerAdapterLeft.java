package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.excelergo.niceexp.R;

import java.util.ArrayList;
import java.util.List;

import entity.PageHistory;

public class MyDrawerAdapterLeft extends BaseAdapter {
    private  int currentItem=-1;
    private static List<PageHistory> list;

    public MyDrawerAdapterLeft() {
        list=new ArrayList<>();
        list.add(new PageHistory(R.drawable.baidu,"百度一下"));
        list.add(new PageHistory(R.drawable.sougou,"搜狗搜索"));
        list.add(new PageHistory(R.drawable.qq,"腾讯官网"));
        list.add(new PageHistory(R.drawable.oneplus,"一加官网"));
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public void setCurrentItem(int i)
    {
        this.currentItem=i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.drawer_layout_left_list_item,null);
        TextView textView=view1.findViewById(R.id.option_item_left);
        ImageView imageView=view1.findViewById(R.id.option_item__logo_left);
        LinearLayout linearLayout=view1.findViewById(R.id.list_left_item);
        textView.setText(list.get(i).getTitle());
        imageView.setImageResource(list.get(i).getLogo());
        if(currentItem==i) {
            linearLayout.setBackgroundColor(viewGroup.getContext().getResources().getColor(R.color.tvSelected));
        }
        return view1;
    }
}
