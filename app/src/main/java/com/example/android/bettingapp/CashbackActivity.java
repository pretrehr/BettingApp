package com.example.android.bettingapp;

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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import betClasses.Match;
import betClasses.Odds;

public class CashbackActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashback);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
    }

    public void bestMatchCashback(View v) {
        String siteString = "betclic";
        double minimumOdd = 1.1;
        double bet = 10;
        boolean freebet = true;
        double combiMax = 0;
        double combiOdd = 1;
        double rateCashback = 1;
        Date dateMin = null;
        Date dateMax = null;
        Odds oddsSite;
        Odds bestOdds;
        Odds odds;
        Odds oddsToCheck;
        Odds bestOverallOdds = null;
        double profit;
        String bestMatch = "";
        String[] bestSites = new String[3];
        String[] sites = new String[3];
        String serializedHashMap =  sharedPref.getString("Football France Ligue 1", "Not found");
        Gson gson = new Gson();
        java.lang.reflect.Type type = new TypeToken<HashMap<String, HashMap<String, Odds>>>(){}.getType();
        int a= 1;
        HashMap<String, HashMap<String, Odds>> allOdds = gson.fromJson(serializedHashMap, type);
        double bestProfit = -bet;
        int bestRank = 0;
        for (String match : allOdds.keySet()) {
            if (allOdds.get(match).containsKey(siteString)) {
//                    && (dateMax == null || match.getDate().compareTo(dateMax) <= 0)
//                    && (dateMin == null || match.getDate().compareTo(dateMin) >= 0)) {
                oddsSite = allOdds.get(match).get(siteString);
                bestOdds = new Odds(oddsSite);
                bestSites[0] = siteString;
                bestSites[1] = siteString;
                bestSites[2] = siteString;
                for (String site : allOdds.get(match).keySet()) {
                    odds = allOdds.get(match).get(site);
                    for (int i = 0; i<3; i++) {
                        if (odds.get(i) > bestOdds.get(i)) {
                            bestOdds.getOddsList().set(i, odds.get(i));
                            bestSites[i] = site;
                        }
                    }
                    for (int i = 0; i<3; i++) {
                        ArrayList<Double> oddsToCheckList = new ArrayList<>();
                        for (int j = 0; j<i; j++) {
                            oddsToCheckList.add(bestOdds.get(j));
                        }
                        oddsToCheckList.add(combiOdd*oddsSite.get(i)*(1+combiMax)-combiMax);
                        for (int j = i+1; j<3; j++) {
                            oddsToCheckList.add(bestOdds.get(j));
                        }
                        oddsToCheck = new Odds(oddsToCheckList);
                        if (oddsToCheck.get(i) >= minimumOdd) {
                            profit = oddsToCheck.pariRembourseSiPerdant(bet, i, freebet, rateCashback);
                            if (profit > bestProfit) {
                                bestRank = i;
                                bestProfit = profit;
                                bestMatch = match;
                                bestOverallOdds = new Odds(oddsToCheck);
                                for (int j = 0; j<i; j++) {
                                    sites[j] = bestSites[j];
                                }
                                sites[i] = site;
                                for (int j = i+1; j<3; j++) {
                                    sites[j] = bestSites[j];
                                }
                            }
                        }
                    }
                }
            }
        }
        Toast.makeText(this, bestMatch, Toast.LENGTH_LONG).show();
        Toast.makeText(this, ""+bestOverallOdds.pariRembourseSiPerdant(bet, bestRank, freebet, rateCashback), Toast.LENGTH_LONG).show();
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
