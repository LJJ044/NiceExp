package com.example.excelergo.niceexp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import db.UrlSQLiteHelper;

public class URLSQLiteAdapter {
    private SQLiteDatabase db;
    private final String TABLE="UrlHistory";
    private Context context;

    public URLSQLiteAdapter(Context context) {
        this.context = context;
    }
    public void openDB(){
        UrlSQLiteHelper helper=new UrlSQLiteHelper(context);
        db=helper.getReadableDatabase();
        db=helper.getWritableDatabase();
    }
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
    private long insert(PageHistory pageHistory){
        long rowId=0;
        ContentValues values = new ContentValues();
        values.put("title", pageHistory.getTitle());
        values.put("url", pageHistory.getUrl());
        rowId=db.insert(TABLE, null, values);
        db.close();
        return rowId;
    }
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
    public void deleteAllhistory(){
        openDB();
        db.execSQL("delete from UrlHistory");

    }
    public void deleteSingleData(PageHistory pageHistory){
        openDB();
        db.delete(TABLE,"title=? and url=?",new String[]{pageHistory.getTitle(),pageHistory.getUrl()});
    }
}
