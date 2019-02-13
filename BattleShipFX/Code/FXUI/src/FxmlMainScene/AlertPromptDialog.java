package FxmlMainScene;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/*
    Source: http://tech.chitgoks.com/2013/07/08/how-to-create-confirm-dialog-window-in-java-fx-2/
 */
public class AlertPromptDialog extends Stage {
    private static final int WIDTH_DEFAULT = 400;

    private static Label label;
    private static AlertPromptDialog popup;
    private static int result;
    private static Image img;
    private static String url;
    public static final int NO = 0;
    public static final int YES = 1;
    public static BorderPane borderPane;

    private AlertPromptDialog() {
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.TRANSPARENT);

        label = new Label();
        label.setWrapText(true);
        label.setGraphicTextGap(40);

        label.styleProperty().bind(Bindings.format("-fx-font-size: 15 ; -fx-font-weight: bold; -fx-background-color: linear-gradient(#61a2b1, #2A5058) "));
        //label.setStyle("-fx-font-style: oblique");

        label.setAlignment(Pos.CENTER);

        Button exitButton = new Button("Continue");
        exitButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                AlertPromptDialog.this.close();
            }
        });

        borderPane = new BorderPane();
        BorderPane dropShadowPane = new BorderPane();
        dropShadowPane.setTop(label);


        HBox hbox = new HBox();
        hbox.setSpacing(15);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().add(exitButton);

        dropShadowPane.setBottom(hbox);
        borderPane.setCenter(dropShadowPane);

        Scene scene = new Scene(borderPane);
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);
    }

    public static void show(Stage owner, String msg,String url) {
        if (popup == null) {
            popup = new AlertPromptDialog();
        }
        label.setText(msg);
        borderPane.styleProperty().bind(Bindings.format("-fx-background-image: url(" + url +");-fx-background-position: center;-fx-background-size: stretch"));

        // calculate width of string
        final Text text = new Text(msg);
        text.snapshot(null, null);
        // + 40 because there is padding 10 left and right and there are 2 containers now
        // + 20 because there is text gap between icon and messge
        int width = (int) text.getLayoutBounds().getWidth() + 60;

        int height = 120;

        popup.setWidth(width);
        popup.setHeight(height);

        // make sure this stage is centered on top of its owner
        popup.setX(owner.getX() + (owner.getWidth() / 2 - popup.getWidth() / 2));
        popup.setY(owner.getY() + (owner.getHeight() / 2 - popup.getHeight() / 2));

        popup.showAndWait();
    }

}