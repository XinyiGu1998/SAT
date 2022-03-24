package SATProblem;

import java.util.ArrayList;

public class GSATCase {
	
	private ArrayList<Clause> clauseList = new ArrayList<Clause>();
	private ArrayList<Literal> literalList = new ArrayList<Literal>();
	private Boolean[] truthAssignment;
	private int numUnsat;
	private int indexOfFlippedPreviously = -1;
	
	public GSATCase(ArrayList<Clause> clauseList, ArrayList<Literal> literalList, Boolean[] truthAssignment) {

		
		for (int index = 0; index < literalList.size(); index++) {
			Literal temp = new Literal(literalList.get(index).getIndex(), literalList.get(index).getSign());
			this.literalList.add(temp);
		}
		
		this.truthAssignment = new Boolean[truthAssignment.length];
		
		for (int index = 0; index < truthAssignment.length; index++) {
			this.truthAssignment[index] = truthAssignment[index];
		}
		
		

		for (int index = 0; index < literalList.size(); index++) {
			literalList.get(index).setValue(truthAssignment[index]);

		}
		
		for (int index = 0; index < clauseList.size(); index++) {
			Clause temp = new Clause();
			for (int count = 0; count < clauseList.get(index).getList().size(); count++) {
				for (int i = 0; i < literalList.size(); i++) {
					if (clauseList.get(index).getList().get(count).getIndex() == literalList.get(i).getIndex() && clauseList.get(index).getList().get(count).getSign() == literalList.get(i).getSign()) {
						temp.addLiteral(literalList.get(i));
					}
				}				
			}
			this.clauseList.add(temp);
		}
	}

	public ArrayList<Clause> getClauseList() {
		return clauseList;
	}

	public void setClauseList(ArrayList<Clause> clauseList) {
		this.clauseList = clauseList;
	}

	public Boolean[] getTruthAssignment() {
		return truthAssignment;
	}

	public void setTruthAssignment(Boolean[] truthAssignment) {
		this.truthAssignment = truthAssignment;
	}

	public int getNumUnsat() {
		return numUnsat;
	}

	public void setNumUnsat(int numUnsat) {
		this.numUnsat = numUnsat;
	}
	
	
	public boolean checkEqual() {
		boolean check = false;
		if (literalList.size() == truthAssignment.length) {
			check = true;
		}
		return check;
	}
	
	public int numUnsat() {
		
		int num = 0;

		
		for (int index = 0; index < clauseList.size(); index++) {

			if (clauseList.get(index).checkValue() == false) {
				num++;
			}
		}
		
		return num;
	}
	
	public void setIndexFlipPrev(int indexOfFlippedPreviously) {
		this.indexOfFlippedPreviously = indexOfFlippedPreviously;
	}
	
	public int getIndexFlipPrev() {
		return indexOfFlippedPreviously;
	}
	
	public void showLiteralValue() {
		for (int index = 0; index < clauseList.size(); index++) {
			for (int count = 0; count < clauseList.get(index).getList().size(); count++) {
				System.out.println(clauseList.get(index).getList().get(count));
			}
		}
	}
	
}
