package com.example.excelergo.niceexp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.PullToRefreshView;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utils.OnJsonStringCallBack;
import utils.OnScrollChangeCallback;

import static com.example.excelergo.niceexp.MainActivity.refreshLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment5 extends Fragment {
    private RecyclerView recyclerView;
    private MyScrollView myScrollView;
    private MyAdapter myAdapter;
    private JsonNewsSQliteAdapter newsSQliteAdapter;
    private JSONObject jsonObjectResult;
    private List<NewsBean> newsBeanList;
    public NewsFragment5() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_fragment5, container, false);
        recyclerView = view.findViewById(R.id.rv_5);
        myScrollView=view.findViewById(R.id.mySv5);
        newsSQliteAdapter=JsonNewsSQliteAdapter.getInstance(getContext());
        newsBeanList = new ArrayList<>();
        init();
        myAdapter=new MyAdapter();
        recyclerView.setAdapter(myAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        MainActivity.refreshLayout.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                init();
                MainActivity.refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.refreshLayout.setRefreshing(false);
                    }
                },1000);
            }
        });
        myScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    refreshLayout.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            init();
                            refreshLayout.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    refreshLayout.setRefreshing(false);
                                }
                            },1000);
                        }
                    });
                }
                return false;
            }
        });
        myScrollView.setMscrollChangeCallback(new OnScrollChangeCallback() {
            @Override
            public void onScroll(int dx, int dy, int dx_change, int dy_change) {
                refreshLayout.setEnabled(false);
                if(dy==0){
                    refreshLayout.setEnabled(true);
                }
            }
        });
        return view;
    }
    //请求网络获取Json数据
    private void init(){
        String url1 = "http://v.juhe.cn/toutiao/index?type=";
        String url2 = "&key=33b5bc8bfd2c5a6775a74c0f35471c33";
        String url3 = "yule";
        String f5News = url1 + url3 + url2;
        MyOkHttpClientUtil.sendRequestForResult(f5News, new OnJsonStringCallBack() {
            @Override
            public void goWithNewsString(String content) {
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    jsonObjectResult = jsonObject.getJSONObject("result");
                    JSONArray jsonArray = jsonObjectResult.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String title = jsonObject1.getString("title");
                        String picture = jsonObject1.getString("thumbnail_pic_s");
                        String url = jsonObject1.getString("url");
                        if(jsonObjectResult!=null) {
                            newsBeanList.add(new NewsBean(title, picture, url));
                            newsSQliteAdapter.insetNewsItem("news_yule",new NewsBean(title, picture, url));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view=LayoutInflater.from(getContext()).inflate(R.layout.recyclerview_list_item5,null);
            MyViewHolder myViewHolder=new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
            Glide.with(getContext()).load(newsBeanList.get(i).getImg()).into(myViewHolder.imageView);
            myViewHolder.textView.setText(newsBeanList.get(i).getTitle());
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getActivity(),NewsActivity.class);
                    intent.putExtra("url",newsBeanList.get(i).getUrl());
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.switchanimation,R.anim.switchanimation1);
                }
            });
        }

        @Override
        public int getItemCount() {
            if(jsonObjectResult==null){
                newsBeanList=newsSQliteAdapter.queryAllNews("news_yule");
            }
            return newsBeanList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private ImageView imageView;
            private TextView textView;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView=itemView.findViewById(R.id.img5);
                textView=itemView.findViewById(R.id.content5);
            }
        }
    }
}

