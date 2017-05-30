package mycargo.wladek.com.mycargo.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import mycargo.wladek.com.mycargo.pojo.BasicUser;

/**
 * Created by wladek on 5/25/17.
 */

public class UserDbHelper extends DbHelper {
    public UserDbHelper(Context context) {
        super(context);
    }

    public boolean saveBasicUser(BasicUser basicUser){
        System.out.println("++++ SAVING BASIC USER ++++++ ");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        boolean SAVE = false;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_BASIC_USER , null);

            ContentValues contentValues = new ContentValues();
            contentValues.put("NAME_IDENTIFIER" , basicUser.getNameidentifier());
            contentValues.put("EMAIL" , basicUser.getEmail());
            contentValues.put("KEY" , basicUser.getKey());
            contentValues.put("SECURITY_STAMP" , basicUser.getSecurityStamp());
            contentValues.put("ROLE" , basicUser.getRole());
            contentValues.put("FIRST_NAME" , basicUser.getFirstname());
            contentValues.put("OTHER_NAME" , basicUser.getOthername());
            contentValues.put("LAST_NAME" , basicUser.getLastname());
            contentValues.put("PROFILE_ID" , basicUser.getProfileId());

            if (cursor.getCount() > 0){
                db.update(TABLE_BASIC_USER, contentValues, null , null);
                SAVE = true;
            }else {
                db.insert(TABLE_BASIC_USER , null , contentValues);
                SAVE = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("++++ DONE SAVING BASIC USER ++++++ ");
        return SAVE;
    }

    public BasicUser getBasicUser(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        BasicUser basicUser = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_BASIC_USER , null);

            if (cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    basicUser = new BasicUser();
                    basicUser.setNameidentifier(cursor.getString(1));
                    basicUser.setEmail(cursor.getString(2));
                    basicUser.setKey(cursor.getString(3));
                    basicUser.setSecurityStamp(cursor.getString(4));
                    basicUser.setRole(cursor.getString(5));
                    basicUser.setFirstname(cursor.getString(6));
                    basicUser.setOthername(cursor.getString(7));
                    basicUser.setLastname(cursor.getString(8));
                    basicUser.setProfileId(cursor.getString(9));
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
            cursor.close();
        }

        return basicUser;
    }
}
