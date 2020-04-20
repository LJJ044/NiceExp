package com.example.excelergo.niceexp;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import utils.GoBackAction;
import static com.example.excelergo.niceexp.Fragment2.webView;
import static com.example.excelergo.niceexp.HistoryFragment.histories;

public class MyHistoryAdapter extends RecyclerView.Adapter<MyHistoryAdapter.MyViewHolder>{
    private GoBackAction goBackAction;
    private Context context;
    private URLSQLiteAdapter adapter;
    private PageHistory pageHistory;

    public MyHistoryAdapter(Context context) {
        this.context = context;
        adapter=new URLSQLiteAdapter(context);
        pageHistory=new PageHistory();
    }
    @NonNull
    @Override
    public MyHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_list_history_item,null);
        final MyHistoryAdapter.MyViewHolder myViewHolder=new MyHistoryAdapter.MyViewHolder(view);
        myViewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
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
                webView.loadUrl(myViewHolder.textView2.getText().toString());
                if(goBackAction!=null){
                    goBackAction.goBack(1);
                }
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHistoryAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.textView1.setText(histories.get(i).getTitle());
        myViewHolder.textView2.setText(histories.get(i).getUrl());
        Bitmap bitmap=ImgeClipUtil.getLocalBitmap(BitmapFileutil.ImgPath+histories.get(i).getTitle()+".jpg");
        if(bitmap!=null) {
            myViewHolder.img_logo.setImageBitmap(ImgeClipUtil.ClipSquareBitmap(bitmap,200,bitmap.getWidth()));
        }else {
            myViewHolder.img_logo.setImageResource(R.drawable.unknown2);
        }
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView1,textView2;
        private ImageView img_logo;
        private ImageView img_delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1=itemView.findViewById(R.id.url_title);
            textView2=itemView.findViewById(R.id.url_detail);
            img_logo=itemView.findViewById(R.id.url_logo);
            img_delete=itemView.findViewById(R.id.delete_one);
        }
    }
    public void setGoBackAction(GoBackAction goBackAction) {
        this.goBackAction = goBackAction;
    }
}
