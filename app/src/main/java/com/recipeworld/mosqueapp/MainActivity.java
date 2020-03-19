package com.recipeworld.mosqueapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ArrayList<String> col0 = new ArrayList<>();
    ArrayList<String> col1 = new ArrayList<>();
    ArrayList<String> col2 = new ArrayList<>();

    TextView tvFajrAzan, tvFajrIqamah;
    TextView tvDuhrAzan, tvDuhrIqamah;
    TextView tvAsrAzan, tvAsrAzanHanafi, tvAsrIqamah;
    TextView tvMaghribAzan, tvMaghribIqamah;
    TextView tvIshaAzan, tvIshaIqamah;
    TextView tvJumaAzan, tvJumaIqamah;
    TextView tvNote, tvSunrise;

    ProgressBar pb;

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvFajrAzan = (TextView) findViewById(R.id.fajr_azan);
        tvFajrIqamah = (TextView) findViewById(R.id.fajr_iqamah);
        tvDuhrAzan = (TextView) findViewById(R.id.duhr_azan);
        tvDuhrIqamah = (TextView) findViewById(R.id.duhr_iqamah);
        tvAsrAzan = (TextView) findViewById(R.id.asr_azan);
        tvAsrAzanHanafi = (TextView) findViewById(R.id.asr_azan_hanafi);
        tvAsrIqamah = (TextView) findViewById(R.id.asr_iqamah);
        tvMaghribAzan = (TextView) findViewById(R.id.maghrib_azan);
        tvMaghribIqamah = (TextView) findViewById(R.id.maghrib_iqamah);
        tvIshaAzan = (TextView) findViewById(R.id.isha_azan);
        tvIshaIqamah = (TextView) findViewById(R.id.isha_iqamah);
        tvJumaAzan = (TextView) findViewById(R.id.juma_azan);
        tvJumaIqamah = (TextView) findViewById(R.id.juma_iqamah);
        tvNote = (TextView) findViewById(R.id.note);
        tvSunrise = (TextView) findViewById(R.id.sunrise);

        pb = (ProgressBar) findViewById(R.id.pb);

        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner_mosque);
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Masjeed Madeena");
        categories.add("Sammamaish");
        categories.add("Mapsred");
        categories.add("Eastside mosque");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        //calling the website by default
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int mosqueKey = preferences.getInt("mosque", 0);

        if (mosqueKey == 0) {
            spinner.setSelection(mosqueKey,true);
            getWebsite0();
        }
        if (mosqueKey == 1) {
            spinner.setSelection(mosqueKey,true);
            getWebsite1();
        }
        if (mosqueKey == 2) {
            spinner.setSelection(mosqueKey,true);
            getWebsite2();
        }
        if (mosqueKey == 3) {
            spinner.setSelection(mosqueKey,true);
            getWebsite3();
        }
    }


    //masjeedmadeena
    private void getWebsite0() {
        pb.setVisibility(View.VISIBLE);
        //clearing prev data
        col0 = new ArrayList<>();
        col1 = new ArrayList<>();
        col2 = new ArrayList<>();


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


                    }

                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pb.setVisibility(View.INVISIBLE);

                        tvFajrAzan.setText(col1.get(0));
                        tvSunrise.setText("SUNRISE: " + col1.get(1));
                        tvDuhrAzan.setText(col1.get(2));
                        tvAsrAzan.setText(col1.get(3));
                        tvAsrAzanHanafi.setText(col1.get(4));
                        tvMaghribAzan.setText(col1.get(5));
                        tvIshaAzan.setText(col1.get(6));
                        tvJumaAzan.setText(col1.get(7));
                        tvFajrIqamah.setText(col2.get(0));
                        tvDuhrIqamah.setText(col2.get(1));
                        tvAsrIqamah.setText(col2.get(2));
                        tvMaghribIqamah.setText(col1.get(3));
                        tvIshaIqamah.setText(col1.get(4));
                        tvJumaIqamah.setText(col1.get(5));
                        tvNote.setText(col0.get(col0.size() - 1));

                        tvNote.setVisibility(View.VISIBLE);

                    }
                });
            }
        }).start();
    }


    //mapsredmond
    private void getWebsite2() {
//clearing prev data
        col0 = new ArrayList<>();
        col1 = new ArrayList<>();
        col2 = new ArrayList<>();

        pb.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                try {
                    Document doc = Jsoup.connect("https://www.mapsredmond.org/").get();
                    Element table = doc.getElementsByTag("tbody").get(0);
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

                    }

                    Log.d("rr", String.valueOf(col0.size()));
                    for (String x : col0)
                        Log.d("rr", x);
                    Log.d("rr", String.valueOf(col1.size()));
                    for (String x : col1)
                        Log.d("rr", x);
                    Log.d("rr", String.valueOf(col2.size()));
                    for (String x : col2)
                        Log.d("rr", x);

                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pb.setVisibility(View.INVISIBLE);

                        tvFajrAzan.setText(col0.get(0));
                        tvSunrise.setText("SUNRISE: " + col0.get(1));
                        tvDuhrAzan.setText(col0.get(2));
                        tvAsrAzan.setText(col0.get(3));
                        tvAsrAzanHanafi.setText(col0.get(3));
                        tvMaghribAzan.setText(col0.get(4));
                        tvIshaAzan.setText(col0.get(5));
                        tvJumaAzan.setText(col0.get(6));

                        tvFajrIqamah.setText(col1.get(0));
                        tvDuhrIqamah.setText(col1.get(1));
                        tvAsrIqamah.setText(col1.get(2));
                        tvMaghribIqamah.setText(col1.get(3));
                        tvIshaIqamah.setText(col1.get(4));

                        if (col1.size() > 5)
                            tvJumaIqamah.setText(col1.get(5));


                        tvNote.setVisibility(View.GONE);
                    }
                });
            }
        }).start();
    }


    //sammamaish
    private void getWebsite1() {
//clearing prev data
        col0 = new ArrayList<>();
        col1 = new ArrayList<>();
        col2 = new ArrayList<>();

        pb.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                try {
                    Document doc = Jsoup.connect("    http://www.muslimfeed.com/timesframe.aspx?mi=2110&bc=FFFFFF&fc=000000&oc=FFFFFF").get();
                    Element table = doc.getElementsByTag("tbody").get(0);
//                    Log.d("doc",String.valueOf(table));

                    Elements rows = table.select("tr");

                    for (int i = 1; i < rows.size(); i++) { //first row is the col names so skip it.
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

                    }

                    Log.d("rr", String.valueOf(col0.size()));
                    for (String x : col0)
                        Log.d("rr", x);
                    Log.d("rr", String.valueOf(col1.size()));
                    for (String x : col1)
                        Log.d("rr", x);
                    Log.d("rr", String.valueOf(col2.size()));
                    for (String x : col2)
                        Log.d("rr", x);

                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pb.setVisibility(View.INVISIBLE);

                        tvFajrAzan.setText(col0.get(0));
                        tvSunrise.setText("SUNRISE: " + col0.get(1));
                        tvDuhrAzan.setText(col0.get(2));
                        tvAsrAzan.setText(col0.get(3));
                        tvAsrAzanHanafi.setText(col0.get(3));
                        tvMaghribAzan.setText(col0.get(5));
                        tvIshaAzan.setText(col0.get(6));
                        tvJumaAzan.setText(col0.get(7));

                        tvFajrIqamah.setText(col1.get(0));
                        tvDuhrIqamah.setText(col1.get(2));
                        tvAsrIqamah.setText(col1.get(3));
                        //COL1-4 IS SUNSET HERE
                        tvMaghribIqamah.setText(col1.get(5));
                        tvIshaIqamah.setText(col1.get(6));
                        tvJumaIqamah.setText(col1.get(7));


                        tvNote.setVisibility(View.GONE);
                    }
                });
            }
        }).start();
    }


    //east side mosque
    private void getWebsite3() {
        pb.setVisibility(View.VISIBLE);

        //clearing prev data
        col0 = new ArrayList<>();
        col1 = new ArrayList<>();
        col2 = new ArrayList<>();

        final ArrayList<String> nt = new ArrayList<>();


        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                try {
                    Document doc = Jsoup.connect("https://www.eastsidemosque.com/").get();
                    Element table = doc.getElementsByTag("tbody").get(0);


                    Elements rows = table.select("tr");


                    for (int i = 0; i < rows.size(); i++) { //first row is the col names so skip it.
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
                    }


//juma is on table2 so catch that too
                    Element table2 = doc.getElementsByTag("tbody").get(1);

                    Elements rows2 = table2.select("tr");


                    for (int i = 0; i < rows2.size(); i++) { //first row is the col names so skip it.
                        Element row = rows2.get(i);
                        Elements cols = row.select("td");

                        for (int k = 0; k < cols.size(); k++) {
                            if (k == 0)
                                nt.add(cols.get(k).text());
                            if (k == 1)
                                nt.add(cols.get(k).text());
                        }
                    }

                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pb.setVisibility(View.INVISIBLE);


                        Log.d("rr", String.valueOf("==================="));
                        for (String x : col0)
                            Log.d("rr", x);
                        Log.d("rr", String.valueOf("==================="));
                        for (String x : col1)
                            Log.d("rr", x);
                        Log.d("rr", String.valueOf("==================="));
                        for (String x : col2)
                            Log.d("rr", x);

                        tvFajrAzan.setText(col1.get(0));
                        tvSunrise.setText("SUNRISE: " + col1.get(1));
                        tvDuhrAzan.setText(col1.get(2));
                        tvAsrAzan.setText(col1.get(3));
                        tvAsrAzanHanafi.setText(col1.get(4));
                        tvMaghribAzan.setText(col1.get(5));
                        tvIshaAzan.setText(col1.get(6));
                        tvJumaAzan.setText(" -- ");

                        tvFajrIqamah.setText(col2.get(0));
                        tvDuhrIqamah.setText(col2.get(1));
                        tvAsrIqamah.setText(col2.get(2));
                        tvMaghribIqamah.setText(col2.get(4));
                        tvIshaIqamah.setText(col2.get(5));
                        tvJumaIqamah.setText("see note");

                        tvNote.setText("---- JUMA IQAMAH ----" + "\n" + nt.get(0) + " : " + nt.get(1) + "\n" +
                                nt.get(2) + " : " + nt.get(3));

                        tvNote.setVisibility(View.VISIBLE);

                    }
                });
            }
        }).start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            int mosqueKey = preferences.getInt("mosque", 0);


            if (mosqueKey == 0) getWebsite0();
            if (mosqueKey == 1) getWebsite1();
            if (mosqueKey == 2) getWebsite2();
            if (mosqueKey == 3) getWebsite3();


            Toast.makeText(this, "Syncing online...", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (position == 0) {

            getWebsite0();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("mosque", 0);
            editor.apply();
        }
        if (position == 1) {

            getWebsite1();


            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("mosque", 1);
            editor.apply();
        }
        if (position == 2) {

            getWebsite2();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("mosque", 2);
            editor.apply();
        }
        if (position == 3) {

            getWebsite3();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("mosque", 3);
            editor.apply();
        } else {
            //do nothing
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}