package com.example.wjdqjqrnjs;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class DB_Page extends AppCompatActivity {


    private static Handler handler_DB;
    TextView text_DB;
    TextView text_Anal;
    public String ChoiceDate;



    BarChart barChart_2;
    ArrayList<BarEntry> barEntries_2 = new ArrayList<>();
    ArrayList<String> theDates_2 = new ArrayList<>();
    BarDataSet barDataSet_2;
    BarData theData_2;
    ImageView State_baby;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db__page);


        //다시 돌아가는 버튼
        Button button_re = (Button) findViewById(R.id.Return);
        button_re.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        //show3("날짜를 선택하세요!");


        //DB보여주는 TextView

        text_DB = (TextView) findViewById(R.id.DB_View);
        text_DB.setMovementMethod(new ScrollingMovementMethod());

        barChart_2 = (BarChart) findViewById(R.id.Bar_Graph_2);
        barChart_2.setTouchEnabled(true);
        barChart_2.setDragEnabled(true);
        barChart_2.setScaleEnabled(true);

        //DB분석 결과 보여주는 부분
        text_Anal = (TextView)findViewById(R.id.DB_Anal);
        text_Anal.setMovementMethod(new ScrollingMovementMethod());

        //아이 얼굴 이미지 보여주는 부분
        State_baby = (ImageView)findViewById(R.id.State_baby);


        //CalendarView 인스턴스 만들기
        CalendarView calendar = (CalendarView) findViewById(R.id.calendar);

        //리스너 등록
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {


            @Override


            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {


                // TODO Auto-generated method stub


                Toast.makeText(DB_Page.this, "DB 분석 중 잠시만 기다려 주세요", Toast.LENGTH_SHORT).show();

                //10일 이상인 경우
                if (dayOfMonth >= 10) {
                    ChoiceDate = "" + year + "-0" + (month + 1) + "-" + dayOfMonth;
                } else if (dayOfMonth < 10) {
                    ChoiceDate = "" + year + "-0" + (month + 1) + "-0" + dayOfMonth;
                }


                //DB를 받아온다
                String resultText_DB = "";
                try {

                    resultText_DB = new Task_get_DB().execute().get();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                String tmp_DB = resultText_DB.toString();
                int idx_start = tmp_DB.indexOf("[");
                int idx_end = tmp_DB.indexOf("]");
                String sub_DB = tmp_DB.substring(idx_start, idx_end + 1);


                StringBuffer sb = new StringBuffer();

                int Count = 0;
                int GoodCount = 0;
                int BadCount = 0;
                Double TemperSum = 0.0;
                Double SoundSum = 0.0;
                Double HumiditySum = 0.0;
                Double LightSum = 0.0;
                Double Good = 0.0;
                Double Bad = 0.0;


                try {
                    JSONArray jarray = new JSONArray(sub_DB);   // JSONArray 생성
                    for (int i = 0; i < jarray.length(); i++) {


                        JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                        String time = jObject.getString("time");
                        String anger = jObject.getString("anger");
                        String contempt = jObject.getString("contempt");
                        String disgust = jObject.getString("disgust");
                        String fear = jObject.getString("fear");
                        String happiness = jObject.getString("happiness");
                        String neutral = jObject.getString("neutral");
                        String sadness = jObject.getString("sadness");
                        String surprise = jObject.getString("surprise");
                        String temperature = jObject.getString("temperature");
                        String light = jObject.getString("light");
                        String humidity = jObject.getString("humidity");
                        String sound = jObject.getString("sound");


                        String data[] = time.split(" ");

                        if (data[0].equals(ChoiceDate)) {
                            //행복 불행 지수
                            Good = Double.valueOf(happiness).doubleValue() + Double.valueOf(neutral).doubleValue();
                            Bad = Double.valueOf(anger).doubleValue() + Double.valueOf(contempt).doubleValue() + Double.valueOf(disgust).doubleValue() + Double.valueOf(fear).doubleValue() + Double.valueOf(sadness).doubleValue() + Double.valueOf(surprise).doubleValue();

                            if (Good > 0.5)
                                GoodCount += 1;
                            if (Bad > 0.5)
                                BadCount += 1;


                            //그 날의 센서 값을 합
                            TemperSum += Double.valueOf(temperature).doubleValue();
                            HumiditySum += Double.valueOf(humidity).doubleValue();
                            SoundSum += Double.valueOf(sound).doubleValue();
                            LightSum += Double.valueOf(light).doubleValue();
                            Count += 1;
                        }
                    }

                    if (Integer.valueOf(GoodCount).intValue() != 0 || Integer.valueOf(BadCount).intValue() != 0 || Double.valueOf(TemperSum).doubleValue() != 0.0 || Double.valueOf(HumiditySum).doubleValue() != 0.0 || Double.valueOf(SoundSum).doubleValue() != 0.0 || Double.valueOf(LightSum).doubleValue() != 0.0) {
                        //날짜 해당하는 전체 돌고 나면 그래프 화
                        barEntries_2.clear();
                        theDates_2.clear();

                        barEntries_2.add(new BarEntry(Float.valueOf(GoodCount).floatValue(), 0));
                        barEntries_2.add(new BarEntry(Float.valueOf(BadCount).floatValue(), 1));



                        //아기 하루 상태 이미지 표현
                        if(  Float.valueOf(GoodCount).floatValue() >= Float.valueOf(BadCount).floatValue() )
                        {
                            State_baby.setImageResource(R.drawable.smile_baby);

                            if( Float.valueOf(GoodCount).floatValue() == 0.0 || Float.valueOf(GoodCount).floatValue() ==0  )
                            {
                                State_baby.setImageResource(R.drawable.whit);
                            }
                        }
                        else
                        {
                            State_baby.setImageResource(R.drawable.cry_baby);
                        }


                        barDataSet_2 = new BarDataSet(barEntries_2, "행복/불행 지수");

                        theDates_2.add("행복 지수");
                        theDates_2.add("불행 지수");


                        theData_2 = new BarData(theDates_2, barDataSet_2);
                        barChart_2.setData(theData_2);



                        sb.append("\n" + "    [ "+ChoiceDate+ " DB분석 ]" + "\n"+
                                "   온도:" + TemperSum / Count +
                                "   습도:" + HumiditySum / Count + "\n" +
                                "   소리:" + SoundSum / Count +
                                "   조도:" + LightSum / Count

                        );


                        text_DB.setText(sb.toString());



                        String DB_Anal="";

                        //DB분석 결과 받아오기 시작
                        try {

                            DB_Anal = new Task_get_Anal().execute().get();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                        String Anal_temp="";
                        String Anal_hum="";
                        String Anal_sound="";
                        String Anal_light="";

                        String tmpString_2 = DB_Anal;
                        int idx_start_2 = tmpString_2.indexOf("[");
                        int idx_end_2 = tmpString_2.indexOf("]");
                        String sub_Anal_DB = tmpString_2.substring(idx_start_2+1, idx_end_2);

                        try {
                            JSONObject jObject = new JSONObject(sub_Anal_DB);   // JSONArray 생성

                            Anal_temp = jObject.getString("temperature");
                            Anal_hum = jObject.getString("humidity");
                            Anal_sound = jObject.getString("sound");
                            Anal_light = jObject.getString("light");

                        }
                        catch(JSONException e)
                        {
                            e.printStackTrace();
                        }

                        double[] sensorCoff = new double[4];
                        String[] Ranksensor = new String[4];

                        sensorCoff[0] = Double.valueOf(Anal_temp).doubleValue();
                        sensorCoff[1] = Double.valueOf(Anal_hum).doubleValue();
                        sensorCoff[2] = Double.valueOf(Anal_sound).doubleValue();
                        sensorCoff[3] = Double.valueOf(Anal_light).doubleValue();

                        //값 오름 차순 정렬
                            Arrays.sort(sensorCoff);

                        StringBuffer sb_2= new StringBuffer();
                        sb_2.append("\n"+"       [ 전체 DB 분석 결과 ]"+"\n"+"\n"+ "         < 환경 예민 순위 >"+"\n");
                        //1등 순위

                            if( sensorCoff[3] == Double.valueOf(Anal_temp).doubleValue() )
                            {
                                Ranksensor[3]="온도";
                            }

                            if( sensorCoff[3] == Double.valueOf(Anal_hum).doubleValue() )
                            {
                                Ranksensor[3]="습도";
                            }
                            if( sensorCoff[3] == Double.valueOf(Anal_sound).doubleValue() )
                            {
                                Ranksensor[3]="소리";
                            }
                            if( sensorCoff[3] == Double.valueOf(Anal_light).doubleValue() )
                            {
                                Ranksensor[3]="조도";
                            }

                            sb_2.append("       1위: "+ Ranksensor[3]);

//                            if( sensorCoff[3]> 0.0 )
//                                sb_2.append("\n: "+"높으면 아이가 싫어해요"+"\n");
//                            else
//                                sb_2.append("\n: "+"낮으면 아이가 싫어해요"+"\n");

                        //2등순위

                            if( sensorCoff[2] == Double.valueOf(Anal_temp).doubleValue() )
                            {
                                Ranksensor[2]="온도";
                            }

                            if( sensorCoff[2] == Double.valueOf(Anal_hum).doubleValue() )
                            {
                                Ranksensor[2]="습도";
                            }
                            if( sensorCoff[2] == Double.valueOf(Anal_sound).doubleValue() )
                            {
                                Ranksensor[2]="소리";
                            }
                            if( sensorCoff[2] == Double.valueOf(Anal_light).doubleValue() )
                            {
                                Ranksensor[2]="조도";
                            }

                            sb_2.append("   2위: "+ Ranksensor[2]+"\n");

//                            if( sensorCoff[3] > 0.0 )
//                                sb_2.append("\n: "+"높으면 아이가 싫어해요"+"\n");
//                            else
//                                sb_2.append("\n: "+"낮으면 아이가 싫어해요"+"\n");


                        //3등 순위

                            if( sensorCoff[1] == Double.valueOf(Anal_temp).doubleValue() )
                            {
                                Ranksensor[1]="온도";
                            }

                            if( sensorCoff[1] == Double.valueOf(Anal_hum).doubleValue() )
                            {
                                Ranksensor[1]="습도";
                            }
                            if( sensorCoff[1] == Double.valueOf(Anal_sound).doubleValue() )
                            {
                                Ranksensor[1]="소리";
                            }
                            if( sensorCoff[1] == Double.valueOf(Anal_light).doubleValue() )
                            {
                                Ranksensor[1]="조도";
                            }

                            sb_2.append("       3위: "+ Ranksensor[1]);

//                            if( sensorCoff[3] > 0.0 )
//                                sb_2.append("\n: "+"높으면 아이가 싫어해요"+"\n");
//                            else
//                                sb_2.append("\n: "+"낮으면 아이가 싫어해요"+"\n");


                        //4등 순위
                            if( sensorCoff[0] == Double.valueOf(Anal_temp).doubleValue() )
                            {
                                Ranksensor[0]="온도";
                            }

                            if( sensorCoff[0] == Double.valueOf(Anal_hum).doubleValue() )
                            {
                                Ranksensor[0]="습도";
                            }
                            if( sensorCoff[0] == Double.valueOf(Anal_sound).doubleValue() )
                            {
                                Ranksensor[0]="소리";
                            }
                            if( sensorCoff[0] == Double.valueOf(Anal_light).doubleValue() )
                            {
                                Ranksensor[0]="조도";
                            }

                            sb_2.append("   4위: "+ Ranksensor[0]+"\n");

                            sb_2.append("    -> "+Ranksensor[3]+"에 가장 예민해요!"+"\n");


//                            if( sensorCoff[3] > 0.0 )
//                                sb_2.append("\n: "+"높으면 아이가 싫어해요");
//                            else
//                                sb_2.append("\n: "+"낮으면 아이가 싫어해요");

                        sb_2.append("\n"+"              < 피드백 >"+"\n");

                        if(Ranksensor[3] =="소리" || Ranksensor[2] == "소리")
                            sb_2.append(" ※"+"소음을 싫어 합니다"+"\n"+" -> 외출 시 조용하게 해주세요"+"\n");

                        if(Ranksensor[3] =="온도" || Ranksensor[2] == "온도")
                            sb_2.append(" ※"+"더위를 싫어 합니다"+"\n"+" -> 외출 시 선풍기 틀어주세요"+"\n");

                        if(Ranksensor[3] =="습도" || Ranksensor[2] == "습도")
                            sb_2.append(" ※"+"습한 것을 싫어 합니다"+"\n"+" -> 습한 날을 피해 주세요"+"\n");

                        if(Ranksensor[3] =="조도" || Ranksensor[2] == "조도")
                            sb_2.append("※"+"밝은 것을 싫어 합니다"+"\n"+" -> 외출 시 햇빛가리개 필요"+"\n");



                        text_Anal.setText(sb_2.toString());

                    } else {

                        long now = System.currentTimeMillis();
                        Date date = new Date(now);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String getTime = sdf.format(date);
                        show3("아이와 여행 간 날이 아닙니다");
                        text_DB.setText("\n" +" "+ ChoiceDate + "\n" + " 여행 날짜가 아닙니다");
//getTime.toString()+"(오늘 날짜)까지 선택 가능합니다"
                        barEntries_2.clear();
                        theDates_2.clear();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }


    public void show3(String baby_state) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("팝업 알림");
        //타이틀설정
        String tv_text = baby_state;
        builder.setMessage(tv_text);

        //내용설정

        builder.setNegativeButton("읽음",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "읽기완료", Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }


    public class Task_get_DB extends AsyncTask<String, Void, String> {

        private String str, receiveMsg;
        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            try {

                url = new URL("http://ec2-3-15-212-160.us-east-2.compute.amazonaws.com/SendAndroid.php");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);                         // 서버에서 읽기 모드 지정

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

            return receiveMsg;
        }


    }

    public class Task_get_Anal extends AsyncTask<String, Void, String> {

        public String str, receiveMsg;
        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            try {

                url = new URL("http://ec2-3-15-212-160.us-east-2.compute.amazonaws.com/SendAnal.php");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);                         // 서버에서 읽기 모드 지정

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
            return receiveMsg;
        }
    }
}












