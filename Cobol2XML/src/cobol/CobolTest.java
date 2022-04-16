package cobol;

import static org.junit.Assert.*;

import org.junit.Test;

public class CobolTest {


	public void testConstantName(String constantName) {
		Cobol cobol = new Cobol();
		cobol.setConstantName(constantName);
		
		if(cobol.getConstantName() != null) {
			System.out.println(cobol.getConstantName());
		}
	}

}
