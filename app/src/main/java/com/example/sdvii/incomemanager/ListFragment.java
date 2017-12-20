package com.example.sdvii.incomemanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by houmam on 17-Dec-17.
 */

public class ListFragment extends Fragment {

    View root;
    TextView tv;
    ArrayList<DataModel> dataModels;
    ListView lv;
    int currPos = 0;
    Spinner spnFilter;
    DatabaseHandler db;
    private static CustomAdapter adapter;
    String months[] = { "Jan", "Feb", "Mar", "Apr",
            "May", "Jun", "July", "Aug",
            "Sep", "Oct", "Nov", "Dec" };

    private static final String[]pathsIn = {"Business", "Salary", "Extra","Scholarship","Other"};
    private static final String[]pathsOut = {"Payment", "Food", "Clothing","Shopping","Travel","Education","Entertainment","Other"};
    int mode,filter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mode = getArguments().getInt("isIncome");
        root = inflater.inflate(R.layout.list, container, false);
        tv = root.findViewById(R.id.tvText);
        lv = root.findViewById(R.id.lvList);
        dataModels = new ArrayList<>();
        db = new DatabaseHandler(root.getContext());
        spnFilter = root.findViewById(R.id.spnFilter);

        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("All");
        spinnerArray.add("Daily");
        spinnerArray.add("Monthly");
        spinnerArray.add("Yearly");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnFilter.setAdapter(adapter);

        getPaymentList();

        spnFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if(position != currPos) {
                    currPos = position;
                    filter = position;
                    refresh();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tv.setText(mode == 1?"Outcome: -"+db.getPaymentsAmounts(0):"Income: "+db.getPaymentsAmounts(1));
        updateTotal();

        return root;
    }

    public void getPaymentList(){

        List<Payment> payments;

        if (mode == 0) {
            payments = db.getPaymentsByModeAndFilter(1,filter);

        } else{
            payments = db.getPaymentsByModeAndFilter(0,filter);
        }

        for (Payment ps : payments) {
            String sDate = ps.getpIncome()+"";

            String m = sDate.substring(4,6)+"";
            String d = sDate.substring(6,8)+"";

            dataModels.add(new DataModel(
                    ps.getpId(),
                    d+" "+months[Integer.parseInt(m)-1],
                    (mode==1?"-":"")+"$"+ps.getpAmount(),
                    (mode==1?pathsOut[ps.getpType()]:pathsIn[ps.getpType()]),
                    ps.getpTitle()));
        }

        adapter = new CustomAdapter(dataModels,getContext(),this);

        lv.setAdapter(adapter);

    }

    public void refresh(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        MainActivity ma = (MainActivity) ListFragment.this.getActivity();
        ma.updateTotal();
        ft.detach(ListFragment.this).attach(ListFragment.this).commit();
    }

    public void updateTotal(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        MainActivity ma = (MainActivity) ListFragment.this.getActivity();
        ma.updateTotal();
    }
}
