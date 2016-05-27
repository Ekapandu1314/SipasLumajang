package com.android.mirzaadr.sipaslumajang;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.List;

/**
 * Created by Mirzaadr on 11/15/2015.
 */
public class SummaryKepalaLapas extends Activity {

    private static String url = "http://smpn1jatiroto.sch.id/lapaslmj/summary.php";

    AlertDialogManager alert = new AlertDialogManager();

    private static final String TAG_SUMMARY = "summary";
    private static final String TAG_PAGI_H = "phadir";
    private static final String TAG_PAGI_TH = "ptidak";
    private static final String TAG_SIANG_H = "shadir";
    private static final String TAG_SIANG_TH = "stidak";
    private static final String TAG_MALAM_H = "mhadir";
    private static final String TAG_MALAM_TH = "mtidak";
    private static final String TAG_H_TOTAL = "htotal";
    private static final String TAG_TH_TOTAL = "ttotal";
    private static final String TAG_BlokA = "bloka";
    private static final String TAG_BlokB = "blokb";
    private static final String TAG_BlokW = "blokw";
    private static final String TAG_BlokS = "bloks";
    private static final String TAG_Blok_Total = "btotal";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_VER = "verifikasi";

    String pagihx;
    String pagitx;
    String sianghx;
    String siangtx;
    String malamhx;
    String malamtx;
    String total_penjagahx;
    String total_penjagatx;
    String blokAx;
    String blokBx;
    String blokWx;
    String blokSx;
    String blokTotalx;
    String veri;

    TextView pagihxx;
    TextView pagitxx;
    TextView sianghxx;
    TextView siangtxx;
    TextView malamhxx;
    TextView malamtxx;
    TextView total_penjagahxx;
    TextView total_penjagatxx;
    TextView blokAxx;
    TextView blokBxx;
    TextView blokWxx;
    TextView blokSxx;
    TextView blokTotalxx;
    TextView tgl;

    public String bulbul;

    Button verif;

    Session session;

    private ProgressDialog pDialog;

    public String tanggal;
    public String tanggall;
    public String tahun;
    public String bulan;

    public String regu1;
    public String regu[];
    public String reguxx;

    public static final String TANGGAL = "TANGGAL";

    JSONArray summary = null;

