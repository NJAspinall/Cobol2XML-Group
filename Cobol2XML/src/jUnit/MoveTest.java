package jUnit;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import cobol.CobolParser;
import parse.Assembly;
import parse.Parser;
import parse.tokens.Literal;
import parse.tokens.TokenAssembly;
import parse.tokens.Tokenizer;

class MoveTest {

	@Test
	void test() {
		Tokenizer t = CobolParser.tokenizer();
		Parser p = CobolParser.start();
		t.setString("move w_number to entry_number");
		Assembly in = new TokenAssembly(t);
		Assembly out = p.bestMatch(in);
		
		assertEquals(4, out.length());
		
		assertNull(out.peek());
		
		ArrayList<Parser> visitedList = new ArrayList<Parser>();
		Literal literal = new Literal("move w_number to entry_number");
		String string = literal.unvisitedString(visitedList);
		assertEquals(string, "move w_number to entry_number");
	}

}
