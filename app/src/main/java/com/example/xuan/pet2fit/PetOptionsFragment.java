package com.example.xuan.pet2fit;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PetOptionsFragment extends Fragment {
    /*
     * 5 options for pets: (default is 1)
     *      1-Blue Dragon
     *      2-Black Dragon
     *      3-Gold Dragon
     *      4-Green Dragon
     *      5-Red Dragon
     */
    int current_choice = 1;
    int image_resource = R.drawable.bd_icon;


    public PetOptionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View pet_options = inflater.inflate(R.layout.fragment_pet_options, container, false);

        /* ~~~ Handle left option ~~~ */
        View left_button = pet_options.findViewById(R.id.left_arrow);
        left_button.setOnClickListener(new View.OnClickListener() { // User clicks on the button
            @Override
            public void onClick(View v) {
                current_choice--;

                if (current_choice == 0) {
                    current_choice = 5;
                }

                setImage();

                ImageView pet_image = (ImageView) getActivity().findViewById(R.id.pet_image);
                pet_image.setImageResource(image_resource);
            }
        });
        /* ~~~ DONE ~~~ */

        /* ~~~ Handle right option ~~~ */
        View right_button = pet_options.findViewById(R.id.right_arrow);
        right_button.setOnClickListener(new View.OnClickListener() { // User clicks on the button
            @Override
            public void onClick(View v) {
                current_choice++;

                if (current_choice == 6) {
                    current_choice = 0;
                }

                setImage();

                ImageView pet_image = (ImageView) getActivity().findViewById(R.id.pet_image);
                pet_image.setImageResource(image_resource);
            }
        });
        /* ~~~ DONE ~~~ */

        return pet_options;
    }

    private void setImage() {
        switch (current_choice) {
            case 1:
                image_resource = R.drawable.bd_icon;
                break;
            case 2:
                image_resource = R.drawable.bkd_icon;
                break;
            case 3:
                image_resource = R.drawable.gd_icon;
                break;
            case 4:
                image_resource = R.drawable.grd_icon;
                break;
            case 5:
                image_resource = R.drawable.rd_icon;
                break;
        }
    }
}
