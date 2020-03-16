package com.recipeworld.mosqueapp;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> downServers = new ArrayList<>();

    ArrayList<String> col0 = new ArrayList<>();
    ArrayList<String> col1 = new ArrayList<>();
    ArrayList<String> col2 = new ArrayList<>();


    TextView tvFajrAzan, tvFajrIqamah;
    TextView tvDuhrAzan, tvDuhrIqamah;
    TextView tvAsrAzan, tvAsrIqamah;
    TextView tvMaghribAzan, tvMaghribIqamah;
    TextView tvIshaAzan, tvIshaIqamah;
    TextView tvJumaAzan, tvJumaIqamah;
    TextView Note;


    private Button getBtn;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = (TextView) findViewById(R.id.result);
        getBtn = (Button) findViewById(R.id.getBtn);

        //calling the website by default
        getWebsite();

        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWebsite();
            }
        });
    }

    private void getWebsite() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                try {
                    Document doc = Jsoup.connect("https://www.masjidmadeena.com").get();
                    Element tableParent = doc.getElementsByClass("border").get(0); //select the first table.
                    Element table = tableParent.getElementsByTag("tbody").get(0);
//                    Log.d("doc",String.valueOf(table));


                    Elements rows = table.select("tr");


                    for (int i = 2; i < rows.size(); i++) { //first row is the col names so skip it.
                        Element row = rows.get(i);
                        Elements cols = row.select("td");

                        for (int k = 0; k < cols.size(); k++) {
                            if (k == 0)
                                col0.add(cols.get(k).text());
                            if (k == 1)
                                col1.add(cols.get(k).text());
                            if (k == 2)
                                col2.add(cols.get(k).text());
                        }

                        downServers.add(cols.text());

                    }

                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String ss = "";
                        for (String x : downServers
                        ) {
                            ss = ss + "\n" + x;
                        }
                        result.setText(ss);

                        Log.d("xx", String.valueOf(col0.size()));
                        Log.d("xx", String.valueOf(col1.size()));
                        Log.d("xx", String.valueOf(col2.size()));


                        for (String xxs : col0) {
                            Log.d("xx", xxs);
                            Log.d("xx", "\n");
                        }
                        for (String xxs : col1) {
                            Log.d("xx", xxs);
                            Log.d("xx", "\n");
                        }
                        for (String xxs : col2) {
                            Log.d("xx", xxs);
                            Log.d("xx", "\n");
                        }



                    }
                });
            }
        }).start();
    }


    public void setToStorage() {

    }

    public void renderFromStorage() {

    }

}