import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javafx.scene.Group;

public class GameEngine {

//	private static Group grid;
//	private static ArrayList<MyRectangle> cells;

	public static String CheckCol(Group grid, ArrayList<MyRectangle> cells, MyRectangle current) {
		return null;
	}

	public static String CheckRows(Group grid, ArrayList<MyRectangle> cells) {
		int N = MathDoku.getN();
		Set<String> duplChecker = new HashSet<String>();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (!duplChecker.add(cells.get(i + j * N).getValue())) {
					System.out.println(cells.get(i + j * N).getValue());
				}
			}
		}
		return "Unique";
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
					return false;
				}
				set.add(num);
			}
		}
		MathDoku.setText("Correct");
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
					return false;
				}
				set.add(num);
			}
		}
		MathDoku.setText("Correct");
		return true;
	}
	
	public static boolean checkAllCages(ArrayList<Cage> cages) {
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
