package kakurasu;

import java.util.Random;

public class Kakurasu {
	
	//size = n for nxn game board
	private int size;
	//this is the array of whats filled in
	private boolean[][] solution;
	//values that go on the column as sums
	private int [] col;
	//values that go on the row as sums
	private int [] row;

	public Kakurasu(int size) {
		this.size = size;
		solution = new boolean[size][size];
		col = new int[size];
		row = new int[size];
		generate();
		findSums();
	}
	
	private void generate() {
		Random r = new Random();
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				solution[i][j] = r.nextBoolean();
			}
		}
	}

	private void findSums() {
		//i denotes the row and j denotes the col
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				if(solution[i][j] ) {
					col[i] += j + 1;
					row[j] += i + 1;
				}
			}
		}
	}
	
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(" ");
		for(int i = 1; i <= size; i ++) {
			//build the top definers
			s.append(i);
		}
		s.append("\n");
		for(int i = 0; i < size; i++) {
			s.append(i + 1);
			for(boolean b : solution[i]){
				if(b){s.append(1);}else{s.append(0);}
			}
			s.append(col[i]);
			s.append("\n");
		}
		//placeholder
		s.append(" ");
		for(int i = 0; i  < size; i ++) {
			s.append(row[i]);
		}
		
		return s.toString();
	}
	
	public String generatePuzzle() { //returns a text version
		StringBuilder s = new StringBuilder();
		s.append(" ");
		for(int i = 1; i <= size; i ++) {
			//build the top definers
			s.append(i);
		}
		s.append("\n");
		for(int i = 0;i < size; i++) {
			s.append(i + 1);
			for(int j = 0; j < size; j++) {
				s.append(0);
			}
			s.append(col[i]);
			s.append("\n");
		}
		s.append(" ");
		for(int i = 0; i  < size; i ++) {
			s.append(row[i]);
		}
		return s.toString();
		
	}

    public String getCol(){
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < size; i++){
            s.append(col[i]);
            s.append(" ");
        }
        return s.toString();
    }
	
    public String getRow(){
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < size; i++){
            s.append(row[i]);
            s.append(" ");
        }
        return s.toString();
    }

    public String getAns(){
        StringBuilder s = new StringBuilder();
        for(boolean[] i : solution){
            for(boolean j : i){
                if(j){ s.append(1);}
                else{s.append(0);}
            }
        }
        return s.toString();
    }
	/*
	    public static void main(String[] args) {
		Kakurasu k = new Kakurasu(4);
		System.out.println(k.generatePuzzle());
		System.out.println("=================");
		System.out.println(k.toString());
	}
	*/

}
