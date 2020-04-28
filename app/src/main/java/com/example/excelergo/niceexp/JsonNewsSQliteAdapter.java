package com.example.excelergo.niceexp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import db.JsonDataSQLiteHelper;

public class JsonNewsSQliteAdapter {
    private static JsonNewsSQliteAdapter adapter;
    private SQLiteDatabase db;
    private JsonNewsSQliteAdapter(Context context) {
        JsonDataSQLiteHelper helper=new JsonDataSQLiteHelper(context);
        db=helper.getReadableDatabase();
        db=helper.getWritableDatabase();
    }
    public static JsonNewsSQliteAdapter getInstance(Context context){
        if(adapter==null){
        synchronized (JsonNewsSQliteAdapter.class){
            if(adapter==null){
                adapter=new JsonNewsSQliteAdapter(context);
            }
        }
        }
        return adapter;
    }
    public void insetNewsItem(String table,NewsBean newsBean){
        Cursor cursor=db.query(table,null,"url=?",new String[]{newsBean.getUrl()},null,null,null);
           if(cursor.getCount()==0) {
               ContentValues values = new ContentValues();
               values.put("title", newsBean.getTitle());
               values.put("img", newsBean.getImg());
               values.put("url", newsBean.getUrl());
               db.insert(table, null, values);
           }
        cursor.close();
    }
    public List<NewsBean> queryAllNews(String table){
        List<NewsBean> newsBeanList=new ArrayList<>();
        Cursor cursor=db.query(table,null,null,null,null,null,null);
        if(cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    String title=cursor.getString(cursor.getColumnIndex("title"));
                    String img=cursor.getString(cursor.getColumnIndex("img"));
                    String url=cursor.getString(cursor.getColumnIndex("url"));
                    newsBeanList.add(new NewsBean(title,img,url));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return newsBeanList;
    }

}
