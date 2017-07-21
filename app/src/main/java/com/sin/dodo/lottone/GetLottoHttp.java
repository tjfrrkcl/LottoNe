package com.sin.dodo.lottone;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by FIC02261 on 2016-09-21.
 */
public class GetLottoHttp {
    Context context = null;

    public GetLottoHttp( Context context ) {
        this.context = context;
    }
    public boolean getConnectivityService() {
        Log.d("getLottoHttp", "getConnectivityService Start..............");
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI && activeNetwork.isConnectedOrConnecting()) {
                Log.d("getLottoHttp", "getConnectivityService WIFI OK..............");
                // wifi 연결중
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE && activeNetwork.isConnectedOrConnecting()) {
                // 모바일 네트워크 연결중
                Log.d("getLottoHttp", "getConnectivityService mobile OK..............");
                return true;
            } else {
                // 네트워크 오프라인 상태.
                Log.d("getLottoHttp", "getConnectivityService Off Line..............");
                return false; //bConnectivity = false;
            }
        } else {
            // 네트워크 null.. 모뎀이 없는 경우??
            Log.d("getLottoHttp", "getConnectivityService Not Found Modem or Network..............");
            return false; //bConnectivity = false;
        }
    }

    public String getLottoNumber(int drwNo) throws JSONException, IOException {
        Log.i("getLottoHttp", "getLottoNumber Start..............");
        HttpURLConnection conn = null;
        String response = null;
        OutputStream os = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;

        try {

            String urlString = "http://www.645lotto.net/common.do?method=getLottoNumber&drwNo=";
            URL url = null;
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Log.i("getLottoHttp", "getLottoNumber URL END..............");
            try {
                conn = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("getLottoHttp", "getLottoNumber openConnection END..............");
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(10000);
            try {
                conn.setRequestMethod("GET");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            JSONObject job = new JSONObject();
            job.put("method", "getLottoNumber");
            job.put("drwNo", drwNo == 0 ? "" : drwNo);

            os = conn.getOutputStream();
            os.write(job.toString().getBytes());
            os.flush();


            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {

                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                byte[] byteBuffer = new byte[1024];
                byte[] byteData = null;
                int nLength = 0;
                while ((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                    baos.write(byteBuffer, 0, nLength);
                }
                byteData = baos.toByteArray();

                response = new String(byteData);
                Log.i("getLottoHttp", "getLottoNumber DATA response = " + response);
            }
        } catch(Exception e) {
            Log.e("getLottoHttp", e.toString());
        } finally {
            if(conn != null ) { try{ conn.disconnect(); } catch(Exception e) {} }
            if(baos != null ) { try{ baos.close(); } catch(Exception e) {} }
            if(is != null ) { try{ is.close(); } catch(Exception e) {} }
            if(os != null ) { try{ os.close(); } catch(Exception e) {} }
        }
        return response;
    }
}
