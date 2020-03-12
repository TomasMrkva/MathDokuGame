import java.util.stream.Stream;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class WinAnimation {

//	static Text selectText;
	public static boolean isWin;

	public static void playAnimation(Pane pane) {
		Gui.getGrid().unregiSterKeys();
		PauseTransition pauseMain = new PauseTransition(Duration.seconds(0.5));
		pauseMain.setOnFinished(mainEvent -> {
	        Circle small = new Circle(70);
	        Circle big = new Circle(150);
	        
	        Circle cir1 = new Circle(40);
	        cir1.setFill(Color.rgb(252, 118, 106));
	        
	        Circle cir2  = new Circle(40);
	        cir2.setFill(Color.TRANSPARENT);
	               
	        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
			pause.setOnFinished(fillEvent -> {
				cir2.setFill(Color.rgb(91, 132, 177));
			}); 
			pause.play();
	        
	        PathTransition path1 = new PathTransition(Duration.seconds(1.5), small);
	        path1.setAutoReverse(true);
	        path1.setCycleCount(2);
	        path1.setNode(cir1);
	        
	        TranslateTransition transitionSmall = new TranslateTransition();
	        transitionSmall.setDuration(Duration.seconds(0.75));
	        transitionSmall.setToX(0);
	        transitionSmall.setToY(0);
	        transitionSmall.setNode(cir1);
	        
	        PathTransition path2 = new PathTransition(Duration.seconds(1.5), big);
	        path2.setCycleCount(2);
	        path2.setNode(cir2);
	        
	        TranslateTransition transitionBig = new TranslateTransition();
	        transitionBig.setDuration(Duration.seconds(0.75));
	        transitionBig.setToX(0);
	        transitionBig.setToY(0);
	        transitionBig.setNode(cir2);
	        
		    SequentialTransition sequence1 = new SequentialTransition(cir1, path1, transitionSmall);
		    sequence1.play();
	        
		    SequentialTransition sequence2 = new SequentialTransition(cir2, path2, transitionBig);
		    sequence2.play();
	        
		    ScaleTransition scale  = new ScaleTransition(Duration.seconds(4), cir2);
		    scale.setDelay(Duration.seconds(3.75));
		    scale.setCycleCount(3);
		    scale.setAutoReverse(true);
//		    scale.setCycleCount(1);
		    scale.setToX(50);
		    scale.setToY(50);
		    scale.play();
		    
		    FillTransition fill = new FillTransition(Duration.seconds(3), cir2, Color.rgb(91, 132, 177), Color.rgb(252, 118, 106));
		    fill.setDelay(Duration.seconds(4));
		    fill.setCycleCount(6);
		    fill.setAutoReverse(true);
		    fill.play();
		    
		    Label text = new Label("Win");
		    text.setFont(new Font(45));
		    text.setStyle("-fx-text-fill: transparent");
		    
		    PauseTransition pauseText = new PauseTransition(Duration.seconds(4));
		    pauseText.setOnFinished(e -> text.setStyle("-fx-text-fill: black"));
		    pauseText.play();
		    
	        FadeTransition fade = new FadeTransition();
	        fade.setDuration(Duration.seconds(3));
	        fade.setAutoReverse(true);
	        fade.setCycleCount(Animation.INDEFINITE);
	        fade.setNode(text);
	        fade.setFromValue(1.0);
	        fade.setToValue(0.3);
		    fade.play();
		    
		    
	        RotateTransition rotate = new RotateTransition();
	        rotate.setAxis(Rotate.Z_AXIS);
	        rotate.setByAngle(360);  
	        rotate.setCycleCount(5);  
	        rotate.setDuration(Duration.seconds(2));  
//	        rotate.setAutoReverse(true);  
	        rotate.setNode(text);
	        rotate.play();
	        
	        VBox vbox = new VBox(5);
//			abh.setStyle("-fx-border-color: blue");
			vbox.setAlignment(Pos.CENTER);
	        vbox.getChildren().add(text);
	        
		    pane.getChildren().addAll(cir1, cir2, vbox);
		    
		});
		pauseMain.play();
		
		ObjectProperty<Label> selectedText = new SimpleObjectProperty<>();
		selectedText.addListener((obs, oldSelectedText, newSelectedText) -> {
		    if (oldSelectedText != null) {
		        oldSelectedText.setStyle("");
		    }
		    if (newSelectedText != null) {
		        newSelectedText.setStyle("-fx-background-color: lightgrey");
		    }
		});
		PauseTransition pauseButton = new PauseTransition(Duration.seconds(13.5));
		pauseButton.setOnFinished(e -> {
			Label endGame = new Label(" Exit ");
			endGame.setFont(new Font(25));
			Label tryAgain = new Label(" Try Again ");
			tryAgain.setFont(new Font(25));
			Label loadGame = new Label(" Load a New Game ");
			loadGame.setFont(new Font(25));
			Stream.of(loadGame, tryAgain, endGame).forEach(t -> t.setOnMouseEntered(event -> selectedText.set(t)));
			selectedText.set(tryAgain);
			
			endGame.setOnMouseClicked(event -> {
				MathDoku.getStage().close();
				System.exit(0);
			});
			tryAgain.setOnMouseClicked(event -> {
//				System.out.println("hi");
				for(int i=pane.getChildren().size()-1; i > 0 ; i--) {
					pane.getChildren().remove(i);
				}
				GridConstructor grid = Gui.getGrid();
				grid.clearBoard();
				StackOperations.stackUndo.clear();
				StackOperations.stackRedo.clear();
				Gui.redo.setDisable(true);
				Gui.undo.setDisable(true);
				if(GameEngine.isSolvabale()) {
					Gui.hint.setDisable(false);
					Gui.solve.setDisable(false);
				}
				grid.keyTyped();
				grid.requestFocus();
			});
			loadGame.setOnMouseClicked(new FileLoaderHandler());
			
			VBox vbox = (VBox) pane.getChildren().get(pane.getChildren().size()-1);
			vbox.getChildren().addAll(loadGame, tryAgain, endGame);
		});
		pauseButton.play();
	}
}
