package com.example.android.bettingapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import Parser.Page;
import Parser.Sport;
import betClasses.Odds;

public class ParserActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView result;
    SharedPreferences sharedPref;
    ArrayList<ItemSpinner> itemSpinners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parser);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        result = findViewById(R.id.result);
        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);

        String[] select_qualification = {
                "Sport", "football", "tennis", "rugby", "basketball", "handball", "volleyball",
                "hockey sur glace", "boxe"};
        Spinner spinner = findViewById(R.id.spinner);

        itemSpinners = new ArrayList<>();

        for (int i = 0; i < select_qualification.length; i++) {
            ItemSpinner itemSpinner = new ItemSpinner();
            itemSpinner.setString(select_qualification[i]);
            itemSpinner.setSelected(false);
            itemSpinners.add(itemSpinner);
        }
        MyAdapter myAdapter = new MyAdapter(getApplicationContext(), 0,
                itemSpinners);
        spinner.setAdapter(myAdapter);
    }

    public void checkConn(View v) {
        ConnectivityManager check = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = check.getAllNetworkInfo();
        for (NetworkInfo anInfo : info) {
            if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                Toast.makeText(this, "Internet is connected", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void parsing(View v) throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                final StringBuilder builder = new StringBuilder();
                ArrayList<String> selectedSports = new ArrayList<>();
                ArrayList<String> urls;
                HashMap<String, HashMap<String, Odds>> parse = new HashMap<>();
                for (ItemSpinner item : itemSpinners) {
                    if (item.isSelected()) {
                        selectedSports.add(item.getString());
                    }
                }
                Sport sport;
                for (String sportString : selectedSports) {
                    sport = new Sport(sportString);
                    urls = sport.getURLs();
                    NotificationManager notif = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification notify = null;
                    for (String url : urls) {
                        Page page = new Page(url);
                        page.parse();
                        parse.putAll(page.parse());
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            if (!page.getSurebets().isEmpty()) {
                                notify = new Notification.Builder(getApplicationContext()).setContentTitle("Surebets").setContentText("" + page.getSurebets()).setSmallIcon(R.drawable.ic_menu_camera).build();
                                notify.flags |= Notification.FLAG_AUTO_CANCEL;
                                notif.notify(0, notify);
                            }
                        }
                        sharedPref.edit().putString("Odds " + page.getSport() + " " + page.getCompetitionName(), gson.toJson(parse)).apply();
                        sharedPref.edit().putString("Matches " + page.getSport() + " " + page.getCompetitionName(), gson.toJson(page.getMatches())).apply();
                    }
                }
                builder.append(parse);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        result.setText(builder.toString());
                    }
                });
            }
        }).start();
    }


    public void simpleParsing(View v) throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                final StringBuilder builder = new StringBuilder();
                String url = "http://www.comparateur-de-cotes.fr/comparateur/football/France-Ligue-1-ed3";
                Page page = new Page(url);
                HashMap<String, HashMap<String, Odds>> parse = page.parse();
                builder.append(parse);
                sharedPref.edit().putString("Odds "+page.getSport()+" "+page.getCompetitionName(), gson.toJson(parse)).apply();
                sharedPref.edit().putString("Matches "+page.getSport()+" "+page.getCompetitionName(), gson.toJson(page.getMatches())).apply();
                NotificationManager notif = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notify = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    if (!page.getSurebets().isEmpty()) {
                        notify = new Notification.Builder(getApplicationContext()).setContentTitle("Surebets").setContentText(""+page.getSurebets()).setSmallIcon(R.drawable.ic_menu_camera).build();
                        notify.flags |= Notification.FLAG_AUTO_CANCEL;
                        notif.notify(0, notify);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        result.setText(builder.toString());
                    }
                });
            }
        }).start();
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.gain) {
            Intent myIntent = new Intent(getApplicationContext(), GainActivity.class);
            startActivityForResult(myIntent, 0);
        } else if (id == R.id.parser) {
            Intent myIntent = new Intent(getApplicationContext(), ParserActivity.class);
            startActivityForResult(myIntent, 0);
        } else if (id == R.id.cashback) {
            Intent myIntent = new Intent(getApplicationContext(), CashbackActivity.class);
            startActivityForResult(myIntent, 0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
