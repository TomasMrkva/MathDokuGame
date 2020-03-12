import java.util.ArrayList;
import java.util.Random;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class GameGenerator {
	
	static GridConstructor grid;

	public static void createGrid(int N) {
		GridConstructor grid = new GridConstructor(N, MathDoku.getWidth());
		Group gameGrid = grid.getGrid();
		grid.makeBorder(MathDoku.width, N, 2, Color.TOMATO);
		Gui gui = new Gui(grid);
		
		StackPane pane = new StackPane();
		pane.getChildren().add(gameGrid);
		pane.setPickOnBounds(false);
		NumberBinding maxScale = Bindings.min(pane.widthProperty().divide((N * 0.83) * 100),
				pane.heightProperty().divide((N * 0.83) * 100));
		pane.scaleXProperty().bind(maxScale);
		pane.scaleYProperty().bind(maxScale);
		
		((BorderPane) MathDoku.pRoot.getChildren().get(0)).setTop(gui.loadGame());
		((BorderPane) MathDoku.pRoot.getChildren().get(0)).setBottom(gui.bottomSide());
		((BorderPane) MathDoku.pRoot.getChildren().get(0)).setLeft(gui.menu());
		((BorderPane) MathDoku.pRoot.getChildren().get(0)).setRight(gui.numbers(N));
		((BorderPane) MathDoku.pRoot.getChildren().get(0)).setCenter(pane);
		
		if(N > 5) {
			MathDoku.getStage().setMinHeight(MathDoku.width * N + 120);
			MathDoku.getStage().setMinWidth(MathDoku.width * N + 140);				
		} else {
			MathDoku.getStage().setMinHeight(MathDoku.width * 6 + 120);
			MathDoku.getStage().setMinWidth(MathDoku.width * 6 + 140);				
		}
		GameGenerator.grid = grid;
		MathDoku.getStage().centerOnScreen();
		grid.requestFocus();
		Gui.setGrid(grid);
		
//		boolean solved = false;
//		while(solved != true) {
//			for(MyRectangle r : grid.getCells()) {
//				for(MyRectangle cell : grid.getCells()) {
//					if(cell.getCellId()%5 == 0)
//					cell.setSolution(rand.nextInt(6)+1);
//				}
//			}
//			solved = solve(grid.getCells(), grid.getCells().size());
//		}
		solve(grid.getCells(), grid.getCells().size());
		Random rand = new Random();
		for(int i=0; i < N*N*N; i++) {
			int first = rand.nextInt(N);
			int second = rand.nextInt(N);
			int choose = rand.nextInt(3);
			if(choose == 0) {
				if(first != second) {
					shuffleCols(first, second);
				} else {
					i--;
				}				
			} else if(choose == 1){
				if(first != second) {
					shuffleRows(first, second);
				} else {
					i--;
				}
			} else {
				i++;
			}
		}
		
		for(MyRectangle cell : grid.getCells()) {
			grid.displayTest(cell);
		}
	}
	
	public static void shuffleRows(int r1, int r2) {
		String row1 = String.valueOf(r1);
		String row2 = String.valueOf(r2);
		
		ArrayList<MyRectangle> row1Cells = new ArrayList<MyRectangle>();
		ArrayList<MyRectangle> row2Cells = new ArrayList<MyRectangle>();
		ArrayList<Integer> temp = new ArrayList<Integer>();
		
		for(MyRectangle r : grid.getCells()) {
			if(r.getRow().equals(row1)) {
				row1Cells.add(r);
			}
			if(r.getRow().equals(row2)) {
				row2Cells.add(r);
			}
		}
		for(int i=0; i<row1Cells.size(); i++) {
			temp.add(row1Cells.get(i).getSolution());
			row1Cells.get(i).setSolution(row2Cells.get(i).getSolution());
		}
		for(int i=0; i<row2Cells.size(); i++) {
			row2Cells.get(i).setSolution(temp.get(i));
		}
	}
	
	public static void shuffleCols(int c1, int c2) {
		String col1 = String.valueOf(c1);
		String col2 = String.valueOf(c2);
		
		ArrayList<MyRectangle> col1Cells = new ArrayList<MyRectangle>();
		ArrayList<MyRectangle> col2Cells = new ArrayList<MyRectangle>();
		ArrayList<Integer> temp = new ArrayList<Integer>();
		
		for(MyRectangle r : grid.getCells()) {
			if(r.getCol().equals(col1)) {
				col1Cells.add(r);
			}
			if(r.getCol().equals(col2)) {
				col2Cells.add(r);
			}
		}
		for(int i=0; i<col1Cells.size(); i++) {
			temp.add(col1Cells.get(i).getSolution());
			col1Cells.get(i).setSolution(col2Cells.get(i).getSolution());
		}
		for(int i=0; i<col2Cells.size(); i++) {
			col2Cells.get(i).setSolution(temp.get(i));
		}
	}
	
	public static boolean solve(ArrayList<MyRectangle> cells, int noCells) {
		int position = 0;
		double limit = Math.sqrt(noCells);
		boolean backtrack = false;
		while(position != cells.size()) {
			if(position < 0) {
				return false;
			}
			MyRectangle curr = cells.get(position);
//			System.out.println(position);
			if(curr.getSolution() == (int) limit) {
				backtrack = true;
			}
			else 
				curr.setSolution(curr.getSolution()+1);
			
			while(curr.getSolution() <= limit) {
				if(backtrack == true) break;
				else if( GameEngine.isRowCorrect(cells, curr) && GameEngine.isColCorrect(cells, curr) ) {
					position++;
					backtrack = false;
					break;
				}
				if(curr.getSolution() == limit) {
					backtrack = true;
					break;
				}
				else
					curr.setSolution(curr.getSolution()+1);
			}
			if(backtrack) {
				curr.setSolution(0);
				position--;	
			}
			backtrack = false;
		}
		return true;
	}
	
}
