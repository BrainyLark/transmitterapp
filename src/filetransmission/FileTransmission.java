/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filetransmission;

import filetransmission.view.MainController;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author erdenebileg
 */
public class FileTransmission extends Application {
    
    private Stage primaryStage;
    private MainController mainController;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("view/Main.fxml"));
        
        try {
            AnchorPane root = (AnchorPane) loader.load();
            mainController = loader.getController();
            
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("File Transmitter");
            primaryStage.show();
            
        } catch (IOException ex) {
            System.out.println("Error occured while loading FXML: " + ex.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
