package com.example.gopikrishnan.codeforces;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        onclick();
    }

    public  String response = null;
    ImageView image;
    public void onclick(){

        final EditText editText = (EditText) findViewById(R.id.editText);
        final TextView textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setMovementMethod(new ScrollingMovementMethod());
        Button button = (Button) findViewById(R.id.button);
        image = (ImageView) findViewById(R.id.imageView3);

        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Asynctaskrunner().execute("http://codeforces.com/api/user.info?handles=".concat((String) editText.getText().toString()));

                        JSONArray array = null;
                        while((response==null))
                        {

                        }

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
                                String lname = jsonObject.getString("lastName");
                                String country = jsonObject.getString("country");
                                String lonline = jsonObject.getString("lastOnlineTimeSeconds");
                                String rating = jsonObject.getString("rating");
                                String friends = jsonObject.getString("friendOfCount");
                                String pic = jsonObject.getString("titlePhoto");
                                String fname = jsonObject.getString("firstName");
                                String contribution = jsonObject.getString("contribution");
                                String organization = jsonObject.getString("organization");
                                String rank = jsonObject.getString("rank");
                                String maxrate = jsonObject.getString("maxRating");
                                String regtime = jsonObject.getString("registrationTimeSeconds");
                                String maxRank = jsonObject.getString("maxRank");

                                new imagerunner().execute(pic);
                               
                                int d = Integer.parseInt(regtime);
                                d+=19800;
                                Date date1 = new Date(d * 1000L);
                                DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
                                String formatted = format.format(date1);

                                int lo = Integer.parseInt(lonline);
                                lo+=19800;
                                Date date2 = new Date(lo * 1000L);
                                DateFormat format1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                format1.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
                                String formatted1 = format.format(date2);

                                    sb = sb.append("NAME :").append(fname).append(lname).append('\n').append("LASTONLINE :").append(formatted1).append(" (IN INDIAN TIME)").append('\n').append("REGISTRATIONTIME :").append(formatted).append(" (IN INDIAN TIME)").append('\n').append("COUNTRY :").append(country).append('\n').append("RATING :").append(rating).append('\n').append("MAXIMUMRATING :").append(maxrate).append('\n').append("RANK :").append(rank).append('\n').append("MAXIMUMRANK :").append(maxRank).append('\n').append("ORGANIZATION :").append(organization).append('\n').append("CONTRIBUTION :").append(contribution).append('\n').append("NO OF FRIENDS :").append(friends).append('\n').append('\n').append('\n');


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        textView2.setText(sb.toString());

                    }
                }
        );

}



    private class Asynctaskrunner extends AsyncTask<String,Void,Void> {
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


        private class imagerunner extends AsyncTask<String,Void,Bitmap>
        {
            @Override
            protected Bitmap doInBackground(String... strings) {
                Bitmap bit = null;
                String url = strings[0];

                try {
                    InputStream input= new URL(url).openStream();
                    bit= BitmapFactory.decodeStream(input);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return bit;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                image.setImageBitmap(bitmap);
            }
        }


}
