package cobol;
import java.util.ArrayList;

import parse.Assembler;
import parse.Assembly;
import parse.tokens.Token;

public class MoveAssembler extends Assembler {
	//System.out.println("Move Assembler");
	
	/**
	* Pop all tokens from the line that contains the move command,
	* and set the move's target and source variables
	* string.
	*
	* @param Assembly the assembly to work on
	*/
	@Override
	public void workOn(Assembly a) {
		
		Cobol c = new Cobol();

		//get the next token
		Token t = (Token) a.pop();
		
		/*
		 * The following code reads all tokens on a line that starts with the keyword 'move'.
		 * It records the pointers for the source location and the target destination of the data
		 * to be moved by recording each following instance of a Word() as an element in an array,
		 * which can be used to rebuild the pointers names to be converted into XML elements.
		 * 
		 * As all tokens are added to stack, they must be read in reverse, for example:
		 * move from 'source' to 'dest'.
		 * 
		 * So first we keep popping until we reach the word 'to', then all elements before that
		 * must be the target location.
		 */
		
				//targetLocation holds the pointer to the location the value will be moved to
				String targetLocation = " ";
				//until the keyword 'to' is met, store all strings and numbers in an array
				while (!t.sval().equalsIgnoreCase("to")) {
					if (t.isNumber()) {
						//convert into a String and add to the current location
						targetLocation = (Double.toString(t.nval()))  + targetLocation;
						//add a space between tokens
						targetLocation = ""  + targetLocation;
					} else {
						targetLocation = (t.sval())  + targetLocation;
						targetLocation = "" + targetLocation;
						targetLocation = targetLocation + " ";
					}
					t = (Token) a.pop();
				}
				
				
				

				//sourceLocation holds the pointer to the location of the value to be moved
				String sourceLocation = "";
				
				//get the next token
				t = (Token) a.pop();
				// if the next token is 'move', end the process
				while (!t.sval().equalsIgnoreCase("move")) {
					//otherwise add next token to the sourcelocation
					if (t.isNumber()) {
						sourceLocation = (Double.toString(t.nval())) + sourceLocation;
						sourceLocation = " "  + sourceLocation;
					} else {
						sourceLocation = (t.sval())  + sourceLocation;
						sourceLocation = " "  + sourceLocation;
					}
					
					t = (Token) a.pop();
				}
				//assigns the source and target variables to the Cobo1l object be read by XMLPayload
				c.setMoveSource(sourceLocation);
				c.setMoveTarget(targetLocation);
				a.setTarget(c);
				
				//System.out.println("[Move] From: " +sourceString+ ". To : " +targetString);
		
	}

}
