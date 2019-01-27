package com.getmate.demo181201.Objects;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    //this is the URL of the paytm folder that we added in the server
    //make sure you are using your ip else it will not work
    String BASE_URL = "https://us-central1-demo181201.cloudfunctions.net/";
    @FormUrlEncoded
    @POST("generate_checksum")
    Call<Object> getChecksum(@Field("MID") String MID,
                             @Field("ORDER_ID") String ORDER_ID,
                             @Field("CUST_ID") String CUST_ID,
                             @Field("CHANNEL_ID") String CHANNEL_ID,
                             @Field("TXN_AMOUNT") String TXN_AMOUNT,
                             @Field("WEBSITE") String WEBSITE,
                             @Field("CALLBACK_URL") String CALLBACK_URL,
                             @Field("INDUSTRY_TYPE_ID") String INDUSTRY_TYPE_ID,
                             @Field("MOBILE_NO") String MOBILE_NO,
                             @Field("EMAIL") String EMAIL);

}