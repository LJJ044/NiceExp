package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.PullToRefreshView;

import activity.NewsActivity;
import adapter.JsonNewsSQliteAdapter;
import entity.NewsBean;
import interfaces.OnJsonStringCallBack;
import interfaces.OnScrollChangeCallback;
import utils.MyOkHttpClientUtil;
import view.MyScrollView;
import com.example.excelergo.niceexp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.excelergo.niceexp.MainActivity.refreshLayout;

public class NewsFragment1 extends Fragment {
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private MyScrollView myScrollView;
    private List<NewsBean> newsBeanList;
    private JSONObject jsonObjectResult;
    private JsonNewsSQliteAdapter newsSQliteAdapter;
    public NewsFragment1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_fragment1, container, false);
        recyclerView = view.findViewById(R.id.rv_1);
        myScrollView=view.findViewById(R.id.mySv1);
        newsSQliteAdapter=JsonNewsSQliteAdapter.getInstance(getContext());
        newsBeanList = new ArrayList<>();
        init();
        myAdapter=new MyAdapter();
        recyclerView.setAdapter(myAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
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
            String url3 = "top";
            String f1News = url1 + url3 + url2;
         MyOkHttpClientUtil.sendRequestForResult(f1News, new OnJsonStringCallBack() {
                @Override
                public void goWithNewsString(String content) {
                    Log.i("新闻Json",content);
                    try {
                        JSONObject jsonObject = new JSONObject(content);
                        jsonObjectResult = jsonObject.getJSONObject("result");
                        JSONArray jsonArray = jsonObjectResult.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            final String title = jsonObject1.getString("title");
                            final String picture = jsonObject1.getString("thumbnail_pic_s");
                            final String url = jsonObject1.getString("url");
                            if(jsonObjectResult!=null) {
                                newsBeanList.add(new NewsBean(title, picture, url));
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        newsSQliteAdapter.deleteAll("news_top");
                                        newsSQliteAdapter.insetNewsItem("news_top",new NewsBean(title, picture, url));
                                    }
                                }).start();
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

        class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.recyclerview_list_item, null);
                MyViewHolder myViewHolder = new MyViewHolder(view);
                return myViewHolder;
            }

            @Override
            public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
                Glide
                        .with(getContext())
                        .load(newsBeanList.get(i).getImg())
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(new SimpleTarget<GlideDrawable>() {
                            @Override
                            public void onResourceReady(GlideDrawable glideDrawable, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                myViewHolder.imageView.setImageDrawable(glideDrawable);
                            }
                        });
                myViewHolder.textView.setText(newsBeanList.get(i).getTitle());
                myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), NewsActivity.class);
                        intent.putExtra("url", newsBeanList.get(i).getUrl());
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.switchanimation, R.anim.switchanimation1);
                    }
                });
            }

            @Override
            public int getItemCount() {
                if (jsonObjectResult == null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            newsBeanList = newsSQliteAdapter.queryAllNews("news_top");
                        }
                    }).start();
                }
                return newsBeanList.size();
            }

            public class MyViewHolder extends RecyclerView.ViewHolder {
                private ImageView imageView;
                private TextView textView;

                public MyViewHolder(@NonNull View itemView) {
                    super(itemView);
                    imageView = itemView.findViewById(R.id.img);
                    textView = itemView.findViewById(R.id.content);
                }
            }
        }
}

