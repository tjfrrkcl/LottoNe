package com.example.fic02261.myapplication;

import android.database.sqlite.SQLiteConstraintException;

import java.util.ArrayList;

/**
 * Created by FIC02261 on 2016-09-21.
 */
public interface LottoListener {
    public void addLotto(Lotto lotto) throws SQLiteConstraintException, Exception;

    public ArrayList<Lotto> getAllLotto();

    public int getLottoCount();

    public int getLottoMaxdrwNo() ;

    public int getLottoNumber3Exists(String checkNumber);

    public int getLottoNumber4Exists(String checkNumber);
}
