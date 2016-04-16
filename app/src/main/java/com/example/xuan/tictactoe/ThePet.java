package com.example.xuan.tictactoe;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Date;

/**
 * Created by Xuan on 4/13/2016.
 */
public final class ThePet {
    private static String pet_name = "name";
    private static String pet_birthday = "01/01/1970";
    private static String pet_gender = "gender";
    private static int current_health = Level.getHealth();

    private ThePet() {}

    static public void setName(String name) {
        ThePet.pet_name = name;
    }

    static public void setBirthday(String birthday) {
        ThePet.pet_birthday = birthday;
    }

    static public void setGender (String gender) {
        ThePet.pet_gender = gender;
    }

    static public String getName() {
        return ThePet.pet_name;
    }

    static public String getBirthday() {
        return ThePet.pet_birthday;
    }

    static public String getGender() {
        return ThePet.pet_gender;
    }

    static public int getCurrentHealth() {
        return ThePet.current_health;
    }

    static public int getLevelHealth() {
        return Level.getHealth();
    }

    private static class Level {
        private static int pet_level;
        private static int pet_health;
        private static int pet_stamina;
        private static int pet_xp;

        static private int getHealth() {
            return Level.pet_health;
        }

        static private void setLevel(XmlResourceParser xrp) throws XmlPullParserException, IOException {
            int eventType = xrp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    // do something
                }
                else if (eventType == XmlPullParser.START_TAG) {
                    // do something
                }
                else if (eventType == XmlPullParser.END_TAG) {
                    // do something
                }
                else if (eventType == XmlPullParser.TEXT) {
                    // do something
                }
                eventType = xrp.next();
            }
        }

    }

}
