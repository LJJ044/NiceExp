package com.example.excelergo.niceexp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;
import db.UserInfoOpenHelper;

public class UserInfo {
    private Context context;
    private SQLiteDatabase db;
    private final String TABLE_USER="User";
    public UserInfo(Context context){
        this.context=context;
        UserInfoOpenHelper helper=new UserInfoOpenHelper(context);
        db=helper.getReadableDatabase();
        db=helper.getWritableDatabase();

    }

    public Long insertUser(RegisterInfo registerInfo){
        long rowId=-1;
        String name=registerInfo.getName();
        String pwd=registerInfo.getPwd();
        Cursor cursor=db.query(TABLE_USER,null,"username=? and pwd=?",new String[]{name,pwd},null,null,null);
        if(cursor.getCount()>0){
            rowId=0;
           Toast.makeText(context,"该用户已存在",Toast.LENGTH_SHORT).show();
        }else if(cursor.getCount()==0) {
            ContentValues values = new ContentValues();
            values.put("username", name);
            values.put("pwd", pwd);
            rowId=db.insert(TABLE_USER, null, values);
            Log.i("用户数据", "插入了");
        }
        cursor.close();
        db.close();
        return rowId;
    }
   public Map<String,String> queryUser(RegisterInfo registerInfo){
       Map<String,String> map=new HashMap<>();
       Cursor cursor=db.query(TABLE_USER,null,"username=? and pwd=?",new String[]{registerInfo.getName(),registerInfo.getPwd()},null,null,null);
       if(cursor.getCount()>0) {
           if (cursor.moveToFirst()) {
               do {
                   String name = cursor.getString(cursor.getColumnIndex("username"));
                   String pwd = cursor.getString(cursor.getColumnIndex("pwd"));
                   map.put("name", name);
                   map.put("pwd", pwd);
               } while (cursor.moveToNext());
           }
       }
       cursor.close();
       return map;
   }

   public void updateUser(RegisterInfo registerInfo){
        ContentValues values=new ContentValues();
        values.put("pwd",registerInfo.getPwd());
        Cursor cursor=db.query(TABLE_USER,null,"username=?",new String[]{registerInfo.getName()},null,null,null);
        if(cursor.getCount()==0){
            Toast.makeText(context,"该用户不存在",Toast.LENGTH_SHORT).show();
        }else {
            db.update(TABLE_USER, values, "username=?", new String[]{registerInfo.getName()});
            Toast.makeText(context,"密码已修改",Toast.LENGTH_SHORT).show();
        }
   }
   public Map<String,String> queryByUserName(String account){
        Map<String,String> map=new HashMap<>();
        Cursor cursor=db.query(TABLE_USER,null,"username=? ",new String[]{account},null,null,null,null);
        if(cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do{
                    String name = cursor.getString(cursor.getColumnIndex("username"));
                    String pwd = cursor.getString(cursor.getColumnIndex("pwd"));
                    map.put("name1", name);
                    map.put("pwd1", pwd);
                } while (cursor.moveToNext());
            }
        }
        return map;

    }
    public Map<String,String> queryByPwd(String psw){
        Map<String,String> map=new HashMap<>();
        Cursor cursor=db.query(TABLE_USER,null,"pwd=? ",new String[]{psw},null,null,null,null);
        if(cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do{
                    String name = cursor.getString(cursor.getColumnIndex("username"));
                    String pwd = cursor.getString(cursor.getColumnIndex("pwd"));
                    map.put("name2", name);
                    map.put("pwd2", pwd);
                } while (cursor.moveToNext());
            }
        }
        return map;

    }

}
