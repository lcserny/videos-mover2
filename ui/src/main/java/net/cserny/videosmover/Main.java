package net.cserny.videosmover;

import com.sun.javafx.application.LauncherImpl;

public class Main {

    public static void main(String[] args) {
        LauncherImpl.launchApplication(MainApplication.class, MainPreloader.class, args);
    }
}
