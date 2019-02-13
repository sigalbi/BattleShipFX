package FxmlFirstScene;
import FxmlMainScene.MainController;
import LogicPackage.BasicGameManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

public class FirstController {

    private Task<Boolean> fileLoadTask;
    Stage m_Stage;
    boolean m_isGameLoaded;
    File m_file;

    private final static int SLEEP_PERIOD = 100;
    private final static int SLEEP_INTERVAL = 20;

    private BasicGameManager logic;
    @FXML
    private Button LoadXmlButton;
    @FXML
    private Button StartGameButton;
    @FXML
    private ProgressBar ProgressBarXml;
    @FXML
    private Label ErrorLabale;

    public void SetSage (Stage i_stg)
    {
        m_Stage = i_stg;
    }

    public void OnclickXmlLoadButton(ActionEvent actionEvent) {
        ProgressBarXml.setVisible(true);
        LoadXmlButton.setDisable(true);

        ProgressBarXml.progressProperty().unbind();
        ProgressBarXml.setProgress(0);

        logic = new BasicGameManager();
        FileChooser fileChooser = new FileChooser();

        //the window of the file chooser
        fileChooser.setTitle("Search for a XML file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML files", "*.xml"));

        File fileIn = fileChooser.showOpenDialog(m_Stage);
        m_file = fileIn;
        fileLoadTask = loadXMLFileTask(fileIn);

        ProgressBarXml.progressProperty().unbind();
        ProgressBarXml.progressProperty().bind(fileLoadTask.progressProperty());

        fileLoadTask.messageProperty().addListener((observable, oldValue, newValue) -> ErrorLabale.setText(newValue));

        new Thread(fileLoadTask).start();
        LoadXmlButton.setDisable(false);
    }

    private Task<Boolean> loadXMLFileTask(File fileIn) {
        //  throws UnmarshalException,ConfigException,FileNotFoundException,FileTypeException,XmlFileSerializerException, JAXBException,BoardSizeException
        return new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                try {
                    for (int i = 0; i < SLEEP_INTERVAL; i++) {
                        Thread.sleep(SLEEP_PERIOD);
                        updateMessage("Loading file... " + ((float) i / SLEEP_INTERVAL) * 100 + "%");
                        updateProgress(i + 1, SLEEP_INTERVAL);
                    }
                    //load the game acoording to the path we get from the user
                    logic.LoadGame(fileIn.getAbsolutePath());
                    m_isGameLoaded = true;
                    //make the 'start game' button visible
                    Platform.runLater(() -> StartGameButton.setDisable(false));
                }
                catch(Exception e)
                {
                    ErrorLabale.setVisible(true);
                    String s = e.getMessage();
                    Platform.runLater(() -> ErrorLabale.setText("Error : " + s ));

                    m_isGameLoaded = false;
                }
                finally
                {
                    Platform.runLater(() -> ProgressBarXml.setVisible(false));
                    if (m_isGameLoaded==true)
                    {
                        //Platform.runLater(() -> labelError.setText("File was loaded successfully. Now you can Play!"));
                        StartGameButton.setDisable(false);
                    }
                    else
                    {
                        StartGameButton.setDisable(true);
                        return false;
                    }
                }
                return true;
            }
        };
    }

    public void onStartButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource("/FxmlMainScene/MainScene.fxml"));
        Parent root = Loader.load();
        MainController crt = Loader.getController();
        crt.initializeGame(logic, root);
        Stage stage = new Stage();
        crt.SetSage(stage);
        crt.setFileLoaded(m_file);
        stage.setTitle("Battleships");
        stage.setScene(new Scene(root));
        stage.show();
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }
}
