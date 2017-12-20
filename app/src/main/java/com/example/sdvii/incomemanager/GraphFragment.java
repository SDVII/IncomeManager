package com.example.sdvii.incomemanager;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.callback.OnPieSelectListener;
import com.razerdp.widget.animatedpieview.data.IPieInfo;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by houmam on 20-Dec-17.
 */

public class GraphFragment extends Fragment {

    View root;
    DatabaseHandler db;
    AnimatedPieView apv;
    AnimatedPieViewConfig apvc;
    int mode;
    TextView tv;
    private static final String[]pathsIn = {"Business", "Salary", "Extra","Scholarship","Other"};
    private static final String[]pathsOut = {"Payment", "Food", "Clothing","Shopping","Travel","Education","Entertainment","Other"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mode = getArguments().getInt("isIncome");
        root = inflater.inflate(R.layout.graphs, container, false);
        tv = root.findViewById(R.id.tvText);
        db = new DatabaseHandler(root.getContext());
        Random rnd = new Random();
        apv = root.findViewById(R.id.apv);
        apvc = new AnimatedPieViewConfig();
        apvc.setStartAngle(-90)// set the start offset angle
                .setDuration(1000)
                .setCanTouch(true)
                .setDrawStrokeOnly(false)
                .setTextSize(30)
                .setOnPieSelectListener(new OnPieSelectListener<IPieInfo>() {
                    @Override
                    public void onSelectPie(@NonNull IPieInfo pieInfo, boolean isScaleUp) {
                        Toast.makeText(getActivity(),pieInfo.getDesc()+"\n"+pieInfo.getValue(),Toast.LENGTH_SHORT).show();
                    }
                });

        List<Payment> payments;

        if (mode == 1) {
            payments = db.getPaymentsByMode(0);
            List<Double> amounts = new  ArrayList<>();
            List<Integer> types = new  ArrayList<>();
            int cnt = 0;

            for (Payment p: payments) {

                int t = p.getpType();
                int count = 0;
                for (int type:types) {
                    if (type == t){count++;}
                }
                if (count == 0){types.add(t);}

            }

            for (int type:types) {
                double a = db.getPaymentsAmountsByType(0,type);
                amounts.add(a);
            }

            for (int type:types) {
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                apvc.addData(new SimplePieInfo(Float.valueOf(String.valueOf(amounts.get(cnt))), color, pathsOut[type]));
                cnt++;
            }
        }
        else {
            payments = db.getPaymentsByMode(1);
            List<Double> amounts = new  ArrayList<>();
            List<Integer> types = new  ArrayList<>();
            int cnt = 0;

            for (Payment p: payments) {

                int t = p.getpType();
                int count = 0;
                for (int type:types) {
                    if (type == t){count++;}
                }
                if (count == 0){types.add(t);}

            }

            for (int type:types) {
                double a = db.getPaymentsAmountsByType(1,type);
                amounts.add(a);
            }

            for (int type:types) {
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                apvc.addData(new SimplePieInfo(Float.valueOf(String.valueOf(amounts.get(cnt))), color, pathsIn[type]));
                cnt++;
            }
        }


        apv.applyConfig(apvc);
        apv.start();

        return root;
    }

}
