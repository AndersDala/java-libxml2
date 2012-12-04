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
package se.liu.lysator.dahlberg.se.liu.lysator.dahlberg.libxml2;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

public class LibXml2ParserFactory extends SAXParserFactory {

	private boolean xinclude;
	private boolean namespaces;

	@Override
	public SAXParser newSAXParser() throws ParserConfigurationException,
			SAXException {
		return new LibXml2Parser(this);
	}

	@Override
	public void setFeature(String name, boolean value)
			throws ParserConfigurationException, SAXNotRecognizedException,
			SAXNotSupportedException {
		throw new UnsupportedOperationException("Not implmented yet");
	}

	@Override
	public boolean getFeature(String name) throws ParserConfigurationException,
			SAXNotRecognizedException, SAXNotSupportedException {
		throw new UnsupportedOperationException("Not implmented yet");
	}

	@Override
	public void setNamespaceAware(boolean awareness) {
		namespaces = awareness;
	}

	@Override
	public void setValidating(boolean validating) {
		throw new UnsupportedOperationException("Not implmented yet");
	}

	@Override
	public boolean isNamespaceAware() {
		return namespaces;
	}

	@Override
	public boolean isValidating() {
		throw new UnsupportedOperationException("Not implmented yet");
	}

	@Override
	public Schema getSchema() {
		throw new UnsupportedOperationException("Not implmented yet");
	}

	@Override
	public void setSchema(Schema schema) {
		throw new UnsupportedOperationException("Not implmented yet");
	}

	@Override
	public void setXIncludeAware(boolean state) {
		xinclude = state;
	}

	@Override
	public boolean isXIncludeAware() {
		return xinclude;
	}
}
