package com.example.xuan.pet2fit;

import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatabaseFragment extends Fragment {
    private CreaturesDAO data_source;

    public DatabaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root_view = inflater.inflate(R.layout.fragment_database, container, false);

        View fight_button = root_view.findViewById(R.id.fight_button);

        // When user clicks on the button
        fight_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCreature();
            }
        });

        return root_view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        data_source = new CreaturesDAO(getActivity());
        data_source.open();
    }

    @Override
    public void onResume() {
        data_source.open();
        super.onResume();
    }

    @Override
    public void onPause() {
        data_source.close();
        super.onPause();
    }

    private void getCreature() {
        AICreature creature = data_source.getCreatureWithSpecificLevel(ThePet.getCurrentLevel());
        System.out.println(creature.toString());
    }
}
