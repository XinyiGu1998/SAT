package SATProblem;

import java.util.ArrayList;
import java.util.Iterator;


public class Clause {
	
	private ArrayList<Literal> list = new ArrayList<Literal>();
	
	
	public Clause() {
		
	}
	
	public void addLiteral(Literal newLiteral) {
		list.add(newLiteral);
	}
	
	public void dropLiteral(int index, String sign) {
		Iterator<Literal> it = list.iterator();
		while (it.hasNext()) {
			Literal temp = it.next();
			if (temp.getIndex() == index && temp.getSign() == sign) {
				it.remove();
			}
			
		}
		
	}
	
	public boolean checkValue() {
		boolean check = false;
		
		for (int index = 0; index < list.size(); index++) {
			if (list.get(index).getValue()) {
				check = true;
			}
		}
		
		return check;
	}
	
	public ArrayList<Literal> getList() {
		ArrayList<Literal> returnList = new ArrayList<Literal>();
		
		for (int index = 0; index < list.size(); index++) {
			Literal newLiteral = new Literal(list.get(index).getIndex() ,list.get(index).getSign());
			returnList.add(newLiteral);
		}
		return returnList;
	}
	
	
	
	public int numLiterals() {
		int num = list.size();
		return num;
	}
	
	
	public boolean checkContain(int index, String sign) {
		boolean check = false;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getIndex() == index && list.get(i).getSign() == sign) {
				check = true;
			}
		}
		return check;
	}
	
	public String toString() {
		String output = "(";
		
		for (int index = 0; index < list.size(); index++) {
			output = output + "X" + list.get(index).getIndex() + list.get(index).getSign();
			if (index+1 < list.size()) {
				output = output + "v";
			}
		}
		output = output + ")";
		
		return output;
	}

}
