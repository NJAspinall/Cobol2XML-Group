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

class DateWrittenTest {

	@Test
	void test() {
		Tokenizer t = CobolParser.tokenizer();
		Parser p = CobolParser.start();
		t.setString("date-written.  01-apr-2022 - mb.");
		Assembly in = new TokenAssembly(t);
		Assembly out = p.bestMatch(in);
		
		assertFalse(out.stackIsEmpty());
		
		ArrayList<Parser> visitedList = new ArrayList<Parser>();
		Literal literal = new Literal("date-written.  01-apr-2022 - mb.");
		String string = literal.unvisitedString(visitedList);
		assertEquals(string, "date-written.  01-apr-2022 - mb.");
		
		Cobol c = (Cobol) out.getTarget();
		assertNotEquals(null, (Integer) c.getDayDateWritten());
		assertNotEquals(null, (Integer) c.getYearDateWritten());
		assertNotEquals(null, (String) c.getMonthDateWritten());
	}

}
