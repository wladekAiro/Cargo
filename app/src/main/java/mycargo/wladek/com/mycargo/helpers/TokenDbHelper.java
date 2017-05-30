package mycargo.wladek.com.mycargo.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import mycargo.wladek.com.mycargo.pojo.Token;

/**
 * Created by wladek on 5/25/17.
 */

public class TokenDbHelper extends DbHelper {

    public TokenDbHelper(Context context) {
        super(context);
    }

    public boolean saveToken(Token token){
        System.out.println(" +++++ SAVING TOKEN ++++ ");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        boolean SAVED = false;


        try {

            ContentValues contentValues = new ContentValues();
            contentValues.put("ACCESS_TOKEN" , token.getAccessToken());
            contentValues.put("EXPIRES_IN" , token.getExpiresIn());


            cursor = db.rawQuery("SELECT * FROM " + TABLE_TOKEN , null);

            if (cursor.getCount() > 0){
                db.update(TABLE_TOKEN, contentValues, null , null);
                SAVED = true;
            }else {
                db.insert(TABLE_TOKEN , null , contentValues);
                SAVED = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
            cursor.close();
        }

        return SAVED;
    }

    public Token getToken(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        Token token = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_TOKEN , null);

            if (cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    token = new Token();
                    token.setAccessToken(cursor.getString(1));
                    token.setExpiresIn(new Long(cursor.getString(2)));
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
            cursor.close();
        }

        return token;
    }
}
