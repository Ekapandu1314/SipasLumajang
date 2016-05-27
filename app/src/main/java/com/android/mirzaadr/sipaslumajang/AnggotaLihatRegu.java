package com.android.mirzaadr.sipaslumajang;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
public class AnggotaLihatRegu extends Activity {

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

    public String tanggal;
    public String bulan;
    public String tanggall;
    public String tahun;
    public String shift;
    public String regu1;

    Session session;

    public static final String TANGGAL = "TANGGAL";
    public static final String SHIFT = "SHIFT";

    // contacts JSONArray
    JSONArray penjaga = null;
    int success;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> contactList;

    ListView lv;

    TextView regu;

    TextView tgl;

    public String bulbul;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.absen_anggota_view);

        tanggall = getParent().getIntent().getStringExtra(Pilih_Tanggal.TANGGAL);
        bulan = getParent().getIntent().getStringExtra(Pilih_Tanggal.BULAN);
        tahun  = getParent().getIntent().getStringExtra(Pilih_Tanggal.TAHUN);
        tanggal = bulan + tanggall + tahun;

        shift = getParent().getIntent().getStringExtra(Pilih_Tanggal.SHIFT);

        tgl = (TextView)findViewById(R.id.tgl_anggota);

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

        session = new Session(getApplicationContext());

        session.checkLogin();

        HashMap<String, String> user = session.getUserDetails();

        regu1 = user.get(Session.KEY_TEAM);

        contactList = new ArrayList<HashMap<String, String>>();

        regu = (TextView)findViewById(R.id.regugu);

        regu.setText("Regu " + regu1.toUpperCase());

        lv = (ListView)findViewById(R.id.list_anggota);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

                //final String nama = ((TextView) view.findViewById(R.id.nama)).getText().toString();
                //final String lock = ((TextView) view.findViewById(R.id.lock)).getText().toString();
                //final String regu = ((TextView) view.findViewById(R.id.regu)).getText().toString();
                //Toast.makeText(getApplicationContext(), nama + "\n" + regu, Toast.LENGTH_LONG).show();

                alert.showAlertDialog(AnggotaLihatRegu.this, "Input data gagal!", "Hanya kepala regu yang dapat memasukkan status anggota", false);

            }
        });

        new GetAbsen().execute();
    }

    private class GetAbsen extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = ProgressDialog.show(AnggotaLihatRegu.this, "Please wait...", "Silahkan tunggu");
            //pDialog.show(AnggotaLihatRegu.this, "Please wait..." , "Silahkan tunggu");


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
                /**
                 * Updating parsed JSON data into ListView
                 * */

                ListAdapter adapter = new SimpleAdapter(
                        AnggotaLihatRegu.this, contactList,
                        R.layout.list_item_penjaga, new String[] {TAG_NAMA, TAG_SHIFT, TAG_KET, TAG_LOCK}, new int[] {R.id.nama1,
                        R.id.regu1, R.id.keterangan1, R.id.lock1 });

                lv.setAdapter(adapter);


        }

    }

}
