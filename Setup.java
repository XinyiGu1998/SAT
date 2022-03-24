package SATProblem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Setup {
	
	public static ArrayList<Literal> literalList = new ArrayList<Literal>();;
	public static ArrayList<Clause> clauseList = new ArrayList<Clause>();
	
	public static void generateLiteral(int numberOfPairs) {
		for (int index = 0; index < numberOfPairs/2; index++) {
			Literal new1 = new Literal(index, "-");
			Literal new2 = new Literal(index, "");
			literalList.add(new1);
			literalList.add(new2);
		}
	}
	
	
	public static void generateClause(int number) {
		Random random = new Random();
		
		for (int index = 0; index < number; index++) {
			
			Clause newC = new Clause();
			
			int rand1 = random.nextInt(literalList.size());
			int rand2 = random.nextInt(literalList.size());
			while (rand2 == rand1) {
				rand2 = random.nextInt(literalList.size());
			}
			int rand3 = random.nextInt(literalList.size());
			while (rand3 == rand2 || rand3 == rand1) {
				rand3 = random.nextInt(literalList.size());
			}

			newC.addLiteral(literalList.get(rand1));
			newC.addLiteral(literalList.get(rand1));
			newC.addLiteral(literalList.get(rand1));
			
			clauseList.add(newC);
		} 
	}
	
	
	
	public static void main(String[] args) {

		generateLiteral(50);
		generateClause(100);
		
		SAT newSAT = new SAT();
		
		


		long before = System.currentTimeMillis();

		Pair randResult = newSAT.randomApprox(literalList, clauseList);
		
		long after = System.currentTimeMillis();
		
		Boolean[] randTruthAssignment = (Boolean[]) randResult.second();
		for (int index = 0; index < literalList.size(); index++) {
			literalList.get(index).setValue(randTruthAssignment[index]);
		}
		
		int numSatisfied = 0;
		for (int index = 0; index < clauseList.size(); index++) {
			if (clauseList.get(index).checkValue()) {
				numSatisfied++;
			}
		}
		
		System.out.println(randResult.first());
		System.out.println(numSatisfied);
		System.out.println("Before: " + before + "; After: " + after + "; Difference " + (after - before));
		
		
		
		
		
		
		
		before = System.currentTimeMillis();
		Pair GSATResult = newSAT.GSAT(literalList, clauseList, 50);
		after = System.currentTimeMillis(); 
		
		Boolean[] GSATTruthAssignment = (Boolean[]) GSATResult.second();
		for (int index = 0; index < literalList.size(); index++) {
			literalList.get(index).setValue(GSATTruthAssignment[index]);
		}
		
		numSatisfied = 0;
		for (int index = 0; index < clauseList.size(); index++) {
			if (clauseList.get(index).checkValue()) {
				numSatisfied++;
			}
		}
	
		System.out.println(GSATResult.first());
		System.out.println(numSatisfied);
		System.out.println("Before: " + before + "; After: " + after + "; Difference " + (after - before));
		
		
		
		
		
		
		
		before = System.currentTimeMillis();
		
		Boolean[] emptyTruthAssignment = new Boolean[literalList.size()];
		
		Pair DPLLResult = newSAT.DPLL(clauseList, emptyTruthAssignment);
		after = System.currentTimeMillis(); 
		
		Boolean[] DPLLTruthAssignment = (Boolean[]) DPLLResult.second();
		

		for (int index = 0; index < literalList.size(); index++) {
			if (DPLLTruthAssignment[index] != null) {
				literalList.get(index).setValue(DPLLTruthAssignment[index]);
			} else {
				literalList.get(index).setValue(true);
			}
		}
		
		numSatisfied = 0;
		for (int index = 0; index < clauseList.size(); index++) {
			if (clauseList.get(index).checkValue()) {
				numSatisfied++;
			}
		}
	
		System.out.println(DPLLResult.first());
		System.out.println(numSatisfied);
		System.out.println("Before: " + before + "; After: " + after + "; Difference " + (after - before));

		
	}

}
