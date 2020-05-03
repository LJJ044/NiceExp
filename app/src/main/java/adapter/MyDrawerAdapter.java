package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.excelergo.niceexp.R;

import java.util.ArrayList;
import java.util.List;

public class MyDrawerAdapter extends BaseAdapter {
    private  int currentItem=-1;
    private static List<String> list;

    public MyDrawerAdapter() {
        list=new ArrayList<>();
        list.add("梧桐中文网");
        list.add("蔷薇书院");
        list.add("凤鸣轩小说");
        list.add("飞卢小说网");
        list.add("话本小说网");
        list.add("一千零一页");
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
        View view1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.drawer_layout_list_item, null);
        TextView textView = view1.findViewById(R.id.option_item);
        textView.setText(list.get(i));
        if (currentItem == i) {
            textView.setBackgroundColor(viewGroup.getContext().getResources().getColor(R.color.tvSelected));
        }
            return view1;
        }

}
