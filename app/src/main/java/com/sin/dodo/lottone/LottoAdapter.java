package com.sin.dodo.lottone;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by FIC02261 on 2016-09-21.
 */
public class LottoAdapter extends BaseAdapter {
    Context context;
    ArrayList<Lotto> listData;

    public LottoAdapter(Context context, ArrayList<Lotto> listData) {
        this.context = context;
        this.listData = listData;
    }
    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
