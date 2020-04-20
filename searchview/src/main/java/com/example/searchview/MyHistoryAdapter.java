package com.example.searchview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MyHistoryAdapter extends RecyclerView.Adapter<MyHistoryAdapter.MyViewHolder>{
private ICallBack iCallBack;
    @NonNull
    @Override
    public MyHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_list_history_item2,null);
        final MyHistoryAdapter.MyViewHolder myViewHolder=new MyHistoryAdapter.MyViewHolder(view);
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(iCallBack!=null){
                   iCallBack.SearchAciton(myViewHolder.textView2.getText().toString());
               }

            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHistoryAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.textView1.setText(SearchView.histories.get(i).getTitle());
        myViewHolder.textView2.setText(SearchView.histories.get(i).getUrl());
        Bitmap bitmap=getLocalBitmap("/sdcard/information/"+SearchView.histories.get(i).getTitle()+".jpg");
        if(bitmap!=null) {
            myViewHolder.imageView.setImageBitmap(ClipSquareBitmap(bitmap,200,bitmap.getWidth()));
        }else {
            myViewHolder.imageView.setImageResource(R.drawable.unknown2);
        }
    }

    @Override
    public int getItemCount() {
        return SearchView.histories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView1,textView2;
        private ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1=itemView.findViewById(R.id.url_title);
            textView2=itemView.findViewById(R.id.url_detail);
            imageView=itemView.findViewById(R.id.url_logo);
        }
    }
    public static Bitmap ClipSquareBitmap(Bitmap bmp, int width, int radius) {
        if (bmp == null || width <= 0)
            return null;
        //如果图片比较小就没必要进行缩放了


        if (bmp.getWidth() > width && bmp.getHeight() > width) {
            if (bmp.getWidth() > bmp.getHeight()) {
                bmp = Bitmap.createScaledBitmap(bmp, (int) (((float) width) * bmp.getWidth() / bmp.getHeight()), width, false);
            } else {
                bmp = Bitmap.createScaledBitmap(bmp, width, (int) (((float) width) * bmp.getHeight() / bmp.getWidth()), false);
            }

        } else {
            width = bmp.getWidth() > bmp.getHeight() ? bmp.getHeight() : bmp.getWidth();
            Log.d("zeyu","宽" + width + ",w" + bmp.getWidth() + ",h" + bmp.getHeight());
            if (radius > width) {
                radius = width;
            }
        }
        Bitmap output = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        //设置画笔全透明
        canvas.drawARGB(0, 0, 0, 0);
        Paint paints = new Paint();
        paints.setColor(Color.WHITE);
        paints.setAntiAlias(true);//去锯齿
        paints.setFilterBitmap(true);
        //防抖动
        paints.setDither(true);

        //把图片圆形绘制矩形
        if (radius <= 0)
            canvas.drawRect(new Rect(0, 0, width, width), paints);
        else //绘制圆角
            canvas.drawRoundRect(new RectF(0, 0, width, width), radius, radius, paints);
        // 取两层绘制交集。显示前景色。
        paints.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        Rect rect = new Rect();
        if (bmp.getWidth() >= bmp.getHeight()) {
            rect.set((bmp.getWidth() - width) / 2, 0, (bmp.getWidth() + width) / 2, width);
        } else {
            rect.set(0, (bmp.getHeight() - width) / 2, width, (bmp.getHeight() + width) / 2);
        }
        Rect rect2 = new Rect(0, 0, width, width);
        //第一个rect 针对bmp的绘制区域，rect2表示绘制到上面位置
        canvas.drawBitmap(bmp, rect, rect2, paints);
        bmp.recycle();
        return output;
    }
    public static Bitmap getLocalBitmap(String sdPath){
        try {
            FileInputStream fis=new FileInputStream(sdPath);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setiCallBack(ICallBack iCallBack) {
        this.iCallBack = iCallBack;
    }
}
