package SATProblem;

public class Literal {
	
	
	private int index;
	private Boolean value;
	private String sign;
	
	public Literal(int index, String sign) {
		this.index = index;
		this.sign = sign;
	}


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public Boolean getValue() {
		return value;
	}


	public void setValue(Boolean value) {
		this.value = value;
	}
	
	public String getSign() {
		return sign;
	}
	
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	
	public void flip() {
		value = !value;
	}
	
	public String toString() {
		String output = "X" + index + sign +  "=" + value;
		return output;
		
	}

}
