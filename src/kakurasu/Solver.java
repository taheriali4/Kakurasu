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

	public static ArrayList<Long> calcNumOfCombo(int total, int eachBox) {
		ArrayList<Long> combinations = new ArrayList<>(0);
		String min="1";
		String max="9";
		for(int i=0; i< (eachBox-1); i++)
		{
		 	min +="0";
		 	max +="9";
		}
		
		long minCombo = Long.parseLong(min);
		long maxCombo = Long.parseLong(max);
		for(long j= minimum; j <= maxCombo; j++)
		{
			if(isUnique(j) && (sumOfDigits(j) == total) && (!new Long(j).toString().contains("0"))){
				combinations.add(j);
			}
		}
		return combinations;
	}
	
}
