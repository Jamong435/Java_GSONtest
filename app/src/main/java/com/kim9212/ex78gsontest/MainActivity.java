package com.kim9212.ex78gsontest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tv);

    }

    public void clickbtn(View view) {
        //Gson library를 이용하여 json 문자열을 person객체를 곧바로 파싱

        String jsonStr = "{'name':'robin','age':25}";

        Gson gson = new Gson();
        Person p = gson.fromJson(jsonStr, Person.class); //json문자열->person객체로 바로옴
        tv.setText(p.name + "," + p.age);
    }

    public void clickbtn2(View view) {
        Person p = new Person("robin", 25);
        Gson gson = new Gson();
        String jsonStr = gson.toJson(p);
        tv.setText(jsonStr);
    }

    ListView listView;
    ArrayAdapter adapter;
    List<String> items = new ArrayList<>();


    public void clickbtn3(View view) {
        String str = "[{'name':'hong','age':20},{'name':'jin','age':25}]";

        Gson gson = new Gson();
        Person[] personArr = gson.fromJson(str, Person[].class);

        //배열->리스트
//        items= Arrays.asList(personArr);

        for (Person p : personArr) {
            items.add(p.name + "," + p.age);
        }
        //stop다음에 destroy를안시킨다면 start쪽으로 간다.

        listView = findViewById(R.id.lv);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
    }

    public void clickbtn4(View view) {
        final String serverUrl = "http://toki666.dothome.co.kr/test.json";
        new Thread() {
            @Override
            public void run() {

                try {
                    URL url = new URL(serverUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setUseCaches(false);
                    connection.setDoOutput(true);
                    connection.setDoInput(true);

                    InputStream is = url.openStream();
                    InputStreamReader isr = new InputStreamReader(is);

                    //Reader까지만 있으면 GSON 알아서 읽어와서 객체로 피싱해줌.
                    Gson gson = new Gson();
                    final Person p = gson.fromJson(isr, Person.class);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText(p.name + "," + p.age);
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();


    }
}