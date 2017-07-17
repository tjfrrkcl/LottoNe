package com.example.fic02261.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
    DBHandler handler = null; //new DBHandler(this.context);
    private Context context;

    public WebLottoNumberGetter(Context context) {
        this.context = context;
        handler = new DBHandler(this.context);
    }
    @Override
    public String doInBackground(Object[] aaa) {
        String response = null;
        Boolean bConnectivity = false;
        int maxdrwNo = 0;
        int checkDrwNo = 0;
        int checkMaxDrwNo = 0;
        try {
            if(getConnectivityService()) {
                maxdrwNo = handler.getLottoMaxdrwNo();
                Log.d("doInBackground", "maxdrwNo = " + maxdrwNo);
                response = getLottoNumber(0);
                JSONObject responseJSON = new JSONObject(response);
                if ("success".equals(responseJSON.getString("returnValue"))) {
                    checkMaxDrwNo = responseJSON.getInt("drwNo");
                    Log.d("doInBackground", "checkMaxDrwNo = " + responseJSON.getInt("drwNo"));
                    if (maxdrwNo < checkMaxDrwNo) {
                        checkDrwNo = maxdrwNo + 1;
                        while (true) {
                            response = getLottoNumber(checkDrwNo);
                            responseJSON = new JSONObject(response);
                            if ("success".equals(responseJSON.getString("returnValue"))) {
//                    maxdrwNo = handler.getLottoMaxdrwNo();
                                if (responseJSON.getInt("drwNo") <= checkMaxDrwNo) {
                                    Log.d("doInBackground", "drwNo(" + responseJSON.getInt("drwNo") + ") Success !!!");
                                    Lotto lotto = new Lotto();
                                    lotto.setRecu_no(responseJSON.getInt("drwNo"));
                                    lotto.setCom_yn("Y");
                                    lotto.setF1(responseJSON.getInt("drwtNo1"));
                                    lotto.setS2(responseJSON.getInt("drwtNo2"));
                                    lotto.setT3(responseJSON.getInt("drwtNo3"));
                                    lotto.setF4(responseJSON.getInt("drwtNo4"));
                                    lotto.setF5(responseJSON.getInt("drwtNo5"));
                                    lotto.setS6(responseJSON.getInt("drwtNo6"));
                                    try {
                                        handler.addLotto(lotto);// Inserting into DB
                                    } catch (android.database.sqlite.SQLiteConstraintException se) {
                                        Log.e("doInBackground", se.getMessage());
                                        break;
                                    } catch (Exception e) {
                                        Log.e("doInBackground", e.getMessage());
                                        break;
                                    }
                                } else {
                                    Log.d("getLottoNumber", "drwNo(" + responseJSON.getInt("drwNo") + ") <= maxdrwNo(" + maxdrwNo + ")");
                                }
                            } else {
                                Log.i("doInBackground", "drwNo(" + responseJSON.getInt("drwNo") + ") Fail");
                                break;
                            }
                            checkDrwNo++;
                            Log.d("doInBackground", "responseJSON = " + response);
                        }
                    }
                }

            }
        } catch (JSONException je) {
            Log.i("onClick JSon", "JSON Error....");
        } catch( IOException ie ) {
            Log.i("onClick IO", "IO Error....");
        }

        return response;
    }

    protected void onPostExecute(String result) {
        Log.i("onClick Lotto", "Number = " + result);
    }

    public boolean getConnectivityService() {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI && activeNetwork.isConnectedOrConnecting()) {
                // wifi 연결중
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE && activeNetwork.isConnectedOrConnecting()) {
                // 모바일 네트워크 연결중
                return true;
            } else {
                // 네트워크 오프라인 상태.
                return false; //bConnectivity = false;
            }
        } else {
            // 네트워크 null.. 모뎀이 없는 경우??
            return false; //bConnectivity = false;
        }
    }

    public String getLottoNumber(int drwNo) throws JSONException, IOException {
        Log.i("getLottoNumber", "Start..............");
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
                Log.i("getLottoNumber", "DATA response = " + response);
            }
        } catch(Exception e) {
            Log.e("getLottoNumber", e.toString());
        } finally {
            if(conn != null ) { try{ conn.disconnect(); } catch(Exception e) {} }
            if(baos != null ) { try{ baos.close(); } catch(Exception e) {} }
            if(is != null ) { try{ is.close(); } catch(Exception e) {} }
            if(os != null ) { try{ os.close(); } catch(Exception e) {} }
        }
        return response;
    }
}
