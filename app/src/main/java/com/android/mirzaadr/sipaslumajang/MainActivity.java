package com.android.mirzaadr.sipaslumajang;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
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


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private EditText editTextUserName;
    private EditText editTextPassword;

    Session session;
    AlertDialogManager alert = new AlertDialogManager();

    //public static final String USER = "USER";
    //public static final String NAME = "NAME";
    //public static final String TEAM = "TEAM";

    String username;
    String password;

    private Boolean exit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        session = new Session(getApplicationContext());

        editTextUserName = (EditText) findViewById(R.id.usernameField);
        editTextPassword = (EditText) findViewById(R.id.passwordField);
    }


        public void loginPost(View view){
            username = editTextUserName.getText().toString();
            password = editTextPassword.getText().toString();

            if(username.trim().length() > 0 && password.trim().length() > 0){

                login(username, password);

            }
            else{
                // user didn't entered username or password
                // Show alert asking him to enter the details
                alert.showAlertDialog(MainActivity.this, "Login failed..", "Please enter username and password", false);
                //Toast.makeText(getApplicationContext(), "Login Failed! Pleas enter username and password", Toast.LENGTH_LONG).show();

            }
        }

        private void login(final String username, String password) {

            class LoginAsync extends AsyncTask<String, Void, String> {

                private Dialog loadingDialog;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loadingDialog = ProgressDialog.show(MainActivity.this, "Please wait", "Silahkan tunggu...");
                }

                @Override
                protected String doInBackground(String... params) {

                    String user = params[0];
                    String pass = params[1];

                    InputStream is = null;
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("username", user));
                    nameValuePairs.add(new BasicNameValuePair("password", pass));
                    String result = null;

                    try{
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost("http://smpn1jatiroto.sch.id/lapaslmj/login2.php");
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

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

                    loadingDialog.dismiss();

                    if(result!=null)
                    {
                        String s_1 = result.trim();

                        String s_1array[] = s_1.split("%");



                        if(s_1array[0].equalsIgnoreCase("berhasil")){
                            //Toast.makeText(getApplicationContext(), s_1array[3], Toast.LENGTH_LONG).show();
                            //Intent intent = new Intent(MainActivity.this, KepalaTanggal.class);

                            String s_2array[] = s_1array[2].split("'");
                            session.createLoginSession(s_1array[1], s_2array[1], s_1array[3]);
                            //intent.putExtra(USER, sarray[1]);
                            Intent k = new Intent(getApplicationContext(), Pilih_Tanggal.class);
                            startActivity(k);
                            finish();



                        }
                        else if (s_1array[0].equalsIgnoreCase("gagal"))
                        {
                            //Toast.makeText(getApplicationContext(), "Login Failed! Username/Password is incorrect", Toast.LENGTH_LONG).show();
                            alert.showAlertDialog(MainActivity.this, "Login failed..", "Username/Password is incorrect", false);
                        }
                        else {
                            //Toast.makeText(getApplicationContext(), "Login Failed! Check your internet connection", Toast.LENGTH_LONG).show();
                            alert.showAlertDialog(MainActivity.this, "Login failed..", "Check your internet connection", false);
                        }
                    }
                    else
                    {
                        alert.showAlertDialog(MainActivity.this, "Login failed..", "Check your internet connection", false);
                    }


                }
            }

            LoginAsync la = new LoginAsync();
            la.execute(username, password);

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

/*
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
