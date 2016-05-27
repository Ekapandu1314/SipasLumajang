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
import android.widget.EditText;
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
 * Created by Mirzaadr on 11/17/2015.
 */
public class WanitaKamar extends Activity {

    private Dialog pDialog;
    AlertDialogManager alert = new AlertDialogManager();
    // URL to get contacts JSON
    private static String url = "http://smpn1jatiroto.sch.id/lapaslmj/cekabsenkamarpjg.php";

    private static final String TAG_ABSEN = "absenkamar";
    private static final String TAG_BLOK = "blok";
    private static final String TAG_NO = "no_kamar";
    private static final String TAG_JUMLAH = "jumlah";
    private static final String TAG_LOCK = "kunci";
    private static final String TAG_SUCCESS = "success";
    public static final String TANGGAL = "TANGGAL";

    public static final String KUNCI_KAMAR = "kunci_kamar";
    public static final String KUNCI_PENJAGA = "kunci_penjaga";

    ListView listbloka;
    ListView listblokb;
    ListView listblokwanita;
    ListView listbloksel;

    TextView verr;

    String kunci_penjaga;

    String dikunci_l;
    String dikunci_p;
    String dikunci_s;

    Button kunci_l;
    Button kunci_p;
    Button kunci_s;

    public String tanggal;
    public String tanggall;
    public String bulan;
    public String tahun;


    public String lis_l = "coba";
    public String lis_p = "coba";
    public String lis_s = "coba";



    Session session;

    // contacts JSONArray
    JSONArray absenkamar = null;
    int success;

    Boolean list_l = true;
    Boolean list_p = true;
    Boolean list_s = true;

