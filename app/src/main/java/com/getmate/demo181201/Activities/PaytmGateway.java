package com.getmate.demo181201.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.getmate.demo181201.Objects.Api;
import com.getmate.demo181201.Objects.Constants;
import com.getmate.demo181201.Objects.Paytm;
import com.getmate.demo181201.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.paytm.pgsdk.Log;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PaytmGateway extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm_gateway);


        if (ContextCompat.checkSelfPermission(PaytmGateway.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PaytmGateway.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }


        findViewById(R.id.buttonBuy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //calling the method generateCheckSum() which will generate the paytm checksum for payment
                generateCheckSum();
            }
        });



    }

    private void generateCheckSum() {

        //getting the tax amount first.
        //creating a retrofit object.

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)).
                        addConverterFactory(ScalarsConverterFactory.create())
                .build();

        //creating the retrofit api service
        Api apiService = retrofit.create(Api.class);

        //creating paytm object
        //containing all the values required
        final Paytm paytm = new Paytm(Constants.M_ID, Constants.CHANNEL_ID,
                Constants.Transection, Constants.WEBSITE, Constants.CALLBACK_URL, Constants.INDUSTRY_TYPE_ID,
                "7092541851", "dhruvg.dakshana16@gmail.com");



                    Gson gson1 = new Gson();
        String json = gson1.toJson(paytm);

        Log.i("Kaun",json);
        Log.i("Kaun",paytm.getCALLBACK_URL());
            //creating a call object from the apiService
        Call<Object> call = apiService.getChecksum(paytm.getMID(),
                paytm.getORDER_ID(),
                paytm.getCUST_ID(),
                paytm.getCHANNEL_ID(),
                paytm.getTXN_AMOUNT(),
                paytm.getWEBSITE(),
                paytm.getCALLBACK_URL(),
                paytm.getINDUSTRY_TYPE_ID(),
                paytm.getMOBILE_NO(),
                paytm.getEMAIL());



        //making the call to generate checksum
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("Kaun","checkSumHash received"+response);
                Log.i("Kaun","checkSumHash received"+response.body());
                Gson gson2 = new Gson();
               // ResponseFromCheckSum response1 = gson2.fromJson((String) response.body(),ResponseFromCheckSum.class);
                LinkedTreeMap<Object,Object> linkedTreeMap=  (LinkedTreeMap)response.body();
                Log.i("KaunHuMein",linkedTreeMap.get("CHECKSUMHASH").toString());
                initializePaytmPayment(linkedTreeMap.get("CHECKSUMHASH").toString(),linkedTreeMap);

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
                Log.i("Kaun","Request for checkSum HAsh Failed"+call+" lol "+t.getCause());
            }
        });
    }




    private void initializePaytmPayment(String checksumHash, LinkedTreeMap<Object,Object> linkedTreeMap) {


        PaytmPGService Service = PaytmPGService.getStagingService();

        //PaytmPGService Service = PaytmPGService.getProductionService();

        HashMap<String, String> paramMap = new HashMap<String,String>();
        paramMap.put( "MID" , linkedTreeMap.get("MID").toString());
        // Key in your staging and production MID available in your dashboard
        paramMap.put( "ORDER_ID" , linkedTreeMap.get("ORDER_ID").toString());
        paramMap.put( "CUST_ID" , linkedTreeMap.get("CUST_ID").toString());
        paramMap.put( "MOBILE_NO" , linkedTreeMap.get("MOBILE_NO").toString());
        paramMap.put( "EMAIL" , linkedTreeMap.get("EMAIL").toString());
        paramMap.put( "CHANNEL_ID" , linkedTreeMap.get("CHANNEL_ID").toString());
        paramMap.put( "TXN_AMOUNT" , linkedTreeMap.get("TXN_AMOUNT").toString());
        paramMap.put( "WEBSITE" ,linkedTreeMap.get("WEBSITE").toString());
// This is the staging value. Production value is available in your dashboard
        paramMap.put( "INDUSTRY_TYPE_ID" , linkedTreeMap.get("INDUSTRY_TYPE_ID").toString());
// This is the staging value. Production value is available in your dashboard
        paramMap.put( "CALLBACK_URL", linkedTreeMap.get("CALLBACK_URL").toString());
        paramMap.put( "CHECKSUMHASH" , checksumHash);
        PaytmOrder Order = new PaytmOrder(paramMap);

        Service.initialize(Order,null);

        Service.startPaymentTransaction(this, true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void onTransactionResponse(Bundle inResponse) {
                        Toast.makeText(getApplicationContext(), "Payment Transaction response "
                                + inResponse.toString(), Toast.LENGTH_LONG).show();
                        Log.i("Kaun","1"+"\n"+inResponse.toString());
                        /*
                            Bundle[{STATUS=TXN_SUCCESS,
                             CHECKSUMHASH=k/oJ8dlX0cTaGiCBq0lH8F8MUQEzKKM3NaIieuBR7IWS+HQqRD81NZk7ZP1INoLxdwyjul53QkAx41Wtr+s3sz+JrAumm7TxH7ys1rfNHxY=,
                              BANKNAME=STATE BANK OF INDIA,
                               ORDERID=orde,
                                TXNAMOUNT=5.00,
                                 TXNDATE=2019-01-15 01:27:47.0,
                                  MID=YJSuVL99726696757391,
                                   TXNID=20190115111212800110168529300162811,
                                    RESPCODE=01,
                                     PAYMENTMODE=DC,
                                     BANKTXNID=4036217121962950,
                                      CURRENCY=INR,
                                       GATEWAYNAME=HDFC,
                                        RESPMSG=Txn Success}]

                         */

                    }

                    @Override
                    public void networkNotAvailable() {
                        Log.i("Kaun","2");
                        Toast.makeText(getApplicationContext(),

                                "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        //Client authentication failure:
                        //
                        //This can happen due to multiple reason -
                        //
                        //    Paytm services are not available due to a downtime
                        //    Server unable to generate checksum or checksum response is not in proper format
                        //    Server failed to authenticate the client. That is value of payt_STATUS is 2. // payt_STATUS hasn't been defined anywhere
                        Log.i("Kaun","3");
                        Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
//This is caused when SDK is unable to load the payment page.
// This might happen in case SDK is not able to parse the transaction payload received from the app

                        Toast.makeText(getApplicationContext(), "UI Error " +
                                inErrorMessage , Toast.LENGTH_LONG).show();
                        Log.i("Kaun","4");

                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                        Log.i("Kaun","5");
                        Toast.makeText(getApplicationContext(), "Unable to load webpage " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onBackPressedCancelTransaction() {
                        Log.i("Kaun","6");
                        Toast.makeText(getApplicationContext(), "Transaction cancelled" , Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                        Log.i("Kaun","7");
                        Toast.makeText(getApplicationContext(), "Transaction Cancelled" + inResponse.toString(), Toast.LENGTH_LONG).show();

                    }
                });





    }




    private void checkSumHashVerificaionFromServer(){

    }
}





