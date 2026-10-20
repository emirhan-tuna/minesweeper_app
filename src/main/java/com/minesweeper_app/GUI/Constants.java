package com.minesweeper_app.GUI;

public class Constants {
    static final int height = 800;
    static final int width = 1000;
    static final String beginner = "BEGINNER";
    static final String medium = "MEDIUM";
    static final String expert = "EXPERT";
    static final double buttonWidth = 220;
    static final double buttonHeight = 45;

    public static String getColor(int num) {
        switch (num) {
            case 1:
                return "#0000ff";
            case 2:
                return "#008000";
            case 3:
                return "#ff0000";
            case 4:
                return "#0000ff";
            case 5:
                return "#800000";
            case 6: 
                return "#008080";
            case 7:
                return "#000000";
            case 8:
                return "#808080";
            default:
                return "#ffffff";
        }
    }
}
