package com.example.apipaymentapp;

import java.text.DecimalFormat;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	static double chocolate;
    static double oil;
    static double eighth;
    static double quarter;
	static double donationAmount;
	static double totalAmount;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	//Supposed to use BigDouble to represent money but #yolo
    	chocolate = 18.64;
        oil = 27.97;
        eighth = 55.94;
        quarter = 93.24;
        donationAmount = 0.00;
        totalAmount = 0.00;
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    public void updateValues()
    {
    	DecimalFormat df = new DecimalFormat("#.00");

    	TextView widgetText = (TextView) findViewById(R.id.textView1);
    	widgetText.setText("Widgets:  " + numWidgets);

    	TextView donationText = (TextView) findViewById(R.id.TextView03);
    	donationText.setText("Donation: $" + df.format(donationAmount));
    	
    	TextView totalText = (TextView) findViewById(R.id.TextView02);
    	totalText.setText("Total: $" + df.format(totalAmount));
    }
    
    public void addWidgetClicked(View v)
    {
    	numWidgets ++;
    	totalAmount = (numWidgets * widgetPrice) + donationAmount;
    	
    	updateValues();
    	
    }
    
    public void donateClicked(View v)
    {
    	totalAmount = (numWidgets * widgetPrice);	//clears previous donation from total

    	int nextDollar = 0;

    	nextDollar = (int)totalAmount + 1;	//Bugs on x.00
    	
    	donationAmount = (double) nextDollar - totalAmount;
    	
    	totalAmount = (numWidgets * widgetPrice) + donationAmount;
    	
    	updateValues();
    	
    	
    }
    
    public void payClicked(View v)
    {
    	//Do something magical
    	
    	//pass donationAmount and totalAmount to an AsyncTask I think
    	
    	
    	
    }
    
}
