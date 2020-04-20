package db;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UrlFavSQLiteHelper extends SQLiteOpenHelper {
    private String CREATE_TABLE="create table UrlFav(id integer primary key autoincrement,title text,url text)";
    public UrlFavSQLiteHelper(Context context) {
        super(context, "urlfav.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
