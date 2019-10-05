package com.example.android.bettingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import betClasses.Odds;

public class GainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gain);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ConstraintLayout gainLayout = findViewById(R.id.gainLayout);
        for( int i = 0; i < gainLayout.getChildCount(); i++ ) {
            editTextOddsList.add((EditText) gainLayout.getChildAt(i));
            if (i < lengthOddsList) {
                editTextOddsList.get(i).setVisibility(View.VISIBLE);
            } else {
                editTextOddsList.get(i).setVisibility(View.INVISIBLE);
            }
        }
    }
    ArrayList<EditText> editTextOddsList = new ArrayList<>();

    int lengthOddsList = 3;

    public void add(View v) {
        editTextOddsList.get(lengthOddsList).setVisibility(View.VISIBLE);
        lengthOddsList++;
        if (lengthOddsList == 6) {
            findViewById(R.id.addButton).setVisibility(View.INVISIBLE);
        }
        if (lengthOddsList == 2) {
            findViewById(R.id.removeButton).setVisibility(View.VISIBLE);
        }
    }

    public void remove(View v) {
        lengthOddsList--;
        if (lengthOddsList == 5) {
            findViewById(R.id.addButton).setVisibility(View.VISIBLE);
        }
        if (lengthOddsList == 1) {
            findViewById(R.id.removeButton).setVisibility(View.INVISIBLE);
        }
        editTextOddsList.get(lengthOddsList).setVisibility(View.INVISIBLE);
    }


    public void gain(View v) {
        ArrayList<Double> listOdds = new ArrayList<>();
        double odd;
        boolean warningDisplayed = false;
        for (EditText et : editTextOddsList) {
            if (et.getVisibility() == View.INVISIBLE) {
                break;
            }
            try {
                odd = Double.parseDouble(et.getText().toString());
                if (odd>0) {
                    listOdds.add(odd);
                }
                else {
                    throw new NumberFormatException();
                }
            }
            catch (NumberFormatException e) {
                if (!warningDisplayed) {
                    Toast.makeText(this, "Champs vides ou nuls ignorés", Toast.LENGTH_SHORT).show();
                    warningDisplayed = true;
                }
            }
        }
        Odds odds = new Odds(listOdds);
        ((TextView) findViewById(R.id.gainResult)).setText(Double.toString(odds.gain()));
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
