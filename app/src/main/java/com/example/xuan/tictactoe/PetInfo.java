package com.example.xuan.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PetInfo extends Activity {
    TextView birthday;
    EditText name;
    String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pet_info);

        birthday = (TextView) findViewById(R.id.pet_birth);
        birthday.setText(birthday.getText() + getCurrentDate());
    }


    private String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        return sdf.format(cal.getTime());
    }


    public void onGenderClicked(View view) {
        boolean is_checked = ((RadioButton) view).isChecked();

        if (is_checked) {
            switch (view.getId()) {
                case (R.id.pet_female):
                    Log.d("gender", "pet is female");
                    gender = "Female";
                    break;
                case (R.id.pet_male):
                    Log.d("gender", "pet is male");
                    gender = "Male";
                    break;
            }
        }
    }

    public void createMain(View view) {
        name = (EditText) findViewById(R.id.pet_name);

        ThePet.setName(name.getText().toString());
        ThePet.setBirthday(getCurrentDate());
        ThePet.setGender(gender);

        Intent intent = new Intent (this, MainGame.class);
        startActivity(intent);
    }
}