    TextView tgl;
    public String bulbul;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> BlokA;
    ArrayList<HashMap<String, String>> BlokB;
    ArrayList<HashMap<String, String>> BlokWanita;
    ArrayList<HashMap<String, String>> BlokSel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wanita_lihat_kamar);

        tanggall = getParent().getIntent().getStringExtra(Pilih_Tanggal.TANGGAL);
        bulan = getParent().getIntent().getStringExtra(Pilih_Tanggal.BULAN);
        tahun  = getParent().getIntent().getStringExtra(Pilih_Tanggal.TAHUN);
        tanggal = bulan + tanggall + tahun;

        tgl = (TextView)findViewById(R.id.tgl_wanitakmr);

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

        verr = (TextView)findViewById(R.id.ver_kamarWanita);

        kunci_l = (Button)findViewById(R.id.lock_kmr_l_wanita);
        kunci_p = (Button)findViewById(R.id.lock_kmr_lihatwanita);
        kunci_s = (Button)findViewById(R.id.lock_kmr_s_wanita);

        kunci_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert.showAlertDialog(WanitaKamar.this, "Data Input Failed!", "Hanya penjaga laki-laki yang dapat memasukkan data kamar blok A", false);

            }
        });

        kunci_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lock_kamar(tanggal, "w", "1");
                if(lis_p.equalsIgnoreCase("berhasil"))
                {
                    list_p = false;
                    kunci_p.setClickable(false);
                }

            }
        });

        kunci_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert.showAlertDialog(WanitaKamar.this, "Data Input Failed!", "Hanya penjaga laki-laki yang dapat memasukkan data kamar blok A", false);

            }
        });

        listbloka = (ListView) findViewById(R.id.list_blok1_wanita);

        // Listview on item click listener
        listbloka.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

                alert.showAlertDialog(WanitaKamar.this, "Data Input Failed!", "Hanya penjaga laki-laki yang dapat memasukkan data kamar blok A", false);

            }
        });

        listblokb = (ListView)findViewById(R.id.list_blok2_wanita);

        listblokb.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

                alert.showAlertDialog(WanitaKamar.this, "Data Input Failed!", "Hanya penjaga laki-laki yang dapat memasukkan data kamar blok B", false);





            }
        });

        listblokwanita = (ListView)findViewById(R.id.list_lihatwanita);

        listblokwanita.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

                if(list_p == true) {
                    final String no_kamar = ((TextView) view.findViewById(R.id.kamar)).getText().toString();
                    final String jumlah = ((TextView) view.findViewById(R.id.jumlah)).getText().toString();
                    final String blok = "wanita";
                    final String lock = ((TextView) view.findViewById(R.id.lock2)).getText().toString();
                    //final String regu = ((TextView) view.findViewById(R.id.regu)).getText().toString();
                    //Toast.makeText(getApplicationContext(), nama + "\n" + regu, Toast.LENGTH_LONG).show();

                    if (lock.equalsIgnoreCase("1")) {
                        alert.showAlertDialog(WanitaKamar.this, "Data Terkunci!", "Silahkan hubungi kepala lapas untuk edit data", false);
                    } else if (lock.equalsIgnoreCase("2")) {
                        alert.showAlertDialog(WanitaKamar.this, "Data Terkunci!", "Data telah diverifikasi oleh kepala lapas", false);
                    } else if (lock.equalsIgnoreCase("0")) {
                        final Dialog dialog = new Dialog(WanitaKamar.this);
                        dialog.setContentView(R.layout.dialog_box_kamar);
                        dialog.setTitle("Masukkan jumlah....");

                        TextView text = (TextView) dialog.findViewById(R.id.no_kamar);
                        text.setText("Kamar Wanita\nKamar nomor : " + no_kamar);
                        final Button submit_status = (Button) dialog.findViewById(R.id.submit_kamar);


                        submit_status.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                EditText jml = (EditText) dialog.findViewById(R.id.jumlah_penghuni);
                                String jumlah_penghuni = jml.getText().toString();

                                submit_kamar(tanggal, blok, no_kamar, jumlah_penghuni);

                                ((TextView) view.findViewById(R.id.jumlah)).setText(jumlah_penghuni);

                                dialog.dismiss();

                            }
                        });

                        dialog.show();

                    } else {
                        Toast.makeText(getApplicationContext(), "error lainnya", Toast.LENGTH_LONG).show();
                    }

                }
                else if(list_p == false)
                {
                    alert.showAlertDialog(WanitaKamar.this, "Data Terkunci!", "Silahkan hubungi kepala lapas untuk edit data", false);
                }


            }
        });



        listbloksel = (ListView)findViewById(R.id.list_sel_wanita);

        listbloksel.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

                alert.showAlertDialog(WanitaKamar.this, "Data Input Failed!", "Hanya penjaga laki-laki yang dapat memasukkan data kamar blok sel", false);



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
            pDialog = ProgressDialog.show(WanitaKamar.this, "Please wait...", "Silahkan tunggu");
            //pDialog.show(KepalaReguAbsenKamar.this, "Please wait...", "Silahkan tunggu");


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

                            String blok = c.getString(TAG_BLOK);
                            String no_kamar = c.getString(TAG_NO);
                            String jumlah = c.getString(TAG_JUMLAH);
                            String lock = c.getString(TAG_LOCK);

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
                                bloka.put(TAG_LOCK, lock);
                                BlokA.add(bloka);
                                dikunci_l = lock;
                            }
                            else if(blok.equalsIgnoreCase("b"))
                            {
                                blokb.put(TAG_BLOK, blok);
                                blokb.put(TAG_NO, no_kamar);
                                blokb.put(TAG_JUMLAH, jumlah);
                                blokb.put(TAG_LOCK, lock);
                                BlokB.add(blokb);
                                dikunci_l = lock;
                            }
                            else if(blok.equalsIgnoreCase("wanita"))
                            {
                                blokwanita.put(TAG_BLOK, blok);
                                blokwanita.put(TAG_NO, no_kamar);
                                blokwanita.put(TAG_JUMLAH, jumlah);
                                blokwanita.put(TAG_LOCK, lock);
                                BlokWanita.add(blokwanita);
                                dikunci_p = lock;
                            }
                            else if(blok.equalsIgnoreCase("sel"))
                            {
                                bloksel.put(TAG_BLOK, blok);
                                bloksel.put(TAG_NO, no_kamar);
                                bloksel.put(TAG_JUMLAH, jumlah);
                                bloksel.put(TAG_LOCK, lock);
                                BlokSel.add(bloksel);
                                dikunci_s = lock;
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

            if (dikunci_l.equalsIgnoreCase("2"))
            {
                verr.setText("TELAH DIVERIFIKASI");
                kunci_l.setClickable(false);

            }
            else if(dikunci_l.equalsIgnoreCase("1"))
            {
                kunci_l.setClickable(false);
            }
            else if(dikunci_l.equalsIgnoreCase("0"))
            {
                kunci_l.setClickable(true);
            }

            if (dikunci_p.equalsIgnoreCase("2"))
            {
                //verr.setText("TELAH DIVERIFIKASI");
                kunci_p.setClickable(false);

            }
            else if(dikunci_p.equalsIgnoreCase("1"))
            {
                kunci_p.setClickable(false);
            }
            else if(dikunci_p.equalsIgnoreCase("0"))
            {
                kunci_p.setClickable(true);
            }

            if (dikunci_s.equalsIgnoreCase("2"))
            {
                //verr.setText("TELAH DIVERIFIKASI");
                kunci_s.setClickable(false);

            }
            else if(dikunci_s.equalsIgnoreCase("1"))
            {
                kunci_s.setClickable(false);
            }
            else if(dikunci_s.equalsIgnoreCase("0"))
            {
                kunci_s.setClickable(true);
            }

            /**
             * Updating parsed JSON data into ListView
             * */

            ListAdapter adapter_bloka = new SimpleAdapter(
                    WanitaKamar.this, BlokA,
                    R.layout.list_item_kamar, new String[] {TAG_NO, TAG_JUMLAH, TAG_LOCK}, new int[] {R.id.kamar,
                    R.id.jumlah, R.id.lock2});

            ListAdapter adapter_blokb = new SimpleAdapter(
                    WanitaKamar.this, BlokB,
                    R.layout.list_item_kamar, new String[] {TAG_NO, TAG_JUMLAH, TAG_LOCK}, new int[] {R.id.kamar,
                    R.id.jumlah, R.id.lock2});

            ListAdapter adapter_blokwanita = new SimpleAdapter(
                    WanitaKamar.this, BlokWanita,
                    R.layout.list_item_kamar, new String[] {TAG_NO, TAG_JUMLAH, TAG_LOCK}, new int[] {R.id.kamar,
                    R.id.jumlah, R.id.lock2});

            ListAdapter adapter_bloksel = new SimpleAdapter(
                    WanitaKamar.this, BlokSel,
                    R.layout.list_item_kamar, new String[] {TAG_NO, TAG_JUMLAH, TAG_LOCK}, new int[] {R.id.kamar,
                    R.id.jumlah, R.id.lock2});

            listbloka.setAdapter(adapter_bloka);
            listblokb.setAdapter(adapter_blokb);
            listblokwanita.setAdapter(adapter_blokwanita);
            listbloksel.setAdapter(adapter_bloksel);



        }

    }

    private void submit_kamar(final String tanggal, final String blok, String no_kamar, String jumlah) {

        class LoginAsync extends AsyncTask<String, Void, String>{


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected String doInBackground(String... parameter) {

                String tanggal = parameter[0];
                String blok = parameter[1];
                String no_kamar = parameter[2];
                String jumlah = parameter[3];


                InputStream is = null;
                List<NameValuePair> valuepairs = new ArrayList<NameValuePair>();
                valuepairs.add(new BasicNameValuePair("tanggal", tanggal));
                valuepairs.add(new BasicNameValuePair("blok", blok));
                valuepairs.add(new BasicNameValuePair("no_kamar", no_kamar));
                valuepairs.add(new BasicNameValuePair("jumlah", jumlah));
                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://smpn1jatiroto.sch.id/lapaslmj/inputabsenkamar.php");
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
                        Toast.makeText(getApplicationContext(), "Input jumlah wbs berhasil", Toast.LENGTH_SHORT).show();

                    }
                    else if (res.equalsIgnoreCase("gagal"))
                    {
                        Toast.makeText(getApplicationContext(), "Input jumlah wbs gagal", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Another error", Toast.LENGTH_SHORT).show();

                    }

                    if(blok.equalsIgnoreCase("a") || blok.equalsIgnoreCase("b"))
                    {
                        lis_l = res;
                    }
                    else if(blok.equalsIgnoreCase("sel"))
                    {
                        lis_s = res;
                    }
                    else if(blok.equalsIgnoreCase("wanita"))
                    {
                        lis_p = res;
                    }
                }
                else
                {
                    //pDialog.dismiss();
                    alert.showAlertDialog(WanitaKamar.this, "Data tidak ditemukan!", "Cek koneksi internet anda kemudian coba lagi", false);
                }


            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(tanggal, blok, no_kamar, jumlah);

    }

    private void lock_kamar(final String tanggal, final String kode, String kunci) {

        class LoginAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(WanitaKamar.this, "Please wait", "Locking...");

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

                    if(kode == "a" || kode == "b")
                    {
                        lis_l = "berhasil";
                    }
                    else if (kode == "s")
                    {
                        lis_s = "berhasil";
                    }
                    else if(kode == "p")
                    {
                        lis_p = "berhasil";
                    }

                    if(hasil.equalsIgnoreCase("berhasil")){
                        Toast.makeText(getApplicationContext(), "Penguncian data kamar berhasil", Toast.LENGTH_SHORT).show();

                    }
                    else if (hasil.equalsIgnoreCase("gagal"))
                    {
                        Toast.makeText(getApplicationContext(), "Penguncian data kamar gagal", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Another error", Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    loadingDialog.dismiss();
                    alert.showAlertDialog(WanitaKamar.this, "Data tidak ditemukan!", "Cek koneksi internet anda kemudian coba lagi", false);
                }


            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(tanggal, kode, kunci);

    }


}
