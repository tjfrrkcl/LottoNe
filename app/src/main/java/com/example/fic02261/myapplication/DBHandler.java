package com.example.fic02261.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
    private static final String KEY_RECU_NO = "recu_no";
    private static final String KEY_COM_YN = "com_yn";
    private static final String KEY_F1 = "f1";
    private static final String KEY_S2 = "s2";
    private static final String KEY_T3 = "t3";
    private static final String KEY_F4 = "f4";
    private static final String KEY_F5 = "f5";
    private static final String KEY_S6 = "s6";

    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                        + KEY_RECU_NO + " INTEGER PRIMARY KEY,"
                        + KEY_COM_YN + " TEXT,"
                        + KEY_F1 + " INTEGER,"
                        + KEY_S2 + " INTEGER,"
                        + KEY_T3 + " INTEGER,"
                        + KEY_F4 + " INTEGER,"
                        + KEY_F5 + " INTEGER,"
                        + KEY_S6 + " INTEGER)";
    String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void addLotto(Lotto lotto) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put(KEY_RECU_NO, lotto.getRecu_no());
            values.put(KEY_COM_YN, lotto.getCom_yn());
            values.put(KEY_F1,lotto.getF1());
            values.put(KEY_S2,lotto.getS2());
            values.put(KEY_T3,lotto.getT3());
            values.put(KEY_F4,lotto.getF4());
            values.put(KEY_F5,lotto.getF5());
            values.put(KEY_S6,lotto.getS6());
            db.insert(TABLE_NAME, null, values);
            db.close();
        }catch (Exception e){
            Log.e("problem", e + "");
        }
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
                    lotto.setF1(cursor.getInt(2));
                    lotto.setS2(cursor.getInt(3));
                    lotto.setT3(cursor.getInt(4));
                    lotto.setF4(cursor.getInt(5));
                    lotto.setF5(cursor.getInt(6));
                    lotto.setS6(cursor.getInt(7));
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
    public int getLottoNumber3Exists( String checkNumber ) {
        int returnValue = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        checkNumber = checkNumber.replace("[", "").replace("]", "").replaceAll(" ", "");
        String QUERY = "select count(*) as cnt from ( \n"
                     + "    select f1 || ',' || s2 || ',' || t3 as f1 from ( \n"
                     + "        select recu_no, f1, s2, t3 from nanumlotto \n"
                     + "        union select recu_no, f1, s2, t3 from nanumlotto \n"
                     + "        union select recu_no, f1, s2, f4 from nanumlotto \n"
                     + "        union select recu_no, f1, s2, f5 from nanumlotto \n"
                     + "        union select recu_no, f1, s2, s6 from nanumlotto \n"
                     + "        union select recu_no, f1, t3, f4 from nanumlotto \n"
                     + "        union select recu_no, f1, t3, f5 from nanumlotto \n"
                     + "        union select recu_no, f1, t3, s6 from nanumlotto \n"
                     + "        union select recu_no, f1, f4, f5 from nanumlotto \n"
                     + "        union select recu_no, f1, f4, s6 from nanumlotto \n"
                     + "        union select recu_no, f1, f5, s6 from nanumlotto \n"
                     + "        union select recu_no, s2, t3, f4 from nanumlotto \n"
                     + "        union select recu_no, s2, t3, f5 from nanumlotto \n"
                     + "        union select recu_no, s2, t3, s6 from nanumlotto \n"
                     + "        union select recu_no, s2, f4, f5 from nanumlotto \n"
                     + "        union select recu_no, s2, f4, s6 from nanumlotto \n"
                     + "        union select recu_no, s2, f5, s6 from nanumlotto \n"
                     + "        union select recu_no, t3, f4, f5 from nanumlotto \n"
                     + "        union select recu_no, t3, f4, s6 from nanumlotto \n"
                     + "        union select recu_no, f4, f5, s6 from nanumlotto \n"
                     + "    ) nl \n"
                     + " ) \n"
                     + " where f1 = '" + checkNumber + "' \n"
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
                + "    select f1 || ',' || s2 || ',' || t3 || ',' || f4 as f1 from ( \n"
                + "        select recu_no, f1, s2, t3, f4 from nanumlotto \n"
                + "        union select recu_no, f1, s2, t3, f5 from nanumlotto \n"
                + "        union select recu_no, f1, s2, t3, s6 from nanumlotto \n"
                + "        union select recu_no, f1, s2, f4, f5 from nanumlotto \n"
                + "        union select recu_no, f1, s2, f4, s6 from nanumlotto \n"
                + "        union select recu_no, f1, s2, f5, s6 from nanumlotto \n"
                + "        union select recu_no, f1, t3, f4, f5 from nanumlotto \n"
                + "        union select recu_no, f1, t3, f4, s6 from nanumlotto \n"
                + "        union select recu_no, f1, t3, f5, s6 from nanumlotto \n"
                + "        union select recu_no, f1, f4, f5, s6 from nanumlotto \n"
                + "        union select recu_no, s2, t3, f4, f5 from nanumlotto \n"
                + "        union select recu_no, s2, t3, f4, s6 from nanumlotto \n"
                + "        union select recu_no, s2, t3, f5, s6 from nanumlotto \n"
                + "        union select recu_no, s2, f4, f5, s6 from nanumlotto \n"
                + "        union select recu_no, t3, f4, f5, s6 from nanumlotto \n"
                + "    ) nl \n"
                + " ) \n"
                + " where f1 = '" + checkNumber + "' \n"
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
