
package offrep;
import okhttp3.*;
import org.json.*;

import java.io.IOException;
import java.util.Base64;
public class Mpesa {
    String appKey;
    String appSecret;
    
    public String account = "Nazi-Gracious";
    
    public Mpesa(String app_key, String app_secret){
        appKey=app_key;
        appSecret=app_secret;
    }
    public String authenticate() throws IOException {
        String app_key = appKey/*"GvzjNnYgNJtwgwfLBkZh65VPwfuKvs0V"*/;
        String app_secret = appSecret;
        String appKeySecret = app_key + ":" + app_secret;
        byte[] bytes = appKeySecret.getBytes("ISO-8859-1");
        String encoded = Base64.getEncoder().encodeToString(bytes);


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials")
                .get()
                .addHeader("authorization", "Basic "+encoded)
                .addHeader("cache-control", "no-cache")

                .build();

        String jsonobj = "";
        try{
            Response response = client.newCall(request).execute();
            JSONObject jsonObject=new JSONObject(response.body().string());
            jsonobj = jsonObject.getString("access_token");
            System.out.println(jsonObject.getString("access_token"));
            }catch(Exception e){

            }
        return jsonobj;
    }
    
        public String STKPushSimulation(String businessShortCode, String password, String timestamp,String transactionType, String amount, 
                String phoneNumber, String partyA, String partyB, String callBackURL, String queueTimeOutURL,String accountReference, String transactionDesc)
                throws IOException {
        JSONArray jsonArray=new JSONArray();
        JSONObject jsonObject=new JSONObject();
        try{
            jsonObject.put("BusinessShortCode", businessShortCode);
            jsonObject.put("Password", password);
            jsonObject.put("Timestamp", timestamp);
            jsonObject.put("TransactionType", transactionType);
            jsonObject.put("Amount",amount);
            jsonObject.put("PhoneNumber", phoneNumber);
            jsonObject.put("PartyA", partyA);
            jsonObject.put("PartyB", partyB);
            jsonObject.put("CallBackURL", callBackURL);
            jsonObject.put("AccountReference", accountReference);
            jsonObject.put("QueueTimeOutURL", queueTimeOutURL);
            jsonObject.put("TransactionDesc", transactionDesc);
        }catch(Exception e){
            
        }


        jsonArray.put(jsonObject);

        String requestJson=jsonArray.toString().replaceAll("[\\[\\]]","");

        OkHttpClient client = new OkHttpClient();
        String url="https://api.safaricom.co.ke/mpesa/stkpush/v1/processrequest";
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, requestJson);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("authorization", "Bearer "+authenticate())
                .addHeader("cache-control", "no-cache")
                .build();


        Response response = client.newCall(request).execute();
        
        System.out.println(response.body().string());
        
        return response.body().toString();
    }
//    public static void main(String[] args) throws IOException {
//        PaymentGateway p=new PaymentGateway("B8H5SaUJBabzaY9I8bK8AVoyBdlFT49g","ASrkcIwp5CasCHdz");
//        p.STKPushSimulation("174379","MTc0Mzc5YmZiMjc5ZjlhYTliZGJjZjE1OGU5N2RkNzFhNDY3Y2QyZTBjODkzMDU5YjEwZjc4ZTZiNzJhZGExZWQyYzkxOTIwMTcwODI0MTU1MDU1",
//                "20170824155055","CustomerPayBillOnline","1500","254723095840","254723095840","174379","http://tolclin.com/mpesa-api/calllback.php?account="
//                        + ""+p.account+"","",""+p.account+"","asdasd");
//        System.out.println(p.account);
//            Mpesa p=new Mpesa("mQNxJDUjAOc6iiOlO4tH0p4R1GHzOs1M","zSsJvTTXAPJ0lpME");
//            p.STKPushSimulation("224343","MjI0MzQzN2IyZGEyZTM2Y2ZiYjc4YjU0ZmRkODliMjVlMDgyZThhZWJmMmE0MDRmNWE4Y2ExM2VkN2I0M2I2Yjk5NjE4YzIwMTgxMTAzMTYwNzMz",
//                    "20181103160733","CustomerPayBillOnline","15000","254723095840","254723095840","224343","http://tolclin.com/mpesa-api/calllback.php?account="
//                            + ""+p.account+"","",""+p.account+"",""+p.account+"");
//            System.out.println(p.account);
//    }
    
}
    
