package com.example.e1_531_use.travelmate;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public ActionBarDrawerToggle getToggle;
    public DrawerLayout drawer;
    public ActionBarDrawerToggle toggle;
    public Drawable dra;

    private static String DATABASE_TABLE = "ProcessList";
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //R.id.toolbar在app_bar_main裡面

        setSupportActionBar(toolbar);

        //右下的小加號
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getToolbarNavigationClickListener();
        toggle.setDrawerIndicatorEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawer.addDrawerListener(toggle);



        //toolbar.setNavigationIcon(android.R.id.navigationBarBackground);
        assert toolbar != null;

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (toolbar.getNavigationIcon()!= dra) {
                    toolbar.setNavigationIcon(dra);
                    toggle.setDrawerIndicatorEnabled(true);
                    getSupportActionBar().setHomeButtonEnabled(true);
                    onBackPressed();

                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                    getSupportActionBar().setTitle("TravelMate");
                    toggle.setDrawerIndicatorEnabled(true);
                }
                else
                {
                    System.out.println("th");

                    drawer.openDrawer(GravityCompat.START);
                }
                System.out.println("this");
                System.out.println(android.R.id.navigationBarBackground);
                System.out.println(android.R.id.home);

            }
        });

        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //將 dra設漢堡圖示
        dra = toolbar.getNavigationIcon();
        //toggle.setToolbarNavigationClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        MyProcessFragment myProcess = new MyProcessFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.index,myProcess).commit();

        //在側邊攔設為已點
        navigationView.getMenu().findItem(R.id.my_process).setChecked(true);

        dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();

        /*boolean isrunning =true;
        if(isrunning)
        {
            navigationView.getMenu().findItem(R.id.running_process).setChecked(true);
            RunningProcessFragment runningProcess = new RunningProcessFragment();
            fragmentManager.beginTransaction().replace(R.id.index,runningProcess).commit();
        }
        else
        {
            navigationView.getMenu().findItem(R.id.planning_process).setChecked(true);
            PlanningProcessFragment planningProcess = new PlanningProcessFragment();
            fragmentManager.beginTransaction().replace(R.id.index,planningProcess).commit();
        }
        */
    }

    @Override
    public void onBackPressed() {
        System.out.println("ssssss");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
//        if(toggle.setToolbarNavigationClickListener();)
  //      {
            System.out.println("helpashelp");
      //      return true;
    //    }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            toggle.setDrawerIndicatorEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            return true;
        }

        if(item.getItemId()==android.R.id.home) {

                Toast.makeText(this,"fackee",Toast.LENGTH_SHORT).show();
                FragmentManager fm = getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fragmentManager = getSupportFragmentManager();

        switch(id)
        {
            case R.id.my_process:
                MyProcessFragment myProcess = new MyProcessFragment();

                fragmentManager.beginTransaction().replace(R.id.index,myProcess).commit();
                //fragmentManager.executePendingTransactions();
                //Toast.makeText(this,"有切到",Toast.LENGTH_SHORT).show();
                break;
            case R.id.other_process:
                //OtherProcessFragment otherProcess = new OtherProcessFragment();
                //fragmentManager.beginTransaction().replace(R.id.index,otherProcess).commit();
                break;
            case R.id.find_sight:
                SightFragment sightFragment = new SightFragment();
                fragmentManager.beginTransaction().replace(R.id.index,sightFragment).commit();
            case R.id.helper:
                break;
            case R.id.prefer_setting:
                break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //System.out.println("有增資料");
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 1:
                if(resultCode ==RESULT_OK)
                {

                    Bundle bundle = data.getExtras();
                    ContentValues cv = new ContentValues();
                    cv.put("ProcessName",bundle.getString("ProcessName"));
                    cv.put("ProcessAddress",bundle.getString("ProcessAddress"));
                    cv.put("ProcessDepartment",bundle.getString("ProcessDepartment"));
                    db.insert(DATABASE_TABLE,null,cv);
                    db.close();
                }
                break;
        }
    }
}
