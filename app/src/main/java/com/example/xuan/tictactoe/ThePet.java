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
    private static int current_health;
    private static int current_stamina;
    private static int current_xp;

    private ThePet() {}

    public static void setName(String name) {
        ThePet.pet_name = name;
    }

    public static void setBirthday(String birthday) {
        ThePet.pet_birthday = birthday;
    }

    public static void setGender (String gender) {
        ThePet.pet_gender = gender;
    }

    public static void setPetLevel(XmlResourceParser xrp, int lv) throws IOException, XmlPullParserException {
        Level.setLevel(xrp, lv);
        ThePet.setCurrentHealth(ThePet.getLevelHealth());
        ThePet.setCurrentStamina(ThePet.getLevelStamina());
        ThePet.setCurrentXP(0);
    }

    public static void setCurrentHealth(int h) {
        if (h <= ThePet.getLevelHealth())
            ThePet.current_health = h;
    }

    public static void setCurrentStamina(int s) {
        if (s <= ThePet.getLevelStamina())
            ThePet.current_stamina = s;
    }

    public static void setCurrentXP(int xp) {
        if (xp <= ThePet.getLevelXP())
            ThePet.current_xp = xp;
    }

    public static String getName() {
        return ThePet.pet_name;
    }

    public static String getBirthday() {
        return ThePet.pet_birthday;
    }

    public static String getGender() {
        return ThePet.pet_gender;
    }

    public static int getCurrentHealth() {
        return ThePet.current_health;
    }

    public static int getCurrentStamina() {
        return ThePet.current_stamina;
    }

    public static int getCurrentXP() {
        return ThePet.current_xp;
    }

    public static int getLevelHealth() {
        return Level.getHealth();
    }

    public static int getLevelStamina() {
        return Level.getStamina();
    }

    public static int getLevelXP() {
        return Level.getXP();
    }


    private static class Level {
        private static int pet_level;
        private static int pet_health;
        private static int pet_stamina;
        private static int pet_xp;

        static private void setLevel(XmlResourceParser xrp, int lv) throws XmlPullParserException, IOException {
            int eventType = xrp.getEventType();
            boolean done = false;

            while ((!done) || (eventType != XmlPullParser.END_DOCUMENT)) {
                if ((eventType == XmlPullParser.START_TAG)
                        && (xrp.getName().equalsIgnoreCase("name"))) {
                    eventType = xrp.next();

                    if ((eventType == XmlPullParser.TEXT) &&
                            (xrp.getText().equalsIgnoreCase(Integer.toString(lv)))) {
                        Level.pet_level = Integer.parseInt(xrp.getText());

                        while (!done) {
                            eventType = xrp.next();

                            if ((eventType == XmlPullParser.END_TAG)
                                    && (xrp.getName().equalsIgnoreCase("level"))) {
                                done = true;
                            } else if ((eventType == XmlPullParser.START_TAG) &&
                                    (xrp.getName().equalsIgnoreCase("health"))) {
                                eventType = xrp.next();

                                if (eventType == XmlPullParser.TEXT) {
                                    Level.pet_health = Integer.parseInt(xrp.getText());
                                }
                            } else if ((eventType == XmlPullParser.START_TAG) &&
                                    (xrp.getName().equalsIgnoreCase("stamina"))) {
                                eventType = xrp.next();

                                if (eventType == XmlPullParser.TEXT) {
                                    Level.pet_stamina = Integer.parseInt(xrp.getText());
                                }
                            } else if ((eventType == XmlPullParser.START_TAG) &&
                                    (xrp.getName().equalsIgnoreCase("xp"))) {
                                eventType = xrp.next();

                                if (eventType == XmlPullParser.TEXT) {
                                    Level.pet_xp = Integer.parseInt(xrp.getText());
                                }
                            }
                        }
                    }
                }

                eventType = xrp.next();
            }
        }

        static private int getHealth() {
            return Level.pet_health;
        }

        private static int getStamina() {
            return Level.pet_stamina;
        }

        private static int getXP() {
            return Level.pet_xp;
        }
    }

}
