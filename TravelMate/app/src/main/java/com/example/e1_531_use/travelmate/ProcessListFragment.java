package com.example.e1_531_use.travelmate;


import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.wallet.LineItem;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProcessListFragment extends Fragment implements DirectionFinderListener {

    private String title;
    private String content;
    private Context cc;
    private static final int ADD_SIGHT = 1;

    private RecyclerView recyclerView;
    private RecyclerAdapter_ProcessList adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ProgressDialog progressDialog;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private List<Polyline> polycirclePaths = new ArrayList<>();

    public ArrayList<String> ProcessDuration = new ArrayList<String>();
    public ArrayList<String> ProcessDistance = new ArrayList<String>();
    public ArrayList<String> ProcessName = new ArrayList<String>();
    public ArrayList<String> ProcessAddress = new ArrayList<String>();
    public ArrayList<String> ProcessPx = new ArrayList<String>();
    public ArrayList<String> ProcessPy = new ArrayList<String>();


    private static String DATABASE_TABLE = "ProcessList";
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public ProcessListFragment() {
        // Required empty public constructor

    }
    public ProcessListFragment(Context a,String s, String c) {
        content=c;
        title = s;
        cc = a;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ProcessName.add("學校");
        ProcessAddress.add("桃園市龜山區文化一路259號");

        // Inflate the layout for this fragment

        ActionBar actionbar =  ((AppCompatActivity) getActivity()).getSupportActionBar();
        assert  actionbar!= null;
        actionbar.setTitle(title);
        View v = inflater.inflate(R.layout.fragment_process_list, container, false);

        //設標題跟內容
        TextView rr = (TextView)v.findViewById(R.id.test);
        rr.setText(content);

        Button button = (Button)v.findViewById(R.id.addProcess);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("ProcessDepartment",title);
                Intent launchAddSight = new Intent();
                launchAddSight.setClass(getContext(),AddSight.class);
                launchAddSight.putExtras(bundle);
                startActivityForResult(launchAddSight,ADD_SIGHT);

            }
        });


        //開資料庫取得recyclerview元件
        dbHelper = new DBHelper(getActivity());
        db = dbHelper.getReadableDatabase();
        String [] colName = new String[]{"ProcessName","ProcessAddress"};
        String where = "ProcessDepartment = " + title;
        Cursor cursor = db.query(DATABASE_TABLE, colName, where, null, null, null, null);

        cursor.moveToFirst();
        Toast.makeText(getContext(),String.valueOf(cursor.getCount()),Toast.LENGTH_SHORT).show();
        for(int i=0;i<cursor.getCount();i++)
        {
            ProcessName.add(cursor.getString(0));
            ProcessAddress.add(cursor.getString(1));
            cursor.moveToNext();
        }

        recyclerView = (RecyclerView)v.findViewById(R.id.process_recycler_view);
//        System.out.println(getActivity());
        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //加中間分隔線條
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
        BindData();


        return v;
    }



    @Override
    public void onStop() {
//        db.close();
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("資料");
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 1:
                if(resultCode ==getActivity().RESULT_OK)
                {
                    db = dbHelper.getReadableDatabase();
                    Bundle bundle = data.getExtras();
                    ContentValues cv = new ContentValues();
                    cv.put("ProcessName",bundle.getString("ProcessName"));
                    cv.put("ProcessAddress",bundle.getString("ProcessAddress"));
                    ProcessPx.add(bundle.getString("ProcessPx"));
                    ProcessPy.add(bundle.getString("ProcessPy"));
                    System.out.println(ProcessPx.size());
                    if(ProcessPx.size()==2)
                    {
                        try {
                            new DirectionFinder(this, ProcessPy.get(0) + ", " + ProcessPx.get(0),ProcessPy.get(1) + ", " + ProcessPx.get(1)).execute();
                            ProcessPx.remove(0);
                            ProcessPy.remove(0);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    cv.put("ProcessDepartment",bundle.getString("ProcessDepartment"));
                    db.insert(DATABASE_TABLE,null,cv);
                    db.close();
                    BindData();
                }
                break;
        }
    }
    public void BindData()
    {
        ProcessName.clear();
        ProcessAddress.clear();
        //開資料庫取得recyclerview元件
        dbHelper = new DBHelper(getActivity());
        db = dbHelper.getReadableDatabase();
        String [] colName = new String[]{"ProcessName","ProcessAddress"};
        String where = "ProcessDepartment = " + title;
        Cursor cursor = db.query(DATABASE_TABLE, colName, where, null, null, null, null);

        cursor.moveToFirst();

        for(int i=0;i<cursor.getCount();i++)
        {

            ProcessName.add(cursor.getString(0));
            ProcessAddress.add(cursor.getString(1));
            cursor.moveToNext();
        }


        adapter = new RecyclerAdapter_ProcessList(ProcessName,ProcessAddress);
        adapter.setOnItemClickListener(new RecyclerAdapter_ProcessList.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(),"短",Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(getContext(),"長",Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(getContext(), "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        polycirclePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
        //    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));

            //導航結果時間距離
            //((TextView) view.findViewById(R.id.tvDuration)).setText(route.duration.text);
            System.out.println(route.duration.text);
            ProcessName.add(route.duration.text);
            Toast.makeText(getContext(),"所需時間:" + route.duration.text,Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(),"所需距離:" + route.distance.text,Toast.LENGTH_SHORT).show();
            ProcessAddress.add(route.distance.text);

            //((TextView) view.findViewById(R.id.tvDistance)).setText(route.distance.text);

//            originMarkers.add(mMap.addMarker(new MarkerOptions().title(route.startAddress).position(route.startLocation)));
  //          destinationMarkers.add(mMap.addMarker(new MarkerOptions().title(route.endAddress).position(route.endLocation)));
            PolylineOptions polylineCircle = new PolylineOptions().geodesic(true).color(Color.GREEN).width(10);
            PolylineOptions polylineOptions = new PolylineOptions().geodesic(true).color(Color.BLUE).width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));


            //polylineCircle.

        }
    }
}