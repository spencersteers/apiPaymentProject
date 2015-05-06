package com.example.apipaymentapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.apipaymentapp.Content.Products;

import java.text.DecimalFormat;


public class PayActivity extends Activity implements View.OnClickListener {

    private boolean shouldDonate = false;

    private Button charityButton1;
    private Button charityButton2;
    private Button charityButton3;
    private Button charityButton4;
    private Button charityButton5;
    private Button payButton;

    private double productPrice;
    private double roundedPrice;
    private double donationPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        charityButton1 = (Button)findViewById(R.id.charityButton1);
        charityButton2 = (Button)findViewById(R.id.charityButton2);
        charityButton3 = (Button)findViewById(R.id.charityButton3);
        charityButton4 = (Button)findViewById(R.id.charityButton4);
        charityButton5 = (Button)findViewById(R.id.charityButton5);
        payButton = (Button)findViewById(R.id.payButton);

        charityButton1.setOnClickListener(this);
        charityButton2.setOnClickListener(this);
        charityButton3.setOnClickListener(this);
        charityButton4.setOnClickListener(this);
        charityButton5.setOnClickListener(this);
        payButton.setOnClickListener(this);

        int productIndex = getIntent().getIntExtra("productIndex", 0);
        Products.Product p = Products.PRODUCTS.get(productIndex);

        productPrice = p.price;
        roundedPrice = Math.ceil(p.price);
        donationPrice = roundedPrice - productPrice;

        DecimalFormat df = new DecimalFormat("#0.00");
        TextView detailText = (TextView) findViewById(R.id.detailText);
        detailText.setText(p.toString());
        TextView donateDetailText = (TextView) findViewById(R.id.donateDetailText);
        donateDetailText.setText("By selecting a charity, $" + df.format(donationPrice) + " will be donated. You will be charged a total of " + roundedPrice + ".");
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        if(v == charityButton1) {
            toggleButton(charityButton1);
        }
        else if(v == charityButton2) {
            toggleButton(charityButton2);
        }
        else if (v == charityButton3) {
            toggleButton(charityButton3);
        }
        else if (v == charityButton4) {
            toggleButton(charityButton4);
        }
        else if (v == charityButton5) {
            toggleButton(charityButton5);
        }
        else if (v == payButton) {
            double price;
            if (shouldDonate) {
                price = roundedPrice;
            }
            else {
                price = productPrice;
            }
            processPayment(productPrice);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    private void processPayment(double price) {
        System.out.println(price);
        if (shouldDonate) {
            Globals.totalDonations += donationPrice;
        }

    }

    private void toggleButton(Button button) {
        if (button.isSelected()) {
            button.setSelected(false);
            shouldDonate = false;
        }
        else {
            charityButton1.setSelected(false);
            charityButton2.setSelected(false);
            charityButton3.setSelected(false);
            charityButton4.setSelected(false);
            charityButton5.setSelected(false);

            button.setSelected(true);
            shouldDonate = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pay, menu);
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
    }
}
