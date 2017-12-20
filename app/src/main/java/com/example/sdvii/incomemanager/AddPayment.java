package com.example.sdvii.incomemanager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

public class AddPayment extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener, AdapterView.OnItemSelectedListener{

    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    EditText etDate,etAmount,etTitle;
    Switch swIncome;
    int _day = cal.get(Calendar.DAY_OF_MONTH);
    int _month = cal.get(Calendar.MONTH);
    int _year = cal.get(Calendar.YEAR);
    Context _context;
    Spinner spnType;
    ArrayAdapter<String> adapter;
    Button btnSave, btnCancel;
    private static final String[]pathsIn = {"Business", "Salary", "Extra","Scholarship","Other"};
    private static final String[]pathsOut = {"Payment", "Food", "Clothing","Shopping","Travel","Education","Entertainment","Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);

        setTitle("Add Payment");

        _context = this;
        etDate = findViewById(R.id.etDate);
        etAmount = findViewById(R.id.etAmount);
        etTitle = findViewById(R.id.etTitle);
        spnType = findViewById(R.id.spnType);
        swIncome = findViewById(R.id.swIncome);

        swIncome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                fillSpinner();
            }
        });

        etDate.setOnClickListener(this);

        fillSpinner();

    }

    public void fillSpinner(){
        adapter = new ArrayAdapter<>(AddPayment.this,
                android.R.layout.simple_spinner_item,swIncome.isChecked()?pathsIn:pathsOut);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnType.setAdapter(adapter);
        spnType.setOnItemSelectedListener(this);
    }

    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        _day = day;
        _month = month;
        _year = year;
        updateDisplay();
    }

    private void updateDisplay() {
        etDate.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(_day).append("/").append(_month + 1).append("/").append(_year).append(" "));
    }

    @Override
    public void onClick(View view) {

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        DatePickerDialog dialog = new DatePickerDialog(
                _context,
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.updateDate(_year,_month,_day);
        dialog.show();

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void savePayment(View view) {
        String type = spnType.getSelectedItemPosition()+"";
        String amount = etAmount.getText().toString().trim();
        String title = etTitle.getText().toString().trim();
        int income = swIncome.isChecked()?1:0;

        if (title.length()>20)
        {
            Toast.makeText(AddPayment.this,"The title should not exceed 20 characters",Toast.LENGTH_SHORT).show();
            return;
        }
        if (title.length() == 0){title = " ";}
        if (amount.length()<1)
        {
            Toast.makeText(AddPayment.this,"Please enter an amount",Toast.LENGTH_SHORT).show();
            return;
        }

        if (_day == 0){_day = Calendar.DAY_OF_MONTH;}
        if (_month == 0){_day = Calendar.MONTH;}
        if (_year == 0){_day = Calendar.YEAR;}


        String data[] = {type,amount,title};
        int date[] = {_year,_month,_day,income};

        Intent res = new Intent();
        res.putExtra("data", data);
        res.putExtra("date", date);


        setResult(Activity.RESULT_OK, res);
        finish();
    }

    public void cancelPayment(View view) {

        Intent res = new Intent();
        setResult(Activity.RESULT_CANCELED, res);
        finish();
    }
}
