package com.getmate.demo181201.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getmate.demo181201.CurrentUserData;
import com.getmate.demo181201.Objects.Api;
import com.getmate.demo181201.Objects.Constants;
import com.getmate.demo181201.Objects.Event;
import com.getmate.demo181201.Objects.Paytm;
import com.getmate.demo181201.Objects.Ticket;
import com.getmate.demo181201.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.paytm.pgsdk.Log;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PaytmGateway extends AppCompatActivity {

Button RemoveOneTicket,AddOneTicket,NoOfTickets,Buy;
TextView title,address, dateAndTime,creator,ticketsSubtotalText,
        ticketsSubtotalPriice,convenienceText,convenienceFees,TotalPrice;

int noT=1;
Event event= new Event();
double totalPrice=0.00;
    FirebaseUser currentUser;
    Ticket ticket;
    private FirebaseFirestore db;;

    private String orderId=null;
    private String custId=null;
    private String currentTimeStamp=null;
    private Button downloadTicket;
    private OutputStream os;
    private Intent mShareIntent;
    private  Event eventCopy = new Event();

public void findViewsById(){
    RemoveOneTicket = findViewById(R.id.remove_one_ticket_pga);
    AddOneTicket = findViewById(R.id.add_one_ticket_pga);
    NoOfTickets = findViewById(R.id.no_of_tickets_pga);
    Buy = findViewById(R.id.buttonBuy);
    title = findViewById(R.id.title_pga);
    address = findViewById(R.id.address_pga);
    dateAndTime = findViewById(R.id.address_pga);
    creator = findViewById(R.id.creator_pga);
    ticketsSubtotalText = findViewById(R.id.tickets_subtotal);
    ticketsSubtotalPriice= findViewById(R.id.price_of_tickets_only);
    convenienceFees = findViewById(R.id.price_of_convenience_fees);
    TotalPrice = findViewById(R.id.total_fees);
    convenienceText = findViewById(R.id.convenience_text);
    downloadTicket = findViewById(R.id.downlaod_ticket);

}



public void updateUIForPaymentSummary(){
    NoOfTickets.setText(String.valueOf(noT));
    ticketsSubtotalText.setText("Rs. "+event.getTicketPrice()+"  X "+ String.valueOf(noT)+ " Ticket");
    ticketsSubtotalPriice.setText(String.valueOf(event.getTicketPrice()*noT));
    convenienceFees.setText(String.valueOf(event.getConvenienceFees()*noT));
    totalPrice = event.getTicketPrice()*noT+event.getConvenienceFees()*noT;
    TotalPrice.setText("Rs. "+String.valueOf(totalPrice));
    Buy.setText("Click to Pay Rs. "+String.valueOf(totalPrice));


}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm_gateway);
        findViewsById();
         currentUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        if (ContextCompat.checkSelfPermission(PaytmGateway.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PaytmGateway.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }

        String s = getIntent().getStringExtra("data");
        if (s!=null){
            Gson gson = new Gson();
            event = gson.fromJson(s,Event.class);
            creator.setText(event.getCreatorName());
            title.setText(event.getTitle());

            CharSequence time = DateUtils.getRelativeTimeSpanString
                    (Long.parseLong(event.getTime()),System.currentTimeMillis(),DateUtils.SECOND_IN_MILLIS);
            dateAndTime.setText(time);
            address.setText(event.getAddress());
            NoOfTickets.setText(String.valueOf(noT));
        }


        AddOneTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noT=noT+1;
                updateUIForPaymentSummary();
            }
        });

        RemoveOneTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (noT>1){
                    noT= noT-1;
                    updateUIForPaymentSummary();
                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "Number of Tickets can't be less than 1",Toast.LENGTH_LONG).show();
                }

            }
        });




        downloadTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i = new Intent(PaytmGateway.this,TicketActivity.class);
               Gson gson = new Gson();
               String s = gson.toJson(ticket);

               i.putExtra("ticket",s);
               startActivity(i);
            }
        });


        Buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                db.collection("events").document(event.getFirebaseId()).get().addOnCompleteListener(
                        new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                     eventCopy = task.getResult().toObject(Event.class);
                                    if (eventCopy.getTicketsLeft()>noT){

                                        if (eventCopy.getEventType().equals("free")){
                                            //TODO: directly go to generate tickets
                                            ticket = new Ticket(currentTimeStamp,
                                                    "transection Id",
                                                    noT,currentTimeStamp,
                                                    currentUser.getDisplayName(),
                                                    currentUser.getUid(),
                                                    currentUser.getDisplayName(),
                                                    currentUser.getPhotoUrl().toString(),
                                                    currentUser.getEmail(),
                                                    currentUser.getPhoneNumber(),
                                                    event.getCreatorName(),
                                                    event.getCreatorFirebaseId(),
                                                    event.getCreatorHandle(),
                                                    event.getCreatorProfilePic(),
                                                    event.getCreatorEmail(),
                                                    event.getCreatorPhoneNo(),
                                                    event.getTitle(),
                                                    event.getAddress(),
                                                    event.getTime(),
                                                    event.getTicketPrice(),
                                                    event.getConvenienceFees(),
                                                    totalPrice,
                                                    currentUser.getDisplayName(),
                                                    event.getCreatorName(),
                                                    "orderId",
                                                    custId,
                                                    "banktxnId" );
                                            addTicketTodatabase(ticket);


                                        }

                                        if (eventCopy.getEventType().equals("paid")){
                                            generateCheckSum();
                                        }



                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),"Sorry, Only "
                                                +String.valueOf(eventCopy.getTicketsLeft()+" Tickets Left"),Toast.LENGTH_LONG).show();

                                    }
                                }
                                else {
                                    //TODO: Add a popup here to show the bad signature
                                    Toast.makeText(getApplicationContext(),"Sorry,No internet connection ",Toast.LENGTH_LONG).show();

                                }

                            }
                        }
                );


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

        custId =currentUser.getUid() ;
        orderId ="";
        currentTimeStamp = String.valueOf(Calendar.getInstance().getTimeInMillis());
        orderId= event.getFirebaseId().
                concat(currentTimeStamp).concat(String.valueOf(noT));

        String transectionAmount = String.valueOf(4.44);




        final Paytm paytm = new Paytm(Constants.M_ID,
                Constants.CHANNEL_ID,
                custId,
                orderId,
                transectionAmount,//String.valueOf(totalPrice)
                Constants.WEBSITE,
                Constants.CALLBACK_URL,
                Constants.INDUSTRY_TYPE_ID,
                currentUser.getPhoneNumber(), //currentUSer.getPhoneNo()
                currentUser.getEmail() //currentUSer.getEmailId();
        );


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


        PaytmPGService Service = PaytmPGService.getProductionService();

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
                new     PaytmPaymentTransactionCallback() {
                    @Override
                    public void onTransactionResponse(Bundle inResponse) {
                        Toast.makeText(getApplicationContext(), "Payment Transaction response "
                                + inResponse.toString(), Toast.LENGTH_LONG).show();
                        Log.i("Kaun","1"+"\n"+inResponse.toString());
                        String status = inResponse.getString("STATUS");
                        Log.i("Kaun","document id is"+status);
                        if (status.equals("TXN_SUCCESS")){
                            //generateTicket();



                            ticket = new Ticket(currentTimeStamp,
                                    inResponse.getString("TXNID"),
                                    noT,currentTimeStamp,
                                    currentUser.getDisplayName(),
                                    currentUser.getUid(),
                                    currentUser.getDisplayName(),
                                    currentUser.getPhotoUrl().toString(),
                                    currentUser.getEmail(),
                                    currentUser.getPhoneNumber(),
                                    event.getCreatorName(),
                                    event.getCreatorFirebaseId(),
                                    event.getCreatorHandle(),
                                    event.getCreatorProfilePic(),
                                    event.getCreatorEmail(),
                                    event.getCreatorPhoneNo(),
                                    event.getTitle(),
                                    event.getAddress(),
                                    event.getTime(),
                                    event.getTicketPrice(),
                                    event.getConvenienceFees(),
                                    totalPrice,
                                    currentUser.getDisplayName(),
                                    event.getCreatorName(),
                                    inResponse.getString("ORDERID"),
                                    custId,
                                    inResponse.getString("BANKTXNID") );
                            Log.i("Kaun","Ticket created successfully");
                            Log.i("Kaun",new Gson().toJson(ticket));
                            Log.i("Kaun","UPDATing ticket in database");



                            addTicketTodatabase(ticket);

               }
                        else {
                            Toast.makeText(getApplicationContext(),"TransectionFailed",Toast.LENGTH_LONG).show();
                            Log.i("Kaun","transection failed");
                            Log.i("Kaun","5");
                        }

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



    private void createPdf(Ticket ticket) {
        Log.i("PAYTMGATEWAY","create pdf called");

        View view = findViewById(R.layout.pdf_structure_for_ticket);
        ImageView imageView = view.findViewById(R.id.image_view_pdf);
        TextView title = view.findViewById(R.id.title_pdf);
        TextView timeAndDate = view.findViewById(R.id.time_pdf);
        TextView address = view.findViewById(R.id.address_pdf);
        TextView creator = view.findViewById(R.id.creator_name_pdf);
        TextView BName = view.findViewById(R.id.name_pdf);
        TextView BEmail = view.findViewById(R.id.email_pdf);
        TextView BPhone = view.findViewById(R.id.phone_pdf);
        TextView not = view.findViewById(R.id.not_pdf);

        String QRCode = "An encoded String to be Decoded using creator's fireBaseId";
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(QRCode, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            Log.i("PAYTMGATEWAY","bitmap creted");

            imageView.setImageBitmap(bitmap);
            title.setText(ticket.geteTitle());
            CharSequence time = DateUtils.getRelativeTimeSpanString
                    (Long.parseLong(event.getTime()),System.currentTimeMillis(),DateUtils.SECOND_IN_MILLIS);
            timeAndDate.setText(time);
            address.setText(ticket.geteAddress());
            creator.setText(ticket.getcName()+"\n"+ticket.getcEmail()+"\t"+ticket.getcPhone());
            BName.setText(ticket.getbName());
            BEmail.setText(ticket.getbEmail());
            BPhone.setText(ticket.getbPhone());
            not.setText(String.valueOf(ticket.getNoOfTickets()));

        } catch (WriterException e) {
            e.printStackTrace();
        }




        PrintAttributes printAttributes =
                new PrintAttributes.Builder().setColorMode(PrintAttributes.COLOR_MODE_MONOCHROME)
                        .setMediaSize(PrintAttributes.MediaSize.NA_LETTER)
                        .setResolution(new PrintAttributes.Resolution("lol",PRINT_SERVICE,300,300))
                        .setMinMargins(PrintAttributes.Margins.NO_MARGINS).build();
        PdfDocument pdfDocument = new PrintedPdfDocument(getApplicationContext(),printAttributes);
        // crate a page description
        PdfDocument.PageInfo pageInfo = new
                PdfDocument.PageInfo.Builder(700, 1200, 1).create();
        // create a new page from the PageInfo
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        // repaint the user's text into the page

        view.draw(page.getCanvas());

        // do final processing of the page
        pdfDocument.finishPage(page);
        try {
            File pdfDirPath = new File(getFilesDir(), "pdfs");
            pdfDirPath.mkdirs();
            File file = new File(pdfDirPath, "pdfsend.pdf");
            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(),
                    "com.example.fileprovider", file);
            os = new FileOutputStream(file);
            pdfDocument.writeTo(os);
            pdfDocument.close();
            os.close();
             //sharing PDF
            mShareIntent = new Intent();
            mShareIntent.setAction(Intent.ACTION_SEND);
            mShareIntent.setType("application/pdf");
            // Assuming it may go via eMail:
            mShareIntent.putExtra(Intent.EXTRA_SUBJECT, "Here is a PDF from PdfSend");
            // Attach the PDf as a Uri, since Android can't take it as bytes yet.
            mShareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            Log.i("PAYTMGATEWAY","ticket to be shared");

            startActivity(mShareIntent);
        } catch (IOException e) {
            throw new RuntimeException("Error generating file", e);
        }



    }


    private void addTicketTodatabase(Ticket ticket){
    DocumentReference ref = db.collection("tickets").document();
    ticket.setTicketFId(ref.getId());
    ref.set(ticket).addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {



            db.collection("profiles").document(CurrentUserData.getInstance().getCurrent().getFirebase_id())
                    .update("myTickets",FieldValue.arrayUnion(ref));


            Log.i("Kaun","added  ticket to database");
            Log.i("Kaun","6");
            downloadTicket.setVisibility(View.VISIBLE);
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Log.i("Kaun","ticket adding failed");
            Log.i("Kaun","7");
        }
    });
    }
}





