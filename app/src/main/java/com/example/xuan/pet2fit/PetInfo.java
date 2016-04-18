package com.example.xuan.pet2fit;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PetInfo extends Activity {
    TextView birthday;
    EditText name;
    String gender;
    Resources res;
    XmlResourceParser xrp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pet_info);

        birthday = (TextView) findViewById(R.id.pet_birth);
        birthday.setText(birthday.getText() + getCurrentDate());

        res = this.getResources();
        xrp = res.getXml(R.xml.levels);
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

    public void createMain(View view) throws IOException, XmlPullParserException {
        name = (EditText) findViewById(R.id.pet_name);

        ThePet.setName(name.getText().toString());
        ThePet.setBirthday(getCurrentDate());
        ThePet.setGender(gender);

        // New game so pet level is the lowest level, which is level 1
        ThePet.setPetLevel(xrp, 1);

        Intent intent = new Intent (this, MainGame.class);
        startActivity(intent);
    }
}
