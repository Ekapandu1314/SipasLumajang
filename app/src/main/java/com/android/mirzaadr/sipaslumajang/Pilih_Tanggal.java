package com.android.mirzaadr.sipaslumajang;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Mirzaadr on 11/14/2015.
 */
public class Pilih_Tanggal extends ActionBarActivity {

    AlertDialogManager alert = new AlertDialogManager();
    Session session;

    DatePicker dp;

    String tanggal;
    String bulan;
    String tahun;

    public static final String TANGGAL = "TANGGAL";
    public static final String BULAN = "BULAN";
    public static final String TAHUN = "TAHUN";
    public static final String SHIFT = "SHIFT";

    Calendar c = Calendar.getInstance();

    int hours_calendar = c.get(Calendar.HOUR_OF_DAY);
    int month_calendar = c.get(Calendar.MONTH) + 1;
    int date_calendar = c.get(Calendar.DAY_OF_MONTH);
    int year_calendar = c.get(Calendar.YEAR);

    String jabatan;

    private boolean exit;

    public static Pilih_Tanggal instance = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pilih_tanggal);

        session = new Session(getApplicationContext());

        instance = Pilih_Tanggal.this;

        dp = (DatePicker) findViewById(R.id.datePicker_kepala);

        dp.init(year_calendar, month_calendar - 1, date_calendar, null);

        //Intent intent = getIntent();
        //String nama = intent.getStringExtra(MainActivity.NAME);
        //String regu = intent.getStringExtra(MainActivity.TEAM);

        TextView textView = (TextView) findViewById(R.id.textView4);

        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(Session.KEY_NAME);

        // email
        String team = user.get(Session.KEY_TEAM);

        jabatan = user.get(Session.KEY_JABATAN);

       //namaxx = name;

        String nama[] = name.split("'");
        //String tim[] = team.split("'");

        textView.setText(nama[1]);


    }

    @Override
    public void finish() {
        super.finish();
        instance = null;
    }

    public void submitTanggal_1(View view) {

        //int coba = 123;
        String shift = null;

        //Toast.makeText(getApplicationContext(), jabatan, Toast.LENGTH_LONG).show();

        tanggal = Integer.toString(dp.getDayOfMonth());
        bulan = Integer.toString(dp.getMonth() + 1);
        tahun = Integer.toString(dp.getYear());

        if (hours_calendar <= 7) {
            shift = "P";
        } else if (hours_calendar <= 15 && hours_calendar > 7) {
            shift = "S";
        } else if (hours_calendar > 15) {
            shift = "M";
        } else {
        }

        if (jabatan.equalsIgnoreCase("kplp") || jabatan.equalsIgnoreCase("kalapas"))
        {
            Intent i = new Intent(getApplicationContext(), KepalaLapasTab.class);
            i.putExtra(TANGGAL, tanggal);
            i.putExtra(BULAN, bulan);
            i.putExtra(TAHUN, tahun);
            i.putExtra(SHIFT, shift);
            startActivity(i);
        }
        else if (jabatan.equalsIgnoreCase("kepala"))
        {
            Intent i = new Intent(getApplicationContext(), KepalaReguTab.class);
            i.putExtra(TANGGAL, tanggal);
            i.putExtra(BULAN, bulan);
            i.putExtra(TAHUN, tahun);
            i.putExtra(SHIFT, shift);
            startActivity(i);
        }
        else if (jabatan.equalsIgnoreCase("anggota")  || jabatan.equalsIgnoreCase("wakil") || jabatan.equalsIgnoreCase("plb"))
        {
            Intent i = new Intent(getApplicationContext(), AnggotaTab.class);
            i.putExtra(TANGGAL, tanggal);
            i.putExtra(BULAN, bulan);
            i.putExtra(TAHUN, tahun);
            i.putExtra(SHIFT, shift);
            startActivity(i);
        }
        else if (jabatan.equalsIgnoreCase("wanita"))
        {
            Intent i = new Intent(getApplicationContext(), WanitaTab.class);
            i.putExtra(TANGGAL, tanggal);
            i.putExtra(BULAN, bulan);
            i.putExtra(TAHUN, tahun);
            i.putExtra(SHIFT, shift);
            startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                if (exit) {
                    finish(); // finish activity
                } else {
                    Toast.makeText(this, "Press Back again to Exit.",
                            Toast.LENGTH_SHORT).show();
                    exit = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            exit = false;
                        }
                    }, 3 * 1000);

                }
                break;

            case R.id.log_out:
                session.logoutUser();
                finish();
                break;
        }
        return true;
    }

    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }
}