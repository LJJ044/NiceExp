package com.example.excelergo.niceexp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import utils.GoBackAction;
public class SkinChangeAdapter extends RecyclerView.Adapter<SkinChangeAdapter.MyViewHolder>{
public static List<PageHistory> skins;
public static List<PageHistory> skins2;
private GoBackAction goBackAction;
    public SkinChangeAdapter() {
        skins=new ArrayList<>();
        skins.add(new PageHistory(R.drawable.yellow,"默认"));
        skins.add(new PageHistory(R.drawable.star,"星空"));
        skins.add(new PageHistory(R.drawable.xuanlan,"绚烂"));
        skins.add(new PageHistory(R.drawable.miwu,"五彩迷雾"));
        skins.add(new PageHistory(R.drawable.wucai,"五彩"));
        skins.add(new PageHistory(R.drawable.green,"绿色"));
        skins.add(new PageHistory(R.drawable.luzhu,"露珠花朵"));
        skins.add(new PageHistory(R.drawable.xinxin,"心心相印"));
        skins.add(new PageHistory(R.drawable.tuanyuan,"团团圆圆"));
        skins.add(new PageHistory(R.drawable.shenshou,"神兽祝福"));
        skins.add(new PageHistory(R.drawable.love,"淡淡的爱"));
        skins.add(new PageHistory(R.drawable.iloveyou,"i-love-you"));
        skins.add(new PageHistory(R.drawable.waiting,"雪中的等特"));
        skins2=new ArrayList<>();
        skins2.add(new PageHistory(R.drawable.yellow1,"默认"));
        skins2.add(new PageHistory(R.drawable.star1,"星空"));
        skins2.add(new PageHistory(R.drawable.xuanlan1,"绚烂"));
        skins2.add(new PageHistory(R.drawable.miwu1,"五彩迷雾"));
        skins2.add(new PageHistory(R.drawable.wucai1,"五彩"));
        skins2.add(new PageHistory(R.drawable.green1,"绿色"));
        skins2.add(new PageHistory(R.drawable.luzhu1,"露珠花朵"));
        skins2.add(new PageHistory(R.drawable.xinxin1,"心心相印"));
        skins2.add(new PageHistory(R.drawable.tuanyuan1,"团团圆圆"));
        skins2.add(new PageHistory(R.drawable.shenshou1,"神兽祝福"));
        skins2.add(new PageHistory(R.drawable.love1,"淡淡的爱"));
        skins2.add(new PageHistory(R.drawable.iloveyou1,"i-love-you"));
        skins2.add(new PageHistory(R.drawable.waiting1,"雪中的等特"));
    }

    @NonNull
    @Override
    public SkinChangeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.skins_item,null);
        SkinChangeAdapter.MyViewHolder viewHolder=new SkinChangeAdapter.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SkinChangeAdapter.MyViewHolder myViewHolder, final int i) {
                myViewHolder.textView.setText(skins.get(i).getTitle());
                myViewHolder.imageView.setImageResource(skins.get(i).getLogo());
                myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity.title_bar.setBackgroundResource(skins2.get(i).getLogo());
                    if(goBackAction!=null){
                        goBackAction.goBack(i);
                    }

                    }
                });
    }

    @Override
    public int getItemCount() {
        return skins.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.skin_title);
            imageView=itemView.findViewById(R.id.skin_img);
        }
    }

    public void setGoBackAction(GoBackAction goBackAction) {
        this.goBackAction = goBackAction;
    }
}
