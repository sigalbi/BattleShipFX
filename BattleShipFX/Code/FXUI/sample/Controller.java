package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Controller {
   // @FXML
    //public void OnclickLoadXML()
   // {
    //    System.out.println("asdasdsad");
   // }
    Stage stg;

    public void SetSage (Stage i_stg)
    {
        stg = i_stg;
    }

    public void OnActionTest(ActionEvent actionEvent) {


    }


/*    @FXML
    public void OnActionTest(ActionEvent actionEvent) {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource("Test2.fxml"));
        Parent root = Loader.load();
        Controller crt = Loader.getController();
        crt.SetSage(primaryStage);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        System.out.println("sadas");
    }*/
}
