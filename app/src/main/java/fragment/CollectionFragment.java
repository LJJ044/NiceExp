package fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.excelergo.niceexp.R;

import java.util.List;

import adapter.URLFAVSQLiteAdapter;
import entity.PageHistory;
import utils.ImgeClipUtil;

/**
 * 收藏页面
 */
public class CollectionFragment extends Fragment {
    public static RecyclerView recyclerView;
    private MyFavAdapter myFavAdapter;
    private List<PageHistory> histories;
    private URLFAVSQLiteAdapter adapter;
    PageHistory pageHistory=new PageHistory();

    public CollectionFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_collection,container,false);
        recyclerView=view.findViewById(R.id.rv_fav);
        adapter=new URLFAVSQLiteAdapter(getContext());
        histories=adapter.querAllHistory();
        myFavAdapter=new MyFavAdapter();
        recyclerView.setAdapter(myFavAdapter);
        LinearLayoutManager manager=new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        return view;
    }
    public class MyFavAdapter extends RecyclerView.Adapter<MyFavAdapter.MyViewHolder>{
        @NonNull
        @Override
        public MyFavAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
            View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_list_fav_item,null);
            final MyFavAdapter.MyViewHolder myViewHolder=new MyFavAdapter.MyViewHolder(view);
            myViewHolder.imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pageHistory.setTitle(myViewHolder.textView1.getText().toString());
                    pageHistory.setUrl(myViewHolder.textView2.getText().toString());
                    adapter.deleteSingleData(pageHistory);
                    histories=adapter.querAllHistory();
                    notifyDataSetChanged();

                }
            });
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment2.webView.loadUrl(myViewHolder.textView2.getText().toString());
                    getActivity().finish();
                }
            });
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyFavAdapter.MyViewHolder myViewHolder, int i) {
            myViewHolder.textView1.setText(histories.get(i).getTitle());
            myViewHolder.textView2.setText(histories.get(i).getUrl());
            Bitmap bitmap= ImgeClipUtil.getLocalBitmap("/sdcard/information/"+histories.get(i).getTitle()+".jpg");//获取本地图标bitmap
            if(bitmap!=null) {
                myViewHolder.imageView.setImageBitmap(ImgeClipUtil.ClipSquareBitmap(bitmap,200,bitmap.getWidth()));//设置Bitmap到ImageView
            }else {
                myViewHolder.imageView.setImageResource(R.drawable.unknown2);
            }
        }

        @Override
        public int getItemCount() {
            return histories.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView textView1,textView2;
            private ImageView imageView,imageView2;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                textView1=itemView.findViewById(R.id.url_title);
                textView2=itemView.findViewById(R.id.url_detail);
                imageView=itemView.findViewById(R.id.url_logo);
                imageView2=itemView.findViewById(R.id.delete_one);
            }
        }

    }
}
