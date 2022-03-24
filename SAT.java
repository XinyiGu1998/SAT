package SATProblem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;



public class SAT {
	
	public static Random random = new Random();
	
	
	public SAT() {
		
	}
	
	
	
	
	public Pair randomApprox(ArrayList<Literal> literalList, ArrayList<Clause> clauseList) {
		Boolean[] finalList = new Boolean[clauseList.size()];
		
		
		for (int index = 0; index < literalList.size(); index += 2) {
			int rand = random.nextInt(2);
			if (rand == 0) {
				literalList.get(index).setValue(false);
				literalList.get(index+1).setValue(true);
			} else {
				literalList.get(index).setValue(true);
				literalList.get(index+1).setValue(false);
			}
	
		}
		
		
		
		for (int index = 0; index < clauseList.size(); index++) {
			finalList[index] = clauseList.get(index).checkValue();
		}
		
		int count = 0;
		
		for (int index = 0; index < clauseList.size(); index++) {
			if (finalList[index]) {
				count++;
			}
		}
		
		String satisfied = "";
		
		if (count == clauseList.size()) {
			satisfied = "S";
		} else {
			satisfied = "U";
		}

	
		
		return new Pair(satisfied, finalList);
	}
	
	

	public Pair GSAT(ArrayList<Literal> literalList, ArrayList<Clause> clauseList, int maxFlip) {
		GSATCase finalCase;
		
		Boolean[] randomTruthAssignment = new Boolean[literalList.size()];
		
		for (int index = 0; index < literalList.size(); index += 2) {
			int rand = random.nextInt(2);
			if (rand == 0) {
				randomTruthAssignment[index] = false;
				randomTruthAssignment[index+1] = true;

			} else {
				randomTruthAssignment[index] = true;
				randomTruthAssignment[index+1] = false;
			}
	
		}
		
		boolean satisfied = false;
		ArrayList<GSATCase> caseList = new ArrayList<GSATCase>();
		caseList.add(new GSATCase(clauseList, literalList, randomTruthAssignment));
		
		int indexToFlip = -1;
		int indexOfSatisfied = -1;
		
		
		for (int index = 0; index < maxFlip + 1 && satisfied == false; index++) {
			

			
			if (caseList.get(index).numUnsat() == 0) {
				satisfied = true;
				indexOfSatisfied = index;
			} else {
			
				indexToFlip = random.nextInt(literalList.size());
				while (indexToFlip == caseList.get(index).getIndexFlipPrev()) {
					indexToFlip = random.nextInt(literalList.size());
				}
				
				GSATCase temp = new GSATCase(clauseList, literalList, flip(caseList.get(index).getTruthAssignment(), indexToFlip));
				temp.setIndexFlipPrev(indexToFlip);
				caseList.add(temp);

			}
		}
		
		int count = caseList.get(0).getNumUnsat();
		GSATCase best = caseList.get(0);
		for (int index = 1; index < caseList.size(); index++) {
			if (caseList.get(index).getIndexFlipPrev() < count) {
				best = caseList.get(index);
			}
		}
		
		if (satisfied) {
			return new Pair("S", caseList.get(indexOfSatisfied).getTruthAssignment());
		} else {
			return new Pair("U", best.getTruthAssignment());
		}

	}
	
	
	public Boolean[] flip(Boolean[] original, int indexToFlip) {
		Boolean[] flipped = new Boolean[original.length];
		for (int index = 0; index < original.length; index++) {
			flipped[index] = original[index];
		}
		
		if (indexToFlip % 2 == 0) {
			flipped[indexToFlip] = !original[indexToFlip];
			flipped[indexToFlip + 1] = !original[indexToFlip + 1];
		} else {
			flipped[indexToFlip] = !original[indexToFlip];
			flipped[indexToFlip - 1] = !original[indexToFlip - 1];
		}
		
		return flipped;
	}
	
	
	
	
	public Pair UnitProp(ArrayList<Clause> formula, Boolean[] truthAssignmentSoFar) {
		
		
		while (checkUnitClause(formula) && (formula.size() != 0)) {
			int j = checkUnitClauseIndex(formula);
			int literalCount = -1;
			int index = formula.get(j).getList().get(0).getIndex();
			int pairLiteralCount = -1;
			if (formula.get(j).getList().get(0).getSign() == "")  {
				literalCount = formula.get(j).getList().get(0).getIndex() * 2;
				pairLiteralCount = literalCount -1;
			} else if (formula.get(j).getList().get(0).getSign() == "-") {
				literalCount = formula.get(j).getList().get(0).getIndex() * 2 - 1;
				pairLiteralCount = literalCount +1;
			}
			
			

			if (formula.get(j).getList().get(0).getSign() == "") {
				truthAssignmentSoFar[literalCount-1] = true;
				truthAssignmentSoFar[pairLiteralCount-1] = false;
				Iterator<Clause> it = formula.iterator();
				while(it.hasNext()) {

					Clause temp = it.next();
					if (temp.checkContain(index, "")) {
						it.remove();
					} else if (temp.checkContain(index, "-")) {
						temp.dropLiteral(index, "-");
					}
				}

			} else {
				Iterator<Clause> it = formula.iterator();
				truthAssignmentSoFar[literalCount-1] = false;
				truthAssignmentSoFar[pairLiteralCount -1] = true;
				while(it.hasNext()) {
					Clause temp = it.next();
					if (temp.checkContain(index, "-")) {
						it.remove();
					} else if (temp.checkContain(index, "")) {
						temp.dropLiteral(index, "");
					}
				}
			}			
		}
		return new Pair(formula, truthAssignmentSoFar);
	}
	
	
	
	
	public Pair DPLL(ArrayList<Clause> formulaSoFar, Boolean[] truthAssignmentSoFar) {
		Pair result = UnitProp(formulaSoFar, truthAssignmentSoFar);
		ArrayList<Clause> newFormula = (ArrayList<Clause>) result.first();
		Boolean[] newAssignment = (Boolean[]) result.second();
		
		if (newFormula.size() == 0) {
			return new Pair ("S", newAssignment);
		}
		
		boolean emptyClause = false;
		for (int index = 0; index < newFormula.size(); index++) {
			if (newFormula.get(index).numLiterals() == 0) {
				emptyClause = true;
			}
		}
		
		if (emptyClause == true) {
			return new Pair("U", newAssignment);
		}
		
		int i= -1;
		for (int index = 0; index < newAssignment.length; index++) {
			if (newAssignment[index] == null) {
				i = index;
				break;
			}
		}
		

		int indexOfX = -1;
		
		if (i == 0) {
			indexOfX = 1;
		} else {
			if (i % 2 == 0) {
			indexOfX = i /2;
			} else {
			indexOfX = (i+1) /2;
			}
		}
		
		Clause newClause = new Clause();
		newClause.addLiteral(new Literal(indexOfX, ""));
		newFormula.add(newClause);
		
		

		if (i % 2 == 0) {
			newAssignment[i] = false;
			newAssignment[i+1] = true;
		} else {
			newAssignment[i] = true;
			newAssignment[i-1] = false;
		}
		
		Pair resultPair = DPLL(newFormula, newAssignment);
		if (((String) resultPair.first()) == "S" ) {
			return new Pair("S", newAssignment);
		} else {
			newClause.dropLiteral(indexOfX, "");
			newClause.addLiteral(new Literal(indexOfX, "-"));
			if (i % 2 == 0) {
				newAssignment[i] = true;
				newAssignment[i+1] = false;
			} else {
				newAssignment[i] = false;
				newAssignment[i-1] = true;
			}
			return DPLL(newFormula, newAssignment);
		}
		
	}
	
	
	
	
	
	public boolean checkUnitClause(ArrayList<Clause> formula) {
		boolean result = false;
		
		for (int index = 0; index < formula.size(); index++) {
			if (formula.get(index).numLiterals() == 1) {
				result = true;
			}
		}
		
		return result;
	}
	
	public int checkUnitClauseIndex(ArrayList<Clause> formula) {
		int result = -1;
		
		for (int index = 0; index < formula.size(); index++) {
			if (formula.get(index).numLiterals() == 1) {
				result = index;
			}
		}
		
		return result;
	}
	
	
	

}
