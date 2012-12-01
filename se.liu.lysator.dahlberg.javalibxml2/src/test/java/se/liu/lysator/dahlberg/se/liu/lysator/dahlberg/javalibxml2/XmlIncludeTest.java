package se.liu.lysator.dahlberg.se.liu.lysator.dahlberg.javalibxml2;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for xi:include.
 */
public class XmlIncludeTest extends TestCase {
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

	/**
	 * Simple include
	 */
	public void testApp() {
		System.out.println("Hello World... ;-)");
		assertTrue(true);
	}
}
