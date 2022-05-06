package jUnit;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import cobol.Cobol;
import cobol.CobolParser;
import parse.Assembly;
import parse.Parser;
import parse.tokens.Literal;
import parse.tokens.TokenAssembly;
import parse.tokens.Tokenizer;

class CallTest {

	@Test
	void test() {
		Tokenizer t = CobolParser.tokenizer();
		Parser p = CobolParser.start();
		t.setString("call \"c$toupper\" using entry_char, value 16");
		Assembly in = new TokenAssembly(t);
		Assembly out = p.bestMatch(in);
		
		assertEquals(7, out.length()); 

		assertNull(out.peek());
		
		ArrayList<Parser> visitedList = new ArrayList<Parser>();
		Literal literal = new Literal("call \"c$toupper\" using entry_char, value 16");
		String string = literal.unvisitedString(visitedList);
		assertEquals(string, "call \"c$toupper\" using entry_char, value 16");
		
		Cobol c = (Cobol) out.getTarget();
		assertEquals("\"c$toupper\"", c.getCallSubProgram());
		LinkedList<String> refs = new LinkedList<String>();
		refs.add("entry_char");
		LinkedList<String> vals = new LinkedList<String>();
		vals.add("16");
		assertEquals(refs, c.getCallReferences());
		assertEquals(refs, c.getCallValues());
	}

}
