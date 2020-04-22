package mathdoku;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class RandomGame {
	
	private GridConstructor grid;
	private int difficulty;
	protected ArrayList<MyRectangle> cells;
	protected int N;
	private Random rand;
	private ArrayList<Cage> cages;
	private boolean unique;

	public RandomGame(int N, int difficulty, boolean unique) {
		this.N = N;
		this.difficulty = difficulty;
		this.unique = unique;
//		System.err.println(unique);
		rand = new Random();
		GameEngine.noOfSolutions = 0;
//		grid = new GridConstructor(N, MathDoku.width);
//		cells = grid.getCells();
	}
	
	public void generateRandomGame() {

		if(unique) {
//			int counter = 0;
			long start = System.currentTimeMillis();
			while(GameEngine.noOfSolutions != 1) {
				rand = new Random();
				grid = new GridConstructor(N, MathDoku.width);
				cells = grid.getCells();
//				System.out.println(cells.size());
				solve(cells.size());
				fillGrid();
				cages = createCages();
				grid.addCages(cages);
				GameEngine.solve(cells,"generator");
//				counter++;
				if(N == 7 && difficulty == 7 && System.currentTimeMillis() - start > 30000) {
					difficulty--;
					System.out.println(System.currentTimeMillis() - start + "ms elapsed,"
							+ "setting difficulty to: " + difficulty);
					start=System.currentTimeMillis();
					
				}
//				if(counter == 10 && N == 7 && difficulty == 7 && difficulty > 4) {
//					System.err.println("Setting difficulty to 6");
//					difficulty --;
//				}
//				System.out.println("done");
			}
//			MathDoku.createGame(grid, cages, N, true);
		} else {
			grid = new GridConstructor(N, MathDoku.width);
			cells = grid.getCells();
			solve(cells.size());
			fillGrid();
			cages = createCages();
			grid.addCages(cages);
//			MathDoku.createGame(grid, cages, N, true);
		}
//		MathDoku.createGame(grid, cages, N, true);
	}
	
	public void createGame() {
		MathDoku.createGame(grid, cages, N, true);
	}
	
	private void fillGrid() {
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
	
	private void shuffleRows(int r1, int r2) {
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
		}
		for(int i=0; i<row1Cells.size(); i++) {
			temp.add(row1Cells.get(i).getSolution());
			row1Cells.get(i).setSolution(row2Cells.get(i).getSolution());
		}
		for(int i=0; i<row2Cells.size(); i++) {
			row2Cells.get(i).setSolution(temp.get(i));
		}
	}
	
	private void shuffleCols(int c1, int c2) {
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
	
	private ArrayList<MyRectangle> getNeighBours(MyRectangle cell){
		ArrayList<MyRectangle> neighbours = new ArrayList<MyRectangle>();
		
		int cellRow = Integer.valueOf(cell.getRow());
		int cellCol = Integer.valueOf(cell.getCol());
		int cellPos = cell.getCellId();
		
		if(cellRow == 0) {
			neighbours.add(cells.get(cellPos+N));

			if(cellCol == 0) {
				neighbours.add(cells.get(cellPos+1));
			} else if(cellCol == N-1) {
				neighbours.add(cells.get(cellPos-1));
			} else {
				neighbours.add(cells.get(cellPos+1));
				neighbours.add(cells.get(cellPos-1));
			}
		} else if(cellRow == N-1) {
			neighbours.add(cells.get(cellPos-N));
			if(cellCol == 0) {
				neighbours.add(cells.get(cellPos+1));
			} else if(cellCol == N-1) {
				neighbours.add(cells.get(cellPos-1));
			} else {
				neighbours.add(cells.get(cellPos+1));
				neighbours.add(cells.get(cellPos-1));
			}
		} else if(cellCol == 0) {
			neighbours.add(cells.get(cellPos+1));
			neighbours.add(cells.get(cellPos+N));
			neighbours.add(cells.get(cellPos-N));
		} else if(cellCol == N-1) {
			neighbours.add(cells.get(cellPos-1));
			neighbours.add(cells.get(cellPos+N));
			neighbours.add(cells.get(cellPos-N));
		} else {
			neighbours.add(cells.get(cellPos-N));
			neighbours.add(cells.get(cellPos+N));
			neighbours.add(cells.get(cellPos+1));
			neighbours.add(cells.get(cellPos-1));
		}
		return neighbours;
	}
	
	private ArrayList<Cage> createCages() {
		ArrayList<MyRectangle> cageCells = new ArrayList<MyRectangle>();
		ArrayList<MyRectangle> neighbours = new ArrayList<MyRectangle>();
		ArrayList<Cage> cages = new ArrayList<Cage>();
		
		for(int i=0; i < cells.size(); i++) {
			MyRectangle current = cells.get(i);
			if(!current.isOccupied()) {
				current.setOccupied(true);
				cageCells.add(current);
				neighbours = getNeighBours(current);
				int limit;
				if (difficulty == 5) {
					limit = rand.nextInt(difficulty-2)+2;
				} else if(difficulty == 7) {
					limit = rand.nextInt(difficulty-2)+2;
				} else {
					limit = rand.nextInt(difficulty-2)+2;
				}
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
				if(cageCells.size() == 1) {
					String result = String.valueOf(cageCells.get(0).getSolution());
					cages.add(new Cage(result, arr));
				} else {
					int decision = rand.nextInt(difficulty);
					String result = createOperations(decision, cageCells);
					cages.add(new Cage(result, arr));
				}
				cageCells.clear();
			}
		}
		if(!unique)
			return mergeSizeOneCells(cages);
		else if (N < 8)
			return mergeSizeOneCells(cages);
		else 
			return cages;
	}
	
	private ArrayList<Cage> mergeSizeOneCells(ArrayList<Cage> cages) {
//		System.err.println("begin");
		ArrayList<Cage> sizeOneCages = new ArrayList<Cage>();
		ArrayList<MyRectangle> sizeOneCells = new ArrayList<>();
		ArrayList<MyRectangle> neighbours = new ArrayList<MyRectangle>();
//		int counter = 0;
		
		for(int i=0; i < cages.size(); i++) { 
			Cage cage = cages.get(i);
			if(cage.getCells().size()==1) {
				sizeOneCages.add(cage);
				cages.remove(cage);
				i--;
			}
		}
		for(Cage cage : sizeOneCages) {
			for(MyRectangle cell : cage.getCells()) {
				sizeOneCells.add(cell);
			}
		}
		for(int i=0; i < sizeOneCells.size(); i++) {
			MyRectangle cell = sizeOneCells.get(i);
			neighbours = getNeighBours(cell);
			for(int j=0; j < neighbours.size(); j++) {
				MyRectangle r = neighbours.get(j);
				if (r.getCage().getCells().size() == 1) {
//					System.out.println("Detected a pair: " + r.getCage().getCells().get(0).getCellId()
//							+ " : " + cell.getCellId());
					int decision = rand.nextInt(difficulty);
					ArrayList<MyRectangle> cageCells = new ArrayList<MyRectangle>();
					cageCells.add(cell);
					cageCells.add(r.getCage().getCells().get(0));
					
					sizeOneCages.remove(cell.getCage());
					sizeOneCages.remove(r.getCage());
					sizeOneCells.remove(cell);
					sizeOneCells.remove(r.getCage().getCells().get(0));

					String result = createOperations(decision, cageCells);
//					System.out.println(result);
					MyRectangle[] arr = cageCells.toArray(new MyRectangle[cageCells.size()]);
					Arrays.sort(arr);
					cages.add(new Cage(result, arr));
//					System.out.println("added a new cage: " + result);
					i--;
//					counter++;
//					System.err.println(counter + " iterations");
					break;
				}
			}
		}
		for(Cage c : sizeOneCages) {
			String result = String.valueOf(c.getCells().get(0).getSolution());
			cages.add(new Cage(result, c.getCells().get(0)));
		}
//		System.err.println("final iteration done");
		return cages;
	}
	
	private String createOperations(int decision, ArrayList<MyRectangle> cells) {
		String result = null;
		int total;
		
		if(decision == 0) {
			total = 0;
			for(MyRectangle cell : cells) {
				total = total + cell.getSolution();
			}
			result = String.valueOf(total) + "+";
			return result;
		} else if (decision == 1 || decision == 2) {
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
				return createOperations(0, cells);
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
			if(total < 200 && difficulty == 4) {
				result = String.valueOf(total) + "x";
				return result;			
			}
			else if(total < 1000 && difficulty == 5) {
				result = String.valueOf(total) + "x";
				return result;			
			}
			else if(total < 5000 && difficulty >= 6) {
				result = String.valueOf(total) + "x";
				return result;					
			} else {
				return createOperations(rand.nextInt(3), cells);
			}
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
				return createOperations(4, cells);
			}
		}
	}
	
	private boolean solve(int noCells) {
		int position = 0;
		double limit = Math.sqrt(noCells);
		boolean backtrack = false;
		while(position != cells.size()) {
			if(position < 0) {
				return false;
			}
			MyRectangle curr = cells.get(position);
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
	
//	private void uniqueSolution() {
//		
//		int n = this.nCr(N);
//		
//		int[][][] cols = new int[N][n][2];
//		int[][][] rows = new int[N][n][2];
//		
//		int pos = 0;
//		
//		for(int i = 0; i < N; i++) {
//			pos = 0;
//			for(int j = 0; j < N-1; j++) {
//				for(int y = j; y < N-1; y++) {
//					int pair1 = grid.matrix[j][i].getSolution();
//					int pair2 = grid.matrix[y+1][i].getSolution();
//					cols[i][pos][0] = pair1;
//					cols[i][pos][1] = pair2;
//					pos++;
//				}
//			}
//		}
//		
//		for(int i = 0; i < N; i++) {
//			System.out.println("*********** Col " + i + " ***********");
//			for(int j=0; j<n; j++) {
//				System.out.print("[");
//				for(int k=0; k<2; k++) {
//					System.out.print(cols[i][j][k]);
//					if (k==0) {
//						System.out.print(",");
//					}
//				}
//				System.out.print("]  ");
//			}
//			System.out.println();
//		}
//		
//		for(int i = 0; i < N; i++) {
//			pos = 0;
//			for(int j = 0; j < N-1; j++) {
//				for(int y = j; y < N-1; y++) {
//					int pair1 = grid.matrix[i][j].getSolution();
//					int pair2 = grid.matrix[i][y+1].getSolution();
//					rows[i][pos][0] = pair1;
//					rows[i][pos][1] = pair2;
//					pos++;
//				}
//			}
//		}
//		
//		for(int i = 0; i < N; i++) {
//			System.out.println("*********** Row " + i + " ***********");
//			for(int j=0; j<n; j++) {
//				System.out.print("[");
//				for(int k=0; k<2; k++) {
//					System.out.print(rows[i][j][k]);
//					if (k==0) {
//						System.out.print(",");
//					}
//				}
//				System.out.print("]  ");
//			}
//			System.out.println();
//		}
//		
//	}
//	
//	 
//	private int nCr(int n) { 
//	    return fact(n) / (fact(2) * fact(n - 2)); 
//	} 
//	  
//	private int fact(int n) { 
//	    int res = 1; 
//	    for (int i = 2; i <= n; i++) 
//	        res = res * i; 
//	    return res; 
//	} 
	
}