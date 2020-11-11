package com.WKNS.gather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.WKNS.gather.ui.tabbedViewFragments.CreateEventBudget;
import com.WKNS.gather.ui.tabbedViewFragments.CreateEventSummary;
import com.WKNS.gather.ui.tabbedViewFragments.CreateEventTasks;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class CreateEventActivity extends AppCompatActivity {

    private Toolbar actionbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mAdapter;

    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        // Tab layout
        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        // View pager
        mViewPager  = (ViewPager) findViewById(R.id.view_pager);

        // Adapter
        mAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // 2 Tabs
        mAdapter.addFrag(new CreateEventSummary(), getString(R.string.tab_text_1));
        mAdapter.addFrag(new CreateEventTasks(), getString(R.string.tab_text_2));
        mAdapter.addFrag(new CreateEventBudget(), getString(R.string.tab_text_3));

        mViewPager.setAdapter(mAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

        // setting title of action bar
        actionbar = findViewById(R.id.actionbar);
        setSupportActionBar(actionbar);
        getSupportActionBar().setTitle("New Gathering");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_round_arrow_back_ios_24));

        mFab = findViewById(R.id.fab2);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View root) {
                finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
