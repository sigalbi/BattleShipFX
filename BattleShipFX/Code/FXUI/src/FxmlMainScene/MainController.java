package FxmlMainScene;
import javafx.animation.PauseTransition;
import LogicPackage.BasicGameManager;
import LogicPackage.DateSaver;
import LogicPackage.FrameSaver;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

import static java.lang.Thread.sleep;

public class MainController {

    private Stage m_Stage;
    private BasicGameManager m_Logic;
    private int m_PlayerNumber = 0 ;
    private Parent m_root;
    private DateSaver saver;
    File m_file;
    @FXML
    private Label PlayerNLabel;
    @FXML
    private Label PlayerScoreLabel;
    @FXML
    private Label HitsLabel;
    @FXML
    private Label MissesLabel;
    @FXML
    private Label AverageTimeLabel;
    @FXML
    private Label OpponentsScoreLabel;
    @FXML
    private Label BattleshipsLeftToOpponent;
    @FXML
    private Label TurnNLabel;
    @FXML
    private VBox BattleShipBoard;
    @FXML
    private VBox AttackBoard;
    @FXML
    private HBox MinesstackBox;
    @FXML
    private ImageView Animated;
    @FXML
    private Button PrevScene;
    @FXML
    private Button NextScene;
    @FXML
    private Button loadNewGame;
    @FXML
    private Button RestartGame;
    @FXML
    private TextArea PlayerShips;
    @FXML
    private TextArea OpponentShipLeft;
    @FXML
    private ImageView AnimatePic;


    int m_TurnNumber =1;

    public void initializeGame(BasicGameManager logic, Parent root)throws IOException {
        m_Logic = logic;
        m_root = root;
        saver = new DateSaver();
        initializeLabels();
    }

    public void SetSage (Stage i_stg)
    {
        m_Stage = i_stg;
    }

    private void initializeLabels() throws IOException {
        InitBattleShipBoard();
        InitAttackBoard();
        updateTurnNumber();
        updatePlayerNumber();
        updatePlayerScore();
        updateNumberOfHits();
        updateNumberOfMiss();
        updateAvgOfTurn();
        updateMyShipsNumber();
        updateNumberOfRavelShips();
        updateRavelPlayerScore();
        addMineToBoard();
        m_Logic.StartTurnOfPlayer(m_PlayerNumber);
        saver.NewFrame(m_Logic.GetPlayer(m_PlayerNumber),m_PlayerNumber,m_TurnNumber, m_Logic.AverageTimePlayerTurn(m_PlayerNumber) ,m_Logic.GetPlayer((m_PlayerNumber +1)%2).getM_PlayerScore(),PlayerShips.getText(),OpponentShipLeft.getText());
        m_TurnNumber++;
    }

    public void updateMyShipsNumber() {
        PlayerShips.setText(m_Logic.getPlayerhipDiteles(m_PlayerNumber));
    }

    private void addMineToBoard() {

        for(int i=0; i< m_Logic.GetPlayer(m_PlayerNumber).getM_NumberOfMinesLeft() ;i++)
        {
            ImageView mine = new ImageView("/ResourcesPic/mine.jpg");
            mine.setFitWidth(25);
            mine.setFitHeight(25);
            mine.setOnDragDetected((event) -> {
                WritableImage snapshot = mine.snapshot(new SnapshotParameters(), null);
                Dragboard db = mine.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();
                content.putString(1 + "");
                db.setContent(content);
                db.setDragView(snapshot, snapshot.getWidth() / 2, snapshot.getHeight() / 2);

                event.consume();
            });

            mine.setOnDragDone((event) -> {
                if (event.getTransferMode() == TransferMode.MOVE) {
                    MinesstackBox.getChildren().remove(mine);
                }
                event.consume();
            });

            MinesstackBox.getChildren().add(mine);
        }
    }

    private void updateTurnNumber() {
        TurnNLabel.setText("Turn.N: " +m_TurnNumber);
    }

    private void updatePlayerNumber() {
        PlayerNLabel.setText("Player N." + (m_PlayerNumber+1));
    }

    private void updatePlayerScore() {
        PlayerScoreLabel.setText("Player score: " + m_Logic.GetPlayer(m_PlayerNumber).getM_PlayerScore());
    }

    private void updateNumberOfHits() {
        HitsLabel.setText("Hits: " + m_Logic.GetPlayerHit(m_PlayerNumber));
    }

