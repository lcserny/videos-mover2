package net.cserny.videosmover.javafx;

import com.sun.javafx.application.LauncherImpl;

public class JavaFXMain {

    public static void main(String[] args) {
        LauncherImpl.launchApplication(JavaFXApplication.class, JavaFXPreloader.class, args);
    }
}
