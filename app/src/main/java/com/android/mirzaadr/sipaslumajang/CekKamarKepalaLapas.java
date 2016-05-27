package com.android.mirzaadr.sipaslumajang;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mirzaadr on 11/15/2015.
 */
public class CekKamarKepalaLapas extends Activity {

    private Dialog pDialog;
    AlertDialogManager alert = new AlertDialogManager();
    private static final String TAG_ABSEN = "absenkamar";

    // URL to get contacts JSON
    private static String url = "http://smpn1jatiroto.sch.id/lapaslmj/cekabsenkamar.php";
    private static final String TAG_BLOK = "blok";
    private static final String TAG_NO = "no_kamar";
    private static final String TAG_JUMLAH = "jumlah";
    private static final String TAG_LOCK = "kunci";
    private static final String TAG_SUCCESS = "success";
    public  static final String TANGGAL = "TANGGAL";

    ListView listbloka;
    ListView listblokb;
    ListView listblokwanita;
    ListView listbloksel;

    String dikunci_1;
    String dikunci_2;
    String dikunci_3;
    String dikunci_4;

    Button kunci_1;
    Button kunci_2;
    Button kunci_3;

    TextView verr;

    TextView tgl;

    public String bulbul;

    public String tanggal;
    public String tahun;
    public String bulan;
    public String tanggall;

    Session session;

