import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javafx.scene.Group;
import javafx.scene.paint.Color;

public class GameEngine {

	public static void checkCol(ArrayList<MyRectangle> cells, MyRectangle current) {
		String col = current.getCol();
		ArrayList<MyRectangle> columnCells = new ArrayList<MyRectangle>();
		Set<String> set = new HashSet<String>();
		boolean hasDuplicates = false;
		
		for(MyRectangle cell : cells) {
//			System.out.println(cell.getRow() + "\t" + cell.getCol() + "\t" +  cell.getValue()+ "\t" + cell.getCageId());
			if(cell.getCol().equals(col)) {
				if(cell.getValue() != null) {
					if(set.add(cell.getValue()) == false){
						hasDuplicates = true;
					}
				}
				columnCells.add(cell);
			}
		}
		if(hasDuplicates) {
			highlightRed(columnCells,"col");

		} else {
			unHighlight(columnCells,"col");
		}
	}
	
	public static void checkRow(ArrayList<MyRectangle> cells, MyRectangle current) {
//		GameEngine.current = current;
		String row = current.getRow();
		ArrayList<MyRectangle> rowCells = new ArrayList<MyRectangle>();
		Set<String> set = new HashSet<String>();
		boolean hasDuplicates = false;
		
		for(MyRectangle cell : cells) {
//			System.out.println(cell.getRow() + "\t" + cell.getCol() + "\t" +  cell.getValue()+ "\t" + cell.getCageId());
			if(cell.getRow().equals(row)) {
				if(cell.getValue() != null) {
					if(set.add(cell.getValue()) == false){
						hasDuplicates = true;
					}
				}
				rowCells.add(cell);
			}
		}
		if(hasDuplicates) {
			highlightRed(rowCells,"row");

		} else {
			unHighlight(rowCells,"row");
		}
	}
	
	public static void checkCage(ArrayList<MyRectangle> cells, MyRectangle current, ArrayList<Cage> cages) {
		
		boolean notFullCage = false;
		Cage currentCage = null;
		ArrayList<MyRectangle> cageCells = new ArrayList<MyRectangle>();
		
		for (Cage cage : cages) {
			for (MyRectangle cell : cage.getCells()) {
				if(current == cell) {
					currentCage = cage;
				}
			}
		}
		
		for(MyRectangle cell : currentCage.getCells()) {
			if(cell.getValue() == null) {
				notFullCage = true;
			}
			cageCells.add(cell);
		}
		
		if(notFullCage == false && GameEngine.checkCages(currentCage) == false)
			highlightRed(cageCells,"cage");
		else 
			unHighlight(cageCells, "cage");
		if (notFullCage == true) {
			unHighlight(cageCells, "cage");
		}
	}
	
	public static void highlightRed(ArrayList<MyRectangle> cells, String value) {
		for(MyRectangle cell : cells) {
			System.out.println("entered loop");
			if(value.equals("row")) {
				cell.setRowRed(true);				
				cell.setFill(Color.rgb(255, 0, 0, 0.2));
			}
			else if(value.equals("col")) {
				cell.setColRed(true);
				cell.setFill(Color.rgb(255, 0, 0, 0.2));
			}
			else if(value.equals("cage")) {
				cell.setCageRed(true);
				cell.setFill(Color.rgb(255, 0, 0, 0.2));
			}
		}
	}
	
	public static void unHighlight(ArrayList<MyRectangle> cells, String value) {
		for(MyRectangle cell : cells) {
			if(value.equals("row")) {
				cell.setRowRed(false);				
				if(cell.isColRed() == true || cell.isCageRed() == true) 
					cell.setFill(Color.rgb(255, 0, 0, 0.2));
				else
					cell.setFill(Color.TRANSPARENT);
			}
			else if(value.equals("col")) {
				cell.setColRed(false);
				if(cell.isRowRed() == true || cell.isCageRed() == true)
					cell.setFill(Color.rgb(255, 0, 0, 0.2));
				else
					cell.setFill(Color.TRANSPARENT);
			}
			else if(value.equals("cage")) {
				cell.setCageRed(false);
				if(cell.isRowRed() == true || cell.isColRed() == true)
					cell.setFill(Color.rgb(255, 0, 0, 0.2));
				else
					cell.setFill(Color.TRANSPARENT);
			}
		}
	}
	
	public static boolean isFinished(ArrayList<MyRectangle> cells) {
		for(MyRectangle r : cells) {
			if(r.getValue() == null) {
				MathDoku.setText("NOT END");
				return false;
			}
		}
		MathDoku.setText("END");
		return true;
	}

	public static boolean checkAllRows(MyRectangle[][] matrix) {
		for (int row = 0; row < matrix.length; row++) {
			Set<String> set = new HashSet<String>();
			for (int col = 0; col < matrix.length; col++) {
				String num = matrix[row][col].getValue();
				if (set.contains(num)) {
					MathDoku.setText("Wrong");
					System.out.println("wrong value is: " + num + " r: "+ matrix[row][col].getRow() + " c: " + matrix[row][col].getCol());
					set = null;
					return false;
				}
				set.add(num);
			} set = null;
		}
		return true;
	}
	
	public static boolean checkAllCols(MyRectangle[][] matrix) {
		for (int col = 0; col < matrix[0].length; col++) {
			Set<String> set = new HashSet<String>();
			for (int row = 0; row < matrix.length; row++) {
				String num = matrix[row][col].getValue();
				if (set.contains(num)) {
					MathDoku.setText("Wrong");
					System.out.println("wrong value is: " + num + " r: "+ matrix[row][col].getRow() + " c: " + matrix[row][col].getCol());
					set = null;
					return false;
				}
				set.add(num);
			} set = null;
		}
		MathDoku.setText("Cols Correct");
		return true;
	}
	
	public static boolean checkCages(Cage...cages) {
		int total;
		for(Cage cage : cages) {
			
			switch (cage.getOPSymbol()) {
			case '+':
				total = 0;
				for(MyRectangle cell : cage.getCells()) {
					total = total + Integer.valueOf(cell.getValue());
				}
				if(total != cage.getResult()) return false; 
				break;
				
			case '-':
				Integer[] valuesSub = new Integer[cage.getCells().size()];
				
				for(int i = 0; i < cage.getCells().size(); i++) {
					valuesSub[i] = Integer.valueOf(cage.getCells().get(i).getValue());
				}
				
				Arrays.sort(valuesSub, Collections.reverseOrder());
				int amountSub = valuesSub[0];
				
				for (int i=1; i < valuesSub.length; i++) {
					amountSub = amountSub - valuesSub[i];
				}
				if(amountSub != cage.getResult()) return false;
				break;
				
			case 'x':
				total = 1;
				for(MyRectangle cell : cage.getCells()) {
					total = total * Integer.valueOf(cell.getValue());
				}
				if(total != cage.getResult()) return false;
				break;
			
			case '÷':
				Integer[] valuesDiv = new Integer[cage.getCells().size()];
				
				for(int i = 0; i < cage.getCells().size(); i++) {
					valuesDiv[i] = Integer.valueOf(cage.getCells().get(i).getValue());
				}
				Arrays.sort(valuesDiv, Collections.reverseOrder());
				int amountDiv = valuesDiv[0];
				
				for (int i=1; i < valuesDiv.length; i++) {
					amountDiv = amountDiv / valuesDiv[i];
				}
				if(amountDiv != cage.getResult()) return false;
				break;

			default:
				throw new IllegalArgumentException("Unexpected value: " + cage.getOPSymbol());
			}
		}
		return true;
	}
	
}
