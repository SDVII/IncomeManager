package com.example.sdvii.incomemanager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {


    FloatingActionMenu fam;
    FloatingActionButton fabAdd,fabGraph,fabReset,fabExport,fabAbout;
    String pageData[] = {"income","outcome"};
    int stat[] = {0,1};
    double incomeAmounts,outcomeAmounts, totalAmoutns;
    ViewPager vp;
    PagerSlidingTabStrip tabsStrip;
    DatabaseHandler db;
    FragmentPagerAdapter fpa;
    Fragment incomeFrg,outcomeFrg;
    TextView etTotal;
    private static final String[]pathsIn = {"Business", "Salary", "Extra","Scholarship","Other"};
    private static final String[]pathsOut = {"Payment", "Food", "Clothing","Shopping","Travel","Education","Entertainment","Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vp = findViewById(R.id.vp);
        tabsStrip = findViewById(R.id.tabs);
        fam = findViewById(R.id.famMenu);
        fabAdd = findViewById(R.id.fabAdd);
        fabGraph = findViewById(R.id.fabGraph);
        fabReset = findViewById(R.id.fabReset);
        fabExport = findViewById(R.id.fabExport);
        fabAbout = findViewById(R.id.fabAbout);
        db = new DatabaseHandler(this);
        etTotal = findViewById(R.id.etTotal);

        updateTotal();

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent addPayment = new Intent(MainActivity.this,AddPayment.class);
                startActivityForResult(addPayment,1);
                fam.close(true);
            }
        });

        fabGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showGraph = new Intent(MainActivity.this,ShowGraph.class);
                startActivity(showGraph);
                fam.close(true);
            }
        });

        fabAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent about = new Intent(MainActivity.this,About.class);
                startActivity(about);
                fam.close(true);
            }
        });

        fabExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File file = new File(path,"Report.pdf");
                    if (!file.exists()) {
                        boolean x =file.createNewFile();
                    }
                    Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN,
                            12, Font.BOLD, new BaseColor(0, 0, 0));
                    Font bfBold30 = new Font(Font.FontFamily.TIMES_ROMAN,
                            30, Font.BOLD, new BaseColor(0, 0, 0));
                    Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN,
                            12);
                    Document document = new Document();
                    PdfWriter.getInstance(document, new FileOutputStream(file));
                    document.open();

                    document.add(new Paragraph("Income: ",bfBold12));
                    LineSeparator ls = new LineSeparator();
                    document.add(new Chunk(ls));
                    List <Payment> payments = db.getPaymentsByMode(1);
                    for (Payment pay:payments) {

                        String sDate = pay.getpIncome()+"";
                        String y = sDate.substring(0,4);
                        String m = sDate.substring(4,6);
                        String d = sDate.substring(6,8);

                        document.add(new Paragraph("" +
                                "ID: " + pay.getpId()+
                                ", Date: "+d+"/"+m+"/"+y+
                        ", Type: " + pathsIn[pay.getpType()]+
                        ", Title: " + pay.getpTitle()+
                        ", Amount: " + pay.getpAmount(),bf12));
                    }
                    document.add(new Chunk(ls));
                    document.add(new Paragraph("Total Income: "+db.getPaymentsAmounts(1),bfBold12));
                    document.newPage();

                    document.add(new Paragraph("Outcome: ",bfBold12));
                    document.add(new Chunk(ls));
                    payments = db.getPaymentsByMode(0);
                    for (Payment pay:payments) {

                        String sDate = pay.getpIncome()+"";
                        String y = sDate.substring(0,4);
                        String m = sDate.substring(4,6);
                        String d = sDate.substring(6,8);

                        document.add(new Paragraph("" +
                                "ID: " + pay.getpId()+
                                ", Date: "+d+"/"+m+"/"+y+
                                ", Type: " + pathsIn[pay.getpType()]+
                                ", Title: " + pay.getpTitle()+
                                ", Amount: " + pay.getpAmount(),bf12));
                    }
                    document.add(new Chunk(ls));
                    document.add(new Paragraph("Total Outcome: "+db.getPaymentsAmounts(0),bfBold12));
                    document.newPage();

                    double total = db.getPaymentsAmounts(1)-db.getPaymentsAmounts(0);
                    Paragraph res = new Paragraph("Total Budget: "+total,bfBold30);
                    res.setAlignment(Element.ALIGN_CENTER);
                    document.add(res);

                    document.close();
                }
                catch (IOException e) {
                    Log.e("error",e.getMessage());
                    e.printStackTrace();
                } catch (DocumentException e) {
                    Log.e("error",e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        fabReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(MainActivity.this);
                }
                builder.setTitle("Reset App")
                        .setMessage("Are you sure you want to reset the app?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                db.deleteAllRows();
                                finish();
                                startActivity(getIntent());
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });



        fam.setIconAnimated(false);

        fpa = new FragmentPagerAdapter(getSupportFragmentManager()) {

            String tabTitles[] = {"Income","Outcome"};

            @Override
            public Fragment getItem(int position) {
                int income;
                switch (position){
                    case 0:
                        income = stat[position];
                        Bundle b1 = new Bundle();
                        b1.putInt("isIncome",income);
                        ListFragment lf1 = new ListFragment();
                        lf1.setArguments(b1);
                        incomeFrg = lf1;
                        return lf1;
                    case 1:
                        income = stat[position];
                        Bundle b2 = new Bundle();
                        b2.putInt("isIncome",income);
                        ListFragment lf2 = new ListFragment();
                        lf2.setArguments(b2);
                        outcomeFrg = lf2;
                        return lf2;
                }
                return null;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tabTitles[position];
            }

            @Override
            public int getCount() {
                return pageData.length;
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }

        };

        vp.setAdapter(fpa);
        tabsStrip.setViewPager(vp);
    }

    public void updateTotal(){

        incomeAmounts = db.getPaymentsAmounts(1);
        outcomeAmounts = db.getPaymentsAmounts(0);
        totalAmoutns = incomeAmounts - outcomeAmounts;
        etTotal.setText("Total: " +totalAmoutns);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == Activity.RESULT_OK){

                String res[] = data.getStringArrayExtra("data");
                int date[] = data.getIntArrayExtra("date");

                String m = date[1]+1<10? "0"+date[1]+1:date[1]+1+"";
                String d = date[2]<10? "0"+date[2]:date[2]+"";

                String sDate = date[0]+""+m+""+d;
                int iDate = Integer.parseInt(sDate);

                Payment p = new Payment(
                        Integer.parseInt(res[0]),
                        res[2],Double.parseDouble(res[1]),
                        iDate,
                        date[3]);

                db.addPayment(p);

                fpa.notifyDataSetChanged();
            }
            else {
                return;
            }
        }
        else if (requestCode==2){
            if (resultCode == Activity.RESULT_OK){

                String res[] = data.getStringArrayExtra("data");
                int date[] = data.getIntArrayExtra("date");

                String m = date[1]+1<10? "0"+date[1]+1:date[1]+1+"";
                String d = date[2]<10? "0"+date[2]:date[2]+"";

                String sDate = date[0]+""+m+""+d;
                int iDate = Integer.parseInt(sDate);

                Payment p = new Payment(date[4],
                        Integer.parseInt(res[0]),
                        res[2],Double.parseDouble(res[1]),
                        iDate,
                        date[3]);

                db.updatePayment(p);
                fpa.notifyDataSetChanged();
            }
            else {
                return;
            }
        }

    }

    public void editPaymentActvity(int id){
        Intent editPayment = new Intent(MainActivity.this,EditPayment.class);
        editPayment.putExtra("id",id);
        startActivityForResult(editPayment,2);
    }

}
