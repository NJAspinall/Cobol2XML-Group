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

class DivisionTest {

	@Test
	void test() {
		Tokenizer t = CobolParser.tokenizer();
		Parser p = CobolParser.start();
		t.setString("procedure division.");
		Assembly in = new TokenAssembly(t);
		Assembly out = p.bestMatch(in);
		
		assertFalse(out.stackIsEmpty());
		
		ArrayList<Parser> visitedList = new ArrayList<Parser>();
		Literal literal = new Literal("procedure division.");
		String string = literal.unvisitedString(visitedList);
		assertEquals(string, "procedure division.");
		
		Cobol c = (Cobol) out.getTarget();
		assertNotEquals(null, c.getDivisionName());
		assertEquals("procedure", c.getDivisionName());
	}

}
