package com.recipeworld.mosqueapp;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ArrayList<String> col0 = new ArrayList<>();
    ArrayList<String> col1 = new ArrayList<>();
    ArrayList<String> col2 = new ArrayList<>();

    int init=0;

    TextView tvFajrAzan, tvFajrIqamah;
    TextView tvDuhrAzan, tvDuhrIqamah;
    TextView tvAsrAzan, tvAsrAzanHanafi, tvAsrIqamah;
    TextView tvMaghribAzan, tvMaghribIqamah;
    TextView tvIshaAzan, tvIshaIqamah;
    TextView tvJumaAzan, tvJumaIqamah;
    TextView tvNote, tvSunrise;

    // Spinner element
    Spinner fajrSpinner, duhrSpinner, asrSpinner,
            maghribSpinner, ishaSpinner, jumaSpinner;


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


        fajrSpinner = (Spinner) findViewById(R.id.fajr_spinner);
        duhrSpinner = (Spinner) findViewById(R.id.duhr_spinner);
        asrSpinner = (Spinner) findViewById(R.id.asr_spinner);
        maghribSpinner = (Spinner) findViewById(R.id.maghrib_spinner);
        ishaSpinner = (Spinner) findViewById(R.id.isha_spinner);
        jumaSpinner = (Spinner) findViewById(R.id.juma_spinner);

        // Spinner click listener
        fajrSpinner.setOnItemSelectedListener(this);
        duhrSpinner.setOnItemSelectedListener(this);
        asrSpinner.setOnItemSelectedListener(this);
        maghribSpinner.setOnItemSelectedListener(this);
        ishaSpinner.setOnItemSelectedListener(this);
        jumaSpinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("5 min");
        categories.add("10 min");
        categories.add("15 min");
        categories.add("20 min");
        categories.add("30 min");
        categories.add("1 hr");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        fajrSpinner.setAdapter(dataAdapter);
        duhrSpinner.setAdapter(dataAdapter);
        asrSpinner.setAdapter(dataAdapter);
        maghribSpinner.setAdapter(dataAdapter);
        ishaSpinner.setAdapter(dataAdapter);
        jumaSpinner.setAdapter(dataAdapter);


        //calling the website by default

        getWebsite();

        renderFromStorage();

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


                    }

                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


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

                        setToStorage(col0, col1, col2);

                        if(init!=0)
                        Toast.makeText(MainActivity.this, "Up to date!", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }).start();
    }


    public void setToStorage(ArrayList<String> col0, ArrayList<String> col1, ArrayList<String> col2) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("fajr_azan", col1.get(0));
        editor.putString("SUNRISE", col1.get(1));
        editor.putString("duhr_azan", col1.get(2));
        editor.putString("asr_azan", col1.get(3));
        editor.putString("asr_hanafi_azan", col1.get(4));
        editor.putString("maghrib_azan", col1.get(5));
        editor.putString("isha_azan", col1.get(6));
        editor.putString("juma_azan", col1.get(7));


        editor.putString("fajr_iqamah", col2.get(0));
        editor.putString("duhr_iqamah", col2.get(1));
        editor.putString("asr_iqamah", col2.get(2));
        editor.putString("maghrib_iqamah", col2.get(3));
        editor.putString("isha_iqamah", col2.get(4));
        editor.putString("juma_iqamah", col2.get(5));

        editor.putString("note", col0.get(col0.size() - 1));
        editor.apply();


        //now render
        renderFromStorage();
    }

    public void renderFromStorage() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String fajrAzan = preferences.getString("fajr_azan", "");
        String sunrise = preferences.getString("SUNRISE", "");
        String duhrAzan = preferences.getString("duhr_azan", "");
        String asrAzan = preferences.getString("asr_azan", "");
        String asrHanafiAzan = preferences.getString("asr_hanafi_azan", "");
        String maghribAzan = preferences.getString("maghrib_azan", "");
        String ishaAzan = preferences.getString("isha_azan", "");
        String jumaAzan = preferences.getString("juma_azan", "");

        String fajrIqamah = preferences.getString("fajr_iqamah", "");
        String duhrIqamah = preferences.getString("duhr_iqamah", "");
        String asrIqamah = preferences.getString("asr_iqamah", "");
        String maghribIqamah = preferences.getString("maghrib_iqamah", "");
        String ishaIqamah = preferences.getString("isha_iqamah", "");
        String jumaIqamah = preferences.getString("juma_iqamah", "");

        String note = preferences.getString("note", "");


        int fajrAlert = preferences.getInt("fajrAlert", 2);
        int duhrAlert = preferences.getInt("duhrAlert", 2);
        int asrAlert = preferences.getInt("asrAlert", 2);
        int maghribAlert = preferences.getInt("maghribAlert", 2);
        int ishaAlert = preferences.getInt("ishaAlert", 2);
        int jumaAlert = preferences.getInt("jumaAlert", 2);


        if (TextUtils.isEmpty(fajrAzan)) {
            //so things are not previously called; call the server
            Toast.makeText(this, "Downloading data...", Toast.LENGTH_SHORT).show();
            init++;
            getWebsite();
        } else {
            //now setting things here cz we have data

            tvFajrAzan.setText(fajrAzan);
            tvDuhrAzan.setText(duhrAzan);
            tvAsrAzan.setText(asrAzan);
            tvAsrAzanHanafi.setText(asrHanafiAzan);
            tvMaghribAzan.setText(maghribAzan);
            tvIshaAzan.setText(ishaAzan);
            tvJumaAzan.setText(jumaAzan);

            tvSunrise.setText("SUNRISE: " + sunrise);

            tvFajrIqamah.setText(fajrIqamah);
            tvDuhrIqamah.setText(duhrIqamah);
            tvAsrIqamah.setText(asrIqamah);
            tvMaghribIqamah.setText(maghribIqamah);
            tvIshaIqamah.setText(ishaIqamah);
            tvJumaIqamah.setText(jumaIqamah);

            tvNote.setText(note);

            //setting the selector view
            fajrSpinner.setSelection(fajrAlert, true);
            duhrSpinner.setSelection(duhrAlert, true);
            asrSpinner.setSelection(asrAlert, true);
            maghribSpinner.setSelection(maghribAlert, true);
            ishaSpinner.setSelection(ishaAlert, true);
            jumaSpinner.setSelection(jumaAlert, true);

        }

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
            getWebsite();
            init++;
            Toast.makeText(this, "Syncing online...", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        int ids = parent.getId();
        if (ids == R.id.fajr_spinner) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("fajrAlert", position);
            editor.apply();
        } else if (ids == R.id.duhr_spinner) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("duhrAlert", position);
            editor.apply();
        } else if (ids == R.id.asr_spinner) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("asrAlert", position);
            editor.apply();
        } else if (ids == R.id.maghrib_spinner) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("maghribAlert", position);
            editor.apply();
        } else if (ids == R.id.isha_spinner) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("ishaAlert", position);
            editor.apply();
        } else if (ids == R.id.juma_spinner) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("jumaAlert", position);
            editor.apply();
        } else {

        }

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}