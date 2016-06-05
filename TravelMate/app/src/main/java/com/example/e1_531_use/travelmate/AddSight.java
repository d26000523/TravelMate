package com.example.e1_531_use.travelmate;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddSight extends AppCompatActivity  implements OnMapReadyCallback,DirectionFinderListener  {
    private int pageNum = 1; //目前頁數
    private ProgressDialog progressDialog;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private List<Polyline> polycirclePaths = new ArrayList<>();
    private GoogleMap mMap;
    private SupportMapFragment fragment;

    private RecyclerView recyclerView;
    private RecyclerAdapter_SightList adapter;
    private RecyclerView.LayoutManager layoutManager;
    public String jsonResult=null;

    private String url = "http://web1.widelab.org/~pilika/top10sight.php";
    private ArrayList<String> SightX = new ArrayList<String>();
    private ArrayList<String> SightY = new ArrayList<String>();
    private  ArrayList<String> SightName = new ArrayList<String>();
    private ArrayList<String> SightAddress = new ArrayList<String>();
    private ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();;

    private  String ProcessDepartment = "";
    private static String DATABASE_TABLE = "ProcessList";
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    private Handler UI_Handler = new Handler() ;
    private HandlerThread mThread;
    private Handler mThreadHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sight);
        //取得前一個activity傳值
        final Bundle bundle = this.getIntent().getExtras();
        if(bundle!= null)
        {
            ProcessDepartment = bundle.getString("ProcessDepartment");
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //較handler抓景點資料庫資料
        mThread = new HandlerThread("connect");
        mThread.start();
        mThreadHandler = new Handler(mThread.getLooper());
        nameValuePair.add(new BasicNameValuePair("page", String.valueOf(pageNum)));
        mThreadHandler.post(r1);




        //同步google map
        FragmentManager fm = getSupportFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.mapSightList_add);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.mapSightList_add, fragment).commit();
        }
        fragment.getMapAsync(this);
        Button btn = (Button) findViewById(R.id.nextPageBtn_add);
        assert btn!=null;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNum++;
                nameValuePair.clear();
                nameValuePair.add(new BasicNameValuePair("page", String.valueOf(pageNum)));
                mThreadHandler.post(r1);


            }
        });

    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
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
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));

            //導航結果時間距離
            //((TextView) view.findViewById(R.id.tvDuration)).setText(route.duration.text);
            //((TextView) view.findViewById(R.id.tvDistance)).setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions().title(route.startAddress).position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions().title(route.endAddress).position(route.endLocation)));
            PolylineOptions polylineCircle = new PolylineOptions().geodesic(true).color(Color.GREEN).width(10);
            PolylineOptions polylineOptions = new PolylineOptions().geodesic(true).color(Color.BLUE).width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));


            //polylineCircle.

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera

        Double tmx=0.0,tmy=0.0;
        /*for(int i=0;i<10;i++)
        {

            LatLng sight = new LatLng(Double.parseDouble(SightY.get(i)),Double.parseDouble(SightX.get(i)));
            mMap.addMarker(new MarkerOptions().position(sight).title(SightName.get(i)));
            tmy+=Double.parseDouble(SightY.get(i));
            tmx+=Double.parseDouble(SightX.get(i));


        }*/
//        LatLng center = new LatLng(tmy/10.0,tmx/10.0);
        LatLng center = new LatLng(23.5,121);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center,8));
        //zoom數字越大地圖越近
        /* 使用動畫的效果移動地圖
        CameraPosition cameraPosition = new CameraPosition.Builder().target(center).zoom(14).build();
        //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/

        /*導航(重要):
        //new DirectionFinder(this, "25.032033, 121.386682", "25.035225,121.389539").execute();
        */

    }
    private Runnable r1=new Runnable () {

        @Override
        public void run() {
            GetJsonResult getJsonResult = new GetJsonResult(url,nameValuePair);
            jsonResult = getJsonResult.GetResultQuery();
            UI_Handler.post(r2);
        }
    };
    private Runnable r2=new Runnable () {

        @Override
        public void run() {
//                        progressDialog = ProgressDialog.show(getContext(), "Please wait.",
            //                              "Finding direction..!", true);
            //jsonResult = executeQuery(url);
            ListDrawer(jsonResult);
            recyclerView = (RecyclerView)findViewById(R.id.sight_recycler_view_add);
            layoutManager = new LinearLayoutManager(getBaseContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(getBaseContext(),DividerItemDecoration.VERTICAL_LIST));
            recyclerView.setAdapter(adapter);
            //                progressDialog.dismiss();
        }
    };
    public void ListDrawer(String res)
    {
        try{
            JSONArray jsonMainNode =new JSONArray(res);
            String total = "";
            SightX.clear();
            SightY.clear();
            SightName.clear();
            for (int i=0;i<jsonMainNode.length();i++)
            {
                JSONObject jo =jsonMainNode.getJSONObject(i);
                SightX.add(jo.getString("Px"));
                SightY.add(jo.getString("Py"));
                SightName.add(jo.getString("Name"));
                SightAddress.add(jo.getString("SightAdd"));
            }
            if(mMap!=null)
            {
                mMap.clear();
                Double tmx=0.0,tmy=0.0;
                for(int i=0;i<10;i++)
                {
                    LatLng sight = new LatLng(Double.parseDouble(SightY.get(i)),Double.parseDouble(SightX.get(i)));
                    mMap.addMarker(new MarkerOptions().position(sight).title(SightName.get(i)));
                    tmy+=Double.parseDouble(SightY.get(i));
                    tmx+=Double.parseDouble(SightX.get(i));
                }
                LatLng center = new LatLng(tmy/10.0,tmx/10.0);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center,8));

            }
            adapter = new RecyclerAdapter_SightList(SightName,SightAddress);
            adapter.setOnItemClickListener(new RecyclerAdapter_SightList.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    //Toast.makeText(getBaseContext(),"短",Toast.LENGTH_SHORT).show();
                    Intent reply = new Intent();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("ProcessName",SightName.get(position));
                    bundle1.putString("ProcessAddress",SightAddress.get(position));
                    bundle1.putString("ProcessPx",SightX.get(position));
                    bundle1.putString("ProcessPy",SightY.get(position));
                    bundle1.putString("ProcessDepartment",ProcessDepartment);

                    reply.putExtras(bundle1);
                    setResult(RESULT_OK,reply);
                    finish();
                }
                @Override
                public void onItemLongClick(View view, int position) {
                    Toast.makeText(getBaseContext(),"長",Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (JSONException e)
        {
            //Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            System.out.println(e.toString());
        }
    };


}
