package com.example.excelergo.niceexp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import db.UrlFavSQLiteHelper;

public class URLFAVSQLiteAdapter {
    private SQLiteDatabase db;
    private final String TABLE="UrlFav";
    private Context context;

    public URLFAVSQLiteAdapter(Context context) {
        this.context = context;
    }
    public void openDB(){
        UrlFavSQLiteHelper helper=new UrlFavSQLiteHelper(context);
        db=helper.getReadableDatabase();
        db=helper.getWritableDatabase();
    }
    //插入历史记录
    public void insertHistory(PageHistory pageHistory) {
        openDB();
        Cursor cursor = db.query(TABLE, null, "title=? and url=?", new String[]{pageHistory.getTitle(), pageHistory.getUrl()}, null, null, null);
        if (cursor.getCount() > 0) {
                db.delete(TABLE, "title=? and url=?", new String[]{pageHistory.getTitle(), pageHistory.getUrl()});
                insert(pageHistory);
        } else if (cursor.getCount() == 0) {
            insert(pageHistory);
        }
        cursor.close();

    }
    private void insert(PageHistory pageHistory){
        ContentValues values = new ContentValues();
        values.put("title", pageHistory.getTitle());
        values.put("url", pageHistory.getUrl());
        db.insert(TABLE, null, values);
        db.close();
    }
    //查询历史记录
    public List<PageHistory> querAllHistory(){
        openDB();
        List<PageHistory> list=new ArrayList<>();
        Cursor cursor=db.query(TABLE,null,null,null,null,null,"id desc");
        if(cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String url = cursor.getString(cursor.getColumnIndex("url"));
                    PageHistory pageHistory = new PageHistory();
                    pageHistory.setTitle(title);
                    pageHistory.setUrl(url);
                    list.add(pageHistory);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return list;
    }
    //查询所有历史记录
    public void deleteAllhistory(){
        openDB();
        db.execSQL("delete from  UrlFav");

    }
    //删除单条数据
    public void deleteSingleData(PageHistory pageHistory){
        openDB();
        db.delete(TABLE,"title=? and url=?",new String[]{pageHistory.getTitle(),pageHistory.getUrl()});
    }
}
