package com.recipeworld.mosqueapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity{

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

        //calling the website by default

        getWebsite();

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
            getWebsite();
            Toast.makeText(this, "Syncing online...", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}