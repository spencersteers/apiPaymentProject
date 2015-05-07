package com.example.apipaymentapp;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.apipaymentapp.Content.Products;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final int PAY_REQUEST = 1;

	static double chocolate;
    static double oil;
    static double eighth;
    static double quarter;
	static double donationAmount;
	static double totalAmount;

    static int numWidgets;
    static double widgetPrice;

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    	//Supposed to use BigDouble to represent money but #yolo
    	chocolate = 18.64;
        oil = 27.97;
        eighth = 55.94;
        quarter = 93.24;
        donationAmount = 0.00;
        totalAmount = 0.00;

        button1 = (Button)findViewById(R.id.button1);
        button2 = (Button)findViewById(R.id.button2);
        button3 = (Button)findViewById(R.id.button3);
        button4 = (Button)findViewById(R.id.button4);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, PayActivity.class);
        if(v == button1) {
            intent.putExtra("productIndex", 0);
        }
        else if(v == button2) {
            intent.putExtra("productIndex", 1);
        }
        else if (v == button3) {
            intent.putExtra("productIndex", 2);
        }
        else if (v == button4) {
            intent.putExtra("productIndex", 3);
        }
        startActivityForResult(intent, PAY_REQUEST);
    }

    @Override
    protected void onActivityResult(int aRequestCode, int aResultCode, Intent aData) {
        updateTotal();
        super.onActivityResult(aRequestCode, aResultCode, aData);
    }
    
    public void updateTotal()
    {
    	DecimalFormat df = new DecimalFormat("'$'0.00");

    	TextView totalDonationsText = (TextView) findViewById(R.id.totalDonationsText);
        totalDonationsText.setText("Total Donated: " + df.format(Globals.totalDonations));
    }
}
