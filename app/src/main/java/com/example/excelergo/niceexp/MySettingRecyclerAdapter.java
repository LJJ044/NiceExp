package com.example.excelergo.niceexp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import utils.GoBackAction;



public class MySettingRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private View view;
    private GoBackAction goBackAction;
    final static int pageSlide=0;
    final static int noImge=1;
    final static int noCache=2;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        switch (i) {
            case 0:
                view = LayoutInflater.from(context).inflate(R.layout.recyclerview_setting_item, viewGroup, false);
                OneHolder oneHolder = new OneHolder(view);
                return oneHolder;
            case 1:
                view = LayoutInflater.from(context).inflate(R.layout.recyclerview_setting_item2, viewGroup, false);
                TwoHolder twoHolder = new TwoHolder(view);
                return twoHolder;

            case 2:
                view = LayoutInflater.from(context).inflate(R.layout.recyclerview_setting_item3, viewGroup, false);
                ThreeHolder threeHolder = new ThreeHolder(view);
                return threeHolder;

            case 3:
                view = LayoutInflater.from(context).inflate(R.layout.recyclerview_setting_item4, viewGroup, false);
                FourHolder fourHolder = new FourHolder(view);
                return fourHolder;

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder myViewHolder, int i) {
        if (myViewHolder instanceof OneHolder) {
            OneHolder oneHolder = (OneHolder) myViewHolder;
            oneHolder.tv_download.setText("下载");
        }
        if (myViewHolder instanceof TwoHolder) {
            TwoHolder twoHolder = (TwoHolder) myViewHolder;
            twoHolder.tv_slide.setText("开启手势滑动切换网页");
        }
        if (myViewHolder instanceof ThreeHolder) {
            ThreeHolder threeHolder = (ThreeHolder) myViewHolder;
            threeHolder.tv_img.setText("开启无图模式");
        }
        if (myViewHolder instanceof FourHolder) {
            FourHolder fourHolder = (FourHolder) myViewHolder;
            fourHolder.tv_track.setText("开启无痕浏览");
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else if (position == 1) {
            return 1;
        } else if (position == 2) {
            return 2;
        } else if (position == 3) {
            return 3;
        }
        return 0;
    }

    public class OneHolder extends RecyclerView.ViewHolder {
        private TextView tv_download;

        public OneHolder(@NonNull View itemView) {
            super(itemView);
            tv_download = itemView.findViewById(R.id.tv_download);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(goBackAction!=null){
                        goBackAction.goBack(0);
                    }
                }
            });


        }
    }

    public class TwoHolder extends RecyclerView.ViewHolder {
        private TextView tv_slide;
        private Switch sw_switch;

        public TwoHolder(@NonNull View itemView) {
            super(itemView);
            tv_slide = itemView.findViewById(R.id.tv_slide);
            sw_switch = itemView.findViewById(R.id.sw_slide);
            SharedPreferences sp = context.getSharedPreferences("switch", Context.MODE_PRIVATE);
            int i = sp.getInt("pageSlide", -1);
            if (i == pageSlide) {
                sw_switch.setChecked(true);
            }
            sw_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                            SharedPreferences.Editor editor = getSPDataBase();
                            editor.putInt("pageSlide", pageSlide);
                            editor.apply();

                        } else {
                                SharedPreferences.Editor editor = getSPDataBase();
                                editor.putInt("pageSlide", 10);
                                editor.apply();
                        }
                }

            });
        }
    }
    public class ThreeHolder extends RecyclerView.ViewHolder {
        private TextView tv_img;
        private Switch sw_img;

        public ThreeHolder(@NonNull View itemView) {
            super(itemView);
            tv_img = itemView.findViewById(R.id.tv_noImge);
            sw_img = itemView.findViewById(R.id.sw_noImge);
            SharedPreferences sp=context.getSharedPreferences("switch", Context.MODE_PRIVATE);
            int i=sp.getInt("noImge",-1);
            if(i==noImge){
                sw_img.setChecked(true);
            }
            sw_img.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        SharedPreferences.Editor editor = getSPDataBase();
                        editor.putInt("noImge", noImge);
                        editor.apply();

                    }else {
                        SharedPreferences.Editor editor = getSPDataBase();
                        editor.putInt("noImge", 20);
                        editor.apply();
                    }

                }
            });

        }
    }

    public class FourHolder extends RecyclerView.ViewHolder {
        private TextView tv_track;
        private Switch sw_track;

        public FourHolder(@NonNull View itemView) {
            super(itemView);
            tv_track = itemView.findViewById(R.id.tv_noTrack);
            sw_track = itemView.findViewById(R.id.sw_noTrack);
            SharedPreferences sp=context.getSharedPreferences("switch", Context.MODE_PRIVATE);
            int i=sp.getInt("noCache",-1);
            if(i==noCache){
                sw_track.setChecked(true);
            }
            sw_track.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                            SharedPreferences.Editor editor = getSPDataBase();
                            editor.putInt("noCache", noCache);
                            editor.apply();

                    }else {
                            SharedPreferences.Editor editor = getSPDataBase();
                            editor.putInt("noCache", 30);
                            editor.apply();

                    }
                }
            });
        }
    }

    public void setGoBackAction(GoBackAction goBackAction) {
        this.goBackAction = goBackAction;
    }

    private SharedPreferences.Editor getSPDataBase() {
        SharedPreferences sp = context.getSharedPreferences("switch", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        return editor;
    }
}

