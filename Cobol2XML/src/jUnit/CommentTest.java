package jUnit;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import cobol.Cobol;
import cobol.CobolParser;
import parse.Assembly;
import parse.Parser;
import parse.tokens.Literal;
import parse.tokens.TokenAssembly;
import parse.tokens.Tokenizer;

class CommentTest {

	@Test
	void test() {
		Tokenizer t = CobolParser.tokenizer();
		Parser p = CobolParser.start();
		t.setString("***---  convert from decimal to base system");
		Assembly in = new TokenAssembly(t);
		Assembly out = p.bestMatch(in);
		
		//check all symbols have been picked up properly
		assertEquals(12, out.length());
		
		//check there are no words left
		assertNull(out.peek());
		
		ArrayList<Parser> visitedList = new ArrayList<Parser>();
		Literal literal = new Literal("***---  convert from decimal to base system");
		String string = literal.unvisitedString(visitedList);
		assertEquals(string, "***---  convert from decimal to base system");
		
		Cobol c = (Cobol) out.getTarget();
		assertEquals("convert from decimal to base system", c.getCommentLine().trim());
	}

}
