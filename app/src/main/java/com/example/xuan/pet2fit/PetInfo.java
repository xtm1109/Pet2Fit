package com.example.xuan.pet2fit;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
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
    CreaturesDAO data_source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pet_info);

        birthday = (TextView) findViewById(R.id.pet_birth);
        birthday.setText(birthday.getText() + getCurrentDate());

        res = this.getResources();
        xrp = res.getXml(R.xml.levels);

        data_source = new CreaturesDAO(this);
        data_source.open();
    }

    @Override
    protected void onResume() {
        super.onResume();

        data_source.open(); // Open DAO to write to the database
    }

    @Override
    protected void onPause() {
        super.onPause();

        data_source.close(); // Close DAO
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
                    gender = "Female";
                    break;
                case (R.id.pet_male):
                    gender = "Male";
                    break;
            }
        }
    }

    /*
     * This helper method goes through a xml file
     * and parse the information in the xml file
     * into the database and create a Creatures table
     */
    private void loadCreatureToDb() {
        String n = "";
        int h = 0;
        int a = 0;
        int lv = 0;

        data_source.dropTableAndRecreate();

        try {
            XmlResourceParser xrp = this.getResources().getXml(R.xml.creatures);

            int eventType = xrp.getEventType();
            boolean done = false;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if ((eventType == XmlPullParser.START_TAG)
                        && (xrp.getName().equalsIgnoreCase("creature"))) {

                    while (!done) {
                        eventType = xrp.next();

                        if ((eventType == XmlPullParser.END_TAG)
                                && (xrp.getName().equalsIgnoreCase("creature"))) {
                            done = true;
                        }
                        else if ((eventType == XmlPullParser.START_TAG) &&
                                (xrp.getName().equalsIgnoreCase("name"))) {
                            eventType = xrp.next();

                            if (eventType == XmlPullParser.TEXT) {
                                n = xrp.getText();
                            }
                        }
                        else if ((eventType == XmlPullParser.START_TAG) &&
                                (xrp.getName().equalsIgnoreCase("health"))) {
                            eventType = xrp.next();

                            if (eventType == XmlPullParser.TEXT) {
                                h = Integer.parseInt(xrp.getText());
                            }
                        }
                        else if ((eventType == XmlPullParser.START_TAG) &&
                                (xrp.getName().equalsIgnoreCase("attack"))) {
                            eventType = xrp.next();

                            if (eventType == XmlPullParser.TEXT) {
                                a = Integer.parseInt(xrp.getText());
                            }
                        }
                        else if ((eventType == XmlPullParser.START_TAG) &&
                                (xrp.getName().equalsIgnoreCase("level"))) {
                            eventType = xrp.next();

                            if (eventType == XmlPullParser.TEXT) {
                                lv = Integer.parseInt(xrp.getText());
                            }
                        }
                    }
                    data_source.createCreature(n, h, a, lv);
                }

                done = false;
                eventType = xrp.next();
            }

            /* This prints the database path
             * Using Android Studio terminal, use this command to get the database file:
             * (CAUTION!! sdk/platform-tools must be a Environment Variable)
             * adb exec-out run-as [package_name] cat [path_to_db] > [path_to_save]
             * Then in OS terminal use sqlite3 to view the database file
             */
            //Parameter: name of the database file
            //File dbFile = getActivity().getDatabasePath("creatures.db");
            //System.out.println(dbFile.getAbsolutePath());

        }
        catch (XmlPullParserException e) { }
        catch (IOException e) {}
    }

    public void createMain(View view) throws IOException, XmlPullParserException {
        name = (EditText) findViewById(R.id.pet_name);

        ThePet.setName(name.getText().toString());
        ThePet.setBirthday(getCurrentDate());
        ThePet.setGender(gender);

        // New game so pet level is the lowest level, which is level 1
        ThePet.setPetLevel(xrp, 1);

        // Insert AI creatures from a xml file to the database
        loadCreatureToDb();

        Intent intent = new Intent (this, MainGame.class);
        startActivity(intent);
    }
}
