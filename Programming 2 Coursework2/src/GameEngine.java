import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javafx.scene.paint.Color;

public class GameEngine {
	
	public static void checkRow(ArrayList<MyRectangle> cells, MyRectangle current) {
		String row = current.getRow();
		ArrayList<MyRectangle> rowCells = new ArrayList<MyRectangle>();
		Set<String> set = new HashSet<String>();
		boolean hasDuplicates = false;

		for (MyRectangle cell : cells) {
			if (cell.getRow().equals(row)) {
				if (cell.getValue() != null) {
					if (set.add(cell.getValue()) == false) {
						hasDuplicates = true;
					}
				}
				rowCells.add(cell);
			}
		}
		if (!hasDuplicates)
			unHighlight(rowCells, "row");
	}

	public static void checkCol(ArrayList<MyRectangle> cells, MyRectangle current) {
		String col = current.getCol();
		ArrayList<MyRectangle> columnCells = new ArrayList<MyRectangle>();
		Set<String> set = new HashSet<String>();
		boolean hasDuplicates = false;

		for (MyRectangle cell : cells) {
			if (cell.getCol().equals(col)) {
				if (cell.getValue() != null) {
					if (set.add(cell.getValue()) == false) {
						hasDuplicates = true;
					}
				}
				columnCells.add(cell);
			}
		}
		if (!hasDuplicates)
			unHighlight(columnCells, "col");
	}

	public static void checkCage(MyRectangle current) {
		if (!current.getCage().isFull()) {
			unHighlight(current.getCage().getCells(),"cage");
		}
		else if(GameEngine.checkAllCages(false, current.getCage()) == true) {
			unHighlight(current.getCage().getCells(),"cage");
		}
	}
	
	public static void colorRows(MyRectangle[][] matrix, ArrayList<MyRectangle> cells) {
		for (int row = 0; row < matrix.length; row++) {
			Set<String> set = new HashSet<String>();
			for (int col = 0; col < matrix.length; col++) {
				String num = matrix[row][col].getValue();
				if (set.contains(num)) {
					for (int i = 0; i < matrix.length; i++) {
						MyRectangle cell = matrix[row][i];
						cell.setFill(Color.rgb(255, 0, 0, 0.1));
						cell.setRowRed(true);
					}
					break;
				}
				if (num != null) {
					set.add(num);
				}
			}
			set = null;
		}
	}
	
	public static void colorCols(MyRectangle[][] matrix, ArrayList<MyRectangle> cells) {
		for (int col = 0; col < matrix[0].length; col++) {
			Set<String> set = new HashSet<String>();
			for (int row = 0; row < matrix.length; row++) {
				String num = matrix[row][col].getValue();
				if (set.contains(num)) {
					for (int i = 0; i < matrix.length; i++) {
						MyRectangle cell = matrix[i][col];
						cell.setFill(Color.rgb(255, 0, 0, 0.1));
						cell.setColRed(true);
					}
					break;
				}
				if (num != null) {
					set.add(num);
				}
			}
			set = null;
		}
	}

	public static void colorCages(ArrayList<MyRectangle> cells, ArrayList<Cage> cages) {
		boolean notFullCage = false;
		ArrayList<Cage> fullCages = new ArrayList<Cage>();
		
		for (Cage cage : cages) {
			for (MyRectangle cell : cage.getCells()) {
				if (cell.getValue() == null) {
					notFullCage = true;
				}
			}
			if (notFullCage == false) {
				fullCages.add(cage);
			}
			notFullCage = false;
		}

		for (Cage cage : fullCages) {
			if (GameEngine.checkAllCages(false, cage) == false) {
				for (MyRectangle cell : cage.getCells()) {
					cell.setFill(Color.rgb(255, 0, 0, 0.4));
					cell.setCageRed(true);
				}
			}
		}

	}

	public static void unHighlight(ArrayList<MyRectangle> cells, String value) {
		for (MyRectangle cell : cells) {
			if (value.equals("row")) {
				cell.setRowRed(false);
				if (!cell.isColRed() && !cell.isCageRed()) {
					cell.setFill(Color.TRANSPARENT);
				}
			}
			else if (value.equals("col")) {
				cell.setColRed(false);
				if (!cell.isRowRed() && !cell.isCageRed()) {
					cell.setFill(Color.TRANSPARENT);
				}
			}
			else if(value.equals("cage")) {
				cell.setCageRed(false);
				if(!cell.isRowRed() && !cell.isColRed()) {
					cell.setFill(Color.TRANSPARENT);
				}
			}
		}
	}

	public static boolean isFinished(ArrayList<MyRectangle> cells) {
		for (MyRectangle r : cells) {
			if (r.getValue() == null) {
				Gui.setText("Grid has not been completed!");
				return false;
			}
		}
		Gui.setText("Grid is full but not correct!");
		return true;
	}

