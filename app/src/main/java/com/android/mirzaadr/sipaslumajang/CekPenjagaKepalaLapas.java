package com.android.mirzaadr.sipaslumajang;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
public class CekPenjagaKepalaLapas extends Activity {

    private Dialog pDialog;
    AlertDialogManager alert = new AlertDialogManager();

    // URL to get contacts JSON
    private static String url = "http://smpn1jatiroto.sch.id/lapaslmj/cektanggalklp.php";

    // JSON Node names
    private static final String TAG_KALAPAS = "kalapas";
    private static final String TAG_IDP = "idp";
    private static final String TAG_NAMA = "nama";
    private static final String TAG_REGU = "regu";
    private static final String TAG_SHIFT = "shift";
    private static final String TAG_KET = "ket";
    private static final String TAG_LOCK = "kunci";
    private static final String TAG_SUCCESS = "success";

    ListView listregu1;
    ListView listregu2;
    ListView listregu3;
    ListView listregu4;
    ListView listreguw;

    public static final String TANGGAL = "TANGGAL";

    Context context;

    public String tanggal;
    public String tahun;
    public String bulan;
    public String tanggall;

    Session session;

    String dikunci_1;
    String dikunci_2;
    String dikunci_3;
    String dikunci_4;
    String dikunci_w;

    Button kunci_1;
    Button kunci_2;
    Button kunci_3;
    Button kunci_4;
    Button kunci_w;

    TextView verr;
    TextView tgl;

    // contacts JSONArray
    JSONArray kalapas = null;
    int success;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> Regu1;
    ArrayList<HashMap<String, String>> Regu2;
    ArrayList<HashMap<String, String>> Regu3;
    ArrayList<HashMap<String, String>> Regu4;
    ArrayList<HashMap<String, String>> Reguw;

