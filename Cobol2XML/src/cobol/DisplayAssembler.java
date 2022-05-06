package cobol;

import parse.Assembler;
import parse.Assembly;
import parse.tokens.Token;

public class DisplayAssembler extends Assembler {

	@Override
	public void workOn(Assembly a) {
		
		System.out.println("New Display...");
		
		Cobol c = new Cobol();
		Token t;
		
		//dispose of 'display'
		//t = (Token) a.pop();
		//System.out.println("Initial T : " +t.sval());
		
		//get the strings and variables to display
		String displayString = " ";
		while(a.stackIsEmpty() == false) {
			t = (Token) a.pop();
			if(!t.sval().equalsIgnoreCase("display")) {
				
				displayString = t.sval() + displayString;
				displayString = " " + displayString;
				System.out.println("display T : " +t.sval());
			}
		}
		
		if(!displayString.isBlank()) {
			c.setDisplayString(displayString);
			a.setTarget(c);
		}
	}

}
