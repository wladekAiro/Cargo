package mycargo.wladek.com.mycargo.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wladek on 5/25/17.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "mycargo.db";
    public static final String TABLE_BASIC_USER = "tbl_basic_user";
    public static final String TABLE_PROFILE = "tbl_profile";
    public static final String TABLE_TOKEN = "tbl_token";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME , null , 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_TOKEN + " (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ACCESS_TOKEN TEXT, EXPIRES_IN TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_BASIC_USER + " (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " NAME_IDENTIFIER TEXT, EMAIL TEXT , KEY TEXT, SECURITY_STAMP TEXT, ROLE TEXT, FIRST_NAME TEXT, " +
                "OTHER_NAME TEXT, LAST_NAME TEXT, PROFILE_ID TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_TOKEN);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_BASIC_USER);
        onCreate(db);
    }
}
