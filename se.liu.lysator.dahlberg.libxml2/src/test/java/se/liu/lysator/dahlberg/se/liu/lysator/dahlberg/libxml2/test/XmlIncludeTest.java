/**
 * Copyright (c) 2012 Anders Dahlberg
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software 
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, 
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or 
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING 
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * @author Anders Dahlberg
 */
package se.liu.lysator.dahlberg.se.liu.lysator.dahlberg.libxml2.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.xerces.jaxp.SAXParserFactoryImpl;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

import se.liu.lysator.dahlberg.se.liu.lysator.dahlberg.libxml2.LibXml2ParserFactory;

/**
 * Unit test for xi:include.
 * 
 * Note that only select test cases from the W3C conformance test suite is implemented here.
 * 
 * @see http://www.w3.org/XML/Test/XInclude/
 */
public class XmlIncludeTest extends TestCase {
	private static final String RPATH = "fourthought/result/XInclude/";
	private static final String TPATH = "fourthought/test/XInclude/docs/";
	private static final Logger LOG = Logger.getLogger(XmlIncludeTest.class.getName());
	private SAXParser parser;
	private String newFactoryName;
	private String oldFactoryName;
	
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public XmlIncludeTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(XmlIncludeTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		// Use the libxml2 sax-parser factory.
		oldFactoryName = SAXParserFactoryImpl.class.getName();
		newFactoryName = LibXml2ParserFactory.class.getName();
		System.setProperty("javax.xml.parsers.SAXParserFactory", newFactoryName);
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setXIncludeAware(true);
		parser = factory.newSAXParser();
		
	}
	
	protected void processAndCompare(InputStream source, InputStream result) throws Exception
    {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// Create a handler that will echo all parsed elements
		DefaultHandler handler = new EchoHandler(baos);
		// Transform the original
		parser.parse(source, handler);
		// Retrieve result and compare with expected result 
		byte[] transformedBytes = baos.toByteArray();
		LOG.finer("Result:\n" + new String(transformedBytes));
		ByteArrayInputStream bais = new ByteArrayInputStream(transformedBytes);
		InputSource ss = new InputSource(bais);
		InputSource rs = new InputSource(result);
		System.setProperty("javax.xml.parsers.SAXParserFactory", oldFactoryName);
		// Configure XML Diff to be more relaxed
		XMLUnit.setIgnoreComments(true);
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setIgnoreAttributeOrder(true);
		Diff diff = new Diff(ss, rs);
        LOG.info("Diff result: " + diff.toString());
        assertTrue(diff.identical());
        System.setProperty("javax.xml.parsers.SAXParserFactory", newFactoryName);
    }
	
	/**
	 * Basic compare, same file
	 * @throws Exception 
	 */
	public void testNoInclude() throws Exception {
		InputStream source = getUrl(RPATH + "include1.xml");
		InputStream result = getUrl(RPATH + "include1.xml");
		processAndCompare(source, result);
	}
	
	/**
	 * Simple include
	 * @throws Exception 
	 */
	public void testSimpleInclude() throws Exception {
		InputStream source = getUrl(TPATH + "ft-include1.xml");
		InputStream result = getUrl(RPATH + "include1.xml");
		processAndCompare(source, result);
	}
	
	/**
	 * Nestled include
	 * @throws Exception 
	 */
	public void testNestledInclude() throws Exception {
		InputStream source = getUrl(TPATH + "ft-include2.xml");
		InputStream result = getUrl(RPATH + "include2.xml");
		processAndCompare(source, result);
	}
	
	/**
	 * Include with parse := "text"
	 * @throws Exception 
	 */
	public void testIncludeText() throws Exception {
		InputStream source = getUrl(TPATH + "ft-include3.xml");
		InputStream result = getUrl(RPATH + "include3.xml");
		processAndCompare(source, result);
	}
	
	/**
	 * Xpointer include
	 * @throws Exception 
	 */
	public void testXpointerInclude() throws Exception {
		InputStream source = getUrl(TPATH + "ft-include4.xml");
		InputStream result = getUrl(RPATH + "include4.xml");
		processAndCompare(source, result);
	}
	
	/**
	 * Xpointer multiple-matches include
	 * @throws Exception 
	 */
	public void testXpointerMultipleMatchInclude() throws Exception {
		InputStream source = getUrl(TPATH + "ft-include5.xml");
		InputStream result = getUrl(RPATH + "include5.xml");
		processAndCompare(source, result);
	}
	
	/**
	 * Xpointer include sub tree
	 * @throws Exception 
	 */
	public void testXpointerSubTreeInclude() throws Exception {
		InputStream source = getUrl(TPATH + "ft-include6.xml");
		InputStream result = getUrl(RPATH + "include6.xml");
		processAndCompare(source, result);
	}
	
	private InputStream getUrl(String path) {
		return getClass().getClassLoader().getResourceAsStream(path);
	}
}
