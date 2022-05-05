package cobol;

import parse.Assembler;
import parse.Assembly;
import parse.tokens.Token;

public class FunctionCloseAssembler extends Assembler {

	/**
	 * Pop a string, and set the target functionName to this
	 * string.
	 *
	 * @param   Assembly   the assembly to work on
	 */
	public void workOn(Assembly a) {
		System.out.println("[FunctionCloseAssembler.workOn()] Running...");
		
		Cobol c = new Cobol();
		Token t = (Token) a.pop();
		System.out.println(t.sval().trim());
		c.setFunctionName(t.sval().trim());
		c.setFunctionClosed(true);
		a.setTarget(c);
		
	}

}
