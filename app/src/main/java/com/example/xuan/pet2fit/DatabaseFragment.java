package com.example.xuan.pet2fit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        fight_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AICreature creature = getCreature();

                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getActivity());
                builder.setTitle("Creature Information");
                builder.setMessage("Creature Name: " + creature.getName() + "\n" +
                        "Creature Level: " + creature.getLevel() + "\n" +
                        "Creature Strength: " + creature.getAttack());
                builder.setCancelable(false);
                builder.setPositiveButton("Begin Fight!",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                double user_turn = Math.random();
                                double ai_turn = Math.random();
                                int xp_gain;

                                AlertDialog.Builder builder =
                                        new AlertDialog.Builder(getActivity());

                                if (user_turn >= ai_turn) {
                                    xp_gain = creature.getHealth()/10; // no logic, can be anything
                                    builder.setTitle("You won!");
                                    builder.setMessage("You have gained " + xp_gain + " XP!");
                                }
                                else {
                                    xp_gain = 0 - creature.getHealth()/10;
                                    builder.setTitle("You lost....");
                                    builder.setMessage("You have lost " + xp_gain + " XP...");
                                }
                                ThePet.setCurrentXP(ThePet.getCurrentXP() + xp_gain);
                                builder.setCancelable(false);
                                builder.setPositiveButton("Alrighty!",
                                        new DialogInterface.OnClickListener() {
                                            // Update XP Bar with newly-gained XP
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                MainGameView top = (MainGameView) getActivity().findViewById(R.id.top_main_view);
                                                top.invalidate();
                                            }
                                        });
                                builder.show();
                            }
                        });
                builder.show();
            }
        });

        View battle_button = root_view.findViewById(R.id.battle_button);
        battle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BattleLAN.class);
                startActivity(intent);
            }
        });

        View training_button = root_view.findViewById(R.id.training_button);
        training_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TrainingField.class);
                startActivity(intent);
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

    private AICreature getCreature() {
        return (data_source.getCreatureWithSpecificLevel(ThePet.getCurrentLevel()));
    }
}
