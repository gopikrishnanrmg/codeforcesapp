package com.example.gopikrishnan.codeforces;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onclick();
    }
        public  String response = null;
    public void onclick()
    {
        final TextView textview = (TextView) findViewById(R.id.textView2);
        textview.setMovementMethod(new ScrollingMovementMethod());
        Button button = (Button) findViewById(R.id.button);
        button.setClickable(false);
        button.setText("Loading");
        Asynctaskrunner run = new Asynctaskrunner();
        run.execute("http://codeforces.com/api/contest.list?gym=false");
        while((response==null))
        {

        }
        button.setClickable(true);
        button.setText("Show Contests");
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        JSONArray array = null;
                        try {
                            JSONObject json = new JSONObject(response);
                            array = json.getJSONArray("result");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        StringBuilder sb = new StringBuilder();
                        JSONObject jsonObject;
                        for (int i = 0; i < array.length(); i++) {

                            try {

                                jsonObject = array.getJSONObject(i);
                                String phase = jsonObject.getString("phase");
                                String time =jsonObject.getString("durationSeconds");
                                int hours = Integer.parseInt(time);
                                hours/=60;
                                int min = hours%60;
                                hours/=60;
                                int d = Integer.parseInt(jsonObject.getString("startTimeSeconds"));
                                d+=19800;
                                Date date1 = new Date(d * 1000L);
                                DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
                                String formatted = format.format(date1);




                                if (phase.equals("BEFORE"))
                                    sb = sb.append("NAME :").append(jsonObject.getString("name")).append('\n').append("DURATION :").append(hours).append(" Hours ").append(min).append(" minutes").append('\n').append("DATE & TIME :").append(formatted).append(" (IN INDIAN TIME)").append('\n').append('\n').append('\n');


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        textview.setText(sb.toString());

                    }
                }
        );

    }


    private class Asynctaskrunner extends AsyncTask<String,Void,Void>{
        @Override
        protected Void doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                InputStream in = new BufferedInputStream(conn.getInputStream());
                response=convertStreamToString(in);
            }catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException: " + e.getMessage());
            } catch (ProtocolException e) {
                Log.e(TAG, "ProtocolException: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
            return null;
        }
        private String convertStreamToString(InputStream in){
            String line;
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            try{
            while ((line = reader.readLine())!=null) {
            sb.append(line).append('\n');
            }
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }
        }

    }



