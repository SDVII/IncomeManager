package com.example.sdvii.incomemanager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.astuetz.PagerSlidingTabStrip;

public class ShowGraph extends AppCompatActivity {

    String pageData[] = {"income","outcome"};
    int stat[] = {0,1};
    ViewPager vp;
    PagerSlidingTabStrip tabsStrip;
    DatabaseHandler db;
    FragmentPagerAdapter fpa;
    Fragment incomeFrg,outcomeFrg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_graph);
        setTitle("Graphs");

        vp = findViewById(R.id.vp);
        tabsStrip = findViewById(R.id.tabs);
        db = new DatabaseHandler(this);

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
                        GraphFragment gf1 = new GraphFragment();
                        gf1.setArguments(b1);
                        incomeFrg = gf1;
                        return gf1;
                    case 1:
                        income = stat[position];
                        Bundle b2 = new Bundle();
                        b2.putInt("isIncome",income);
                        GraphFragment gf2 = new GraphFragment();
                        gf2.setArguments(b2);
                        outcomeFrg = gf2;
                        return gf2;
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
}
