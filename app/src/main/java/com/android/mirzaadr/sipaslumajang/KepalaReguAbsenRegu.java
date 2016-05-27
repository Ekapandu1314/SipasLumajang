package com.android.mirzaadr.sipaslumajang;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
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
public class KepalaReguAbsenRegu extends Activity {

    private Dialog pDialog;
    AlertDialogManager alert = new AlertDialogManager();

    // URL to get contacts JSON
    private static String url = "http://smpn1jatiroto.sch.id/lapaslmj/cektanggal.php";

    // JSON Node names
    private static final String TAG_PENJAGA = "penjaga";
    private static final String TAG_IDP = "idp";
    private static final String TAG_NAMA = "nama";
    private static final String TAG_REGU = "regu";
    private static final String TAG_SHIFT = "shift";
    private static final String TAG_KET = "ket";
    private static final String TAG_LOCK = "kunci";
    private static final String TAG_SUCCESS = "success";

    public static final String KUNCI = "kunci";

    final Context context = this;

    public String tanggal;
    public String tahun;
    public String bulan;
    public String tanggall;


    public String shift;
    public String regu1;

    //public String status_anggota;

    //String kunci;

    TextView verr;
    TextView tgl;

    ListView lv;

    Button next;

    Session session;

    String dikunci;

    Button button_kunci;

    Boolean klik = true;

    public String bulbul;

    // contacts JSONArray
    JSONArray penjaga = null;
    int success;

    public static final String TANGGAL = "TANGGAL";

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> contactList;

    private String[] status_spin = { "hadir", "Sakit", "Ijin","Libur", "Tugas", "Lainnya" };

    public String statuss;

    Spinner spinnerOsversions;

    public String cek = "coba";

