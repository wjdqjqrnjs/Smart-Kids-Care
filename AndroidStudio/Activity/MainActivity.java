package com.example.wjdqjqrnjs;


//URL바꿔야 하는 부분:
// 1.아마존  ConnectDB <- ec2서버IP + SendJson

// 2. 안드로이드
// 1)URL: 웹 <- 장고 서버 IP
// 2)Task_get <- 장고 서버IP + SendJson 또는 값 받는 웹  값을 그래프화 시키는 스레드
// 4)Task_get_DB <- ec2서버IP + SendAndroid 값을 받아오는 DB 받아오는 스레드

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Entity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.BaseColumns;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;


import static java.net.Proxy.Type.HTTP;



public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    private GoogleApiClient mGoogleApiClient = null;
    private GoogleMap mGoogleMap = null;
    private Marker currentMarker = null;

    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2002;
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초

    private AppCompatActivity mActivity;
    boolean askPermissionOnceAgain = false;
    boolean mRequestingLocationUpdates = false;
    Location mCurrentLocatiion;
    boolean mMoveMapByUser = true;
    boolean mMoveMapByAPI = true;
    LatLng currentPosition;
    LatLng Save_Position;
    public double Save_car_longitude;
    public double Save_car_latitude;
    public double Save_dest_latitude;
    public double Save_dest_longitude;
    public static int count=1;
    public boolean decision = false;
    public boolean markdecision = false;
    public LatLng endLatLng = new LatLng(0,0);
    public LatLng startLatLng = new LatLng(0,0);
    public boolean Red_Color=true;
    public boolean Wrong_Gps = false;
    public double Sum_dist;
    public boolean show_dest= false;
    public boolean show_car= false;
    public boolean ToCar =false;
    public String emotion_array;
    public String send_string="";
    public static double last_Data_1=0.0;
    public static double last_Data_2=0.0;
    public static double last_Data_3=0.0;
    public static double last_Data_4=0.0;
    public static double last_Data_5=0.0;
    public static double last_Data_6=0.0;
    public static double last_Data_7=0.0;
    public static double last_Data_8=0.0;
    public static double last_Data_9=0.0;
    public static double last_Data_10=0.0;
    public static double last_Data_11=0.0;
    public static double last_Data_12=0.0;
    public DbOpenHelper mDbOpenHelper;
    public Double Global_Longitude;
    public Double Global_Latitude;
    public String Global_Json;



    Intent i;
    SpeechRecognizer mRecognizer;
    TextView Get_voice;
    TextView textView;
    TextView Get_DB;
    BarChart barChart;
    BarChart barChart_2;
    private static Handler handler ;
    private static Handler handler_DB;
    ArrayList<BarEntry> barEntries = new ArrayList<>();
    ArrayList<String> theDates = new ArrayList<>();
    ArrayList<BarEntry> barEntries_2 = new ArrayList<>();
    ArrayList<String> theDates_2 = new ArrayList<>();
    BarDataSet barDataSet;
    BarData theData;
    BarDataSet barDataSet_2;
    BarData theData_2;
    SoundPool Crysound;
    int crysounid;
    int streamid;
    private BluetoothSPP bt;
    TextView test;



    LocationRequest locationRequest = new LocationRequest()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL_MS)
            .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

    private boolean isFragmentB = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);


        Log.d(TAG, "onCreate");
        mActivity = this;


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //차량 출발 버튼
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new Button.OnClickListener(){

            public void onClick(View v){

                Red_Color = false;

                //출발하면서 경로 그려주기 위함
                decision = false;

                ToCar = true;

            }
        });



        //목적지 리셋
        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new Button.OnClickListener(){

            public void onClick(View v){
                //목적지 리셋 버튼 누르면 마커 모드 클리어
                mGoogleMap.clear();

                //마커 없다고 판단하게 한다
                markdecision = false;
            }


        }

        );

        //웹 스트리밍: 유모차 내부 아이 얼굴 스트리밍 하기 위함

        WebView webView = (WebView)findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.setBackgroundColor(255);

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);


        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("http://ec2-3-15-212-160.us-east-2.compute.amazonaws.com/SendAnal.php");



        //아이 상태 결과 받아오는 Thread 및 문자열 전송 Handler
        handler = new ProgressHandler();
        runTime();


        //음성 인식 버튼 누르면 음성 인식 시작
        Get_voice = (TextView)findViewById(R.id.Get_voice);
        Button button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(new Button.OnClickListener(){

            public void onClick(View v){
               speek();
            }
        });

        //아이 감정 막대 그래프 기본 설정
        barChart = (BarChart) findViewById(R.id.Bar_Graph);
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);


        //유모차 환경 막대 그래프 기본 설정
        barChart_2 = (BarChart) findViewById(R.id.Bar_Graph_2);
        barChart_2.setTouchEnabled(true);
        barChart_2.setDragEnabled(true);
        barChart_2.setScaleEnabled(true);



        //DB 상세 환면 전환 버튼
        Button button_DB = (Button)findViewById(R.id.DB_Page);
        button_DB.setOnClickListener(new Button.OnClickListener(){

            public void onClick(View v){

               Intent intent = new Intent(getApplicationContext(), DB_Page.class);
               startActivity(intent);
            }
        });



        //효과음
        Crysound=new SoundPool(1, AudioManager.STREAM_ALARM,0);
        crysounid = Crysound.load(this, R.raw.cry,1);

        //Fan 작동 버튼 추가
        bt = new BluetoothSPP(this);

        if (!bt.isBluetoothAvailable()) { //블루투스 사용 불가
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() { //데이터 수신
            public void onDataReceived(byte[] data, String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() { //연결됐을 때
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext()
                        , "Connected to " + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() { //연결해제
                Toast.makeText(getApplicationContext()
                        , "Connection lost", Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() { //연결실패
                Toast.makeText(getApplicationContext()
                        , "Unable to connect", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnConnect = findViewById(R.id.btnConnect); //연결시도
        btnConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.disconnect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });

        //테스트 문자열
        test = (TextView)findViewById(R.id.Test);
    }




    //아이 상태 받아오는 요청 및 팝업 알림 함수
    public void runTime(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String str="", receiveMsg="";
                try{
                    String resultText = "값 없음";
                    URL url = null;
                    try {
                        url = new URL("http://192.168.137.36:8000/run/?action=emotion");

                        //http://192.168.137.249:8000/run/?action=emotion
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                        if (conn.getResponseCode() == conn.HTTP_OK) {
                            InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                            BufferedReader reader = new BufferedReader(tmp);
                            StringBuffer buffer = new StringBuffer();
                            while ((str = reader.readLine()) != null) {
                                buffer.append(str);
                            }
                            receiveMsg = buffer.toString();
                            Log.i("receiveMsg : ", receiveMsg);

                            reader.close();
                        } else {
                            Log.i("통신 결과", conn.getResponseCode() + "에러");
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    Message message = handler.obtainMessage();
                    message.obj = (Object) receiveMsg;
                    handler.sendMessage(message);

                    Thread.sleep(3000);
                }catch(InterruptedException ex){}
                }
            }
        });
    thread.start();

    }


    class ProgressHandler extends  Handler{
        public void handleMessage(Message msg)
        {
            //json받아오 데이터를 전처리

            String tmp = msg.toString();
            int idx_start = tmp.indexOf("obj");
            int idx_end = tmp.indexOf("target");

            String sub = tmp.substring(idx_start+4,idx_end);

            //test.setText(sub);
            cut_String(sub);

        }

    }


    public void speek()
    {
        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);
        mRecognizer.startListening(i);
    }

    public void onResume() {

        super.onResume();

        if (mGoogleApiClient.isConnected()) {

            Log.d(TAG, "onResume : call startLocationUpdates");
            if (!mRequestingLocationUpdates) startLocationUpdates();
        }


        //앱 정보에서 퍼미션을 허가했는지를 다시 검사해봐야 한다.
        if (askPermissionOnceAgain) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                askPermissionOnceAgain = false;

                checkPermissions();
            }
        }
    }


    private void startLocationUpdates() {

        if (!checkLocationServicesStatus()) {

            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();
        } else {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
                return;
            }


            Log.d(TAG, "startLocationUpdates : call FusedLocationApi.requestLocationUpdates");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
            mRequestingLocationUpdates = true;

            mGoogleMap.setMyLocationEnabled(true);

        }

    }


    private void stopLocationUpdates() {

        Log.d(TAG, "stopLocationUpdates : LocationServices.FusedLocationApi.removeLocationUpdates");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mRequestingLocationUpdates = false;
   }


    @Override
    public void onMapReady(final GoogleMap googleMap) {

        Log.d(TAG, "onMapReady :");

        mGoogleMap = googleMap;


        //런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에
        //지도의 초기위치를 서울로 이동
        setDefaultLocation();

        //mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {

            @Override
            public boolean onMyLocationButtonClick() {

                Log.d(TAG, "onMyLocationButtonClick : 위치에 따른 카메라 이동 활성화");
                mMoveMapByAPI = true;
                return true;
            }
        });
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {


            @Override
            public void onMapClick(LatLng latLng) {

                    //마커가 없을 경우에만 마커가 추가되게 한다
                   if ( markdecision == false ) {
                       MarkerOptions mapPin = new MarkerOptions();

                       Save_Position
                               = new LatLng( Save_dest_latitude, Save_dest_longitude);


                       Double latitude = latLng.latitude;
                       Double longitude = latLng.longitude;



                       //목적지 위치 저장
                       Save_dest_latitude = latLng.latitude;
                       Save_dest_longitude = latLng.longitude;

                       //목적지 주소 문자열
                       String Save_address = getCurrentAddress(Save_Position);
                       //마커 타이틀 설정
                       mapPin.title(Save_address);

                       mapPin.snippet(latitude.toString() + ", " + longitude.toString());
                       mapPin.position(new LatLng(latitude, longitude));
                       googleMap.addMarker(mapPin);


                       //추가 했기 때문에 마커 있다고 선언
                       markdecision = true;

                   }

            }
        });

        mGoogleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {

            @Override
            public void onCameraMoveStarted(int i) {

                if (mMoveMapByUser == true && mRequestingLocationUpdates) {

                    Log.d(TAG, "onCameraMove : 위치에 따른 카메라 이동 비활성화");
                    mMoveMapByAPI = false;
                }

                mMoveMapByUser = true;

            }
        });


        mGoogleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {

            @Override
            public void onCameraMove() {


            }
        });
    }


    @Override
    public void onLocationChanged(Location location) {

        currentPosition
                = new LatLng(location.getLatitude(), location.getLongitude());

        //향후 이용하기 위해서
         Global_Latitude = currentPosition.latitude;
         Global_Longitude=currentPosition.longitude;


        //처음 시작 점 잡아 주기 위해
        if (count == 1) {
            startLatLng = new LatLng(location.getLatitude(), location.getLongitude());

            //시작 위치를 차량 위치로 저장
            Save_car_latitude = currentPosition.latitude;
            Save_car_longitude = currentPosition.longitude;

            //처음 위치 마커로 표시
            MarkerOptions start_mark = new MarkerOptions();
            start_mark.title("차량 위치");

            start_mark.snippet("위도: " + Save_car_latitude + ", " +"경도: "+ Save_car_longitude);
            start_mark.position(new LatLng(Save_car_latitude, Save_car_longitude));
            mGoogleMap.addMarker(start_mark);

            //목적지 설정 팝업 알림
           // show3("목적지를 설정하세요!(지도 터치)");

        }


        Log.d(TAG, "onLocationChanged : ");

        //목적지 설정 안된 경우
        if (markdecision == false) {

            String markerTitle = getCurrentAddress(currentPosition);
            String markerSnippet = ("목적지를 설정 하세요! ");

            //현재 위치에 마커 생성하고 이동
            setCurrentLocation(location, markerTitle, markerSnippet);
        }

        //목적지 설정 된 경우
        else {
//

            Double latitude = currentPosition.latitude;
            Double longitude = currentPosition.longitude;

            //목적지 근처인지 확인 한번 목적지 근처 도착 시 decision true에서 바뀌지 않는다
            //도착했다고 팝업 알림
            if (  Math.abs(latitude-Save_dest_latitude) < 0.00003 && Math.abs(longitude - Save_dest_longitude) < 0.00003 ) {
                decision = true;

                //팝업 알림 보여주지 않은 경우우
                if( show_dest == false )
                    show();

                show_dest = true;

            }


            //차로 출발 하는 경우
            if (ToCar == true) {

                //차량 위치 근처인경우
                if (Math.abs(latitude - Save_car_latitude) < 0.00003 && Math.abs(longitude - Save_car_longitude) < 0.00003) {

                        //팝업 알림 보여주지 않은 경우
                        if (show_car == false)
                            show2();

                        show_car = true;

                    //맵 표시 초기화
                    //mGoogleMap.clear();
                }
            }


            //목적지 근처가 아닌 경우
            if (decision == false) {

                //내 위치에 마커 추가 및 현재 위치 설정
                String markerTitle = "내 위치";
                String markerSnippet = ("현재 위치: " + latitude + longitude);
                setCurrentLocation(location, markerTitle, markerSnippet);

                //경로 그리는 함수 선언

                //단 GPS위치 정보가 튈경우 예외 처리 해야한다

                double x = Math.abs(startLatLng.latitude - endLatLng.latitude);
                double y = Math.abs(startLatLng.longitude-endLatLng.longitude);
                double instantdist = Math.sqrt(x*x + y*y);

                //순간 점 사이의 거리를 더한다
                Sum_dist += instantdist;
                //만약 순간 점사이의 거리가 지금까지 점사이 평균 거리보다 5배이상 커지는 GPS이상인 경우
                if ( instantdist > 5*(Sum_dist/count) )
                    Wrong_Gps = true;
                else
                    Wrong_Gps = false;

                //GPS정보에 이상이 없고 도착 하지 않은 경우에만 경로를 그려주고
                if( Wrong_Gps == false ){
                    endLatLng = new LatLng(latitude,longitude);
                    drawPath();
                    startLatLng = new LatLng(latitude,longitude);
                }
            }

        }

        count++;
        mCurrentLocatiion = location;

    }

    private void drawPath()
    {
        //목적지로 갈때 경로 Red로 그려준다
        if( Red_Color == true ) {
            PolylineOptions Options = new PolylineOptions().add(startLatLng).add(endLatLng).width(5).color(Color.RED);
            mGoogleMap.addPolyline(Options);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(startLatLng));
        }

        //차량으로 돌아 갈때 버튼 누르면 Blue로 그려준다
        else
        {
            PolylineOptions Options = new PolylineOptions().add(startLatLng).add(endLatLng).width(5).color(Color.BLUE);
            mGoogleMap.addPolyline(Options);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(startLatLng));

        }
    }

    //목적지 도착 팝업 알림
    void show(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("팝업 알림");
        //타이틀설정
        String tv_text = "목적지에 도착했습니다. 추적 종료";
        builder.setMessage(tv_text);
        //내용설정

        builder.setNegativeButton("읽음",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"읽기완료",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }

    //차량 도착 팝업
    void show2(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("팝업 알림");
        //타이틀설정
        String tv_text = "차량에 도착했습니다. 추적 종료";
        builder.setMessage(tv_text);
        //내용설정

        builder.setNegativeButton("읽음",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"읽기완료",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }

    //아이의 상태 팝업
    void show3(String baby_state){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("팝업 알림");
        //타이틀설정
        String tv_text = baby_state;
        builder.setMessage(tv_text);

        //내용설정

        builder.setNegativeButton("읽음",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"읽기완료",Toast.LENGTH_LONG).show();
                        Crysound.stop(streamid);
                    }
                });
        builder.show();
    }


    @Override
    protected void onStart() {

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected() == false) {

            Log.d(TAG, "onStart: mGoogleApiClient connect");
            mGoogleApiClient.connect();
        }

        super.onStart();

        //블루투스 셋업업
       if (!bt.isBluetoothEnabled()) { //
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER); //DEVICE_ANDROID는 안드로이드 기기 끼리
                setup();
            }
        }

    }

    //블루투스
    public void setup() {
        Button btnSend = findViewById(R.id.btnSend); //데이터 전송
        btnSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bt.send("1", true);
            }
        });
    }


    //블루투스 정지
    public void onDestroy() {
        super.onDestroy();
        bt.stopService(); //블루투스 중지
    }



    @Override
    protected void onStop() {

        if (mRequestingLocationUpdates) {

            Log.d(TAG, "onStop : call stopLocationUpdates");
            stopLocationUpdates();
        }

        if (mGoogleApiClient.isConnected()) {

            Log.d(TAG, "onStop : mGoogleApiClient disconnect");
            mGoogleApiClient.disconnect();
        }

        super.onStop();
    }




    @Override
    public void onConnected(Bundle connectionHint) {


        if (mRequestingLocationUpdates == false) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

                if (hasFineLocationPermission == PackageManager.PERMISSION_DENIED) {

                    ActivityCompat.requestPermissions(mActivity,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                } else {

                    Log.d(TAG, "onConnected : 퍼미션 가지고 있음");
                    Log.d(TAG, "onConnected : call startLocationUpdates");
                    startLocationUpdates();
                    mGoogleMap.setMyLocationEnabled(true);
                }

            } else {

                Log.d(TAG, "onConnected : call startLocationUpdates");
                startLocationUpdates();
                mGoogleMap.setMyLocationEnabled(true);
            }
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.d(TAG, "onConnectionFailed");
        setDefaultLocation();
    }


    @Override
    public void onConnectionSuspended(int cause) {

        Log.d(TAG, "onConnectionSuspended");
        if (cause == CAUSE_NETWORK_LOST)
            Log.e(TAG, "onConnectionSuspended(): Google Play services " +
                    "connection lost.  Cause: network lost.");
        else if (cause == CAUSE_SERVICE_DISCONNECTED)
            Log.e(TAG, "onConnectionSuspended():  Google Play services " +
                    "connection lost.  Cause: service disconnected");
    }


    public String getCurrentAddress(LatLng latlng) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);


        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }


        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }

    }


    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {

        mMoveMapByUser = false;


        if (currentMarker != null) currentMarker.remove();


        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);


        currentMarker = mGoogleMap.addMarker(markerOptions);


        if (mMoveMapByAPI) {

            Log.d(TAG, "setCurrentLocation :  mGoogleMap moveCamera "
                    + location.getLatitude() + " " + location.getLongitude());
            // CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, 15);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
            mGoogleMap.moveCamera(cameraUpdate);
        }
    }


    public void setDefaultLocation() {

        mMoveMapByUser = false;


        //디폴트 위치, Seoul
        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 요부 확인하세요";


        if (currentMarker != null) currentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker = mGoogleMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        mGoogleMap.moveCamera(cameraUpdate);

    }


    //여기부터는 런타임 퍼미션 처리을 위한 메소드들
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        boolean fineLocationRationale = ActivityCompat
                .shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (hasFineLocationPermission == PackageManager
                .PERMISSION_DENIED && fineLocationRationale)
            showDialogForPermission("앱을 실행하려면 퍼미션을 허가하셔야합니다.");

        else if (hasFineLocationPermission
                == PackageManager.PERMISSION_DENIED && !fineLocationRationale) {
            showDialogForPermissionSetting("퍼미션 거부 + Don't ask again(다시 묻지 않음) " +
                    "체크 박스를 설정한 경우로 설정에서 퍼미션 허가해야합니다.");
        } else if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {


            Log.d(TAG, "checkPermissions : 퍼미션 가지고 있음");

            if (mGoogleApiClient.isConnected() == false) {

                Log.d(TAG, "checkPermissions : 퍼미션 가지고 있음");
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (permsRequestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION && grantResults.length > 0) {

            boolean permissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (permissionAccepted) {


                if (mGoogleApiClient.isConnected() == false) {

                    Log.d(TAG, "onRequestPermissionsResult : mGoogleApiClient connect");
                    mGoogleApiClient.connect();
                }


            } else {

                checkPermissions();
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void showDialogForPermission(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ActivityCompat.requestPermissions(mActivity,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.create().show();
    }

    private void showDialogForPermissionSetting(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(true);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                askPermissionOnceAgain = true;

                Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + mActivity.getPackageName()));
                myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mActivity.startActivity(myAppSettings);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.create().show();
    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d(TAG, "onActivityResult : 퍼미션 가지고 있음");


                        if (mGoogleApiClient.isConnected() == false) {

                            Log.d(TAG, "onActivityResult : mGoogleApiClient connect ");
                            mGoogleApiClient.connect();
                        }
                        return;
                    }
                }

                break;
        }


        //블루투스
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                setup();
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public Void cut_String(String jsonString) {

            String str = jsonString;
            String tempt;
            String Anger = "0.0";
            String Contempt = "0.0";
            String Disgust = "0.0";
            String Fear = "0.0";
            String Happiness = "0.0";
            String Neutral = "0.0";
            String Sadness = "0.0";
            String Surprise = "0.0";
            String Temperature = "0.0";
            String Light = "0.0";
            String Humidity = "0.0";
            String Sound = "0.0";


            try {
                 JSONObject jObject = new JSONObject(str);   // JSONArray 생성


                Anger = jObject.getString("anger");
                Contempt = jObject.getString("contempt");
                Disgust = jObject.getString("disgust");
                Fear = jObject.getString("fear");
                Happiness = jObject.getString("happiness");
                Neutral = jObject.getString("neutral");
                Sadness = jObject.getString("sadness");
                Surprise = jObject.getString("surprise");


            }
            catch(JSONException e)
            {
                e.printStackTrace();
                show3("얼굴 인식 안됨!");
            }

            try {
                JSONObject jObject = new JSONObject(str);

                Temperature = jObject.getString("temperature");
            }
            catch(JSONException e)
            {
                //show3("온도 측정 안됨");
            }

            try {
                JSONObject jObject = new JSONObject(str);

                Humidity = jObject.getString("humidity");
            }
            catch(JSONException e)
            {
                //show3("습도 측정 안됨");
            }

            try {
                JSONObject jObject = new JSONObject(str);

                Light = jObject.getString("light");
            }
            catch(JSONException e)
            {
                //show3("조도 측정 안됨");
            }

            try {
                JSONObject jObject = new JSONObject(str);

                Sound = jObject.getString("sound");
            }
            catch(JSONException e)
            {
                //show3("소리 측정 안됨");
            }

            //상태 판별 및 알림
                //1:화, 3:경멸 , 5:역겨움 7:두려움, 9.:행복 11:평범 13:슬픔 15:놀람 17 더움 19 습함 21 울음 23 밝음

            if( (Double.valueOf(Anger).doubleValue() != last_Data_1 ) || (Double.valueOf(Contempt).doubleValue() != last_Data_2 ) || (Double.valueOf(Disgust).doubleValue() != last_Data_3 ) ||
                    (Double.valueOf(Fear).doubleValue() != last_Data_4 ) || (Double.valueOf(Happiness).doubleValue() != last_Data_5 ) || (Double.valueOf(Neutral).doubleValue() != last_Data_6 ) ||
                    (Double.valueOf(Sadness).doubleValue() != last_Data_7 ) || (Double.valueOf(Surprise).doubleValue() != last_Data_8 ) || (Double.valueOf(Temperature).doubleValue() != last_Data_9 ) ||
                    (Double.valueOf(Humidity).doubleValue() != last_Data_10 ) || (Double.valueOf(Sound).doubleValue() != last_Data_11 ) || (Double.valueOf(Light).doubleValue() != last_Data_12 ) ) {//심하게 울경우
                if (Double.valueOf(Sound).doubleValue() > 23.0) {
                    String state_reason = ("아기가 심하게 울어요!" + "\n");

                    streamid =Crysound.play(crysounid,1.0F,1.0F,1,-1,1.0F);


                    if (Double.valueOf(Anger).doubleValue() > 0.5) {

                        state_reason = state_reason.concat("상태: 아기가 화가 났어요" + "\n");

                        state_reason = state_reason.concat("< 유모차 내부 환경 >" + "\n");

                        if (Double.valueOf(Temperature).doubleValue() > 20) {
                            state_reason = state_reason.concat("온도: " + Temperature + " 너무 더워요" + "\n");
                        }

                        if (Double.valueOf(Humidity).doubleValue() > 70) {
                            state_reason = state_reason.concat("습도: " + Humidity + " 너무 습해서요" + "\n");
                        }

                        if (Double.valueOf(Light) > 15) {
                            state_reason = state_reason.concat("조도: " + Light + " 너무 밝아서요" + "\n");
                        }


                            show3(state_reason);


                        }

                    if (Double.valueOf(Contempt).doubleValue() > 0.5) {
                        state_reason = state_reason.concat("상태: 아기가 혐오스러워 해요" + "\n");

                        state_reason = state_reason.concat("< 유모차 내부 환경 >" + "\n");

                        if (Double.valueOf(Temperature).doubleValue() > 20) {
                            state_reason = state_reason.concat("온도: " + Temperature + " 너무 더워요" + "\n");
                        }

                        if (Double.valueOf(Humidity).doubleValue() > 70) {
                            state_reason = state_reason.concat("습도: " + Humidity + " 너무 습해서요" + "\n");
                        }

                        if (Double.valueOf(Light) > 15) {
                            state_reason = state_reason.concat("조도: " + Light + " 너무 밝아서요" + "\n");
                        }


                        show3(state_reason);




                    }

                    if (Double.valueOf(Disgust).doubleValue() > 0.5) {
                        state_reason = state_reason.concat("상태: 아기가 구역질 해요" + "\n");

                        state_reason = state_reason.concat("< 유모차 내부 환경 >" + "\n");

                        if (Double.valueOf(Temperature).doubleValue() > 20) {
                            state_reason = state_reason.concat("온도: " + Temperature + " 너무 더워요" + "\n");
                        }

                        if (Double.valueOf(Humidity).doubleValue() > 70) {
                            state_reason = state_reason.concat("습도: " + Humidity + " 너무 습해서요" + "\n");
                        }

                        if (Double.valueOf(Light) > 15) {
                            state_reason = state_reason.concat("조도: " + Light + " 너무 밝아서요" + "\n");
                        }


                            show3(state_reason);




                    }

                    if (Double.valueOf(Fear).doubleValue() > 0.5) {
                        state_reason = state_reason.concat("상태: 아기가 두려워 해요" + "\n");

                        state_reason = state_reason.concat("< 유모차 내부 환경 >" + "\n");

                        if (Double.valueOf(Temperature).doubleValue() > 20) {
                            state_reason = state_reason.concat("온도: " + Temperature + " 너무 더워요" + "\n");
                        }

                        if (Double.valueOf(Humidity).doubleValue() > 70) {
                            state_reason = state_reason.concat("습도: " + Humidity + " 너무 습해서요" + "\n");
                        }

                        if (Double.valueOf(Light) > 15) {
                            state_reason = state_reason.concat("조도: " + Light + " 너무 밝아서요" + "\n");
                        }


                            show3(state_reason);




                    }

                    if (Double.valueOf(Sadness).doubleValue() > 0.5) {
                        state_reason = state_reason.concat("상태: 아기가 슬퍼 해요" + "\n");

                        state_reason = state_reason.concat("< 유모차 내부 환경 >" + "\n");

                        if (Double.valueOf(Temperature).doubleValue() > 20) {
                            state_reason = state_reason.concat("온도: " + Temperature + " 너무 더워요" + "\n");
                        }

                        if (Double.valueOf(Humidity).doubleValue() > 70) {
                            state_reason = state_reason.concat("습도: " + Humidity + " 너무 습해서요" + "\n");
                        }

                        if (Double.valueOf(Light) > 15) {
                            state_reason = state_reason.concat("조도: " + Light + " 너무 밝아서요" + "\n");
                        }


                            show3(state_reason);




                    }

                    if (Double.valueOf(Surprise).doubleValue() > 1) {
                        state_reason = state_reason.concat("상태: 아기가 놀랐어요" + "\n");

                        state_reason = state_reason.concat("< 유모차 내부 환경 >" + "\n");

                        if (Double.valueOf(Temperature).doubleValue() > 20) {
                            state_reason = state_reason.concat("온도: " + Temperature + " 너무 더워요" + "\n");
                        }

                        if (Double.valueOf(Humidity).doubleValue() > 70) {
                            state_reason = state_reason.concat("습도: " + Humidity + " 너무 습해서요" + "\n");
                        }

                        if (Double.valueOf(Light) > 15) {
                            state_reason = state_reason.concat("조도: " + Light + " 너무 밝아서요" + "\n");
                        }


                            show3(state_reason);




                    }

                    //최종 상태 팝업 알림




                }


                //심하게 울지 않을 경우
                else {
                    String state_reason_2 = "";

                    if (Double.valueOf(Anger).doubleValue() > 0.5) {

                        state_reason_2 = state_reason_2.concat("상태: 아기가 화가 났어요" + "\n");


                        state_reason_2 = state_reason_2.concat("< 유모차 내부 환경 >" + "\n");

                        if (Double.valueOf(Temperature).doubleValue() > 20) {
                            state_reason_2 = state_reason_2.concat("온도: " + Temperature + " 너무 더워요" + "\n");
                        }

                        if (Double.valueOf(Humidity).doubleValue() > 70) {
                            state_reason_2 = state_reason_2.concat("습도: " + Humidity + " 너무 습해서요" + "\n");
                        }

                        if (Double.valueOf(Light) > 15) {
                            state_reason_2 = state_reason_2.concat("조도: " + Light + " 너무 밝아서요" + "\n");
                        }

                            show3(state_reason_2);




                    }

                    if (Double.valueOf(Contempt).doubleValue() > 0.5) {
                        state_reason_2 = state_reason_2.concat("상태: 아기가 혐오스러워 해요" + "\n");

                        state_reason_2 = state_reason_2.concat("< 유모차 내부 환경 >" + "\n");

                        if (Double.valueOf(Temperature).doubleValue() > 20) {
                            state_reason_2 = state_reason_2.concat("온도: " + Temperature + " 너무 더워요" + "\n");
                        }

                        if (Double.valueOf(Humidity).doubleValue() > 70) {
                            state_reason_2 = state_reason_2.concat("습도: " + Humidity + " 너무 습해서요" + "\n");
                        }

                        if (Double.valueOf(Light) > 15) {
                            state_reason_2 = state_reason_2.concat("조도: " + Light + " 너무 밝아서요" + "\n");
                        }

                            show3(state_reason_2);




                    }

                    if (Double.valueOf(Disgust).doubleValue() > 0.5) {
                        state_reason_2 = state_reason_2.concat("상태: 아기가 구역질 해요" + "\n");

                        state_reason_2 = state_reason_2.concat("< 유모차 내부 환경 >" + "\n");

                        if (Double.valueOf(Temperature).doubleValue() > 20) {
                            state_reason_2 = state_reason_2.concat("온도: " + Temperature + " 너무 더워요" + "\n");
                        }

                        if (Double.valueOf(Humidity).doubleValue() > 70) {
                            state_reason_2 = state_reason_2.concat("습도: " + Humidity + " 너무 습해서요" + "\n");
                        }

                        if (Double.valueOf(Light) > 15) {
                            state_reason_2 = state_reason_2.concat("조도: " + Light + " 너무 밝아서요" + "\n");
                        }

                            show3(state_reason_2);




                    }

                    if (Double.valueOf(Fear).doubleValue() > 0.5) {
                        state_reason_2 = state_reason_2.concat("상태: 아기가 두려워 해요" + "\n");

                        state_reason_2 = state_reason_2.concat("< 유모차 내부 환경 >" + "\n");

                        if (Double.valueOf(Temperature).doubleValue() > 20) {
                            state_reason_2 = state_reason_2.concat("온도: " + Temperature + " 너무 더워요" + "\n");
                        }

                        if (Double.valueOf(Humidity).doubleValue() > 70) {
                            state_reason_2 = state_reason_2.concat("습도: " + Humidity + " 너무 습해서요" + "\n");
                        }

                        if (Double.valueOf(Light) > 15) {
                            state_reason_2 = state_reason_2.concat("조도: " + Light + " 너무 밝아서요" + "\n");
                        }

                            show3(state_reason_2);




                    }

                    if (Double.valueOf(Sadness).doubleValue() > 0.5) {
                        state_reason_2 = state_reason_2.concat("상태: 아기가 슬퍼 해요" + "\n");

                        state_reason_2 = state_reason_2.concat("< 유모차 내부 환경 >" + "\n");

                        if (Double.valueOf(Temperature).doubleValue() > 20) {
                            state_reason_2 = state_reason_2.concat("온도: " + Temperature + " 너무 더워요" + "\n");
                        }

                        if (Double.valueOf(Humidity).doubleValue() > 70) {
                            state_reason_2 = state_reason_2.concat("습도: " + Humidity + " 너무 습해서요" + "\n");
                        }

                        if (Double.valueOf(Light) > 15) {
                            state_reason_2 = state_reason_2.concat("조도: " + Light + " 너무 밝아서요" + "\n");
                        }

                            show3(state_reason_2);



                    }

                    if (Double.valueOf(Surprise).doubleValue() > 0.5) {
                        state_reason_2 = state_reason_2.concat("상태: 아기가 놀랐어요" + "\n");

                        state_reason_2 = state_reason_2.concat("< 유모차 내부 환경 >" + "\n");

                        if (Double.valueOf(Temperature).doubleValue() > 20) {
                            state_reason_2 = state_reason_2.concat("온도: " + Temperature + " 너무 더워요" + "\n");
                        }

                        if (Double.valueOf(Humidity).doubleValue() > 70) {
                            state_reason_2 = state_reason_2.concat("습도: " + Humidity + " 너무 습해서요" + "\n");
                        }

                        if (Double.valueOf(Light) > 15) {
                            state_reason_2 = state_reason_2.concat("조도: " + Light + " 너무 밝아서요" + "\n");
                        }

                            show3(state_reason_2);



                    }


                }

                //실시간 막대 그래프


                //1:화, 3:경멸 , 5:역겨움 7:두려움, 9.:행복 11:평범 13:슬픔 15:놀람 17 더움 19 습함 21 울음 23 밝음
                last_Data_1 = Double.valueOf(Anger).doubleValue();
                last_Data_2 = Double.valueOf(Contempt).doubleValue();
                last_Data_3 = Double.valueOf(Disgust).doubleValue();
                last_Data_4 = Double.valueOf(Fear).doubleValue();
                last_Data_5 = Double.valueOf(Happiness).doubleValue();
                last_Data_6 = Double.valueOf(Neutral).doubleValue();
                last_Data_7 = Double.valueOf(Sadness).doubleValue();
                last_Data_8 = Double.valueOf(Surprise).doubleValue();
                last_Data_9 = Double.valueOf(Temperature).doubleValue();
                last_Data_10 = Double.valueOf(Humidity).doubleValue();
                last_Data_11 = Double.valueOf(Sound).doubleValue();
                last_Data_12 = Double.valueOf(Light).doubleValue();


            }

        //감정 막대 그래프
        barEntries.clear();
        theDates.clear();

        barEntries.add(new BarEntry(Float.valueOf(Anger).floatValue(),0));
        barEntries.add(new BarEntry(Float.valueOf(Contempt).floatValue(),1));
        barEntries.add(new BarEntry(Float.valueOf(Fear).floatValue(),2));
        barEntries.add(new BarEntry(Float.valueOf(Happiness).floatValue(),3));
        barEntries.add(new BarEntry(Float.valueOf(Disgust).floatValue(),4));
        barEntries.add(new BarEntry(Float.valueOf(Sadness).floatValue(),5));
        barEntries.add(new BarEntry(Float.valueOf(Surprise).floatValue(),6));
        barEntries.add(new BarEntry(Float.valueOf(Neutral).floatValue(),7));
        barDataSet=new BarDataSet(barEntries,"Emotions");

        theDates.add("화");
        theDates.add("경멸");
        theDates.add("공포");
        theDates.add("행복");
        theDates.add("역함");
        theDates.add("슬픔");
        theDates.add("놀람");
        theDates.add("평온");

        theData = new BarData(theDates,barDataSet);
        barChart.setData(theData);



        //유모차 환경 유모차 막대 그래프
        barEntries_2.clear();
        theDates_2.clear();

        barEntries_2.add(new BarEntry(Float.valueOf(Temperature).floatValue(),0));
        barEntries_2.add(new BarEntry(Float.valueOf(Humidity).floatValue(),1));
        barEntries_2.add(new BarEntry(Float.valueOf(Sound).floatValue(),2));
        barEntries_2.add(new BarEntry(Float.valueOf(Light).floatValue(),3));
        barDataSet_2=new BarDataSet(barEntries_2,"Enviroment");

        theDates_2.add("온도");
        theDates_2.add("습도");
        theDates_2.add("소리");
        theDates_2.add("조도");


        theData_2 = new BarData(theDates_2,barDataSet_2);
        barChart_2.setData(theData_2);


        return null;
    }




//    //Json받아오는 함수
//   public class Task_insert_DB extends AsyncTask<String, Void, String> {
//
//        String clientKey = "#########################";
//        private String str, receiveMsg;
//        private final String ID = "########";
//
//        @Override
//        protected String doInBackground(String... params) {
//            URL url = null;
//            try {
//                url = new URL("http://ec2-3-17-145-169.us-east-2.compute.amazonaws.com/SendAnal.php");
//
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//                if (conn.getResponseCode() == conn.HTTP_OK) {
//                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
//                    BufferedReader reader = new BufferedReader(tmp);
//                    StringBuffer buffer = new StringBuffer();
//                    while ((str = reader.readLine()) != null) {
//                        buffer.append(str);
//                    }
//                    receiveMsg = buffer.toString();
//                    Log.i("receiveMsg : ", receiveMsg);
//
//                    reader.close();
//                } else {
//                    Log.i("통신 결과", conn.getResponseCode() + "에러");
//                }
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return receiveMsg;
//        }
//    }

//    public class Task_get extends AsyncTask<String, Void, String> {
//        String clientKey = "#########################";
//        private String str, receiveMsg;
//        private final String ID = "########";
//        @Override
//        protected String doInBackground(String... params) {
//            URL url = null;
//            try {
//                url = new URL("http://ec2-3-17-145-169.us-east-2.compute.amazonaws.com/SendJson.php");
//
//                //http://192.168.137.249:8000/run/?action=emotion
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//                if (conn.getResponseCode() == conn.HTTP_OK) {
//                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
//                    BufferedReader reader = new BufferedReader(tmp);
//                    StringBuffer buffer = new StringBuffer();
//                    while ((str = reader.readLine()) != null) {
//                        buffer.append(str);
//                    }
//                    receiveMsg = buffer.toString();
//                    Log.i("receiveMsg : ", receiveMsg);
//
//                    reader.close();
//                } else {
//                    Log.i("통신 결과", conn.getResponseCode() + "에러");
//                }
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return receiveMsg;
//        }
//    }

    //영상 틀기 위해 REST GET방식으로 전송
    public class Task_post extends AsyncTask<Void, Void, ArrayList<String>> {

        String clientKey = "#########################";;
        private String str, receiveMsg;
        private final String ID = "########";


        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            URL url = null;
            try {
                url = new URL("http://192.168.137.36:8000/run/?action=youtube&title="+send_string);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.getResponseCode();

                BufferedReader rd = null;
                ArrayList<String> qResults = new ArrayList<String>();

                rd = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
                String line = "";
                while((line = rd.readLine()) != null)
                {
                    Log.d("BufferedReader",line);
                    if(line != null)
                    {
                        qResults.add(line);
                    }
                }
                return qResults;
            }
            catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }
        protected  void onPostExecute(ArrayList<String> qResults)
        {
            super.onPostExecute(qResults);
        }

    }


    //음성인식 처리 함수
    public RecognitionListener listener = new RecognitionListener()
    {
        @Override
        public void onRmsChanged(float rmsdB)
        {// TODO Auto-generated method stub
        }
        @Override
        public void onResults(Bundle results)
        {// TODO Auto-generated method stub
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
            //Get_voice.setText(""+rs[0]);

            //음성 인식한 문자열을 보내기 위해 할당
            send_string = ""+rs[0];
            Get_voice.setText(send_string);

            //음성인식 문자열 웹에 보낸다
            try {
                new Task_post().execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onReadyForSpeech(Bundle params)
        {// TODO Auto-generated method stub
        }

        @Override
        public void onPartialResults(Bundle partialResults)
        {// TODO Auto-generated method stub//
        }

        @Override
        public void onEvent(int eventType, Bundle params)
        {// TODO Auto-generated method stub
        }

        @Override
        public void onError(int error)
        {// TODO Auto-generated method stub
        }

        @Override
        public void onEndOfSpeech()
        {// TODO Auto-generated method stub
        }

        @Override
        public void onBufferReceived(byte[] buffer)
        {// TODO Auto-generated method stub
        }

        @Override
        public void onBeginningOfSpeech()
        {// TODO Auto-generated method stub
        }


    };

}



