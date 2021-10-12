/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 package LanguageDao;


import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;




public class PaymentHelper {
	private static final String PRIVATE_KEY = System.getenv("FLUTTERWAVE_PRIVATE_KEY");
    public static String payWithMoMo(String transactionId, String mobile, Double amount, String email, String customerName, String returnUrl) throws Exception{
//        String privateKey = "FLWSECK-a6ebdb49b7072a5ba2aa2f60247f733d-X";
        String privateKey = PRIVATE_KEY;
        HttpURLConnection client = (HttpURLConnection) new URL("https://api.flutterwave.com/v3/payments").openConnection();
        client.setRequestMethod("POST");
        client.setDoOutput(true);
        client.addRequestProperty("Content-type", "application/json");
        client.addRequestProperty("Authorization", "Bearer "+privateKey);
        JSONObject customer = new JSONObject();
        customer.put("name", customerName);
        customer.put("email", email);
        JSONObject data = new JSONObject();
        data.put("tx_ref", transactionId);
        data.put("amount", amount);
        data.put("currency", "USD");
        data.put("customer", customer);
        data.put("redirect_url", returnUrl);
        client.getOutputStream().write(data.toJSONString().getBytes());
        client.getInputStream();
        JSONParser jsonp = new JSONParser();
        String url = (String)(((JSONObject)((JSONObject)jsonp.parse(new InputStreamReader(client.getInputStream()))).get("data"))).get("link");
        return url;
    }
    
    public static PaymentData getPaymentData(String transactionId) throws Exception {
    	JSONObject json = null;
    	try {
    		String privateKey = PRIVATE_KEY;
            HttpURLConnection client = (HttpURLConnection) new URL("https://api.flutterwave.com/v3/transactions/"+transactionId+"/verify").openConnection();
            client.setRequestMethod("GET");
            client.setDoOutput(true);
            client.addRequestProperty("Content-type", "application/json");
            client.addRequestProperty("Authorization", "Bearer "+privateKey);
            JSONParser jsonParser = new JSONParser();
            json = (JSONObject) jsonParser.parse(new InputStreamReader(client.getInputStream()));
    	}catch(Exception e) {
    		e.printStackTrace();
    		throw new Exception("Connection error");
    	}
    	if(!json.get("status").equals("success")) {
    		throw new Exception("Transaction Failed STatus:"+json.get("status"));
    	}
    	JSONObject data = (JSONObject)json.get("data");
    	String externaltransactionId=data.get("id")+"";
    	String internalTransactionId=data.get("tx_ref")+"";
    	String amount=data.get("amount")+"";
    	String currency=data.get("currency")+"";
    	String payload=json.toJSONString();
    	
    	return new PaymentData(externaltransactionId, internalTransactionId, payload, amount, currency);
    }
    
    public static String getTransactionIdFromResponse(String resp) throws Exception {
        JSONObject jsono = (JSONObject) new JSONParser().parse(resp);
        String transactionId = (String)((JSONObject)jsono.get("data")).get("txRef");
        return transactionId;
    }
   public static final class PaymentData{
	   private String transactionId;
	   private String internalTransactionId;
	   private String payload;
	   private String amount;
	   private String currency;
	   
	   
	
	public PaymentData(String transactionId, String internalTransactionId, String payload, String amount,
			String currency) {
		super();
		this.transactionId = transactionId;
		this.internalTransactionId = internalTransactionId;
		this.payload = payload;
		this.amount = amount;
		this.currency = currency;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getInternalTransactionId() {
		return internalTransactionId;
	}
	public void setInternalTransactionId(String internalTransactionId) {
		this.internalTransactionId = internalTransactionId;
	}
	public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	   
   }
}