    TextView regu;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kepala_regu_absen_penjaga_view);

        tanggall = getParent().getIntent().getStringExtra(Pilih_Tanggal.TANGGAL);
        bulan = getParent().getIntent().getStringExtra(Pilih_Tanggal.BULAN);
        tahun  = getParent().getIntent().getStringExtra(Pilih_Tanggal.TAHUN);
        tanggal = bulan + tanggall + tahun;

        tgl = (TextView)findViewById(R.id.tgl_kreguabs);

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

        shift = getParent().getIntent().getStringExtra(Pilih_Tanggal.SHIFT);

        button_kunci = (Button)findViewById(R.id.lock_pjg);

        session = new Session(getApplicationContext());
        session.checkLogin();

        final ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, status_spin);
        adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        verr = (TextView)findViewById(R.id.ver_penjaga);

        HashMap<String, String> user = session.getUserDetails();


        regu1 = user.get(Session.KEY_TEAM);

        regu = (TextView)findViewById(R.id.regugugu);

        regu.setText("Regu " + regu1.toUpperCase());

        button_kunci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lock_penjaga("1", tanggal, regu1);


            }
        });


        contactList = new ArrayList<HashMap<String, String>>();

        lv = (ListView)findViewById(R.id.list_kepala);

        // Listview on item click listener

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {

                    if (klik == true) {
                        final String nama = ((TextView) view.findViewById(R.id.nama1)).getText().toString();
                        final String lock = ((TextView) view.findViewById(R.id.lock1)).getText().toString();
                        final String shiftt = ((TextView) view.findViewById(R.id.regu1)).getText().toString();
                        //final String regu = ((TextView) view.findViewById(R.id.regu)).getText().toString();
                        //Toast.makeText(getApplicationContext(), nama + "\n" + regu, Toast.LENGTH_LONG).show();

                        if (lock.equalsIgnoreCase("1")) {
                            alert.showAlertDialog(KepalaReguAbsenRegu.this, "Data Terkunci!", "Silahkan hubungi kepala lapas untuk edit data", false);
                        } else if (lock.equalsIgnoreCase("0")) {
                            final Dialog dialog = new Dialog(KepalaReguAbsenRegu.this);
                            dialog.setContentView(R.layout.dialog_box_penjaga);
                            dialog.setTitle("Masukkan status....");

                            TextView text = (TextView) dialog.findViewById(R.id.nama_dialog_box);
                            text.setText(nama);
                            final Button submit_status = (Button) dialog.findViewById(R.id.button_submit_dialog_box);

                            System.out.println(status_spin.length);

                            spinnerOsversions = (Spinner) dialog.findViewById(R.id.spinner_status);
                            spinnerOsversions.setAdapter(adapter_state);

                            spinnerOsversions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view,
                                                           int position, long id) {
                                    spinnerOsversions.setSelection(position);
                                    String selState = (String) spinnerOsversions.getSelectedItem();
                                    statuss = selState;
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> arg0) {

                                }
                            });
                            ;


                            submit_status.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    //EditText status = (EditText) dialog.findViewById(R.id.status_dialog_box);
                                    //String status_anggota = status.getText().toString();

                                    if (shiftt.equalsIgnoreCase("kosong")) {
                                        submit_status(tanggal, shift, nama, regu1, statuss);
                                        ((TextView) view.findViewById(R.id.keterangan1)).setText(statuss);
                                        ((TextView) view.findViewById(R.id.regu1)).setText(shift);
                                    } else {
                                        submit_status(tanggal, shiftt, nama, regu1, statuss);
                                        ((TextView) view.findViewById(R.id.keterangan1)).setText(statuss);
                                        ((TextView) view.findViewById(R.id.regu1)).setText(shiftt);
                                    }





                                    dialog.dismiss();

                                }
                            });

                            dialog.show();

                        } else if (lock.equalsIgnoreCase("3")) {
                            alert.showAlertDialog(KepalaReguAbsenRegu.this, "Input Failed!", "Anda belum waktunya absen", false);
                        } else if (lock.equalsIgnoreCase("2")) {
                            alert.showAlertDialog(KepalaReguAbsenRegu.this, "Data Terkunci!", "Data telah diverifikasi oleh kepala lapas", false);
                        } else {
                            Toast.makeText(getApplicationContext(), "error lainnya", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (klik == false) {
                        alert.showAlertDialog(KepalaReguAbsenRegu.this, "Data Terkunci!", "Silahkan hubungi kepala lapas untuk edit data", false);
                    }




                }
            });

        new GetAbsen().execute();
    }

    private class GetAbsen extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = ProgressDialog.show(KepalaReguAbsenRegu.this, "Please wait...", "Silahkan tunggu");
            //pDialog.show(KepalaReguAbsenRegu.this, "Please wait...", "Slilahkan tungggu");


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("tanggal", tanggal));
            params.add(new BasicNameValuePair("shift", shift));
            params.add(new BasicNameValuePair("regu", regu1));

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.POST, params);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    penjaga = jsonObj.getJSONArray(TAG_PENJAGA);
                    success = jsonObj.getInt(TAG_SUCCESS);

                    if (success == 1)
                    {
                        for (int i = 0; i < penjaga.length(); i++) {
                            JSONObject c = penjaga.getJSONObject(i);

                            String idp = c.getString(TAG_IDP);
                            String nama = c.getString(TAG_NAMA);
                            String shift = c.getString(TAG_SHIFT);
                            String ket = c.getString(TAG_KET);
                            String lock = c.getString(TAG_LOCK);


                            // Phone node is JSON Object

                            // tmp hashmap for single contact
                            HashMap<String, String> penjaga = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            penjaga.put(TAG_IDP, idp);
                            penjaga.put(TAG_NAMA, nama);
                            penjaga.put(TAG_SHIFT, shift);
                            penjaga.put(TAG_KET, ket);
                            penjaga.put(TAG_LOCK, lock);

                            dikunci = lock;

                            // adding contact to contact list
                            contactList.add(penjaga);
                        }
                    }
                    // looping through All Contacts

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
            {
                pDialog.dismiss();}

                if (dikunci.equalsIgnoreCase("2"))
                {
                    verr.setText("TELAH DIVERIFIKASI");
                    button_kunci.setClickable(false);
                }
                else if (dikunci.equalsIgnoreCase("1"))
                {
                    //verr.setText("TELAH DIKUNCI");
                    button_kunci.setClickable(false);
                }
                else if (dikunci.equalsIgnoreCase("0"))
                {
                    //verr.setText("TELAH DIKUNCI");
                    button_kunci.setClickable(true);
                }

                /**
                 * Updating parsed JSON data into ListView
                 * */

                ListAdapter adapter = new SimpleAdapter(
                        KepalaReguAbsenRegu.this, contactList,
                        R.layout.list_item_penjaga, new String[] {TAG_NAMA, TAG_SHIFT, TAG_KET, TAG_LOCK}, new int[] {R.id.nama1,
                        R.id.regu1, R.id.keterangan1, R.id.lock1 });

                lv.setAdapter(adapter);



        }

    }

    private void submit_status(final String tanggal, String shift, String nama, String regu, String keterangan) {

        class LoginAsync extends AsyncTask<String, Void, String>{


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected String doInBackground(String... parameter) {

                String tanggal = parameter[0];
                String shift = parameter[1];
                String nama = parameter[2];
                String regu = parameter[3];
                String keterangan = parameter[4];

                InputStream is = null;
                List<NameValuePair> valuepairs = new ArrayList<NameValuePair>();
                valuepairs.add(new BasicNameValuePair("tanggal", tanggal));
                valuepairs.add(new BasicNameValuePair("shift", shift));
                valuepairs.add(new BasicNameValuePair("nama", nama));
                valuepairs.add(new BasicNameValuePair("regu", regu));
                valuepairs.add(new BasicNameValuePair("ket", keterangan));
                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://smpn1jatiroto.sch.id/lapaslmj/inputabsenjaga.php");
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
                    if (pDialog.isShowing())
                    {
                        pDialog.dismiss();}
                    String res = result.trim();



                    if(res.equalsIgnoreCase("berhasil")){
                        Toast.makeText(getApplicationContext(), "Input data berhasil", Toast.LENGTH_SHORT).show();


                    }
                    else if (res.equalsIgnoreCase("gagal"))
                    {
                        Toast.makeText(getApplicationContext(), "Input data lainnya", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Another error", Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    pDialog.dismiss();
                    alert.showAlertDialog(KepalaReguAbsenRegu.this, "Data tidak ditemukan!", "Cek koneksi internet anda kemudian coba lagi", false);
                }


            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(tanggal, shift, nama, regu, keterangan);

    }

    private void lock_penjaga(final String kunci, String tanggal, String regu) {

        class LoginAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(KepalaReguAbsenRegu.this, "Please wait", "Locking...");

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
                    if (pDialog.isShowing())
                    {
                        pDialog.dismiss();}
                    String hasil = result.trim();
                    loadingDialog.dismiss();

                    cek = hasil;

                    if(hasil.equalsIgnoreCase("berhasil")){
                        Toast.makeText(getApplicationContext(), "Penguncian data berhasil", Toast.LENGTH_SHORT).show();
                        klik = false;
                        button_kunci.setClickable(false);

                    }
                    else if (hasil.equalsIgnoreCase("gagal"))
                    {
                        Toast.makeText(getApplicationContext(), "Penguncian data gagal", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Another error", Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    pDialog.dismiss();
                    alert.showAlertDialog(KepalaReguAbsenRegu.this, "Data tidak ditemukan!", "Cek koneksi internet anda kemudian coba lagi", false);
                }


            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(kunci, tanggal, regu);

    }




}
