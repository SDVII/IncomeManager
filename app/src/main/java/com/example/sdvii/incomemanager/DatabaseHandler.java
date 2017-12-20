package com.example.sdvii.incomemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by houmam on 19-Dec-17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "incomeManager";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_PAYMENTS = "payment";

    private static final String KEY_ID = "id";
    private static final String KEY_TYPE = "type";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_TITLE = "title";
    private static final String KEY_INCOME = "income";
    private static final String KEY_DATE = "date";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PAYMENTS_TABLE = "CREATE TABLE " + TABLE_PAYMENTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TYPE + " INTEGER,"
                + KEY_AMOUNT + " REAL,"
                + KEY_TITLE + " TEXT,"
                + KEY_INCOME + " INTEGER,"
                + KEY_DATE + " INTEGER"+ ")";
        db.execSQL(CREATE_PAYMENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENTS);
        onCreate(db);
    }

    // Adding new payment
    void addPayment(Payment payment) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, payment.getpType());
        values.put(KEY_AMOUNT, payment.getpAmount());
        values.put(KEY_TITLE, payment.getpTitle());
        values.put(KEY_DATE, payment.getpDate());
        values.put(KEY_INCOME, payment.getpIncome());

        // Inserting Row
        db.insert(TABLE_PAYMENTS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single payment
    Payment getPayments(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PAYMENTS, new String[] { KEY_ID,
                        KEY_TYPE,
                        KEY_AMOUNT,
                        KEY_TITLE,
                        KEY_DATE,
                        KEY_INCOME}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Payment payment = new Payment(Integer.parseInt(cursor.getString(0)),
                cursor.getInt(1),cursor.getString(3),cursor.getDouble(2),cursor.getInt(4),cursor.getInt(5));
        // return payment
        db.close();
        return payment;
    }

    // Getting All Payments
    public List<Payment> getAllPayments() {
        List<Payment> paymentList = new ArrayList<Payment>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PAYMENTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Payment payment = new Payment(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getInt(1),
                        cursor.getString(3),
                        cursor.getDouble(2),
                        cursor.getInt(4),
                        cursor.getInt(5)
                );

                // Adding payment to list
                paymentList.add(payment);
            } while (cursor.moveToNext());
        }

        // return payment list
        return paymentList;
    }

    // Getting Income Payments
    public List<Payment> getPaymentsByMode(int mode) {
        List<Payment> paymentList = new ArrayList<Payment>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PAYMENTS + " WHERE " + KEY_INCOME +" = "+mode+" ORDER BY "+KEY_DATE+" ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Payment payment = new Payment(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getInt(1),
                        cursor.getString(3),
                        cursor.getDouble(2),
                        cursor.getInt(4),
                        cursor.getInt(5)
                );

                // Adding payment to list
                paymentList.add(payment);
            } while (cursor.moveToNext());
        }

        // return payment list
        db.close();
        return paymentList;
    }

    public List<Payment> getPaymentsByModeAndFilter(int mode,int filter) {
        List<Payment> paymentList = getPaymentsByMode(mode);
        List<Payment> filteredPayment = new ArrayList<>();

        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        String day = c.get(Calendar.DAY_OF_MONTH)<10?"0"+c.get(Calendar.DAY_OF_MONTH):c.get(Calendar.DAY_OF_MONTH)+"";
        String month = c.get(Calendar.MONTH)+1<10?"0"+c.get(Calendar.MONTH)+1:c.get(Calendar.MONTH)+1+"";
        String year = c.get(Calendar.YEAR)+"";

        Log.e("date:",day + " " + month + " " + year);

        if(filter == 0){
            return paymentList;
        }
        else if (filter == 1){

            for (Payment p:paymentList) {
                String sDate = p.getpIncome()+"";
                String y = sDate.substring(0,4);
                String m = sDate.substring(4,6);
                String d = sDate.substring(6,8);

                Log.e("date2:",d + " " + m + " " + y);

                if(y.equals(year)&&m.equals(month)&&d.equals(day))
                {
                    filteredPayment.add(p);
                }
            }
            return filteredPayment;
        }
        else if (filter == 2){

            for (Payment p:paymentList) {
                String sDate = p.getpIncome()+"";
                String y = sDate.substring(0,4);
                String m = sDate.substring(4,6);

                if(y.equals(year)&&m.equals(month))
                {
                    filteredPayment.add(p);
                }
            }
            return filteredPayment;
        }
        else {
            for (Payment p:paymentList) {
                String sDate = p.getpIncome()+"";
                String y = sDate.substring(0,4);

                if(y.equals(year))
                {
                    filteredPayment.add(p);
                }
            }
            return filteredPayment;
        }

    }

    // Getting Income Amount by mode
    public double getPaymentsAmounts(int mode) {
        List<Double> paymentAmounts = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  "+KEY_AMOUNT+" FROM " + TABLE_PAYMENTS + " WHERE " + KEY_INCOME +" = "+mode;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                // Adding payment to list
                paymentAmounts.add(cursor.getDouble(0));
            } while (cursor.moveToNext());
        }

        // return payment list
        db.close();
        return getTotalAmount(paymentAmounts);
    }

    // Getting Income Amount by mode and type
    public double getPaymentsAmountsByType(int mode, int type) {
        List<Double> paymentAmounts = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  "+KEY_AMOUNT+" FROM " + TABLE_PAYMENTS + " WHERE " + KEY_INCOME +" = "+mode+" AND " + KEY_TYPE + " = "+type;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                // Adding payment to list
                paymentAmounts.add(cursor.getDouble(0));
            } while (cursor.moveToNext());
        }

        // return payment list
        db.close();
        return getTotalAmount(paymentAmounts);
    }

    public double getTotalAmount(List<Double> aList){
        double res = 0;

        for (double amount:aList) {
            res = res + amount;
        }

        return res;
    }

    // Updating single payment
    public int updatePayment(Payment payment) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, payment.getpType());
        values.put(KEY_AMOUNT, payment.getpAmount());
        values.put(KEY_TITLE, payment.getpTitle());
        values.put(KEY_DATE, payment.getpDate());
        values.put(KEY_INCOME, payment.getpIncome());

        // updating row
        int ret = db.update(TABLE_PAYMENTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(payment.getpId()) });
        db.close();
        return ret;
    }

    // Deleting single payment
    public void deletePayment(Payment payment) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PAYMENTS, KEY_ID + " = ?",
                new String[] { String.valueOf(payment.getpId()) });
        db.close();
    }

    public void deleteAllRows(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_PAYMENTS);
        db.execSQL("vacuum");
        db.close();
    }

    public void deletePaymentById(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_PAYMENTS+" WHERE " +KEY_ID+" = "+id);
        db.close();
    }

    // Getting Payment Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PAYMENTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        db.close();
        return cursor.getCount();
    }

}

