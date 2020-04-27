package mathdoku;
 import java.security.InvalidParameterException;
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
				if(solutionsMode) {
					int val = cage.getCells().get(0).getSolution();
					if(val != cage.getResult()) {
						return false;
					}
				}
				else {
					String val = cage.getCells().get(0).getValue();
					if(Integer.valueOf(val) != cage.getResult()) {
						return false;
					}
				}
				break;

			default:
				throw new IllegalArgumentException("Unexpected value: " + cage.getOPSymbol());
//				return false;
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
	
	private static boolean isSolvable = false;
	public static int noOfSolutions = 0;
	public static ArrayList<Integer[]> solutionSet;
	
	public static boolean isSolvable() {
		return isSolvable;
	}
	
	public static boolean isModeCorrect(String mode) {
		if(mode.equals("random") || mode.equals("button") || mode.equals("single") || mode.equals("generator") || mode.equals("default")) 
			return true;
//		System.err.println("Debugging purposes: Wrong mode for solver: " + mode);
		return false;
	}
	
	public static boolean solve(ArrayList<MyRectangle> cells, String mode) {
		if(!isModeCorrect(mode)) 
			throw new InvalidParameterException("Debugging purposes: Wrong mode for solve(): " + mode);
		//when the solve button is pressed or generating without trying to find
		// multiple solutions is selected show previously done solution, otherwise solve
		if(mode.equals("random") || mode.equals("button")) {
			if(cells.get(0).getSolution() != 0) {
				isSolvable = true;
				return true;
			}
		} else {
			for (MyRectangle cell : cells) {
				cell.setSolution(0);
			} 
		}
		
		long start = System.currentTimeMillis();
		int lastBacktrack = 0;
		int position = 0;
		double limit = Math.sqrt(cells.size());
		boolean backtrack = false;
		solutionSet = new ArrayList<Integer[]>();
		noOfSolutions = 0;
		
		while(position != cells.size()) {
			
			if(System.currentTimeMillis() - start > 30000 && mode.equals("generator")) {
				System.out.println("Backtracking timeout, breaking!");
				noOfSolutions = -1;
				return false;
			}
			if(position < 0 && noOfSolutions == 0) {
				System.err.println("Unsolvable");
				isSolvable = false;
				return false;
			} 
			else if(position < 0) {
				System.out.println("Number of solutions: " + noOfSolutions + "\n");
				for(int i = 0; i < cells.size(); i++) {
					int cellSolution = solutionSet.get(0)[i];
					cells.get(i).setSolution(cellSolution);
				}
				int counter = 0;
				for(Integer [] arr : solutionSet) {
					counter++;
					System.out.println("Solution number: " + counter);
					for(int i=0;i<arr.length;i+=(int)limit){
					    System.out.println(Arrays.toString(Arrays.copyOfRange(arr, i, Math.min(arr.length,i+(int)limit))));
					}          
					System.out.println();
				}
				return true;
			}
			
			MyRectangle curr = cells.get(position);
			if(curr.getSolution() == (int) limit)
				backtrack = true;
			else
				curr.setSolution(curr.getSolution()+1);
			while(curr.getSolution() <= limit) {
				if(backtrack == true) break;
				else if( GameEngine.isRowCorrect(cells, curr) && GameEngine.isColCorrect(cells, curr) ) {
					if(!curr.getCage().isFullSol()) {
						position++;
						backtrack = false;
						break;
					} else {
						if(GameEngine.checkAllCages(true, curr.getCage()) == true) {
							// Found a solution
							if(position == cells.size()-1){
								noOfSolutions++;
								if(noOfSolutions == 1 && mode.equals("single")){
									System.err.println("Found one solution, breaking!");
									return true;
								}
								Integer[] solution = new Integer[cells.size()];
								for(int i = 0; i < cells.size(); i++) {
									solution[i] = cells.get(i).getSolution();
								}
								solutionSet.add(solution);
								while(position > lastBacktrack) {
									MyRectangle tmp = cells.get(position);
									tmp.setSolution(0);
									position--;
								}
								if(noOfSolutions > 1 && mode.equals("generator")) {
									System.out.println("Found more than one solution, breaking!");
									return false;
								}
								curr=cells.get(position);	//last backtracked cell
								isSolvable = true;
								System.err.println("Solvable");
							} else {
								position++;
								backtrack = false;
								break;								
							}
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
				lastBacktrack = position;
//				System.err.println(lastBacktrack);
				position--;	
			}
			backtrack = false;
		}
		return true;
	}
	
}
