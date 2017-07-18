package com.example.fic02261.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by FIC02261 on 2016-09-21.
 */
public class DBHandler extends SQLiteOpenHelper implements LottoListener {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "sin.lotto";
    private static final String TABLE_NAME = "nanumlotto";
    private static final String USER_TABLE_NAME = "userlotto";
    private static final String KEY_RECU_NO = "recu_no";
    private static final String KEY_COM_YN = "com_yn";
    private static final String USER_AUTO_NUM = "auto_key_num";
    private static final String KEY_1 = "k1";
    private static final String KEY_2 = "k2";
    private static final String KEY_3 = "k3";
    private static final String KEY_4 = "k4";
    private static final String KEY_5 = "k5";
    private static final String KEY_6 = "k6";

    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                        + KEY_RECU_NO + " INTEGER PRIMARY KEY,"
                        + KEY_COM_YN + " TEXT,"
                        + KEY_1 + " INTEGER,"
                        + KEY_2 + " INTEGER,"
                        + KEY_3 + " INTEGER,"
                        + KEY_4 + " INTEGER,"
                        + KEY_5 + " INTEGER,"
                        + KEY_6 + " INTEGER)";

    String CREATE_TABLE_USER_LOTTO = "CREATE TABLE " + USER_TABLE_NAME + " ("
            + USER_AUTO_NUM + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_RECU_NO + " INTEGER, "
            + KEY_1 + " INTEGER, "
            + KEY_2 + " INTEGER, "
            + KEY_3 + " INTEGER, "
            + KEY_4 + " INTEGER, "
            + KEY_5 + " INTEGER, "
            + KEY_6 + " INTEGER)";

