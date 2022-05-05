package cobol;

import java.util.ArrayList;
import java.util.LinkedList;

import parse.Assembler;
import parse.Assembly;
import parse.tokens.Token;

public class CallAssembler extends Assembler {

	
	/**
	 * Call will call another program to be run, sometimes with parameters.
	 * 
	 * Pop a string, and set the target call command to this 
	 *
	 * @param   Assembly   the assembly to work on
	 */
	public void workOn(Assembly a) {
		//System.out.println("Running Call Assembly...");
		Cobol c = new Cobol();
		
		//dispose of '.'
		Token t = (Token) a.pop();
		
		//lists to hold the values associated with the call command
		LinkedList<String> finalValues = new LinkedList<String>();
		LinkedList<String> tempValues = new LinkedList<String>();
		LinkedList<String> references = new LinkedList<String>();


		//until the keyword 'using' is met, store all tokens in an array
		while((!t.sval().equalsIgnoreCase("using")) && (a.stackIsEmpty() == false)) {
			System.out.println("next token");
			if(t.isNumber()) {
				tempValues.addFirst(t.sval()+"");
			} else if(!t.sval().equals("value")){
				tempValues.addFirst(t.sval());
			}
			
			//if ',' encountered, store the values in a separate array,
			//then empty it to contain references
			if(t.sval().equals(",")) {
				finalValues = tempValues;
				tempValues.clear();
			}
			t = (Token) a.pop();
		}
		references = tempValues;
		
		//dispose of 'using'
		t = (Token) a.pop();
		
		//get the subprogram name that is being called
		String subProgram = "";
		while((!t.sval().equalsIgnoreCase("call"))) {
			System.out.println("second next token" + t.sval());
			
			if(t.isQuotedString()) {
				subProgram = t.sval();
			}
			t = (Token) a.pop();
		}
		
		
		
		
		
		for(String s : references) {
			System.out.println("ref =" +s);
		}
		for(String s : tempValues) {
			System.out.println("tempVal =" +s);
		}
		if(finalValues.isEmpty() == false) {
			for(String s : finalValues) {
				System.out.println("finVal =" +s);
			}
		}
		
		
		c.setCallValues(finalValues);
		c.setReferences(references);
		c.setCallSubProgram(subProgram);	
		a.setTarget(c);
	}
}
