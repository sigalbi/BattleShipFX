import FxmlFirstScene.FirstController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource("/FxmlFirstScene/FirstScene.fxml"));
        Parent root = Loader.load();
        FirstController crt = Loader.getController();
        crt.SetSage(primaryStage);
        primaryStage.setTitle("BattleShip");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
