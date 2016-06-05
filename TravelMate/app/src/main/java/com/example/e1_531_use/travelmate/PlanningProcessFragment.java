package com.example.e1_531_use.travelmate;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlanningProcessFragment extends Fragment {
    public PlanningProcessFragment() {
        // Required empty public constructor
    }
    private Context cc;
    private RecyclerView recyclerView;
    private RecyclerAdapter_UserProcessList adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressDialog progressDialog;
    public String jsonResult;
    public String url = "http://120.126.17.104/~pilika/test0415.php";
    public ArrayList<String> TripName = new ArrayList<String>();
    public ArrayList<String> TripDate = new ArrayList<String>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_planning_process, container, false);

        final Handler UI_Handler = new Handler() ;
        HandlerThread mThread;
        Handler mThreadHandler;
        mThread = new HandlerThread("connect");
        mThread.start();
        mThreadHandler = new Handler(mThread.getLooper());
        mThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                GetJsonResult getJsonResult = new GetJsonResult(url);
                jsonResult = getJsonResult.GetResultQuery();
                UI_Handler.post(new Runnable()
                {
                    @Override
                    public void run() {

//                        progressDialog = ProgressDialog.show(getContext(), "Please wait.",
  //                              "Finding direction..!", true);
                        //jsonResult = executeQuery(url);
                        ListDrawer(jsonResult);
                        recyclerView = (RecyclerView)getActivity().findViewById(R.id.user_process_my_recycler_view);
                        System.out.println(getActivity());
                        layoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setHasFixedSize(true);

                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
                        recyclerView.setAdapter(adapter);



                    }
                });

//                progressDialog.dismiss();
            }
        });




        // Inflate the layout for this fragment

        return v;
    }
    public void ListDrawer(String res)
    {
        try{
            JSONArray jsonMainNode =new JSONArray(res);
            String total = "";
            TripName.clear();
            TripDate.clear();
            for (int i=0;i<jsonMainNode.length();i++)
            {
                JSONObject jo =jsonMainNode.getJSONObject(i);
                TripName.add(jo.getString("TripName"));
                TripDate.add(jo.getString("TripDate"));
            }
            adapter = new RecyclerAdapter_UserProcessList(TripName,TripDate);
            adapter.setOnItemClickListener(new RecyclerAdapter_UserProcessList.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Toast.makeText(getContext(),"短",Toast.LENGTH_SHORT).show();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                    cc = getActivity();
                    ProcessListFragment processList = new ProcessListFragment(cc,String.valueOf(recyclerView.getChildAdapterPosition(view)),"這是第幾個行程:" + String.valueOf(recyclerView.getChildAdapterPosition(view)));

                    ft.replace(R.id.index, processList).addToBackStack(null).commit();
                    ActionBar bar = ((AppCompatActivity)getActivity()).getSupportActionBar();
                    assert bar != null;
                    bar.setDisplayHomeAsUpEnabled(false);
                    bar.setDisplayHomeAsUpEnabled(true);
                    bar.setDisplayShowHomeEnabled(true);
                }
                @Override
                public void onItemLongClick(View view, int position) {
                    Toast.makeText(getContext(),"長",Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (JSONException e)
        {
            //Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            System.out.println(e.toString());
        }
    }




}
