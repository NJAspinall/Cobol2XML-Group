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

class DivisonTest {

	@Test
	void test() {
		Tokenizer t = CobolParser.tokenizer();
		Parser p = CobolParser.start();
		t.setString("procedure divison.");
		Assembly in = new TokenAssembly(t);
		Assembly out = p.bestMatch(in);
		
		assertTrue(out.stackIsEmpty());
		
		ArrayList<Parser> visited = new ArrayList<Parser>();
		
		Literal lit = new Literal("procedure divison.");
		
		String sr = lit.unvisitedString(visited);
		
		assertEquals(sr, "procedure divison.");
	}

}
