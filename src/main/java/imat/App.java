/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author oloft
 */
public class App extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("iMat.fxml"));

        Scene scene = new Scene(root, 1360, 765);

        stage.getIcons().add(new Image(getClass().getResource(".dat215/heart2.png").toExternalForm()));
        stage.setTitle("iMat");
        stage.setScene(scene);
        stage.setMinWidth(630);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> HomeModel.getInstance().shutDown()));
    }
    
}
