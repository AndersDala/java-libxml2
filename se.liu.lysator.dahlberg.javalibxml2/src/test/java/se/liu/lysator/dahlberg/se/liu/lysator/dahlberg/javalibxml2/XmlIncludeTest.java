package se.liu.lysator.dahlberg.se.liu.lysator.dahlberg.javalibxml2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Unit test for xi:include.
 */
public class XmlIncludeTest extends TestCase {
	private static final String RPATH = "fourthought/result/XInclude/";
	private static final String TPATH = "fourthought/test/XInclude/docs/";
	private static final Logger LOG = Logger.getLogger(XmlIncludeTest.class.getName());
	private SAXParser parser;
	
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
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setXIncludeAware(true);
		parser = factory.newSAXParser();
		// Configure XML Diff to be more relaxed
		XMLUnit.setIgnoreComments(true);
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setIgnoreAttributeOrder(true);
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
		LOG.finest("Result:\n" + new String(transformedBytes));
		ByteArrayInputStream bais = new ByteArrayInputStream(transformedBytes);
		InputSource ss = new InputSource(bais);
		InputSource rs = new InputSource(result);
		Diff diff = new Diff(ss, rs);
        LOG.info("Diff result: " + diff.toString());
        assertTrue(diff.similar());
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