	public static boolean checkAllRows(MyRectangle[][] matrix) {
		for (int row = 0; row < matrix.length; row++) {
			Set<String> set = new HashSet<String>();
			for (int col = 0; col < matrix.length; col++) {
				String num = matrix[row][col].getValue();
				if (set.contains(num)) {
//					MathDoku.setText("Wrong");
//					System.out.println("wrong value is: " + num + " r: " + matrix[row][col].getRow() + " c: "
//							+ matrix[row][col].getCol());
					set = null;
					return false;
				}
				set.add(num);
			}
			set = null;
		}
		return true;
	}

	public static boolean checkAllCols(MyRectangle[][] matrix) {
		for (int col = 0; col < matrix[0].length; col++) {
			Set<String> set = new HashSet<String>();
			for (int row = 0; row < matrix.length; row++) {
				String num = matrix[row][col].getValue();
				if (set.contains(num)) {
//					MathDoku.setText("Wrong");
//					System.out.println("wrong value is: " + num + " r: " + matrix[row][col].getRow() + " c: "
//							+ matrix[row][col].getCol());
					set = null;
					return false;
				}
				set.add(num);
			}
			set = null;
		}
		return true;
	}

	public static boolean checkAllCages(boolean solutionsMode, Cage... cages) {
		int total;
		for (Cage cage : cages) {
			switch (cage.getOPSymbol()) {
			case '+':
				total = 0;
				for (MyRectangle cell : cage.getCells()) {
					if(solutionsMode)
						total = total + cell.getSolution();
					else
						total = total + Integer.valueOf(cell.getValue());
				}
				if (total != cage.getResult())
					return false;
				break;

			case '-':
				Integer[] valuesSub = new Integer[cage.getCells().size()];
				for (int i = 0; i < cage.getCells().size(); i++) {
					if (solutionsMode) 
						valuesSub[i] = cage.getCells().get(i).getSolution();
					else 
						valuesSub[i] = Integer.valueOf(cage.getCells().get(i).getValue());
				}
				Arrays.sort(valuesSub, Collections.reverseOrder());
				int amountSub = valuesSub[0];

				for (int i = 1; i < valuesSub.length; i++) {
					amountSub = amountSub - valuesSub[i];
				}
				if (amountSub != cage.getResult())
					return false;
				break;

			case 'x':
				total = 1;
				for (MyRectangle cell : cage.getCells()) {
					if(solutionsMode)
						total = total * cell.getSolution();
					else
						total = total * Integer.valueOf(cell.getValue());
				}
				if (total != cage.getResult())
					return false;
				break;

			case '÷':
				Double[] valuesDiv = new Double[cage.getCells().size()];
				for (int i = 0; i < cage.getCells().size(); i++) {
					if(solutionsMode)
						valuesDiv[i] = Double.valueOf(cage.getCells().get(i).getSolution());
					else
						valuesDiv[i] = Double.valueOf(cage.getCells().get(i).getValue());
				}
				Arrays.sort(valuesDiv, Collections.reverseOrder());
				double amountDiv = valuesDiv[0];

				for (int i = 1; i < valuesDiv.length; i++) {
					amountDiv = amountDiv / valuesDiv[i];
				}
				if (amountDiv != cage.getResult() || amountDiv % 1 != 0)
					return false;
				break;
				
			case ' ':
				String val = cage.getCells().get(0).getValue();
				if(Integer.valueOf(val) != cage.getResult()) {
					return false;
				}
				break;

			default:
				throw new IllegalArgumentException("Unexpected value: " + cage.getOPSymbol());
			}
		}
		return true;
	}
	
	public static boolean isRowCorrect(ArrayList<MyRectangle> cells, MyRectangle current) {
		String row = current.getRow();
		Set<Integer> set = new HashSet<Integer>();
		
		for (MyRectangle cell : cells) {
			if (cell.getRow().equals(row)) {
				if (cell.getSolution() != 0) {
					if (set.add(cell.getSolution()) == false) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public static boolean isColCorrect(ArrayList<MyRectangle> cells, MyRectangle current) {
		String col = current.getCol();
		Set<Integer> set = new HashSet<Integer>();

		for (MyRectangle cell : cells) {
			if (cell.getCol().equals(col)) {
				if (cell.getSolution() != 0) {
					if (set.add(cell.getSolution()) == false) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public static void solve(ArrayList<MyRectangle> cells, int noCells) {
		int position = 0;
		double limit = Math.sqrt(noCells);
		boolean backtrack = false;
		while(position != cells.size()) {
//			System.out.println(position);
			MyRectangle curr = cells.get(position);
			if(curr.getSolution() == (int) limit) {
				backtrack = true;
			}
			else {
				curr.setSolution(curr.getSolution()+1);
			}
			while(curr.getSolution() <= limit) {
				if(backtrack == true) break;
				else if( GameEngine.isRowCorrect(cells, curr) && GameEngine.isColCorrect(cells, curr) ) {
					if(!curr.getCage().isFullSol()) {
						position++;
						backtrack = false;
						break;
					} else {
						if( GameEngine.checkAllCages(true, curr.getCage()) == true ) {
							position++;
							backtrack = false;
							break;
						}
					}
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
	}
	
}
