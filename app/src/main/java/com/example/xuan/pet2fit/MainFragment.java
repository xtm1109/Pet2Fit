package com.example.xuan.pet2fit;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainFragment extends Fragment {
    private AlertDialog mDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.fragment_main, container, false);

        /* ~~~ Handle all buttons ~~~ */

        /* ~~~ Handle new_button ~~~ */
        View new_button = root_view.findViewById(R.id.new_button);
        // When user clicks on the button
        new_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                * Clear all values in SharedPreferences when user starts a new game
                */
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();

                Intent intent = new Intent(getActivity(), NewGame.class);
                startActivity(intent);
            }
        });
        /* ~~~ DONE HANDLE NEW ~~~ */

        /* ~~~ Handle continue_button ~~~ */
        View continue_button = root_view.findViewById(R.id.continue_button);
        // When user clicks on the button
        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check SharedPreferences for a boolean value of
                // whether there is an existing game
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                boolean value = prefs.getBoolean("existed_game", false);

                if (value) { // there is an existing game, continue the game
                    Intent intent = new Intent(getActivity(), MainGame.class);
                    startActivity(intent);
                }
                else { // no existing game, pop up a message
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(getActivity());
                    builder.setTitle("Oops!");
                    builder.setMessage("There is no existing game!");
                    builder.setCancelable(false);
                    builder.setPositiveButton(R.string.ok_label,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //nothing
                                }
                            });
                    mDialog = builder.show();
                }
            }
        });
        /* ~~~ DONE HANDLE CONTINUE ~~~ */

        /* ~~~ Handle about_button ~~~ */
        View aboutButton = root_view.findViewById(R.id.about_button);
        // When user clicks on the button
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // pop up a message
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.about_title);
                builder.setMessage("Pet2Fit\nThis is a simulation LAN game " +
                        "that functions based on user fitness activity.\n\n" +
                        "Credits: All the sprites are belonged to\n" +
                        "Heroes Might of Magic game series.");
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.ok_label,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //nothing
                            }
                        });
                mDialog = builder.show();
            }
        });
        /* ~~~ DONE HANDLE ABOUT ~~~ */

        /* ~~~ DONE HANDLE ALL BUTTONS~~~ */

        return root_view;
    }

    @Override
    public void onPause() {
        super.onPause();

        //Get rid of any box
        if (mDialog != null)
            mDialog.dismiss();
    }
}