package com.example.e1_531_use.travelmate;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class EndingProcessFragment extends Fragment  {
    private FragmentActivity myContext;
    Button button;

    public EndingProcessFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ending_process, container, false);
        button = (Button) v.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(),"fackee",Toast.LENGTH_SHORT).show();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);

                ProcessListFragment processList = new ProcessListFragment(null,"這是測試","測試業");

                ft.replace(R.id.index, processList).addToBackStack(null).commit();
                ActionBar bar = ((AppCompatActivity)getActivity()).getSupportActionBar();
                assert bar != null;
                bar.setDisplayHomeAsUpEnabled(false);
                bar.setDisplayHomeAsUpEnabled(true);
                bar.setDisplayShowHomeEnabled(true);


            }
        });

        // Inflate the layout for this fragment
        return v;
    }



}
