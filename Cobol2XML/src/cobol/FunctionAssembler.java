package cobol;

import parse.*;
import parse.tokens.*;
public class FunctionAssembler extends Assembler {

	
	/**
	 * Pop a string, and set the target functionName to this
	 * string.
	 *
	 * @param   Assembly   the assembly to work on
	 */
	public void workOn(Assembly a) {
		System.out.println("[Function CloseAssembler.workOn()] Running...");
		
		Cobol c = new Cobol();
		String name;
		boolean closed;
		
		//get the function name
		Token t = (Token) a.pop();
		name = t.sval().trim();
		
		while(a.stackIsEmpty() == false) {
			//if a function close tag
			if(t.sval().equals("-ex.")) {
				c.setFunctionClosed(true);
			} 
			//if a function start tag
			else if(t.sval().equals(".")) {
				c.setFunctionClosed(false);
			}
			
			
			else if(t.sval().equals("-")) {
				name = name + t.sval();
			}
			
			else if(t.isWord()) {
				name = name + t.sval();
				
			}
			
			
			
			t = (Token) a.pop();
		}
		System.out.println(t.sval().trim());
		
		
		c.setFunctionName(name);
		a.setTarget(c);
		
	}

}
