/*
 * @(#)XMLPayload.java	 0.1.0
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
 */package xmlwriter;


import cobol.*;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;




public class XMLPayload {
	Document doc;
	Element rootElement;
	
	protected ArrayList<Element> elements = new ArrayList<Element>();
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public XMLPayload() {
		try {
		DocumentBuilderFactory dbFactory =
		         DocumentBuilderFactory.newInstance();
		dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); // XML parsers should not be vulnerable to XXE attacks
		dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); // XML parsers should not be vulnerable to XXE attacks
		DocumentBuilder dBuilder = 
		            dbFactory.newDocumentBuilder();
		doc = dBuilder.newDocument();
		
		 // root element
        rootElement = doc.createElement("cobol");
        doc.appendChild(rootElement);
		
		 } catch (Exception e) {
	         e.printStackTrace();
	     }
		
	}
	
	
	
	/***
	 * Adds the elements that relate to the Cobol file metadata, is only used once at the start.
	 * 
	 * @param c : the Cobol file object
	 * 
	 * -Nathan
	 */
	public void addElements(Cobol c) {
		/*
		 *  add sectionName element
		 */		
		String sectionName = c.getSectionName();
		if (sectionName != null) {
			this.addSectionElement( sectionName );
			
			// Add contents of procedure division
		} else {
			// Section Name null
		}
		
		/*
		 *  add divisionName element
		 */		
		String divisionName = c.getDivisionName();
		if (divisionName != null) {
			this.addDivisionElement( divisionName );
			// Got Division
			// Add contents of procedure division
		} else {
			// Division Name null
		}
		
		/*
		 *  add ProgramID element
		 */		
		String programIDName = c.getProgram_ID();
		if (programIDName != null) {
			this.addProgram_IDElement( programIDName );
			// Got ProgramID
			// Add contents of procedure division
		} else {
			// ProgramID null
		}
		
		/*
		 *  add DateWritten element
		 */	
		// DayDateWritten
		int dayDateWritten = c.getDayDateWritten();
		if(dayDateWritten != 0) {
			this.addDayDateWrittenElement( dayDateWritten );
		}
		
		//MonthDateWritten
		String monthDateWritten = c.getMonthDateWritten();
		if (monthDateWritten != null) {
			this.addMonthDateWrittenElement( monthDateWritten );
			// Got month
			// Add contents of procedure division
		} else {
			// month null
		}

		// YearDateWritten
		int yearDateWritten = c.getYearDateWritten();
		if(yearDateWritten != 0) {
			this.addYearDateWrittenElement( yearDateWritten );
		}
		
		
		String commentLine = c.getCommentLine();
		if(commentLine != null ) {
			this.addCommentLineElement( commentLine );
			//System.out.println("Got Section");
			// Add contents of procedure division
		} else {
			//System.out.println("Comment Line null");
		}
		
		
		//move
		String from = c.getMoveSource();
		String to = c.getMoveTarget();
		if((from != null) && (to != null)) {
			this.addMoveElement( from, to );
		} else {
			//from or to null
		}
				
				
				
		//call
		String subProgram = c.getCallSubProgram();
		LinkedList<String> values = c.getCallValues();
		LinkedList<String> references = c.getCallReferences();
		this.addCallElement(subProgram, references, values);
		
		
		//function
		String functionName = c.getFunctionName();
		
		if(c.getFunctionClosed() != null) {
			boolean closed = c.getFunctionClosed();
			
			if(c.getFunctionClosed() == true) {
				//this.addFunctionClose(functionName, closed);
				this.addFunction(functionName, closed, elements);
			}
			else if(c.getFunctionClosed() == false) {
				//this.addFunctionOpen(functionName, closed);#
				this.addFunction(functionName, closed, elements);
			}
		}
		
		
		//display
		String displayString = c.getDisplayString();
		
		if(displayString != null) {
			this.addDisplayElement(displayString);
		}
		
	}
	

 	void addProgram_IDElement(String stringElement) {
		//  Program ID element
		
		if(stringElement != null) {
			Element cobolname = doc.createElement("Program-ID");
			cobolname.appendChild(doc.createTextNode(stringElement));
			rootElement.appendChild(cobolname);
		}
	}
 	
	void addCommentLineElement(String stringElement) {
		//  Comment Line element
		
		if(stringElement != null) {
			Element cobolname = doc.createElement("comment");
			cobolname.appendChild(doc.createTextNode(stringElement));
			rootElement.appendChild(cobolname);
		}
	}
 	
 	
 	
 	void addSectionElement(String stringElement) {
		//  Section element
		
		if(stringElement != null) {
			Element cobolname = doc.createElement("section");
			cobolname.appendChild(doc.createTextNode(stringElement));
			rootElement.appendChild(cobolname);
		}
	}
 	
 	void addDivisionElement(String stringElement) {
		//  Division element
		if(stringElement != null) {
			Element cobolname = doc.createElement("division");
			cobolname.appendChild(doc.createTextNode(stringElement));
			rootElement.appendChild(cobolname);
		}
	}
	
	void addDayDateWrittenElement(int intElement) {
		//  DayDateWritten element
		
		if(intElement != 0) {
			Element cobolname = doc.createElement("day-date-written");
			String s = "" + intElement;
			cobolname.appendChild(doc.createTextNode(s));
			rootElement.appendChild(cobolname);
		}
	}
 	
	void addMonthDateWrittenElement(String stringElement) {
		//  MonthWritten element
		
		if(stringElement != null) {
			Element cobolname = doc.createElement("month-date-written");
			cobolname.appendChild(doc.createTextNode(stringElement));
			rootElement.appendChild(cobolname);
		}
	}

	void addYearDateWrittenElement(int intElement) {
		//  YearDateWritten element
		
		if(intElement != 0) {
			Element cobolname = doc.createElement("year-date-written");
			String s = "" + intElement;
			cobolname.appendChild(doc.createTextNode(s));
			rootElement.appendChild(cobolname);
		}
	}
	
	
	
	
	/***
	 * Creates a function element.
	 * 
	 * @param stringElement : the name of the Cobol function
	 * 
	 * -Nathan
	 */
	void addFunctionOpen(String stringElement, boolean closed) {
		if(closed == false) {
			Element cobolname = doc.createElement("functionStart");
			cobolname.setAttribute("name", stringElement);
			cobolname.appendChild(doc.createTextNode(" "));
			rootElement.appendChild(cobolname);
		}
	}
	
	
	/***
	 * Closes the open function element
	 * 
	 * -Nathan
	 */
	void addFunctionClose(String stringElement, boolean closed) {
		if(closed == true) {
			Element cobolname = doc.createElement("functionEnd");
			cobolname.setAttribute("name", stringElement);
			cobolname.appendChild(doc.createTextNode(" "));
			rootElement.appendChild(cobolname);
		}
	}
	
	
	void addFunction(String functionName, boolean closed, List<Element> elements) {
		if(closed == true) {
			Element function = doc.createElement("Function");
			
			
			
			
			for(Element e : elements) {
				function.appendChild(e);
			}
		}
	}
	
	

	//void addMoveElement(String source, String target, Element function) {
	void addMoveElement(String source, String target) {
		// move command element
		//holds pointers from source location and target destination
		// - Nathan
		
		
		if((source != null) && (target != null))  {
			Element cobolname = doc.createElement("move");
			
			//move source
			Element from = doc.createElement("from");
			from.appendChild(doc.createTextNode(source));
			
			//move destination
			Element to = doc.createElement("to");
			to.appendChild(doc.createTextNode(target));
			
			cobolname.appendChild(from);
			cobolname.appendChild(to);
			
			rootElement.appendChild(cobolname);
			//elements.add(cobolname);
			}
	}
	
	
	void addCallElement(String sp, LinkedList<String> references, LinkedList<String> values) {
		
		if((sp != "") && (references.isEmpty() != true) && (values.isEmpty() != true)) {
			//create call element
			Element cobolname = doc.createElement("Call");
			
			//add the subProgram name
			Element subProgram = doc.createElement("Sub-Program");
			subProgram.appendChild(doc.createTextNode(sp));
			cobolname.appendChild(subProgram);
			
			
			//add references
			for(String s : references) {
				cobolname = this.addCallReferenceElement(cobolname, s);
			}
			
			
			//add values
			for(String s : values) {
				cobolname = this.addCallValueElement(cobolname, s);
			}
			
			rootElement.appendChild(cobolname);
			//elements.add(cobolname);
		}
	}
	
	
	Element addCallReferenceElement(Element function, String refText) {
		
		Element ref = doc.createElement("Reference");
		ref.appendChild(doc.createTextNode(refText));
		
		function.appendChild(ref);
		return function;
	}
	
	Element addCallValueElement(Element function, String valText) {
		
		Element ref = doc.createElement("Value");
		ref.appendChild(doc.createTextNode(valText));
		
		function.appendChild(ref);
		return function;
	}
	
	
	
	
	
	void addDisplayElement(String displayString) {
		if(displayString != "") {
			Element display = doc.createElement("Display");
			display.appendChild(doc.createTextNode(displayString));
			
			rootElement.appendChild(display);
		}
	}
	
	
	
	
	
	public void writeFile(String filename) {
		try {
		// write the content into xml file
		// insert debug printf "WriteFile Filename: " + filename
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); // XML parsers should not be vulnerable to XXE attacks
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, ""); // XML parsers should not be vulnerable to XXE attacks
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        
        StreamResult result = new StreamResult(new File(filename));
        transformer.transform(source, result);
        
        // Output to console for testing
        StreamResult consoleResult = new StreamResult(System.out);
        transformer.transform(source, consoleResult);
        
        //A character stream that collects its output in a string buffer, 
        //which can then be used to construct a string.
        StringWriter writer = new StringWriter();
        
        //transform document to string 
        transformer.transform(source, new StreamResult(writer));
        
        String xmlString = writer.getBuffer().toString();
        LOGGER.info(xmlString);
		 } catch (Exception e) {
	         e.printStackTrace();
	     }
	}

}
