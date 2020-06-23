package kakurasu;

public class Solver {
	
	private int size;
	private int[]cols, rows;
	public Solver(int size, int[] cols, int[] rows) {
		this.size = size;
		this.rows = rows;
		this.cols = cols;
		if(cols.length != size || rows.length != size) {
			throw new Error("row or columns incorrectly sized, size: " + size + " rows: " + rows.length + " cols:" + cols.length);
		}
	}
	
	//this is the brute force solver
	public int[][] bruteForce(){
		
		return null;
	}

}