    private void updateNumberOfMiss() {
        MissesLabel.setText("Misses: " + m_Logic.GetPlayerMiss(m_PlayerNumber));
    }

    private void updateAvgOfTurn() {
        AverageTimeLabel.setText("Average move time: " + m_Logic.AverageTimePlayerTurn(m_PlayerNumber));
    }

    private void updateRavelPlayerScore() {
        OpponentsScoreLabel.setText("Opponent score: " + m_Logic.GetPlayer((m_PlayerNumber +1)%2).getM_PlayerScore());

    }

    private void updateNumberOfRavelShips() {
        OpponentShipLeft.setText(m_Logic.getPlayerhipDiteles((m_PlayerNumber +1)%2));
    }

    private void InitBattleShipBoard() throws IOException {
        int fontSize;
        int size = m_Logic.getGame().getBoardSize();
        char [][] CharsOfPlayerBord = m_Logic.GetPlayer(m_PlayerNumber).getM_charPlayerBoard();
        int buttonSize = ButtonSize();
        fontSize = buttonSize /4 ;
        for(int i=0 ;i<size; i++)
        {
            HBox newLineOfButtons = new HBox(1);
            for(int j=0; j<size; j++) {
                MyCell buttonOfShip = new MyCell();
                buttonOfShip.setX(i);
                buttonOfShip.setY(j);
                buttonOfShip.styleProperty().bind(Bindings.format("-fx-background-image: url(/ResourcesPic/blue.png) ; -fx-font: " + fontSize + " arial"));
                buttonOfShip.setMaxSize(buttonSize, buttonSize);
                buttonOfShip.setMinSize(buttonSize, buttonSize);
                buttonOfShip.setPrefSize(buttonSize, buttonSize);
                if(checkPossiblePlace(buttonOfShip.getY(),buttonOfShip.getX())) {
                buttonOfShip.setOnDragDropped((event) -> {
                    Dragboard db = event.getDragboard();
                    boolean success = false;
                    if (db.hasString()) {
                        buttonOfShip.styleProperty().bind(Bindings.format("-fx-background-image: url(/ResourcesPic/mine.jpg) ;-fx-background-position: center;-fx-background-size: stretch"));
                        m_Logic.addMine(m_PlayerNumber, buttonOfShip.getY(), buttonOfShip.getX());
                        m_PlayerNumber = (m_PlayerNumber + 1) % 2;
                        DeleteBoard();
                        try {
                            initializeLabels();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        success = true;
                    }
                    event.setDropCompleted(success);
                    event.consume();
                });
            }

                buttonOfShip.setOnDragOver((event) -> {
                    if (event.getDragboard().hasString()) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    }
                    event.consume();
                });
                if(CharsOfPlayerBord[i][j] == 'I')
                    buttonOfShip.styleProperty().bind(Bindings.format("-fx-background-image: url(/ResourcesPic/mine.jpg) ;-fx-background-position: center;-fx-background-size: stretch"));
                else if(CharsOfPlayerBord[i][j]!= ' ')
                {
                    buttonOfShip.setText(String.valueOf(CharsOfPlayerBord[i][j]));
                }
                newLineOfButtons.getChildren().add(buttonOfShip);

            }
            BattleShipBoard.getChildren().add(newLineOfButtons);

        }
    }

    private boolean checkPossiblePlace(int x, int y) {
        if(m_Logic.PossiblePlaceForMine(m_PlayerNumber,x,y))
            return true;
        return false;
    }

