package com.example.sdvii.incomemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class About extends AppCompatActivity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("About");

        tv = findViewById(R.id.tvGit);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='https://github.com/SDVII/IncomeManager'> GitHub Repo </a>";
        tv.setText(Html.fromHtml(text));

    }
}
