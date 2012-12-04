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

import javax.xml.parsers.SAXParser;
import javax.xml.validation.Schema;

import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

@SuppressWarnings("deprecation")
public class LibXml2Parser extends SAXParser {

	private LibXml2ParserFactory factory;

	public LibXml2Parser(LibXml2ParserFactory factory) {
		this.factory = factory;
	}

	@Override
	public Parser getParser() throws SAXException {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public XMLReader getXMLReader() throws SAXException {
		return new LibXml2Reader(factory);
	}

	@Override
	public boolean isNamespaceAware() {
		return factory.isNamespaceAware();
	}

	@Override
	public boolean isValidating() {
		return factory.isValidating();
	}

	@Override
	public void setProperty(String name, Object value)
			throws SAXNotRecognizedException, SAXNotSupportedException {
		throw new UnsupportedOperationException("Not implmented yet");
	}

	@Override
	public Object getProperty(String name) throws SAXNotRecognizedException,
			SAXNotSupportedException {
		throw new UnsupportedOperationException("Not implmented yet");
	}

	@Override
	public Schema getSchema() {
		return factory.getSchema();
	}

	@Override
	public boolean isXIncludeAware() {
		return factory.isXIncludeAware();
	}

}
