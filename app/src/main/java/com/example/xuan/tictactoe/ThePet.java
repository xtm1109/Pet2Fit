package com.example.xuan.tictactoe;

import java.util.Date;

/**
 * Created by Xuan on 4/13/2016.
 */
public final class ThePet {
    private static String pet_name = "name";
    private static String pet_birthday = "01/01/1970";
    private static String pet_gender = "gender";
    private static Level pet_level = new Level();

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

    static public void setLevel (Level lv) {
        ThePet.pet_level = lv;
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

    private static class Level {
        private int pet_level;
        private int pet_health;
        private int pet_stamina;
        private int pet_xp;

        private Level() {
            this.pet_level = 1;
            this.pet_health = 100;
            this.pet_stamina = 50;
            this.pet_xp = 0;
        }

        private Level(int lv, int h, int s, int xp) {
            this.pet_level = lv;
            this.pet_health = h;
            this.pet_stamina = s;
            this.pet_xp = xp;
        }


    }

}