    // contacts JSONArray
    JSONArray absenkamar = null;
    int success;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> BlokA;
    ArrayList<HashMap<String, String>> BlokB;
    ArrayList<HashMap<String, String>> BlokWanita;
    ArrayList<HashMap<String, String>> BlokSel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ketua_lihat_kamar);

        tanggall = getParent().getIntent().getStringExtra(Pilih_Tanggal.TANGGAL);
        bulan = getParent().getIntent().getStringExtra(Pilih_Tanggal.BULAN);
        tahun  = getParent().getIntent().getStringExtra(Pilih_Tanggal.TAHUN);
        tanggal = bulan + tanggall + tahun;

        tgl = (TextView)findViewById(R.id.tgl_ketukmr);

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

        kunci_1 = (Button)findViewById(R.id.unlock_kmr_l);
        kunci_2 = (Button)findViewById(R.id.unlock_kmr_p);
        kunci_3 = (Button)findViewById(R.id.unlock_kmr_s);

        listbloka = (ListView)findViewById(R.id.list_blok1);
        listblokb = (ListView)findViewById(R.id.list_blok2);
        listblokwanita = (ListView)findViewById(R.id.list_wanita);
        listbloksel = (ListView)findViewById(R.id.list_sel);

        verr = (TextView)findViewById(R.id.ver_ketua_kmr);

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
            pDialog = ProgressDialog.show(CekKamarKepalaLapas.this, "Please wait...", "Silahkan tunggu");
            //pDialog.show(CekKamarKepalaLapas.this, "Please wait...", "Silahkan tunggu");


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
                                dikunci_1 = kunci;
                            }
                            else if(blok.equalsIgnoreCase("b"))
                            {
                                blokb.put(TAG_BLOK, blok);
                                blokb.put(TAG_NO, no_kamar);
                                blokb.put(TAG_JUMLAH, jumlah);
                                blokb.put(TAG_LOCK, kunci);
                                BlokB.add(blokb);
                                dikunci_1 = kunci;
                            }
                            else if(blok.equalsIgnoreCase("wanita"))
                            {
                                blokwanita.put(TAG_BLOK, blok);
                                blokwanita.put(TAG_NO, no_kamar);
                                blokwanita.put(TAG_JUMLAH, jumlah);
                                blokwanita.put(TAG_LOCK, kunci);
                                BlokWanita.add(blokwanita);
                                dikunci_2 = kunci;
                            }
                            else if(blok.equalsIgnoreCase("sel"))
                            {
                                bloksel.put(TAG_BLOK, blok);
                                bloksel.put(TAG_NO, no_kamar);
                                bloksel.put(TAG_JUMLAH, jumlah);
                                bloksel.put(TAG_LOCK, kunci);
                                BlokSel.add(bloksel);
                                dikunci_3 = kunci;
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

                if (dikunci_1.equalsIgnoreCase("2"))
                {
                    kunci_1.setClickable(false);
                    verr.setText("TELAH DIVERIFIKASI");
                }
                else if (dikunci_1.equalsIgnoreCase("1"))
                {
                    kunci_1.setClickable(true);
                }
                else if (dikunci_1.equalsIgnoreCase("0"))
                {
                    kunci_1.setClickable(true);
                }

                if (dikunci_2.equalsIgnoreCase("2"))
                {
                    kunci_2.setClickable(false);
                }
                else if (dikunci_2.equalsIgnoreCase("1"))
                {
                    kunci_2.setClickable(true);
                }
                else if (dikunci_2.equalsIgnoreCase("0"))
                {
                    kunci_2.setClickable(true);
                }

                if (dikunci_3.equalsIgnoreCase("2"))
                {
                    kunci_3.setClickable(false);
                }
                else if (dikunci_3.equalsIgnoreCase("1"))
                {
                    kunci_3.setClickable(true);
                }
                else if (dikunci_3.equalsIgnoreCase("0"))
                {
                    kunci_3.setClickable(true);
                }


                /**
                 * Updating parsed JSON data into ListView
                 * */

                ListAdapter adapter_bloka = new SimpleAdapter(
                        CekKamarKepalaLapas.this, BlokA,
                        R.layout.list_item_kamar, new String[] {TAG_NO, TAG_JUMLAH, TAG_LOCK}, new int[] {R.id.kamar,
                        R.id.jumlah, R.id.lock2});

                ListAdapter adapter_blokb = new SimpleAdapter(
                        CekKamarKepalaLapas.this, BlokB,
                        R.layout.list_item_kamar, new String[] {TAG_NO, TAG_JUMLAH, TAG_LOCK}, new int[] {R.id.kamar,
                        R.id.jumlah, R.id.lock2});

                ListAdapter adapter_blokwanita = new SimpleAdapter(
                        CekKamarKepalaLapas.this, BlokWanita,
                        R.layout.list_item_kamar, new String[] {TAG_NO, TAG_JUMLAH, TAG_LOCK}, new int[] {R.id.kamar,
                        R.id.jumlah, R.id.lock2});

                ListAdapter adapter_bloksel = new SimpleAdapter(
                        CekKamarKepalaLapas.this, BlokSel,
                        R.layout.list_item_kamar, new String[] {TAG_NO, TAG_JUMLAH, TAG_LOCK}, new int[] {R.id.kamar,
                        R.id.jumlah, R.id.lock2});

                listbloka.setAdapter(adapter_bloka);
                listblokb.setAdapter(adapter_blokb);
                listblokwanita.setAdapter(adapter_blokwanita);
                listbloksel.setAdapter(adapter_bloksel);


        }

    }

    private void unlock_kamar(final String tanggal, String kode, String kunci) {

        class LoginAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(CekKamarKepalaLapas.this, "Please wait", "Locking...");

            }

            @Override
            protected String doInBackground(String... param) {

                String tanggal = param[0];
                String kode = param[1];
                String kunci = param[2];

                InputStream is = null;
                List<NameValuePair> pair = new ArrayList<NameValuePair>();

                pair.add(new BasicNameValuePair("tanggal", tanggal));
                pair.add(new BasicNameValuePair("kode", kode));
                pair.add(new BasicNameValuePair("lock", kunci));

                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://smpn1jatiroto.sch.id/lapaslmj/kuncikamar.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(pair));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                    is = entity.getContent();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;


            }

            @Override
            protected void onPostExecute(String result){

                if(result.trim()!=null)
                {
                    String hasil = result.trim();
                    loadingDialog.dismiss();

                    if(hasil.equalsIgnoreCase("berhasil")){
                        Toast.makeText(getApplicationContext(), "Buka revisi data kamar berhasil", Toast.LENGTH_SHORT).show();

                    }
                    else if (hasil.equalsIgnoreCase("gagal"))
                    {
                        Toast.makeText(getApplicationContext(), "Buka revisi data kamar gagal", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Another error", Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    loadingDialog.dismiss();
                    alert.showAlertDialog(CekKamarKepalaLapas.this, "Data tidak ditemukan!", "Cek koneksi internet anda kemudian coba lagi", false);
                }


            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(tanggal, kode, kunci);

    }

    public void unlockKamar_bloklaki(View view) {
        unlock_kamar(tanggal, "l", "0");
        //unlock_kamar(tanggal, "b", "0");
    }

    public void unlockKamar_blokwanita (View view)
    {
        unlock_kamar(tanggal, "w", "0");
    }

    public void unlockKamar_bloksel (View view)
    {
        unlock_kamar(tanggal, "s", "0");
    }

}