    public String bulbul;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ketua_lihat_penjaga);

        tanggall = getParent().getIntent().getStringExtra(Pilih_Tanggal.TANGGAL);
        bulan = getParent().getIntent().getStringExtra(Pilih_Tanggal.BULAN);
        tahun  = getParent().getIntent().getStringExtra(Pilih_Tanggal.TAHUN);
        tanggal = bulan + tanggall + tahun;

        tgl = (TextView)findViewById(R.id.tgl_ketuabs);

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

        verr = (TextView)findViewById(R.id.ver_ketua_pjg);

        listregu1 = (ListView)findViewById(R.id.list_regu1);
        listregu2 = (ListView)findViewById(R.id.list_regu2);
        listregu3 = (ListView)findViewById(R.id.list_regu3);
        listregu4 = (ListView)findViewById(R.id.list_regu4);
        listreguw = (ListView)findViewById(R.id.list_reguWanita);

        kunci_1 = (Button)findViewById(R.id.unlock_regu1);
        kunci_2 = (Button)findViewById(R.id.unlock_regu2);
        kunci_3 = (Button)findViewById(R.id.unlock_regu3);
        kunci_4 = (Button)findViewById(R.id.unlock_regu4);
        kunci_w = (Button)findViewById(R.id.unlock_reguWanita);

        session = new Session(getApplicationContext());
        session.checkLogin();

        Regu1 = new ArrayList<HashMap<String, String>>();
        Regu2 = new ArrayList<HashMap<String, String>>();
        Regu3 = new ArrayList<HashMap<String, String>>();
        Regu4 = new ArrayList<HashMap<String, String>>();
        Reguw = new ArrayList<HashMap<String, String>>();


        new GetAbsen().execute();
    }

    private class GetAbsen extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = ProgressDialog.show(CekPenjagaKepalaLapas.this, "Please wait...", "Silahkan tunggu");
            //pDialog.show(CekPenjagaKepalaLapas.this, "Please wait...", "Silahkan tunggu");


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
                    kalapas = jsonObj.getJSONArray(TAG_KALAPAS);
                    success = jsonObj.getInt(TAG_SUCCESS);

                    if (success == 1)
                    {
                        for (int i = 0; i < kalapas.length(); i++) {
                            JSONObject c = kalapas.getJSONObject(i);


                            String idp = null;
                            String nama = null;
                            String regu = null;
                            String shift = null;
                            String ket = null;
                            String lock = null;

                            idp = c.getString(TAG_IDP);
                            nama = c.getString(TAG_NAMA);
                            regu = c.getString(TAG_REGU);
                            shift = c.getString(TAG_SHIFT);
                            ket = c.getString(TAG_KET);
                            lock = c.getString(TAG_LOCK);

                            // Phone node is JSON Object

                            // tmp hashmap for single contact
                            HashMap<String, String> regu1 = new HashMap<String, String>();
                            HashMap<String, String> regu2 = new HashMap<String, String>();
                            HashMap<String, String> regu3 = new HashMap<String, String>();
                            HashMap<String, String> regu4 = new HashMap<String, String>();
                            HashMap<String, String> reguw = new HashMap<String, String>();


                            // adding each child node to HashMap key => value
                            if (regu.equalsIgnoreCase("i"))
                            {
                                regu1.put(TAG_IDP, idp);
                                regu1.put(TAG_NAMA, nama);
                                regu1.put(TAG_REGU, regu);
                                regu1.put(TAG_SHIFT, shift);
                                regu1.put(TAG_KET, ket);
                                regu1.put(TAG_LOCK, lock);
                                Regu1.add(regu1);
                                dikunci_1 = lock;
                            }
                            else if(regu.equalsIgnoreCase("ii"))
                            {
                                regu2.put(TAG_IDP, idp);
                                regu2.put(TAG_NAMA, nama);
                                regu2.put(TAG_REGU, regu);
                                regu2.put(TAG_SHIFT, shift);
                                regu2.put(TAG_KET, ket);
                                regu2.put(TAG_LOCK, lock);
                                Regu2.add(regu2);
                                dikunci_2 = lock;
                            }
                            else if(regu.equalsIgnoreCase("iii"))
                            {
                                regu3.put(TAG_IDP, idp);
                                regu3.put(TAG_NAMA, nama);
                                regu3.put(TAG_REGU, regu);
                                regu3.put(TAG_SHIFT, shift);
                                regu3.put(TAG_KET, ket);
                                regu3.put(TAG_LOCK, lock);
                                Regu3.add(regu3);
                                dikunci_3 = lock;
                            }
                            else if(regu.equalsIgnoreCase("iv"))
                            {
                                regu4.put(TAG_IDP, idp);
                                regu4.put(TAG_NAMA, nama);
                                regu4.put(TAG_REGU, regu);
                                regu4.put(TAG_SHIFT, shift);
                                regu4.put(TAG_KET, ket);
                                regu4.put(TAG_LOCK, lock);
                                Regu4.add(regu4);
                                dikunci_4 = lock;
                            }
                            else if(regu.equalsIgnoreCase("w1") || regu.equalsIgnoreCase("w2") || regu.equalsIgnoreCase("w3") || regu.equalsIgnoreCase("w4"))
                            {
                                reguw.put(TAG_IDP, idp);
                                reguw.put(TAG_NAMA, nama);
                                reguw.put(TAG_REGU, regu);
                                reguw.put(TAG_SHIFT, shift);
                                reguw.put(TAG_KET, ket);
                                reguw.put(TAG_LOCK, lock);
                                Reguw.add(reguw);
                                dikunci_w = lock;
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

            if(dikunci_1!=null) {
                if (dikunci_1.equalsIgnoreCase("2")) {
                    kunci_1.setClickable(false);
                    verr.setText("TELAH DIVERIFIKASI");
                } else if (dikunci_1.equalsIgnoreCase("1")) {
                    kunci_1.setClickable(true);
                } else if (dikunci_1.equalsIgnoreCase("0")) {
                    kunci_1.setClickable(true);
                }
            }


            if(dikunci_2!=null) {
                if (dikunci_2.equalsIgnoreCase("2")) {
                    kunci_2.setClickable(false);
                } else if (dikunci_2.equalsIgnoreCase("1")) {
                    kunci_2.setClickable(true);
                } else if (dikunci_2.equalsIgnoreCase("0")) {
                    kunci_2.setClickable(true);
                }
            }

            if(dikunci_3!=null) {
                if (dikunci_3.equalsIgnoreCase("2")) {
                    kunci_3.setClickable(false);
                } else if (dikunci_3.equalsIgnoreCase("1")) {
                    kunci_3.setClickable(true);
                } else if (dikunci_3.equalsIgnoreCase("0")) {
                    kunci_3.setClickable(true);
                }
            }

            if(dikunci_4!=null) {
                if (dikunci_4.equalsIgnoreCase("2")) {
                    kunci_4.setClickable(false);
                } else if (dikunci_4.equalsIgnoreCase("1")) {
                    kunci_4.setClickable(true);
                } else if (dikunci_4.equalsIgnoreCase("0")) {
                    kunci_4.setClickable(true);
                }
            }

            if(dikunci_w!=null) {
                if (dikunci_w.equalsIgnoreCase("2")) {
                    kunci_w.setClickable(false);
                } else if (dikunci_w.equalsIgnoreCase("1")) {
                    kunci_w.setClickable(true);
                } else if (dikunci_w.equalsIgnoreCase("0")) {
                    kunci_w.setClickable(true);
                }
            }



                ListAdapter adapter_regu1 = new SimpleAdapter(
                        CekPenjagaKepalaLapas.this, Regu1,
                        R.layout.list_item_penjaga, new String[] {TAG_NAMA, TAG_SHIFT, TAG_KET, TAG_LOCK}, new int[] {R.id.nama1,
                        R.id.regu1, R.id.keterangan1, R.id.lock1 });

                ListAdapter adapter_regu2 = new SimpleAdapter(
                        CekPenjagaKepalaLapas.this, Regu2,
                        R.layout.list_item_penjaga, new String[] {TAG_NAMA, TAG_SHIFT, TAG_KET, TAG_LOCK}, new int[] {R.id.nama1,
                        R.id.regu1, R.id.keterangan1, R.id.lock1 });

                ListAdapter adapter_regu3 = new SimpleAdapter(
                        CekPenjagaKepalaLapas.this, Regu3,
                        R.layout.list_item_penjaga, new String[] {TAG_NAMA, TAG_SHIFT, TAG_KET, TAG_LOCK}, new int[] {R.id.nama1,
                        R.id.regu1, R.id.keterangan1, R.id.lock1 });

                ListAdapter adapter_regu4 = new SimpleAdapter(
                        CekPenjagaKepalaLapas.this, Regu4,
                        R.layout.list_item_penjaga, new String[] {TAG_NAMA, TAG_SHIFT, TAG_KET, TAG_LOCK}, new int[] {R.id.nama1,
                        R.id.regu1, R.id.keterangan1, R.id.lock1 });

                ListAdapter adapter_reguw = new SimpleAdapter(
                        CekPenjagaKepalaLapas.this, Reguw,
                        R.layout.list_item_penjaga, new String[] {TAG_NAMA, TAG_SHIFT, TAG_KET, TAG_LOCK}, new int[] {R.id.nama1,
                        R.id.regu1, R.id.keterangan1, R.id.lock1 });



                listregu1.setAdapter(adapter_regu1);
                listregu2.setAdapter(adapter_regu2);
                listregu3.setAdapter(adapter_regu3);
                listregu4.setAdapter(adapter_regu4);
                listreguw.setAdapter(adapter_reguw);


        }

    }

    private void unlock(final String kunci, String tanggal, String regu) {

        class LoginAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(CekPenjagaKepalaLapas.this, "Please wait", "Locking...");

            }

            @Override
            protected String doInBackground(String... param) {

                String kunci = param[0];
                String tanggal = param[1];
                String regu = param[2];

                InputStream is = null;
                List<NameValuePair> pair = new ArrayList<NameValuePair>();

                pair.add(new BasicNameValuePair("tanggal", tanggal));
                pair.add(new BasicNameValuePair("regu", regu));
                pair.add(new BasicNameValuePair("lock", kunci));

                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://smpn1jatiroto.sch.id/lapaslmj/kunci.php");
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
                        Toast.makeText(getApplicationContext(), "Buka revisi data penjaga berhasil", Toast.LENGTH_SHORT).show();

                    }
                    else if (hasil.equalsIgnoreCase("gagal"))
                    {
                        Toast.makeText(getApplicationContext(), "Buka revisi data penjaga gagal", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Another error", Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    loadingDialog.dismiss();
                    alert.showAlertDialog(CekPenjagaKepalaLapas.this, "Data tidak ditemukan!", "Cek koneksi internet anda kemudian coba lagi", false);
                }


            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(kunci, tanggal, regu);

    }

    private void unlock_wanita(final String kunci, String tanggal, String regu) {

        class LoginAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(CekPenjagaKepalaLapas.this, "Please wait", "Locking...");

            }

            @Override
            protected String doInBackground(String... param) {

                String kunci = param[0];
                String tanggal = param[1];
                String regu = param[2];

                InputStream is = null;
                List<NameValuePair> pair = new ArrayList<NameValuePair>();

                pair.add(new BasicNameValuePair("tanggal", tanggal));
                pair.add(new BasicNameValuePair("regu", regu));
                pair.add(new BasicNameValuePair("lock", kunci));

                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://smpn1jatiroto.sch.id/lapaslmj/kuncip.php");
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

                String hasil = result.trim();
                loadingDialog.dismiss();

                if(hasil.equalsIgnoreCase("berhasil")){
                    Toast.makeText(getApplicationContext(), "Buka revisi data penjaga berhasil", Toast.LENGTH_SHORT).show();

                }
                else if (hasil.equalsIgnoreCase("gagal"))
                {
                    Toast.makeText(getApplicationContext(), "Buka revisi data penjaga gagal", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(getApplicationContext(), "Another error", Toast.LENGTH_SHORT).show();

                }
            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(kunci, tanggal, regu);

    }

    public void unlockData_regu1(View view){

        unlock("0", tanggal, "i");

    }

    public void unlockData_regu2(View view){

        unlock("0", tanggal, "ii");

    }

    public void unlockData_regu3(View view){

        unlock("0", tanggal, "iii");

    }

    public void unlockData_regu4(View view){

        unlock("0", tanggal, "iv");

    }

    public void unlockData_reguw(View view){

        unlock_wanita("0", tanggal, "w1");
        unlock_wanita("0", tanggal, "w2");
        unlock_wanita("0", tanggal, "w3");

    }
}
