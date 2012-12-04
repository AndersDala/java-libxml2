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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.xerces.jaxp.SAXParserFactoryImpl;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import se.liu.lysator.dahlberg.libxml2.NarSystem;

/**
 * @author anders
 *
 */
public class LibXml2Reader implements XMLReader {
	private static final Logger LOG = Logger.getLogger(LibXml2Reader.class.getName());

	private SAXParserFactory factory;
	private ContentHandler contentHandler;
	private EntityResolver entityResolver;
	private ErrorHandler errorHandler;
	private DTDHandler dtdHandler;

	private boolean libraryLoaded;

	public LibXml2Reader(SAXParserFactory factory) {
		this.factory = factory;
	}

	public boolean getFeature(String name) throws SAXNotRecognizedException,
			SAXNotSupportedException {
		throw new UnsupportedOperationException("Not implmented yet");
	}

	public void setFeature(String name, boolean value)
			throws SAXNotRecognizedException, SAXNotSupportedException {
		throw new UnsupportedOperationException("Not implmented yet");
	}

	public Object getProperty(String name) throws SAXNotRecognizedException,
			SAXNotSupportedException {
		throw new UnsupportedOperationException("Not implmented yet");
	}

	public void setProperty(String name, Object value)
			throws SAXNotRecognizedException, SAXNotSupportedException {
		throw new UnsupportedOperationException("Not implmented yet");
	}

	public void setEntityResolver(EntityResolver resolver) {
		entityResolver = resolver;
	}

	public EntityResolver getEntityResolver() {
		return entityResolver;
	}

	public void setDTDHandler(DTDHandler handler) {
		dtdHandler = handler;
	}

	public DTDHandler getDTDHandler() {
		return dtdHandler;
	}

	public void setContentHandler(ContentHandler handler) {
		contentHandler = handler;
	}

	public ContentHandler getContentHandler() {
		return contentHandler;
	}

	public void setErrorHandler(ErrorHandler handler) {
		errorHandler = handler;
	}

	public ErrorHandler getErrorHandler() {
		return errorHandler;
	}

	public void parse(InputSource input) throws IOException, SAXException {
		InputStream bs = input.getByteStream();
		int sizeInBytes = bs.available();
		byte[] buffer = new byte[sizeInBytes];
		bs.read(buffer, 0, sizeInBytes);
		LOG.fine(String.format("Transforming %s bytes of content:\n%s", sizeInBytes, new String(buffer)));
		// Parse using libxml2
		byte[] result = parse(buffer);
		// TODO remove this cheating ;-)
		SAXParserFactory standardFactory = SAXParserFactory.newInstance(SAXParserFactoryImpl.class.getName(), getClass().getClassLoader());
		ByteArrayInputStream bais = new ByteArrayInputStream(result);
		InputSource rs = new InputSource(bais);
		try {
			SAXParser parser = standardFactory.newSAXParser();
			parser.parse(rs, (DefaultHandler)contentHandler);
		} catch (ParserConfigurationException e) {
			throw new SAXException(e);
		}
	}

	public void parse(String systemId) throws IOException, SAXException {
		throw new UnsupportedOperationException("Not implmented yet");
	}
	
	/**
	 * Run synchronized until multi-threading has been verified.
	 * @param buffer Bytes to parse.
	 * @param reader Callback object.
	 * @throws SAXException 
	 */
	private synchronized byte[] parse(byte[] buffer) throws SAXException {
		if (!libraryLoaded) {
			loadLibrary();
			libraryLoaded = true;
		}
		byte[] result = libxml2_parse(buffer, this);
		return result;
	}

	private void loadLibrary() throws SAXException {
		try {
			NarSystem.loadLibrary();
		} catch (UnsatisfiedLinkError e) {
			String message = e.getMessage();
			RuntimeException re = new RuntimeException(e);
			SAXParseException exception = new SAXParseException(message, null, re);
			getErrorHandler().fatalError(exception);
		}
	}
	
	private native byte[] libxml2_parse(byte[] buffer, XMLReader reader) throws SAXException;
}
