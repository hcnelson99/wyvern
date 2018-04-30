package wyvern.stdlib.support;

public class Int {
    public static final Int utils = new Int();

    public int from(String s) {
        return Integer.parseInt(s.trim());
    }
	
	public int max_value(){
		return Integer.MAX_VALUE;
	}
	
	public int min_value(){
		return Integer.MIN_VALUE;
	}
}
