package com.example.apipaymentapp;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.TimeZone;


/**
 * Created by ssteers on 5/6/15.
 */
public class Vantiv {
    static final String licenseid = "YOUR LICENSE";

    public static LinkedHashMap<String, Object> getCard() {
        LinkedHashMap<String, Object> obj = new LinkedHashMap<String, Object>();
        obj.put("CardType", "visa");
        obj.put("PartialApprovalCode", "not_supported");
        obj.put("CardNumber", "4445222299990007");
        obj.put("ExpirationMonth", "12");
        obj.put("ExpirationYear", "2017");
        obj.put("CVV", "382");

        return obj;
    }

    public static LinkedHashMap<String, Object> getAddress() {
        LinkedHashMap<String, Object> obj = new LinkedHashMap<String, Object>();
        obj.put("BillingAddress1", "1234 main street");
        obj.put("BillingCity", "Cincinnati");
        obj.put("BillingState", "OH");
        obj.put("BillingZipcode", "45209");
        obj.put("CountryCode", "US");

        return obj;
    }

    public static LinkedHashMap<String, Object> getTransaction() {

        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());

        LinkedHashMap<String, Object> obj = new LinkedHashMap<String, Object>();
        obj.put("TransactionID", "123456");
        obj.put("PaymentType", "single");
        obj.put("ReferenceNumber", "100001");
        obj.put("DraftLocatorID", "100000001");
        obj.put("ClerkNumber", "1234");
        obj.put("MarketCode", "present");
        obj.put("TransactionTimestamp", nowAsISO);
        obj.put("SystemTraceID", "100002");
        obj.put("TokenRequested", "false");
        obj.put("PartialApprovalCode", "not_supported");
        obj.put("TransactionAmount", "10.00");

        return obj;
    }

    public static LinkedHashMap<String, Object> getTransaction(String transactionAmount) {

        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());

        LinkedHashMap<String, Object> obj = new LinkedHashMap<String, Object>();
        obj.put("TransactionID", "123456");
        obj.put("PaymentType", "single");
        obj.put("ReferenceNumber", "100001");
        obj.put("DraftLocatorID", "100000001");
        obj.put("ClerkNumber", "1234");
        obj.put("MarketCode", "present");
        obj.put("TransactionTimestamp", nowAsISO);
        obj.put("SystemTraceID", "100002");
        obj.put("TokenRequested", "false");
        obj.put("PartialApprovalCode", "not_supported");
        obj.put("TransactionAmount", transactionAmount);

        return obj;
    }

    public static LinkedHashMap<String, Object> getTerminal() {
        LinkedHashMap<String, Object> obj = new LinkedHashMap<String, Object>();
        obj.put("TerminalID", "1");
        obj.put("EntryMode", "manual");
        obj.put("Ipv4Address", "192.0.2.235");
        obj.put("TerminalEnvironmentalCode", "electronic_cash_register");
        obj.put("PinEntry", "none");
        obj.put("BalanceInquiry", "false");
        obj.put("HostAdjustment", "false");
        obj.put("DeviceType", "Terminal");
        obj.put("CardInputCode", "ManualKeyed");

        return obj;
    }

    public static LinkedHashMap<String, Object> getMerchant() {
        LinkedHashMap<String, Object> obj = new LinkedHashMap<String, Object>();
        obj.put("NetworkRouting", "2J");
        obj.put("CashierNumber", "12345678");
        obj.put("LaneNumber", "123");
        obj.put("DivisionNumber", "000");
        obj.put("ChainCode", "70110");
        obj.put("StoreNumber", "00000001");
        obj.put("MerchantID", "4445012916098");

        return obj;
    }

    public static JSONObject getBody() {
        LinkedHashMap<String, Object> obj = new LinkedHashMap<String, Object>();
        obj.put("merchant", getMerchant());
        obj.put("terminal", getTerminal());
        obj.put("transaction", getTransaction());
        obj.put("address", getAddress());
        obj.put("card", getCard());

        return new JSONObject(obj);
    }

    public static JSONObject getBody(double price) {
        LinkedHashMap<String, Object> obj = new LinkedHashMap<String, Object>();
        obj.put("merchant", getMerchant());
        obj.put("terminal", getTerminal());
        obj.put("transaction", getTransaction(String.format("%.2f", price)));
        obj.put("address", getAddress());
        obj.put("card", getCard());

        return new JSONObject(obj);
    }

    public static String getReferenceNumberFromResponse(HttpResponse<JsonNode> response) {
        JSONObject json = response.getBody().getObject();
        String key = (String) json.keys().next();
        try {
            return json.getJSONObject(key).getString("ReferenceNumber");
        }
        catch (Exception e) {
            return null;
        }

    }

    public static String getAuthorizationCodeFromResponse(HttpResponse<JsonNode> response) {
        JSONObject json = response.getBody().getObject();
        String key = (String) json.keys().next();
        try {
            return json.getJSONObject(key).getString("AuthorizationCode");
        }
        catch (Exception e) {
            return null;
        }
    }

    public static String getTransmissionTimestampFromResponse(HttpResponse<JsonNode> response) {
        JSONObject json = response.getBody().getObject();
        String key = (String) json.keys().next();
        try {
            return json.getJSONObject(key).getString("TransmissionTimestamp");
        }
        catch (Exception e) {
            return null;
        }
    }
}
