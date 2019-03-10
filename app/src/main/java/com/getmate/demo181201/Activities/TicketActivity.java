package com.getmate.demo181201.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getmate.demo181201.Objects.Ticket;
import com.getmate.demo181201.R;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.paytm.pgsdk.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TicketActivity extends AppCompatActivity {
    ImageView imageView;
    TextView title;
    TextView timeAndDate;
    TextView address;
    TextView creator;
    TextView BName;
    TextView BEmail;
    TextView BPhone;
    FileOutputStream os;
    Intent mShareIntent;
    TextView not;
    View pdfView;
    Button shareTicket,viewTicket;
    String s = null;

    Ticket ticket = new Ticket();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        findViewsVyId();
        pdfView = findViewById(R.id.lin_lay_pdf);

        s= getIntent().getStringExtra("ticket");

        if (s!=null){
            Gson gson = new Gson();
            ticket = gson.fromJson(s,Ticket.class);
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
                        (Long.parseLong(ticket.geteTimeStamp()),System.currentTimeMillis(),DateUtils.SECOND_IN_MILLIS);
                timeAndDate.setText(time);
                address.setText(ticket.geteAddress());
                creator.setText(ticket.getcName()+"\n"+ticket.getcEmail()+"\n"+ticket.getcPhone());
                BName.setText(ticket.getbName());
                BEmail.setText(ticket.getbEmail());
                BPhone.setText(ticket.getbPhone());
                not.setText(String.valueOf(ticket.getNoOfTickets()));

            } catch (WriterException e) {
                e.printStackTrace();
            }

        }
        else {
            Toast.makeText(getApplicationContext(),"ticket Empty",Toast.LENGTH_LONG).show();
        }




        viewTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareTicket.setVisibility(View.GONE);
                viewTicket.setVisibility(View.GONE);
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

                pdfView.draw(page.getCanvas());

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
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(contentUri,"application/pdf");
                    startActivity(intent);
                } catch (IOException e) {
                    throw new RuntimeException("Error generating file", e);
                }
            }
        });


        shareTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareTicket.setVisibility(View.GONE);

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

                pdfView.draw(page.getCanvas());

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
        });


    }

    private void findViewsVyId() {
         imageView = findViewById(R.id.image_view_pdf);
         title = findViewById(R.id.title_pdf);
         timeAndDate = findViewById(R.id.time_pdf);
         address = findViewById(R.id.address_pdf);
         creator = findViewById(R.id.creator_name_pdf);
         BName = findViewById(R.id.name_pdf);
         BEmail = findViewById(R.id.email_pdf);
         BPhone = findViewById(R.id.phone_pdf);
         not = findViewById(R.id.not_pdf);
         shareTicket = findViewById(R.id.share_ticket);
         viewTicket = findViewById(R.id.view_ticket);
    }
}
