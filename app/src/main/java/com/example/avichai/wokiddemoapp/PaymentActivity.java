package com.example.avichai.wokiddemoapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class PaymentActivity extends AppCompatActivity {
    String payStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setLogo(R.drawable.wokid);
        //getSupportActionBar().setDisplayUseLogoEnabled(true);

        setContentView(R.layout.activity_payment);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            payStr= null;
        } else {
            payStr= extras.getString("products_in_cart");
        }

        printPayment();




    }

    private void printPayment(){
        TextView billText = (TextView)findViewById(R.id.textBill);
        //String displayMe="Your Bill:\n\nCorny: 5$\nWafles: 3$\nTotal: 8$\nThank you!\nCome again!";
        billText.setText(payStr);
    }

}