    private void InitAttackBoard()
    {
        int size = m_Logic.getGame().getBoardSize();
        int buttonSize = ButtonSize();
        char [][] CharsOfPlayerBord = m_Logic.GetPlayer(m_PlayerNumber).getM_AttackBoard();
        for(int i=0 ;i<size; i++)
        {
            HBox newLineOfButtons = new HBox(1);

            for(int j=0; j<size; j++) {
                MyCell buttonOfShip = new MyCell();
                buttonOfShip.setX(i);
                buttonOfShip.setY(j);
                if(CharsOfPlayerBord[i][j] != ' ')
                {
                    buttonOfShip.styleProperty().bind(Bindings.format("-fx-background-image: url(/ResourcesPic/red.png)"));
                    buttonOfShip.setDisable(true);
                }
                else
                    buttonOfShip.styleProperty().bind(Bindings.format("-fx-background-image: url(/ResourcesPic/blue.png)"));
                buttonOfShip.setMaxSize(buttonSize,buttonSize);
                buttonOfShip.setMinSize(buttonSize,buttonSize);
                buttonOfShip.setPrefSize(buttonSize,buttonSize);
                buttonOfShip.setOnMouseClicked(event -> {
                    try {
                        onPressButton(buttonOfShip);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                newLineOfButtons.getChildren().add(buttonOfShip);
            }
            AttackBoard.getChildren().add(newLineOfButtons);

        }
    }
    private int ButtonSize()
    {
        int returnSize;
        int size = m_Logic.getGame().getBoardSize();
        double d = BattleShipBoard.getPrefWidth();
        returnSize = (int)(d / size);
        return returnSize;
    }

    public void onPressButton(MyCell buttonClicked) throws IOException {
        boolean isHit;
        buttonClicked.styleProperty().bind(Bindings.format("-fx-background-image: url(/ResourcesPic/red.png)"));
        isHit = m_Logic.UpdateBoards(m_PlayerNumber,buttonClicked.getY(),buttonClicked.getX());
        if(!isHit)
        {

            m_PlayerNumber = (m_PlayerNumber +1) % 2;
            if(m_Logic.getHitMine())
            {
                AlertPromptDialog.show(m_Stage,"You hit a mine!","/ResourcesPic/mine.gif");

            }
            else{
                AlertPromptDialog.show(m_Stage,"You missed..","/ResourcesPic/miss.gif");

            }

        }
        else {

            AlertPromptDialog.show(m_Stage,"Direct hit!","/ResourcesPic/hit.gif");

        }
        if(m_Logic.CheckIfPlayerWon(m_PlayerNumber))
        {
            PrevScene.setVisible(true);
            NextScene.setVisible(true);
            loadNewGame.setVisible(true);
            RestartGame.setVisible(true);
            if(!saver.StillHavePrev())
            {
                PrevScene.setDisable(true);
            }
            if(!saver.StillHaveNext())
            {
                NextScene.setDisable(true);
            }
            AlertPromptDialog.show(m_Stage,"Victory!","/ResourcesPic/Victory.gif");
        }
        else {
            DeleteBoard();
            initializeLabels();
        }
    }

    public void DeleteBoard()
    {

        for(Node line :  BattleShipBoard.getChildren())
        {
            ((HBox)line).setVisible(false);
            ((HBox)line).getChildren().clear();
        }
        BattleShipBoard.getChildren().clear();
        for(Node line :  AttackBoard.getChildren())
        {
            ((HBox)line).setVisible(false);
            ((HBox)line).getChildren().clear();
        }
        MinesstackBox.getChildren().clear();
    }

    public void QuitGame()
    {
        PrevScene.setVisible(true);
        NextScene.setVisible(true);
        loadNewGame.setVisible(true);
        RestartGame.setVisible(true);
        if(!saver.StillHavePrev())
        {
            PrevScene.setDisable(true);
        }
        if(!saver.StillHaveNext())
        {
            NextScene.setDisable(true);
        }

        AlertPromptDialog.show(m_Stage,"You quit:(","/ResourcesPic/Quit.gif");


    }

    public void OnPrevButtonPress()
    {
        DeleteBoard();
        ShowFrame(saver.GetPrevFrame());
        if(!saver.StillHavePrev())
        {
            PrevScene.setDisable(true);
        }
        NextScene.setDisable(false);
    }
    public void OnNextButtonPress(){
        DeleteBoard();
        ShowFrame(saver.GetNextFrame());
        if(!saver.StillHaveNext())
        {
            NextScene.setDisable(true);

        }
        PrevScene.setDisable(false);
    }

    public void OnLoadNewGameButton(){

        m_Logic = new BasicGameManager();
        FileChooser fileChooser = new FileChooser();
        //the window of the file chooser
        fileChooser.setTitle("Search for a XML file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML files", "*.xml"));
        File fileIn = fileChooser.showOpenDialog(m_Stage);

        try {
            m_Logic.LoadGame(fileIn.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            StartNewGame();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();

        }
    }

    private void StartNewGame() throws IOException {
        loadNewGame.getScene().getWindow().hide();
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource("/FxmlMainScene/MainScene.fxml"));
        Parent root = Loader.load();
        MainController crt = Loader.getController();
        crt.initializeGame(m_Logic, root);
        Stage stage = new Stage();
        crt.SetSage(stage);
        crt.setFileLoaded(m_file);
        stage.setTitle("Battleships");
        stage.setScene(new Scene(root));
        stage.show();
    }
    public void OnRestartGameButton() throws Exception {
        m_Logic.LoadGame(m_file.getAbsolutePath());
        StartNewGame();
    }


    public void setFileLoaded(File fileLoaded) {
        m_file= fileLoaded;
    }

    private void ShowFrame(FrameSaver fm)
    {
        TurnNLabel.setText("Turn.N: " + fm.Getm_TurnNumber());
        PlayerNLabel.setText("Player N." +  (fm.Getm_PlayerNumber() + 1));
        PlayerScoreLabel.setText("Player score: " +  fm.Getm_m_PlayerScore());
        HitsLabel.setText("Hits: " +  fm.Getm_Hits());
        MissesLabel.setText("Misses: " +  fm.Getm_Miss());
        AverageTimeLabel.setText("Average move time: " +  fm.Getm_AverageMoveTime());
        OpponentsScoreLabel.setText("Opponent score: " +  fm.Getm_OpponentScore());
        PlayerShips.setText(fm.getPlayerhipDiteles());
        OpponentShipLeft.setText(fm.getOpponentShipLeft());
        AddMineForFrameWithoutFunction(fm);
        AddBattleShipBoardWithoutFunction(fm);
        AddAttackBoardWithoutFunction(fm);

    }
    private void AddMineForFrameWithoutFunction(FrameSaver fm)
    {
        for(int i=0; i< fm.Getm_NumberOfMinet();i++) {
            ImageView mine = new ImageView("/ResourcesPic/mine.jpg");
            mine.setFitWidth(25);
            mine.setFitHeight(25);
            MinesstackBox.getChildren().add(mine);
        }
    }
    private void AddBattleShipBoardWithoutFunction(FrameSaver fm)
    {
        int fontSize;
        int size = m_Logic.getGame().getBoardSize();
        char [][] CharsOfPlayerBord = fm.Getm_BattleshipsBoard();
        int buttonSize = ButtonSize();
        fontSize = buttonSize /4 ;
        for(int i=0 ;i<size; i++)
        {
            HBox newLineOfButtons = new HBox(1);
            for(int j=0; j<size; j++) {
                MyCell buttonOfShip = new MyCell();
                buttonOfShip.setX(i);
                buttonOfShip.setY(j);
                buttonOfShip.styleProperty().bind(Bindings.format("-fx-background-image: url(/ResourcesPic/blue.png) ; -fx-font: " + fontSize + " arial"));
                buttonOfShip.setMaxSize(buttonSize, buttonSize);
                buttonOfShip.setMinSize(buttonSize, buttonSize);
                buttonOfShip.setPrefSize(buttonSize, buttonSize);
                if(CharsOfPlayerBord[i][j] == 'I')
                    buttonOfShip.styleProperty().bind(Bindings.format("-fx-background-image: url(/ResourcesPic/mine.jpg) ;-fx-background-position: center;-fx-background-size: stretch"));
                else if(CharsOfPlayerBord[i][j]!= ' ')
                {
                    buttonOfShip.setText(String.valueOf(CharsOfPlayerBord[i][j]));
                }
                newLineOfButtons.getChildren().add(buttonOfShip);

            }
            BattleShipBoard.getChildren().add(newLineOfButtons);
        }
    }

    private void AddAttackBoardWithoutFunction(FrameSaver fm)
    {
        int size = m_Logic.getGame().getBoardSize();
        int buttonSize = ButtonSize();
        char [][] CharsOfPlayerBord = fm.Getm_AttackBoard();
        for(int i=0 ;i<size; i++)
        {
            HBox newLineOfButtons = new HBox(1);

            for(int j=0; j<size; j++) {
                MyCell buttonOfShip = new MyCell();
                buttonOfShip.setX(i);
                buttonOfShip.setY(j);
                if(CharsOfPlayerBord[i][j] != ' ')
                {
                    buttonOfShip.styleProperty().bind(Bindings.format("-fx-background-image: url(/ResourcesPic/red.png)"));
                    buttonOfShip.setDisable(true);
                }
                else
                    buttonOfShip.styleProperty().bind(Bindings.format("-fx-background-image: url(/ResourcesPic/blue.png)"));
                buttonOfShip.setMaxSize(buttonSize,buttonSize);
                buttonOfShip.setMinSize(buttonSize,buttonSize);
                buttonOfShip.setPrefSize(buttonSize,buttonSize);
                newLineOfButtons.getChildren().add(buttonOfShip);
            }
            AttackBoard.getChildren().add(newLineOfButtons);

        }
    }
}
