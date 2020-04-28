package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class JsonDataSQLiteHelper extends SQLiteOpenHelper {
    private String CREATE_TABLE1="create table news_top (id integer primary key autoincrement,title text,img text,url text)";
    private String CREATE_TABLE2="create table news_shehui (id integer primary key autoincrement,title text,img text,url text)";
    private String CREATE_TABLE3="create table news_guonei (id integer primary key autoincrement,title text,img text,url text)";
    private String CREATE_TABLE4="create table news_guoji (id integer primary key autoincrement,title text,img text,url text)";
    private String CREATE_TABLE5="create table news_yule (id integer primary key autoincrement,title text,img text,url text)";
    private String CREATE_TABLE6="create table news_tiyu (id integer primary key autoincrement,title text,img text,url text)";
    private String CREATE_TABLE7="create table news_junshi (id integer primary key autoincrement,title text,img text,url text)";
    private String CREATE_TABLE8="create table news_keji (id integer primary key autoincrement,title text,img text,url text)";
    private String CREATE_TABLE9="create table news_caijing (id integer primary key autoincrement,title text,img text,url text)";
    private String CREATE_TABLE10="create table news_shishang (id integer primary key autoincrement,title text,img text,url text)";
    public JsonDataSQLiteHelper(@Nullable Context context) {
        super(context, "news.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE1);
        db.execSQL(CREATE_TABLE2);
        db.execSQL(CREATE_TABLE3);
        db.execSQL(CREATE_TABLE4);
        db.execSQL(CREATE_TABLE5);
        db.execSQL(CREATE_TABLE6);
        db.execSQL(CREATE_TABLE7);
        db.execSQL(CREATE_TABLE8);
        db.execSQL(CREATE_TABLE9);
        db.execSQL(CREATE_TABLE10);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