    int success;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ketua_summary);

        tanggall = getParent().getIntent().getStringExtra(Pilih_Tanggal.TANGGAL);
        bulan = getParent().getIntent().getStringExtra(Pilih_Tanggal.BULAN);
        tahun  = getParent().getIntent().getStringExtra(Pilih_Tanggal.TAHUN);
        tanggal = bulan + tanggall + tahun;

        tgl = (TextView)findViewById(R.id.tgl_ketusmr);

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

        verif = (Button)findViewById(R.id.ver_iya);

        new GetSummary().execute();
    }

    private class GetSummary extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SummaryKepalaLapas.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            // Creating service handler class instance
            try {

                ServiceHandler sh = new ServiceHandler();

                List<NameValuePair> paramss = new ArrayList<NameValuePair>();

                paramss.add(new BasicNameValuePair("tanggal", tanggal));

                // Making a request to url and getting response
                String jsonStr = sh.makeServiceCall(url, ServiceHandler.POST, paramss);

                Log.d("Response: ", "> " + jsonStr);

                JSONObject jsonObj = new JSONObject(jsonStr);

                summary = jsonObj.getJSONArray(TAG_SUMMARY);
                success = jsonObj.getInt(TAG_SUCCESS);



                if (jsonStr != null) {
                    runOnUiThread(new Runnable() {


                        @Override
                        public void run() {
                            try {

                                pagihxx = (TextView) findViewById(R.id.jml_pagi);
                                pagitxx = (TextView) findViewById(R.id.jml_pagit);
                                sianghxx = (TextView) findViewById(R.id.jml_siang);
                                siangtxx = (TextView) findViewById(R.id.jml_siangt);
                                malamhxx = (TextView) findViewById(R.id.jml_malam);
                                malamtxx = (TextView) findViewById(R.id.jml_malamt);
                                total_penjagahxx = (TextView) findViewById(R.id.jml_total);
                                total_penjagatxx = (TextView) findViewById(R.id.jml_totalt);
                                blokAxx = (TextView) findViewById(R.id.jml_BlokA);
                                blokBxx = (TextView) findViewById(R.id.jml_BlokB);
                                blokWxx = (TextView) findViewById(R.id.jml_BlokWanita);
                                blokSxx = (TextView) findViewById(R.id.jml_BlokSel);
                                blokTotalxx = (TextView) findViewById(R.id.jml_total_kamar);
                                // Getting JSON Array node

                                if (success == 1) {
                                    for (int i = 0; i < 1; i++) {
                                        JSONObject c = summary.getJSONObject(i);

                                        String pagih = c.getString(TAG_PAGI_H);
                                        String pagit = c.getString(TAG_PAGI_TH);
                                        String siangh = c.getString(TAG_SIANG_H);
                                        String siangt = c.getString(TAG_SIANG_TH);
                                        String malamh = c.getString(TAG_MALAM_H);
                                        String malamt = c.getString(TAG_MALAM_TH);
                                        String total_penjagah = c.getString(TAG_H_TOTAL);
                                        String total_penjagat = c.getString(TAG_TH_TOTAL);
                                        String blokA = c.getString(TAG_BlokA);
                                        String blokB = c.getString(TAG_BlokB);
                                        String blokW = c.getString(TAG_BlokW);
                                        String blokS = c.getString(TAG_BlokS);
                                        String blokTotal = c.getString(TAG_Blok_Total);
                                        String ver = c.getString(TAG_VER);

                                        pagihx = pagih;
                                        pagitx = pagit;
                                        sianghx = siangh;
                                        siangtx = siangt;
                                        malamhx = malamh;
                                        malamtx = malamt;
                                        total_penjagahx = total_penjagah;
                                        total_penjagatx = total_penjagat;
                                        blokAx = blokA;
                                        blokBx = blokB;
                                        blokWx = blokW;
                                        blokSx = blokS;
                                        blokTotalx = blokTotal;
                                        veri = ver;

                                        pagihxx.setText(pagihx);
                                        pagitxx.setText(pagitx);
                                        sianghxx.setText(sianghx);
                                        siangtxx.setText(siangtx);
                                        malamhxx.setText(malamhx);
                                        malamtxx.setText(malamtx);
                                        total_penjagahxx.setText(total_penjagahx);
                                        total_penjagatxx.setText(total_penjagatx);
                                        blokAxx.setText(blokAx);
                                        blokBxx.setText(blokBx);
                                        blokWxx.setText(blokWx);
                                        blokSxx.setText(blokSx);
                                        blokTotalxx.setText(blokTotalx);


                                    }


                                }
                                // looping through All Data

                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }

                        }
                    });
                } else {
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }




            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            //super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
            {
                pDialog.dismiss();
                if (veri.equalsIgnoreCase("0"))
                {
                    verif.setClickable(true);
                }
                else if (veri.equalsIgnoreCase("1"))
                {
                    verif.setClickable(false);
                }
                else if (veri.equalsIgnoreCase("2"))
                {
                    verif.setClickable(false);
                }
            }



            //settext = true;
        }

    }

    public void verifi (View view)
    {
        verifikasi(tanggal, "1");
        verif.setClickable(false);
    }

    private void verifikasi(final String tanggal, String verifikasi) {

        class LoginAsync extends AsyncTask<String, Void, String>{


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected String doInBackground(String... parameter) {

                String tang = parameter[0];
                String ve = parameter[1];



                InputStream is = null;
                List<NameValuePair> valuepairs = new ArrayList<NameValuePair>();
                valuepairs.add(new BasicNameValuePair("tanggal", tang));
                valuepairs.add(new BasicNameValuePair("verifikasi", ve));

                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://smpn1jatiroto.sch.id/lapaslmj/verifikasi.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(valuepairs));

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
                    String res = result.trim();

                    if(res.equalsIgnoreCase("berhasil")){
                        Toast.makeText(getApplicationContext(), "Verifikasi untuk tanggal " + tanggal + " berhasil", Toast.LENGTH_SHORT).show();

                    }
                    else if (res.equalsIgnoreCase("gagal"))
                    {
                        Toast.makeText(getApplicationContext(), "Verifikasi untuk tanggal " + tanggal + " gagal", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "error lainnya", Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    //pDialog.dismiss();
                    alert.showAlertDialog(SummaryKepalaLapas.this, "Data tidak ditemukan!", "Cek koneksi internet anda kemudian coba lagi", false);
                }


            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(tanggal, verifikasi);

    }
}
