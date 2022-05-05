/*
 * @(#)CobolParser.java	 0.1.0
 *
 * Copyright (c) 2019 Julian M. Bass
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
package cobol;

import parse.Alternation;
import parse.Empty;
import parse.Parser;
import parse.Repetition;
import parse.Sequence;
import parse.tokens.CaselessLiteral;
import parse.tokens.Num;
import parse.tokens.QuotedString;
import parse.tokens.Symbol;
import parse.tokens.Tokenizer;
import parse.tokens.Word;

public class CobolParser {
	/**
	 * Return a parser that will recognize the selected COBOL source code constructs:
	 * 
	 * 
	 * This parser creates a <code>COBOL</code> object
	 * as an assembly's target.
	 *
	 * @return a parser that will recognize and build a 
	 *         <object>COBOL</object> from a source code file.
	 */
	public Parser cobol() {
		Alternation a = new Alternation();
		
		Symbol fullstop = new Symbol('.');
		fullstop.discard();
		
		a.add( ProgramID() );
		
		a.add( DivisionName() );
		
		a.add( SectionName() );
		
		a.add( DateWritten() );
		
		a.add( CommentLine() );

		a.add( Function() );

				
		a.add( Move() );
				
		a.add( Call() );
		
		a.add(new Empty());
		return a;
	}
	
	/*
	 * Return a parser that will recognize the grammar:
	 * 
	 *    Program Identifier = Word
	 *
	 */
	protected Parser ProgramID() {
		Sequence s = new Sequence();
		s.add(new CaselessLiteral("program-id") );
		s.add(new Symbol('.').discard());	
		s.add(new Word().setAssembler(new Program_idAssembler()));
		return s;
	}



	/*
	 * Return a parser that will recognise the grammar:
	 * 
	 *    <divisionName> division
	 *
	 */
	protected Parser DivisionName() {
		Sequence s = new Sequence();
		s.add(new Word().setAssembler(new DivisionAssembler()));
		s.add(new CaselessLiteral("division") );
		s.add(new Symbol('.').discard());
		return s;
	}
	
	/*
	 * Return a parser that will recognize the grammar:
	 * 
	 *    Section Name = Word
	 *
	 */
	protected Parser SectionName() {
		Sequence s = new Sequence();
		s.add(new Word().setAssembler(new SectionNameAssembler()));
		s.add(new CaselessLiteral("section") );
		s.add(new Symbol('.').discard());	

		return s;
	}
	
	/*
	 * Return a parser that will recognise the grammar:
	 * 
	 *    working-storage section
	 *
	 */
	protected Parser DateWritten() {
		
		Sequence s = new Sequence();
		
		s.add(new CaselessLiteral("date-written") );
		//remove the '.' appending 'date-written'
		s.add(new Symbol('.').discard());
		//get the following available sequence of numbers until the next datatype is reached
		s.add(new Num());
		//remove the hyphen between date and month
		s.add(new Symbol('-').discard());

		//This next Word actually contains month and year (which are extracted in DateAssembler
		//get the next sequence of string until the next data type is reached
		s.add(new Word());
		//remove the hyphen between '1995' and 'mb.'
		s.add(new Symbol('-').discard());
		//remove the following string until the next data type is reached ('mb')
		s.add(new Word().discard());
		//remove the '.' appending 'mb'
		s.add(new Symbol('.').discard());
		//assign a date assembler to piece together the gathered Token data types
		s.setAssembler(new DateAssembler());
		return s;
	}
	
	
	
	/*
	* Return a parser that will recognize the grammar:
	*
	* ***--- comment text
	*
	*/
	protected Parser CommentLine() {
	//System.out.println("commentLine()");
	Sequence s = new Sequence();
	Repetition r = new Repetition(new Word());
	s.add(new Symbol("*"));
	s.add(new Symbol("*"));
	s.add(new Symbol("*"));
	s.add(new Symbol("-"));
	s.add(new Symbol("-"));
	s.add(new Symbol("-"));
	s.add(new Word());
	s.add(r);
	s.setAssembler(new CommentLineAssembler());
	return s;
	}

	
	
	
	/*
	 * Return a parser that will recognise the grammar:
	 * 
	 * <functionName>'-ex.'
	 */
	protected Parser Function() {
		Sequence s = new Sequence();
		
		//base
		s.add(new Word());
		//-
		s.add(new Symbol("-"));
		//to
		s.add(new Word());
		//-
		s.add(new Symbol("-"));
		//decimal
		s.add(new Word());
		//		'.' / '-ex'.
		Alternation a = new Alternation();
		a.add(new Symbol("."));
		a.add(new CaselessLiteral("-ex."));
		Repetition r = new Repetition(a);
		s.add(r);
		
		
		
		
		
		
		//a.add(new CaselessLiteral("-ex."));
		//a.add(new Symbol("."));
		
		s.setAssembler(new FunctionAssembler());
		return s;
	}
	

	 /* Return a parser that will recognize the grammar:
	 * 
	 * <move> 'from' <source>(startPosition) 'to' <target>(startPosition:length)
	 */
	protected Parser Move() {

		//The 'move' Sequence
		Sequence s = new Sequence();
		Alternation source  = new Alternation();
		//add move to the Sequence
		s.add(new CaselessLiteral("move"));
		
		//adds either of the following to the Alternation
		source.add(new Word());
		source.add(new Num());

		
		
		
		
		//get start position
		//this could be extended to look for an instance of ':' to find a second length parameter,
		//but all of the Cobol examples simply use a single variable which I assume acts as both
		//startPos and length
		Sequence position = new Sequence();
		position.add(new Symbol("("));
		//adds all text between the brackets (startPosition)
		position.add(new Word());
		//closes parameters
		position.add(new Symbol(")"));
		//add the start position parameter
		source.add(position);
		
		Repetition r = new Repetition(source);
		s.add(r);
		
		s.add(new CaselessLiteral("to"));
		Alternation destination  = new Alternation();
		destination.add(new Word());

		
		
		//get destination startPosition and length
		Sequence destParameters = new Sequence();
		//some 'move to' commands contain a start position and length values e.g. entry_char(ind:1)
		destParameters.add(new Symbol("("));
		//get start position
		destParameters.add(new Word());	
		//get separator value
		destParameters.add(new Symbol(":"));
		//get length
		destParameters.add(new Num());	
		destParameters.add(new Symbol(")"));
		
		destination.add(destParameters);
		
		Repetition re = new Repetition(destination);
		s.add(re);
		
		s.setAssembler(new MoveAssembler());
		return s;
	}
		
		
	
	
	 /* Returns a parser that will recognize the grammar:
	 * 
	 * <call> <call identifier> "using" <variable> <value>
	 */
	protected Parser Call() {
		Sequence s = new Sequence();
		//get call keyword
		s.add(new CaselessLiteral("call"));
		//get the subprogram name
		s.add(new QuotedString());
		//get using keyword
		s.add(new CaselessLiteral("using"));
		
		//get reference
		Alternation a = new Alternation();
		//get reference
		a.add(new Word());
		// get any separators between references
		a.add(new Symbol(","));
		
		Repetition r = new Repetition(a);
		s.add(r);
		
		//get values
		a = new Alternation();
		// get any seperators between values
		a.add(new Symbol(","));
		// or any variables given as values
		a.add(new Word());
		// or any numerical values
		a.add(new Num());
		
		
		r = new Repetition(a);
		s.add(r);
		
		s.setAssembler(new CallAssembler());
		return s;
		
	}
	
	

	/**
	 * Return the primary parser for this class -- cobol().
	 *
	 * @return the primary parser for this class -- cobol()
	 */
	public static Parser start() {
		return new CobolParser().cobol();
	}

	/**
	 * Returns a tokenizer that does not allow spaces to appear inside
	 * the "words" that identify cobol's grammar.
	 *
	 * @return a tokenizer that does not allow spaces to appear inside
	 * the "words" that identify cobol grammar.
	 */
	public static Tokenizer tokenizer() {
		Tokenizer t = new Tokenizer();
		t.wordState().setWordChars(' ', ' ', false);
		return t;
	}

}
