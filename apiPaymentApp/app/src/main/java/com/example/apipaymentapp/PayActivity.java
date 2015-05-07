package com.example.apipaymentapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.apipaymentapp.Content.Products;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;


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

        DecimalFormat df = new DecimalFormat("'$'0.00");
        TextView detailText = (TextView) findViewById(R.id.detailText);
        detailText.setText(p.toString());
        TextView donateDetailText = (TextView) findViewById(R.id.donateDetailText);
        donateDetailText.setText("By selecting a charity, " + df.format(donationPrice) + " will be donated. You will be charged a total of " + df.format(roundedPrice) + ".");
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
                Globals.totalDonations += donationPrice;
            }
            else {
                price = productPrice;
            }

            processPayment(productPrice);
            processPayment(roundedPrice);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    private void processPayment(double price) {
        System.out.println(price);
        final double fPrice = price;

        FutureTask future = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                try {
                    Unirest.setDefaultHeader("Content-Type", "application/json");
                    Unirest.setDefaultHeader("licenseid", Vantiv.licenseid);
                    JSONObject json = Vantiv.getBody(fPrice);
                    Log.d("Vantiv", "sending auth");
                    HttpResponse<JsonNode> authResponse = Unirest.post("https://apis.cert.vantiv.com/v1/credit/authorization")
                            .queryString("sp", "1")
                            .body(json.toString())
                            .asJson();

                    Log.d("Vantiv/AuthResponse", authResponse.getBody().toString());

                    String authCode = Vantiv.getAuthorizationCodeFromResponse(authResponse);
                    String refNumber = Vantiv.getReferenceNumberFromResponse(authResponse);

                    json = Vantiv.getBody(fPrice);
                    json.getJSONObject("transaction").put("OriginalReferenceNumber", refNumber);
                    json.getJSONObject("transaction").put("AuthorizationCode", authCode);
                    json.getJSONObject("transaction").put("OriginalAuthorizedAmount", fPrice);
                    json.getJSONObject("transaction").put("CaptureAmount", fPrice);
                    HttpResponse<JsonNode> captureResponse = Unirest.post("https://apis.cert.vantiv.com/v1/credit/authorizationcompletion")
                            .queryString("sp", "1")
                            .body(new JsonNode(json.toString()))
                            .asJson();

                    System.out.println("CaptureResponse: " + captureResponse.getBody().toString());


                    // Send Purchase
                    json = Vantiv.getBody(fPrice);
                    HttpResponse<JsonNode> purchaseResponse = Unirest.post("https://apis.cert.vantiv.com/v1/credit")
                            .queryString("sp", "1")
                            .body(new JsonNode(json.toString()))
                            .asJson();
                    System.out.println("PurchaseResponse: " + captureResponse.getBody().toString());

                    // Send Cancel
                    authCode = Vantiv.getAuthorizationCodeFromResponse(purchaseResponse);
                    refNumber = Vantiv.getReferenceNumberFromResponse(purchaseResponse);
                    String transactionTimestamp = Vantiv.getTransmissionTimestampFromResponse(purchaseResponse);
                    json = Vantiv.getBody(fPrice);
                    json.getJSONObject("transaction").put("OriginalReferenceNumber", refNumber);
                    json.getJSONObject("transaction").put("OriginalAuthCode", authCode);
                    json.getJSONObject("transaction").put("OriginalTransactionTimestamp", transactionTimestamp);
                    json.getJSONObject("transaction").put("CancelType", "purchase");

                    HttpResponse<JsonNode> cancelResponse = Unirest.post("https://apis.cert.vantiv.com/v1/credit/reversal")
                            .queryString("sp", "1")
                            .body(new JsonNode(json.toString()))
                            .asJson();

                    System.out.println("CancelResponse: " + cancelResponse.getBody().toString());
                } catch (Exception e) {
                    Log.d("Vantiv Exception", e.toString());
                    return false;
                }
                return true;
            }
        });


        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.execute(future);
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
