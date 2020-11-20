package com.WKNS.gather;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.WKNS.gather.ui.tabbedViewFragments.CreateEventSummary;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;

public class CreateEventActivity extends AppCompatActivity {

    public static String TAG = CreateEventActivity.class.getSimpleName();
    private Context context;

    private Toolbar actionbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mAdapter;

    private CreateEventSummary summaryFragment;

    public FloatingActionButton mFabCancel, mFabDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        context = this;

        mTabLayout = findViewById(R.id.tabs);
        mViewPager  = findViewById(R.id.view_pager);
        mAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        summaryFragment = new CreateEventSummary();

        mAdapter.addFrag(summaryFragment, getString(R.string.tab_text_1));

        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        // setting title of action bar
        actionbar = findViewById(R.id.actionbar);
        setSupportActionBar(actionbar);
        getSupportActionBar().setTitle("New Gathering");

        mFabCancel = findViewById(R.id.fab_cancel);
        mFabDone = findViewById(R.id.fab_done);

        mFabCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View root) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to abandon this event?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                summaryFragment.deleteEvent();
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        mFabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Would you like to draft or publish this event?")
                        .setCancelable(true)
                        .setPositiveButton("Publish", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (summaryFragment.validateData()) {
                                    try {
                                        summaryFragment.addEvent(true);
                                        Toast.makeText(context, "Event Published",
                                                    Toast.LENGTH_SHORT).show();
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                        Toast.makeText(context, "Error: Event Not Published",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    finish();
                                }
                            }
                        })
                        .setNegativeButton("Draft", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    summaryFragment.addEvent(false);
                                    Toast.makeText(context, "Draft Saved",
                                            Toast.LENGTH_SHORT).show();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    Toast.makeText(context, "Error: Draft Not Saved",
                                            Toast.LENGTH_SHORT).show();
                                }
                                finish();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
}
