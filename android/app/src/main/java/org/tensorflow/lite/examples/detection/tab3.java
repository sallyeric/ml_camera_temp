package org.tensorflow.lite.examples.detection;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class tab3 extends Fragment {

    private ArrayList<Integer> imageList;
    private static final int DP = 24;

    ImageButton commuBtn;
    ImageButton scrapBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab3, container, false);

        this.initializeData();

        ViewPager viewPager = view.findViewById(R.id.viewPager);
        viewPager.setClipToPadding(false);

        float density = getResources().getDisplayMetrics().density;
        int margin = (int) (DP * density);
        viewPager.setPadding(margin, 0, margin, 0);
        viewPager.setPageMargin(margin/2);

        viewPager.setAdapter(new ViewPagerAdapter(this.getContext(), imageList));

        commuBtn=(ImageButton)view.findViewById(R.id.gatherCommunity);
        scrapBtn=(ImageButton)view.findViewById(R.id.scrap);

        commuBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Intent
                Context context = v.getContext();
                Intent commuIntent = new Intent(context, CommuActivity.class);
                startActivity(commuIntent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
            }

        });

        return view;
    }

    public void initializeData()
    {
        imageList = new ArrayList();

        imageList.add(R.drawable.iu2);
        imageList.add(R.drawable.iu4);
        imageList.add(R.drawable.iu5);
        imageList.add(R.drawable.iufullscreen);
        imageList.add(R.drawable.iutwo);
    }

}
