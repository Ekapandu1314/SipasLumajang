package com.android.mirzaadr.sipaslumajang;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mirzaadr on 11/15/2015.
 */
public class AnggotaLihatKamar  extends Activity{

    private Dialog pDialog;
    AlertDialogManager alert = new AlertDialogManager();
    // URL to get contacts JSON
    private static String url = "http://smpn1jatiroto.sch.id/lapaslmj/cekabsenkamar.php";

    private static final String TAG_ABSEN = "absenkamar";
    private static final String TAG_BLOK = "blok";
    private static final String TAG_NO = "no_kamar";
    private static final String TAG_JUMLAH = "jumlah";
    private static final String TAG_LOCK = "kunci";
    private static final String TAG_SUCCESS = "success";

    ListView listbloka;
    ListView listblokb;
    ListView listblokwanita;
    ListView listbloksel;

    Button next;

    public String tanggal;
    public String bulan;
    public String tanggall;
    public String tahun;

    Session session;

    // contacts JSONArray
    JSONArray absenkamar = null;
    int success;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> BlokA;
    ArrayList<HashMap<String, String>> BlokB;
    ArrayList<HashMap<String, String>> BlokWanita;
    ArrayList<HashMap<String, String>> BlokSel;

    TextView tgl;

    public String bulbul;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kamar_anggota_view);

        tanggall = getParent().getIntent().getStringExtra(Pilih_Tanggal.TANGGAL);
        bulan = getParent().getIntent().getStringExtra(Pilih_Tanggal.BULAN);
        tahun  = getParent().getIntent().getStringExtra(Pilih_Tanggal.TAHUN);
        tanggal = bulan + tanggall + tahun;

        tgl = (TextView)findViewById(R.id.tgl_anggotakmr);

        if(bulan.equalsIgnoreCase("1"))
        {
            bulbul = "Januari";
        }
        else if(bulan.equalsIgnoreCase("2"))
        {
            bulbul = "Feburari";
        }
        else if(bulan.equalsIgnoreCase("3"))
        {
            bulbul = "Maret";
        }
        else if(bulan.equalsIgnoreCase("4"))
        {
            bulbul = "April";
        }
        else if(bulan.equalsIgnoreCase("5"))
        {
            bulbul = "Mei";
        }
        else if(bulan.equalsIgnoreCase("6"))
        {
            bulbul = "Juni";
        }
        else if(bulan.equalsIgnoreCase("7"))
        {
            bulbul = "Juli";
        }
        else if(bulan.equalsIgnoreCase("8"))
        {
            bulbul = "Agustus";
        }
        else if(bulan.equalsIgnoreCase("9"))
        {
            bulbul = "September";
        }
        else if(bulan.equalsIgnoreCase("10"))
        {
            bulbul = "Oktober";
        }
        else if(bulan.equalsIgnoreCase("11"))
        {
            bulbul = "November";
        }
        else if(bulan.equalsIgnoreCase("12"))
        {
            bulbul = "Desember";
        }
        else
        {
            bulbul = null;
        }


        tgl.setText(tanggall + " " + bulbul + " " + tahun);

        listbloka = (ListView)findViewById(R.id.list_blok1);

        listbloka.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

                //final String nama = ((TextView) view.findViewById(R.id.nama)).getText().toString();
                //final String lock = ((TextView) view.findViewById(R.id.lock)).getText().toString();
                //final String regu = ((TextView) view.findViewById(R.id.regu)).getText().toString();
                //Toast.makeText(getApplicationContext(), nama + "\n" + regu, Toast.LENGTH_LONG).show();

                alert.showAlertDialog(AnggotaLihatKamar.this, "Tidak Boleh!", "Hanya kepala regu yang dapat memasukkan status anggota", false);

            }
        });

        listblokb = (ListView)findViewById(R.id.list_blok2);

        listblokb.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

                //final String nama = ((TextView) view.findViewById(R.id.nama)).getText().toString();
                //final String lock = ((TextView) view.findViewById(R.id.lock)).getText().toString();
                //final String regu = ((TextView) view.findViewById(R.id.regu)).getText().toString();
                //Toast.makeText(getApplicationContext(), nama + "\n" + regu, Toast.LENGTH_LONG).show();

                alert.showAlertDialog(AnggotaLihatKamar.this, "Tidak Boleh!", "Hanya kepala regu yang dapat memasukkan status anggota", false);

            }
        });

        listblokwanita = (ListView)findViewById(R.id.list_wanita);

        listblokwanita.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

                //final String nama = ((TextView) view.findViewById(R.id.nama)).getText().toString();
                //final String lock = ((TextView) view.findViewById(R.id.lock)).getText().toString();
                //final String regu = ((TextView) view.findViewById(R.id.regu)).getText().toString();
                //Toast.makeText(getApplicationContext(), nama + "\n" + regu, Toast.LENGTH_LONG).show();

                alert.showAlertDialog(AnggotaLihatKamar.this, "Tidak Boleh!", "Hanya kepala regu yang dapat memasukkan status anggota", false);

            }
        });

        listbloksel = (ListView)findViewById(R.id.list_sel);

        listbloksel.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

                //final String nama = ((TextView) view.findViewById(R.id.nama)).getText().toString();
                //final String lock = ((TextView) view.findViewById(R.id.lock)).getText().toString();
                //final String regu = ((TextView) view.findViewById(R.id.regu)).getText().toString();
                //Toast.makeText(getApplicationContext(), nama + "\n" + regu, Toast.LENGTH_LONG).show();

                alert.showAlertDialog(AnggotaLihatKamar.this, "Tidak Boleh!", "Hanya kepala regu yang dapat memasukkan status anggota", false);

            }
        });

        session = new Session(getApplicationContext());
        session.checkLogin();

        BlokA = new ArrayList<HashMap<String, String>>();
        BlokB = new ArrayList<HashMap<String, String>>();
        BlokWanita = new ArrayList<HashMap<String, String>>();
        BlokSel = new ArrayList<HashMap<String, String>>();

        new GetAbsen_Kamar().execute();
    }

    private class GetAbsen_Kamar extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = ProgressDialog.show(AnggotaLihatKamar.this, "Please wait...", "Silahkan tunggu");
            //pDialog.show(AnggotaLihatKamar.this, "Please wait...", "Silahkan tunggu");


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("tanggal", tanggal));

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.POST, params);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    absenkamar = jsonObj.getJSONArray(TAG_ABSEN);
                    success = jsonObj.getInt(TAG_SUCCESS);

                    if (success == 1)
                    {
                        for (int i = 0; i < absenkamar.length(); i++) {
                            JSONObject c = absenkamar.getJSONObject(i);

                            String blok = c.getString(TAG_BLOK);;
                            String no_kamar = c.getString(TAG_NO);;
                            String jumlah = c.getString(TAG_JUMLAH);;
                            String kunci = c.getString(TAG_LOCK);;

                            // Phone node is JSON Object

                            // tmp hashmap for single contact
                            HashMap<String, String> bloka = new HashMap<String, String>();
                            HashMap<String, String> blokb = new HashMap<String, String>();
                            HashMap<String, String> blokwanita = new HashMap<String, String>();
                            HashMap<String, String> bloksel = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            if (blok.equalsIgnoreCase("a"))
                            {
                                bloka.put(TAG_BLOK, blok);
                                bloka.put(TAG_NO, no_kamar);
                                bloka.put(TAG_JUMLAH, jumlah);
                                bloka.put(TAG_LOCK, kunci);
                                BlokA.add(bloka);
                            }
                            else if(blok.equalsIgnoreCase("b"))
                            {
                                blokb.put(TAG_BLOK, blok);
                                blokb.put(TAG_NO, no_kamar);
                                blokb.put(TAG_JUMLAH, jumlah);
                                blokb.put(TAG_LOCK, kunci);
                                BlokB.add(blokb);
                            }
                            else if(blok.equalsIgnoreCase("wanita"))
                            {
                                blokwanita.put(TAG_BLOK, blok);
                                blokwanita.put(TAG_NO, no_kamar);
                                blokwanita.put(TAG_JUMLAH, jumlah);
                                blokwanita.put(TAG_LOCK, kunci);
                                BlokWanita.add(blokwanita);
                            }
                            else if(blok.equalsIgnoreCase("sel"))
                            {
                                bloksel.put(TAG_BLOK, blok);
                                bloksel.put(TAG_NO, no_kamar);
                                bloksel.put(TAG_JUMLAH, jumlah);
                                bloksel.put(TAG_LOCK, kunci);
                                BlokSel.add(bloksel);
                            }

                            // adding contact to contact list
                            //alert.showAlertDialog(this, "Login failed..", nama + " " + shift + " " + regu + " " + ket, true);
                            //Toast.makeText(c, nama + " " + shift + " " + regu + " " + ket, Toast.LENGTH_LONG).show();
                        }
                    }
                    // looping through All Data

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog


                if (pDialog.isShowing())
                    pDialog.dismiss();

                /**
                 * Updating parsed JSON data into ListView
                 * */

                ListAdapter adapter_bloka = new SimpleAdapter(
                        AnggotaLihatKamar.this, BlokA,
                        R.layout.list_item_kamar, new String[] {TAG_NO, TAG_JUMLAH, TAG_LOCK}, new int[] {R.id.kamar,
                        R.id.jumlah, R.id.lock2});

                ListAdapter adapter_blokb = new SimpleAdapter(
                        AnggotaLihatKamar.this, BlokB,
                        R.layout.list_item_kamar, new String[] {TAG_NO, TAG_JUMLAH, TAG_LOCK}, new int[] {R.id.kamar,
                        R.id.jumlah, R.id.lock2});

                ListAdapter adapter_blokwanita = new SimpleAdapter(
                        AnggotaLihatKamar.this, BlokWanita,
                        R.layout.list_item_kamar, new String[] {TAG_NO, TAG_JUMLAH, TAG_LOCK}, new int[] {R.id.kamar,
                        R.id.jumlah, R.id.lock2});

                ListAdapter adapter_bloksel = new SimpleAdapter(
                        AnggotaLihatKamar.this, BlokSel,
                        R.layout.list_item_kamar, new String[] {TAG_NO, TAG_JUMLAH, TAG_LOCK}, new int[] {R.id.kamar,
                        R.id.jumlah, R.id.lock2});

                listbloka.setAdapter(adapter_bloka);
                listblokb.setAdapter(adapter_blokb);
                listblokwanita.setAdapter(adapter_blokwanita);
                listbloksel.setAdapter(adapter_bloksel);



        }

    }

}