    String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + USER_TABLE_NAME;

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void addLotto(Lotto lotto) {
        SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_RECU_NO, lotto.getRecu_no());
            values.put(KEY_COM_YN, lotto.getCom_yn());
            values.put(KEY_1,lotto.getK1());
            values.put(KEY_2,lotto.getK2());
            values.put(KEY_3,lotto.getK3());
            values.put(KEY_4,lotto.getK4());
            values.put(KEY_5,lotto.getK5());
            values.put(KEY_6,lotto.getK6());
            db.insert(TABLE_NAME, null, values);
            db.close();
    }

    @Override
    public ArrayList<Lotto> getAllLotto() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Lotto> lottoList = null;
        try{
            lottoList = new ArrayList<Lotto>();
            String QUERY = "SELECT * FROM "+TABLE_NAME;
            Cursor cursor = db.rawQuery(QUERY, null);
            if(!cursor.isLast())
            {
                while (cursor.moveToNext())
                {
                    Lotto lotto = new Lotto();
                    lotto.setRecu_no(cursor.getInt(0));
                    lotto.setCom_yn(cursor.getString(1));
                    lotto.setK1(cursor.getInt(2));
                    lotto.setK2(cursor.getInt(3));
                    lotto.setK3(cursor.getInt(4));
                    lotto.setK4(cursor.getInt(5));
                    lotto.setK5(cursor.getInt(6));
                    lotto.setK6(cursor.getInt(7));
                    lottoList.add(lotto);
                }
            }
            db.close();
        }catch (Exception e){
            Log.e("error", e + "");
        }
        return lottoList;
    }

    @Override
    public int getLottoCount() {
        int num = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            String QUERY = "SELECT * FROM "  + TABLE_NAME;
            Cursor cursor = db.rawQuery(QUERY, null);
            num = cursor.getCount();
            db.close();
            return num;
        }catch (Exception e){
            Log.e("error",e+"");
        }
        return 0;
    }

    @Override
    public int getLottoMaxdrwNo() {
        int num = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            String QUERY = "SELECT max(recu_no) as KEY_RECU_NO FROM "  + TABLE_NAME;
            Cursor cursor = db.rawQuery(QUERY, null);
            if(!cursor.isLast()) cursor.moveToNext();
            num = cursor.getInt(0);
            db.close();
            return num;
        }catch (Exception e){
            Log.e("error",e+"");
        }
        return 0;
    }

    @Override
    public int getLottoNumber3Exists( String checkNumber ) {
        int returnValue = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        checkNumber = checkNumber.replace("[", "").replace("]", "").replaceAll(" ", "");
        String QUERY = "select count(*) as cnt from ( \n"
                     + "    select k1 || ',' || k2 || ',' || k3 as k1 from ( \n"
                     + "        select recu_no, k1, k2, k3 from nanumlotto \n"
                     + "        union select recu_no, k1, k2, k3 from nanumlotto \n"
                     + "        union select recu_no, k1, k2, k4 from nanumlotto \n"
                     + "        union select recu_no, k1, k2, k5 from nanumlotto \n"
                     + "        union select recu_no, k1, k2, k6 from nanumlotto \n"
                     + "        union select recu_no, k1, k3, k4 from nanumlotto \n"
                     + "        union select recu_no, k1, k3, k5 from nanumlotto \n"
                     + "        union select recu_no, k1, k3, k6 from nanumlotto \n"
                     + "        union select recu_no, k1, k4, k5 from nanumlotto \n"
                     + "        union select recu_no, k1, k4, k6 from nanumlotto \n"
                     + "        union select recu_no, k1, k5, k6 from nanumlotto \n"
                     + "        union select recu_no, k2, k3, k4 from nanumlotto \n"
                     + "        union select recu_no, k2, k3, k5 from nanumlotto \n"
                     + "        union select recu_no, k2, k3, k6 from nanumlotto \n"
                     + "        union select recu_no, k2, k4, k5 from nanumlotto \n"
                     + "        union select recu_no, k2, k4, k6 from nanumlotto \n"
                     + "        union select recu_no, k2, k5, k6 from nanumlotto \n"
                     + "        union select recu_no, k3, k4, k5 from nanumlotto \n"
                     + "        union select recu_no, k3, k4, k6 from nanumlotto \n"
                     + "        union select recu_no, k4, k5, k6 from nanumlotto \n"
                     + "    ) nl \n"
                     + " ) \n"
                     + " where k1 = '" + checkNumber + "' \n"
                     ;

        Log.d("checkNumber = ", checkNumber);
        Log.d("Query = ", QUERY);
        Cursor cursor = db.rawQuery(QUERY, null);
        if(cursor.moveToFirst()) {
            Log.d("cursor = ", cursor.getInt(0) + "");
            returnValue = cursor.getInt(cursor.getColumnIndex("cnt"));
        }
        return returnValue;
    }

    @Override
    public int getLottoNumber4Exists( String checkNumber ) {
        int returnValue = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        checkNumber = checkNumber.replace("[", "").replace("]", "").replaceAll(" ", "");
        String QUERY = "select count(*) as cnt from ( \n"
                + "    select k1 || ',' || k2 || ',' || k3 || ',' || k4 as k1 from ( \n"
                + "        select recu_no, k1, k2, k3, k4 from nanumlotto \n"
                + "        union select recu_no, k1, k2, k3, k5 from nanumlotto \n"
                + "        union select recu_no, k1, k2, k3, k6 from nanumlotto \n"
                + "        union select recu_no, k1, k2, k4, k5 from nanumlotto \n"
                + "        union select recu_no, k1, k2, k4, k6 from nanumlotto \n"
                + "        union select recu_no, k1, k2, k5, k6 from nanumlotto \n"
                + "        union select recu_no, k1, k3, k4, k5 from nanumlotto \n"
                + "        union select recu_no, k1, k3, k4, k6 from nanumlotto \n"
                + "        union select recu_no, k1, k3, k5, k6 from nanumlotto \n"
                + "        union select recu_no, k1, k4, k5, k6 from nanumlotto \n"
                + "        union select recu_no, k2, k3, k4, k5 from nanumlotto \n"
                + "        union select recu_no, k2, k3, k4, k6 from nanumlotto \n"
                + "        union select recu_no, k2, k3, k5, k6 from nanumlotto \n"
                + "        union select recu_no, k2, k4, k5, k6 from nanumlotto \n"
                + "        union select recu_no, k3, k4, k5, k6 from nanumlotto \n"
                + "    ) nl \n"
                + " ) \n"
                + " where k1 = '" + checkNumber + "' \n"
                ;

        Log.d("checkNumber = ", checkNumber);
        Log.d("Query = ", QUERY);
        Cursor cursor = db.rawQuery(QUERY, null);
        if(cursor.moveToFirst()) {
            Log.d("cursor = ", cursor.getInt(0) + "");
            returnValue = cursor.getInt(cursor.getColumnIndex("cnt"));
        }
        return returnValue;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
}
