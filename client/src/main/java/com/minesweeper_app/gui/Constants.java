package com.minesweeper_app.gui;

public class Constants {
    static double height;
    static double width;
    public static final String beginner = "BEGINNER";
    public static final String medium = "MEDIUM";
    public static final String expert = "EXPERT";
    public static final double buttonWidth = 220;
    public static final double buttonHeight = 45;
    public static final String OPEN = "-fx-background-color: #dcdcdc; -fx-border-color: #a0a0a0; -fx-border-width: 0.5px; -fx-font-weight: bold; -fx-padding: 0; -fx-text-fill: ";
    public static final String HIDDEN = "-fx-background-color: #c0c0c0; -fx-border-color: #ffffff #7b7b7b #7b7b7b #ffffff; -fx-border-width: 2px; -fx-padding: 0; -fx-font-weight: bold;";

    public static void setSizes(double width, double height) {
        Constants.height = height;
        Constants.width = width;
    }

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
