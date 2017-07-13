package com.example.fic02261.myapplication;

import android.graphics.Color;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity  implements  View.OnClickListener {

    //버튼객체 참조 변수 선언
    Button[][] btn;
    Button btn1, btn2, btn3;
    //랜덤 seed 변수 선언
    long rndSeed;
    private final int DYNAMIC_VIEW_ID = 0x8000;
    private LinearLayout dynamicLayout;
    LinearLayout[] dynamicLayouts;
    List<List> lottoLists = new ArrayList<List>();
    LottoAdapter adapter;
    ArrayList<Lotto> lottoArrayList;
    DBHandler handler;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //현재 시간을 초기 값으로 세팅 (값이 계속 바뀌어야 하기 때문에..현재 시간은 계속 흘러 가므로~~)
        rndSeed = System.currentTimeMillis();

        btn = new Button[5][6];
        dynamicLayouts = new LinearLayout[5];

        //--- 로또 번호가 담길 버튼 객체 얻기 --------//
        btn1 = (Button) findViewById(R.id.bt1);
        btn2 = (Button) findViewById(R.id.bt2);
        btn3 = (Button) findViewById(R.id.bt3);
        //생성과, 초기화 버튼 리스너 등록
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        handler = new DBHandler(this);
        if(handler.getLottoCount() == 0 )
        {
            new DataFetcherTask().execute();
        }
//        else
//        {
//            ArrayList<Lotto> lottoList = handler.getAllLotto();
//            adapter = new LottoAdapter(MainActivity.this, lottoList);
//        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void onClickRotto(View v) {
        // TODO Auto-generated method stub
        dynamicLayout = (LinearLayout)findViewById(R.id.dynamicArea);
        dynamicLayout.removeAllViews();
        lottoLists.clear();
        int numButton;
        numButton = 0;
//        ShapeDrawable sd = new ShapeDrawable(new OvalShape());
//        sd.getPaint().setShader(new RadialGradient(60, 30, 50, Color.LTGRAY, Color.LTGRAY, Shader.TileMode.CLAMP));
        ShapeDrawable sYellow = new ShapeDrawable(new OvalShape());
        sYellow.getPaint().setShader(new RadialGradient(60, 30, 50, Color.WHITE, Color.rgb(255,215,0), Shader.TileMode.CLAMP));
        ShapeDrawable sBlue = new ShapeDrawable(new OvalShape());
        sBlue.getPaint().setShader(new RadialGradient(60, 30, 50, Color.WHITE, Color.BLUE, Shader.TileMode.CLAMP));
        ShapeDrawable sRed = new ShapeDrawable(new OvalShape());
        sRed.getPaint().setShader(new RadialGradient(60, 30, 50, Color.WHITE, Color.RED, Shader.TileMode.CLAMP));
        ShapeDrawable sBlack = new ShapeDrawable(new OvalShape());
        sBlack.getPaint().setShader(new RadialGradient(60, 30, 50, Color.WHITE, Color.BLACK, Shader.TileMode.CLAMP));
        ShapeDrawable sGreen = new ShapeDrawable(new OvalShape());
        sGreen.getPaint().setShader(new RadialGradient(60, 30, 50, Color.WHITE, Color.rgb(50,205,50), Shader.TileMode.CLAMP));

        //버튼 객체 배열 사이즈 만큼 루프를 돌면서
        //랜덤값을 뽑아내고 각각의 버튼에 숫자를 넣어준다.
        //끝내기 버튼을 눌렀을시에 종료~
        if (v == btn1) {
            for (int i = 0; i < 5; i++) {
                dynamicLayouts[i] = new LinearLayout(this);
                Set<Integer> lottoSet = new HashSet<Integer>();
                while( true ) {
                    lottoSet.add( getRand(0, 45) + 1 );
                    if( lottoSet.size() == 3 ) {
                        ArrayList<Integer> tempList = new ArrayList<Integer>(lottoSet);
                        Collections.sort(tempList);
                        if ( handler.getLottoNumber3Exists(tempList.toString()) != 0 ) lottoSet.clear();
                    } else if( lottoSet.size() == 4 ) {
                        ArrayList<Integer> tempList = new ArrayList<Integer>(lottoSet);
                        Collections.sort(tempList);
                        if ( handler.getLottoNumber4Exists(tempList.toString()) == 0 ) break;
                        else lottoSet.clear();
                    }
                }
                while( lottoSet.size() < 6 ) {
                    lottoSet.add( getRand(0, 45) + 1 );
                }
                List<Integer> lottoList = new ArrayList<Integer>(lottoSet);
                Collections.sort(lottoList);
                lottoLists.add(lottoList);

                for (int j = 0; j < 6; j++) {
                    //number을 문자열로 캐스팅~
                    DisplayMetrics dm = getResources().getDisplayMetrics();
                    LinearLayout.LayoutParams btn_param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    btn_param.rightMargin = Math.round(5 * dm.density);
                    btn[i][j] = new Button(this, null, android.R.attr.buttonStyleSmall);
                    btn[i][j].setClickable(false);
                    btn[i][j].setHeight(20);
                    btn[i][j].setWidth(20);
                    btn[i][j].setRight(10);
                    btn[i][j].setId(DYNAMIC_VIEW_ID + (numButton++));
                    int number =lottoList.get(j);
                    btn[i][j].setText(String.valueOf(number));
                    if( number < 11 ) {
                        btn[i][j] = setBackgroundButton(btn[i][j], sYellow);
                        btn[i][j].setTextColor(Color.BLUE);
                    } else if( number < 21 ) {
                        btn[i][j] = setBackgroundButton(btn[i][j], sBlue);
                        btn[i][j].setTextColor(Color.WHITE);
                    } else if( number < 31 ) {
                        btn[i][j] = setBackgroundButton(btn[i][j], sRed);
                        btn[i][j].setTextColor(Color.WHITE);
                    } else if( number < 41 ) {
                        btn[i][j] = setBackgroundButton(btn[i][j], sBlack);
                        btn[i][j].setTextColor(Color.WHITE);
                    } else {
                        btn[i][j] = setBackgroundButton(btn[i][j], sGreen);
                        btn[i][j].setTextColor(Color.BLUE);
                    }
                    btn[i][j].setLayoutParams(btn_param);

                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    param.setMargins(20, 20, 10, 10);
                    dynamicLayouts[i].addView(btn[i][j]);
                    dynamicLayouts[i].setLayoutParams(param);
                }
                dynamicLayout.addView(dynamicLayouts[i]);
//                LinearLayout.LayoutParams sparam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                sparam.gravity = Gravity.CENTER_VERTICAL;
                dynamicLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        }
        else if (v == btn2) {

            System.exit(0);
        }
    }

    public Button setBackgroundButton( Button btn, ShapeDrawable sd ) {
        if(android.os.Build.VERSION.SDK_INT < 16) {
            btn.setBackgroundDrawable(sd);
        }
        else {
            btn.setBackground(sd);
        }
        return btn;
    }

    //---랜덤난수~ 원하는 숫자에서~ 숫자만큼의 사이에서 발생 시키는 method!---//
    public int setRandSeed() {
        rndSeed = (rndSeed * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
        return (int) ((rndSeed >> (48 - 32)) & 2147483647);
    }

    public int getRand(int a, int b) {
        return ((setRandSeed() % (b - a)) + a);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.fic02261.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.fic02261.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onClick(View v) {
        Log.i("onClick", "Start....");
        String ssss = null;
        if( v == btn3) {
            new WebLottoNumberGetter().execute("aaaa");
            Log.i("WebLottoNumberGetter", "End");
        } else {
            onClickRotto(v);
        }
    }

    class DataFetcherTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String lottoJsonData = null;// String object to store fetched data from server
            // Http Request Code start
            BufferedReader lottoBr = null;
            try {
                lottoBr = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.nanumlotto)));
                StringBuffer lottoSb = new StringBuffer();
                while( ( lottoJsonData = lottoBr.readLine() ) != null ) {
                    Log.d("first read == ", lottoJsonData);
                    lottoSb.append(lottoJsonData);
                }
                lottoJsonData = lottoSb.toString();
                Log.d("LottoJsonData = ", lottoJsonData);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try{
                    if( lottoBr != null ) lottoBr.close();
                } catch( Exception e) {
                    e.printStackTrace();
                }
            }
            // Http Request Code end
            // Json Parsing Code Start
            try {
                lottoArrayList = new ArrayList<Lotto>();
                Log.d("lottoJsonData = ", lottoJsonData);
                JSONObject jsonObject = new JSONObject(lottoJsonData);
                JSONArray jsonArray = jsonObject.getJSONArray("rows");
                for (int i=0;i<jsonArray.length();i++)
                {
                    JSONArray lottoArray = jsonArray.getJSONArray(i);
                    int recu_no = lottoArray.getInt(0);
                    String com_yn = lottoArray.getString(1);
                    int f1 = lottoArray.getInt(2);
                    int s2 = lottoArray.getInt(3);
                    int t3 = lottoArray.getInt(4);
                    int f4 = lottoArray.getInt(5);
                    int f5 = lottoArray.getInt(6);
                    int s6 = lottoArray.getInt(7);
                    Lotto lotto = new Lotto();
                    lotto.setRecu_no(recu_no);
                    lotto.setCom_yn(com_yn);
                    lotto.setF1(f1);
                    lotto.setS2(s2);
                    lotto.setT3(t3);
                    lotto.setF4(f4);
                    lotto.setF5(f5);
                    lotto.setS6(s6);
                    handler.addLotto(lotto);// Inserting into DB
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Json Parsing code end
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ArrayList<Lotto> cityList = handler.getAllLotto();
            adapter = new LottoAdapter(MainActivity.this,cityList);
        }
    }
}
