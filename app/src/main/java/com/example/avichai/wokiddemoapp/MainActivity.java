package com.example.avichai.wokiddemoapp;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static com.example.avichai.wokiddemoapp.R.id.textCart;

public class MainActivity extends AppCompatActivity {
    String prodStr="11";
    String payStr="11";





    int num_cartC=0;

    private RadioGroup radioGroup;







        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.wokid);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        setContentView(R.layout.activity_main);

        //perform operation every second (check change in cart)

    }




    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.cart1:
                if (checked)
                    num_cartC=0;
                break;
            case R.id.cart2:
                if (checked)
                    num_cartC=1;
                break;
            case R.id.cart3:
                if (checked)
                    num_cartC=2;
                break;
            case R.id.cart4:
                if (checked)
                    num_cartC=3;
                break;
        }
    }



    public void proceedCheckout(View view) {
        // Do something in response to button

        Intent intent = new Intent(this, PaymentActivity.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra("products_in_cart",payStr);

        startActivity(intent);
    }



    Handler h = new Handler();
    int delay = 100; //0.1 second
    Runnable runnable;
    @Override
    protected void onStart() {
//start handler as activity become visible

        h.postDelayed(new Runnable() {
            public void run() {
                updateFromServer();
                printCart();
                runnable=this;

                h.postDelayed(runnable, delay);
            }
        }, delay);

        super.onStart();
    }

    @Override
    protected void onPause() {
        h.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }












    //Utility function and classes for HTTP request
    public String downloadText (String URL) {

        String ergebnis;
        try {
            ergebnis = new DownloadTextTask().execute(URL).get();
        } catch (InterruptedException e) {
            ergebnis = "Keine Daten";
        } catch (ExecutionException e) {
            ergebnis = "Keine Daten";
        }
        return ergebnis;
    }

    private class DownloadTextTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                InputStream inputStream = downloadInhalt(urls[0]);
                String Text = InputStreamToString(inputStream);
                inputStream.close();
                return Text;

            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
    }
    private InputStream downloadInhalt(String myurl) throws IOException {
        InputStream is;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream();
            return is;
        } catch (Exception e) {
            return null;
        }
    }

    private String InputStreamToString(InputStream stream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        return total.toString();
    }











    private void updateFromServer(){
        //The HTTP request from Wokid server, calls update of representation on screen
        try {
            String s = downloadText("http://www.wokiddemoapp-167715.appspot.com");
            updateStrings(s);
        } catch (Exception e) {
            Log.d("error", e.toString());
            e.printStackTrace();
        }
    }



    private void printCart(){
        TextView cartText = (TextView)findViewById(textCart);
        //String displayMe="Corny\nWafles";
        String displayMe=prodStr;
        cartText.setText(displayMe);
    }

    private void updateStrings(String inputLine) {
        //Use input data to update string list
        //String[] prodArray = new String[]{"Bags","Olives","Mushroon Noodles","Reva Lesheva","Veg Noodles"};
        //int[] payArray = new int[]{3,1,5,4,6};
        String[] prodArray = new String[]{"Waffles","Bags"};
        int[] payArray = new int[]{3,2};

        int s=0;
        int num_cart;
        prodStr="";
        payStr="Your bill:\n";
/* For two carts this is better
        CheckBox c;
        c = (CheckBox) findViewById(R.id.checkBox);
        if (c.isChecked())
            num_cart=1;
        else
            num_cart=0;
*/

        for(int i=0;i<2;i++)
            if(inputLine.charAt(2*num_cartC+i)=='1'){
                prodStr=prodStr+prodArray[i]+'\n';
                payStr=payStr+prodArray[i]+' '+String.valueOf(payArray[i])+"$\n";
                s=s+payArray[i];
        }
        payStr=payStr+"Total: "+String.valueOf(s)+"$\nThank you,\nCome again!";


    }









}
