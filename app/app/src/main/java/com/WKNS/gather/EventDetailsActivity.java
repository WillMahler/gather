package com.WKNS.gather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.WKNS.gather.ui.tabbedViewFragments.EventDetailsBudget;
import com.WKNS.gather.ui.tabbedViewFragments.EventDetailsSummary;
import com.WKNS.gather.ui.tabbedViewFragments.EventDetailsTasks;
import com.google.android.material.tabs.TabLayout;

public class EventDetailsActivity extends AppCompatActivity {

    private Toolbar actionbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        // Tab layout
        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        // View pager
        mViewPager  = (ViewPager) findViewById(R.id.view_pager);

        // Adapter
        mAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // 2 Tabs
        mAdapter.addFrag(new EventDetailsSummary(), getString(R.string.tab_text_1));
        mAdapter.addFrag(new EventDetailsTasks(), getString(R.string.tab_text_2));
        mAdapter.addFrag(new EventDetailsBudget(), getString(R.string.tab_text_3));

        mViewPager.setAdapter(mAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

        // setting title of action bar
        actionbar = findViewById(R.id.actionbar);
        setSupportActionBar(actionbar);
        getSupportActionBar().setTitle("Event details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_round_arrow_back_ios_24));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
