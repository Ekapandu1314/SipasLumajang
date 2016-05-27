package com.android.mirzaadr.sipaslumajang;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;



/**
 * Created by Mirzaadr on 11/15/2015.
 */
public class AnggotaTab extends AppCompatActivity {

    private TabHost mTabHost;

    private Boolean exit = false;

    Session session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_host);

        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        LocalActivityManager mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);
        mTabHost.setup(mLocalActivityManager);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        session = new Session(getApplicationContext());

        session.checkLogin();

        // Inbox Tab
        TabHost.TabSpec OneSpec = mTabHost.newTabSpec("ONE");
        Intent OneIntent = new Intent(this, AnggotaLihatRegu.class);
        // Tab Content
        OneSpec.setContent(OneIntent);
        OneSpec.setIndicator("Anggota");

        // Outbox Tab
        TabHost.TabSpec TwoSpec = mTabHost.newTabSpec("TWO");
        Intent TwoIntent = new Intent(this, AnggotaLihatKamar.class);
        TwoSpec.setContent(TwoIntent);

        TwoSpec.setIndicator("Kamar");

        // Adding all TabSpec to TabHost
        mTabHost.addTab(OneSpec); // Adding Inbox tab
        mTabHost.addTab(TwoSpec); // Adding Outbox tab
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
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (item.getItemId()) {
            case android.R.id.home:
                //Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show();
                //Intent i = new Intent(getApplicationContext(), Pilih_Tanggal.class);
                //i.putExtra(TANGGAL, bulan + tanggal + tahun);
                //i.putExtra(SHIFT, shift);
                //startActivity(i);
                finish();
                break;

            case R.id.log_out:
                session.logoutUser();
                if(Pilih_Tanggal.instance != null) {
                try {
                    Pilih_Tanggal.instance.finish();
                } catch (Exception e) {}
            }
                finish();
                break;
        }
        return true;

        /*if (id == R.id.log_out) {
            session.logoutUser();
            return true;
        }
        else if (id == R.id.home)
        {
            Intent i = new Intent(getApplicationContext(), Pilih_Tanggal.class);
            //i.putExtra(TANGGAL, bulan + tanggal + tahun);
            //i.putExtra(SHIFT, shift);
            startActivity(i);
            finish();
            return true;
        }



        return super.onOptionsItemSelected(item);*/
    }

    @Override
    public void onBackPressed() {

        finish();


    }


}
