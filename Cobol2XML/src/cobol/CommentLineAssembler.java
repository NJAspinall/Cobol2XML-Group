package cobol;

import java.util.ArrayList;

import parse.Assembler;
import parse.Assembly;
import parse.tokens.Token;

public class CommentLineAssembler extends Assembler {
	/**
	* Pop a string, and set the target DataDivision to this
	* string.
	*
	* @param Assembly the assembly to work on
	*/
	
	@Override
	public void workOn(Assembly a) {
		Cobol c = new Cobol();
		ArrayList<String> words = new ArrayList<String>();
		
		while (!a.stackIsEmpty()) {
			Token t = (Token) a.pop();
			String value = t.sval();
			// only words, not - or *
			if (!(value.equals("-") || value.equals("*") )) {
				words.add(value);
			}
		}

		// Concatenating all the comment words into a single string
		int commentsize = words.size() -1;
		// The final concatenated single string (commentline)
		String commentline = "";
		while ( commentsize >= 0 && commentsize < words.size()) {
			// space between each word added
			commentline += words.get(commentsize) + " ";
			commentsize--;
		}

		// Set the concatenated comment line for the target
		if (!commentline.isEmpty()) {
			c.setCommentLine(commentline);
			a.setTarget(c);
		}
	}

}
