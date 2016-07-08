/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sceneControllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.commons.lang3.*;

/**
 * FXML Controller class
 *
 * @author Andrew
 */
public class MainWindowSceneController implements Initializable {

    Stage mainstage;
    private double X, Y;
    
    @FXML
    private TextField timeTextField;
    @FXML
    private Button closeButton;
    @FXML
    private Button startButton;
    @FXML
    private ProgressBar progressBar;

    @FXML
    protected void onRectanglePressed(MouseEvent event) {
        X = mainstage.getX() - event.getScreenX();
        Y = mainstage.getY() - event.getScreenY();
    }
    @FXML
    protected void onRectangleDragged(MouseEvent event) {
        mainstage.setX(event.getScreenX() + X);
        mainstage.setY(event.getScreenY() + Y);
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }    
    
    public void setDefaults(Stage stage){
        mainstage = stage;
    }

    @FXML
    private void close() {
        System.exit(0);
    }
    
    @FXML
    private void actionStartButton() {
        if (timeTextField.getText().trim().matches("\\d+")) {
            try {
                Runtime.getRuntime().exec(shutdownCommand());
                progressBarThread();
            } catch (IOException ex) {
                Logger.getLogger(MainWindowSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("Entered Time Is Not Valid");
        }
    }

    private String shutdownCommand(){
        String timeInSeconds = String.valueOf(Integer.valueOf(timeTextField.getText().trim()) * 60);

        if (SystemUtils.IS_OS_WINDOWS) {
            return "shutdown.exe -s -f -t " + timeInSeconds;
        } else if (SystemUtils.IS_OS_AIX) {
            return "shutdown -Fh " + timeInSeconds;
        } else if (SystemUtils.IS_OS_HP_UX) {
            return "shutdown -hy " + timeInSeconds;
        } else if (SystemUtils.IS_OS_IRIX) {
            return "shutdown -y -g " + timeInSeconds;
        } else if (SystemUtils.IS_OS_SOLARIS || SystemUtils.IS_OS_SUN_OS) {
            return "shutdown -y -i5 -g" + timeInSeconds;
        } else if (SystemUtils.IS_OS_FREE_BSD || SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC 
                || SystemUtils.IS_OS_MAC_OSX || SystemUtils.IS_OS_NET_BSD || SystemUtils.IS_OS_OPEN_BSD 
                || SystemUtils.IS_OS_UNIX) {
            return "shutdown -h " + timeInSeconds;
        } else {
            return "";
        }
    }
    
    private void progressBarThread() {
        Thread timerThread = new Thread() {
            @Override
            public void run() {
                int currentTime = 0;
                int totalTime = Integer.valueOf(timeTextField.getText().trim()) * 600;

                while (currentTime < totalTime) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MainWindowSceneController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    currentTime++;
                    updateProgressBar(currentTime, totalTime);
                }
            }
        };
        timerThread.start();
    }
    
    private void updateProgressBar(final double currentValue, final double maxValue) {
        Platform.runLater(() -> {
            progressBar.setProgress(currentValue / maxValue);
        });
    }
        
}
