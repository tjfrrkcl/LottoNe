package com.example.fic02261.myapplication;

import android.os.AsyncTask;
import android.util.Log;

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

import static javax.net.ssl.SSLEngineResult.Status.OK;

/**
 * Created by hasung007 on 2017-07-10.
 */

//public class WebLottoNumberGetter  extends AsyncTask {
public class WebLottoNumberGetter extends AsyncTask {
    @Override
    public String doInBackground(Object[] aaa) {
        String ssss = null;
        try {
            ssss = getLottoNumber(0);
            Log.i("getLottoNumber", "SSSS = " + ssss);
        } catch (JSONException je) {
            Log.i("onClick JSon", "JSON Error....");
        } catch( IOException ie ) {
            Log.i("onClick IO", "IO Error....");
        }
        return ssss;
    }

    protected void onPostExecute(String result) {
        Log.i("onClick Lotto", "Number = " + result);
    }

    public String getLottoNumber(int drwNo) throws JSONException, IOException {
        Log.i("getLottoNumber", "Start..............");
        HttpURLConnection conn = null;
        String response = null;
        OutputStream os = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;

        String urlString = "http://www.645lotto.net/common.do?method=getLottoNumber&drwNo=";
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.i("getLottoNumber", "URL END..............");
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("getLottoNumber", "openConnection END..............");
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
        job.put("drwNo", "");

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

            JSONObject responseJSON = new JSONObject(response);
//            Boolean result = (Boolean) responseJSON.get("result");
//            String age = (String) responseJSON.get("age");
//            String job = (String) responseJSON.get("job");

            Log.i("getLottoNumber", "DATA response = " + response);

        }
        return response;
    }


//    protected Object doInBackground(Object[] params) {
//        return null;
//    }
}
