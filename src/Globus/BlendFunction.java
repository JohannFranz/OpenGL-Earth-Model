package Globus;

public class BlendFunction {

	private String 	name;
	private int 	function;

	BlendFunction( int function, String name ) {
		this.name = name;
		this.function = function;
	}
	
	public String toString() {
		return name;
	}
	
	public int getFunction() {
		return function;
	}
}
