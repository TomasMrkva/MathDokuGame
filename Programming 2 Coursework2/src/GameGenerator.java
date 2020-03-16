import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class GameGenerator {
	
	static GridConstructor grid;
	static int difficulty;
	static ArrayList<MyRectangle> cells = new ArrayList<MyRectangle>();
	static int N;
	static Random rand = new Random();


	public static void createGrid(int N, int difficulty) {
		
		if(MathDoku.pRoot.getChildren().size() > 1) {
			for(int i=MathDoku.pRoot.getChildren().size()-1; i>=1 ;i--) {
				MathDoku.pRoot.getChildren().remove(i);
			}
		}
		
		GridConstructor grid = new GridConstructor(N, MathDoku.getWidth());
		GameGenerator.cells  = grid.getCells();
		GameGenerator.N = N;
		GameGenerator.difficulty = difficulty;
		
		Group gameGrid = grid.getGrid();
		GameGenerator.grid = grid;
		
		solve(cells.size());
		GameGenerator.fillGrid();

		grid.addCages(createCages());
		grid.makeLabels();
		
		grid.makeBorder(MathDoku.width, N, 2, Color.TOMATO);
		Gui gui = new Gui(GameGenerator.grid);
		
		StackPane pane = new StackPane();
		pane.getChildren().add(gameGrid);
		pane.setPickOnBounds(false);
		NumberBinding maxScale = Bindings.min(pane.widthProperty().divide((N * 0.83) * 100),
				pane.heightProperty().divide((N * 0.83) * 100));
		pane.scaleXProperty().bind(maxScale);
		pane.scaleYProperty().bind(maxScale);
//		pane.setStyle("-fx-border-color: blue");
		
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
		
		MathDoku.getStage().centerOnScreen();
		grid.requestFocus();
		Gui.setGrid(GameGenerator.grid);
	}
	
	public static void fillGrid() {
//		for(MyRectangle cell : grid.getCells()) {
//			grid.displayTest(cell);
////			getNeighBours(cell);
//		}
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
		
		
	}
	
	public static void shuffleRows(int r1, int r2) {
		String row1 = String.valueOf(r1);
		String row2 = String.valueOf(r2);
		
		ArrayList<MyRectangle> row1Cells = new ArrayList<MyRectangle>();
		ArrayList<MyRectangle> row2Cells = new ArrayList<MyRectangle>();
		ArrayList<Integer> temp = new ArrayList<Integer>();
		
		for(MyRectangle r : cells) {
			if(r.getRow().equals(row1)) {
				row1Cells.add(r);
			}
			if(r.getRow().equals(row2)) {
				row2Cells.add(r);
			}
			if(r.getSolution() == 0) {
				System.err.println("here");
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
		
		for(MyRectangle r : cells) {
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
	
	public static ArrayList<Cage> createCages() {
		ArrayList<MyRectangle> cageCells = new ArrayList<MyRectangle>();
		ArrayList<MyRectangle> neighbours = new ArrayList<MyRectangle>();
		ArrayList<Cage> cages = new ArrayList<Cage>();
		
		for(int i=0; i < cells.size(); i++) {
			MyRectangle current = cells.get(i);
			if(current.getSolution() == 0) System.err.println("errrorrr420");
			if(!current.isOccupied()) {
				current.setOccupied(true);
				cageCells.add(current);
				neighbours = getNeighBours(current);
				int limit = rand.nextInt(N/2)+2;
//				System.out.println(limit);
				for(int j = 0; j < limit; j++) {
					int index = rand.nextInt(neighbours.size());
					if(!neighbours.get(index).isOccupied()){
						cageCells.add(neighbours.get(index));
						neighbours.get(index).setOccupied(true);						
						neighbours = getNeighBours(cageCells.get(cageCells.size()-1));
					}
				}
				neighbours.clear();
				MyRectangle[] arr = cageCells.toArray(new MyRectangle[cageCells.size()]);
				Arrays.sort(arr);
//				int result=0;
//				for (MyRectangle cell : cageCells) {
//					result = result + cell.getSolution();
//				}
//				String resultString = String.valueOf(result);
				if(cageCells.size() == 1) {
					String result = String.valueOf(cageCells.get(0).getSolution());
					cages.add(new Cage(result, arr));
				} else {
					int decision = rand.nextInt(7);
					String result = test(decision, cageCells);
					cages.add(new Cage(result, arr));
				}
				cageCells.clear();
			}
		}
		return cages;
	}
	
//	public static String createOps(ArrayList<MyRectangle> cells) {
//		int decision = rand.nextInt(4);
//		return getResult(decision, cells);
//	}
	
	
	public static String test(int decision, ArrayList<MyRectangle> cells) {
		String result = null;
		int total;
		
		if(decision == 0) {
			total = 0;
			for(MyRectangle cell : cells) {
				total = total + cell.getSolution();
			}
			result = String.valueOf(total) + "+";
			return result;
		} else if (decision == 1) {
			total = 0;
			Integer[] valuesSub = new Integer[cells.size()];
			for (int i = 0; i < cells.size(); i++) {
				valuesSub[i] = cells.get(i).getSolution();
			}
			Arrays.sort(valuesSub, Collections.reverseOrder());
			int amountSub = valuesSub[0];
			
			for (int i = 1; i < valuesSub.length; i++) {
				amountSub = amountSub - valuesSub[i];
			}
			if(amountSub < 1) {
				return test(0, cells);
			}
			else {
				result = String.valueOf(amountSub) + "-";	
				return result;
			}
		} else if (decision % 2 == 0) {
			total = 1;
			for (MyRectangle cell : cells) {
				total = total * Integer.valueOf(cell.getSolution());
			}
			result = String.valueOf(total) + "x";
			return result;		
		} else {
			Double[] valuesDiv = new Double[cells.size()];
			for (int i = 0; i < cells.size(); i++) {
				valuesDiv[i] = Double.valueOf(cells.get(i).getSolution());
			}
			Arrays.sort(valuesDiv, Collections.reverseOrder());
			double amountDiv = valuesDiv[0];

			for (int i = 1; i < valuesDiv.length; i++) {
				amountDiv = amountDiv / valuesDiv[i];
			}
			if(amountDiv % 1 == 0) {
				int convertedDouble = (int) amountDiv;
				result = String.valueOf(convertedDouble) + "÷";
				return result;
			} else {
				return test(4, cells);
			}
		}
	}
	
	public static ArrayList<MyRectangle> getNeighBours(MyRectangle cell){
		ArrayList<MyRectangle> neighbours = new ArrayList<MyRectangle>();
		
		int cellRow = Integer.valueOf(cell.getRow());
		int cellCol = Integer.valueOf(cell.getCol());
		int cellPos = cell.getCellId();
		
		if(cellRow == 0) {
			neighbours.add(cells.get(cellPos+N));

			if(cellCol == 0) {
				neighbours.add(cells.get(cellPos+1));
//				System.out.println("CellID 0:0 : " + cell.getCellId());
			} else if(cellCol == N-1) {
				neighbours.add(cells.get(cellPos-1));
//				System.out.println("CellID 0:N-1 : " + cell.getCellId());
			} else {
				neighbours.add(cells.get(cellPos+1));
				neighbours.add(cells.get(cellPos-1));
//				System.out.println("CellID 0:* : " + cell.getCellId());
			}
		} else if(cellRow == N-1) {
			neighbours.add(cells.get(cellPos-N));
			if(cellCol == 0) {
				neighbours.add(cells.get(cellPos+1));
//				System.out.println("CellID N-1:0 : " + cell.getCellId());
			} else if(cellCol == N-1) {
				neighbours.add(cells.get(cellPos-1));
//				System.out.println("CellID N-1:N-1 : " + cell.getCellId());
			} else {
				neighbours.add(cells.get(cellPos+1));
				neighbours.add(cells.get(cellPos-1));
//				System.out.println("CellID N-1:* : " + cell.getCellId());
			}
		} else if(cellCol == 0) {
			neighbours.add(cells.get(cellPos+1));
			neighbours.add(cells.get(cellPos+N));
			neighbours.add(cells.get(cellPos-N));
//			System.out.println("CellID *:0 : " + cell.getCellId());
		} else if(cellCol == N-1) {
			neighbours.add(cells.get(cellPos-1));
			neighbours.add(cells.get(cellPos+N));
			neighbours.add(cells.get(cellPos-N));
//			System.out.println("CellID *:N-1 : " + cell.getCellId());
		} else {
			neighbours.add(cells.get(cellPos-N));
			neighbours.add(cells.get(cellPos+N));
			neighbours.add(cells.get(cellPos+1));
			neighbours.add(cells.get(cellPos-1));
//			System.out.println("CellID *:* : " + cell.getCellId());
		}
//		System.out.println("Size " + neighbours.size());
//		System.out.println("POSITION: " + cell.getCellId() + " VALUE: " + cell.getSolution());
//		for(MyRectangle r : neighbours) {
//			System.out.println("\tCol: " + r.getCol() + " Row: " + r.getRow() + " Pos: " + r.getCellId() 
//			+ " Value: " + r.getSolution());
//		}
//		System.out.println("************************************************");
		return neighbours;
	}
	
	public static boolean solve(int noCells) {
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
		for(MyRectangle cell : cells) {
			if (cell.getSolution() == 0) {
				System.err.println("errrrrooooorr");
			}
		}
		return true;
	}
	
}
