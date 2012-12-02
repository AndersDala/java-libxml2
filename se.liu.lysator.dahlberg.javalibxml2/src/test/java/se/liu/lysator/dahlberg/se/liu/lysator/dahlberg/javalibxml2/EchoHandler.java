package se.liu.lysator.dahlberg.se.liu.lysator.dahlberg.javalibxml2;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.Logger;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class EchoHandler extends DefaultHandler {
	private static final Logger LOG = Logger.getLogger(EchoHandler.class.getName());
	private static final String LINE_END = System.getProperty("line.separator");
	private PrintStream out;
	
	public EchoHandler(OutputStream os) {
		BufferedOutputStream bos = new BufferedOutputStream(os);
		out = new PrintStream(bos);
	}

	@Override
	public void startDocument() throws SAXException {
		LOG.fine("Start of document");
		super.startDocument();
		append("<?xml version='1.0' encoding='UTF-8'?>");
		append(LINE_END);
	}

	@Override
	public void endDocument() throws SAXException {
		LOG.fine("End of document");
		append(LINE_END);
		out.flush();
		super.endDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		String name = getName(localName, qName);
		LOG.fine("Begin element: " + name);
		append("<%s", name);

        if (attributes != null) {
        	for (int i = 0; i < attributes.getLength(); i++) {
                addAttribute(attributes, i);
            }
        }

        append(">");
        append(LINE_END);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		String name = getName(localName, qName);
		LOG.fine("End element: " + name);
		append("</%s>", name);
		append(LINE_END);
	}

	private void addAttribute(Attributes attributes, int i) {
		String localName = attributes.getLocalName(i);
		String qName = attributes.getQName(i);
		String attribute = getName(localName, qName);
		String value = attributes.getValue(i);
		LOG.fine("Adding attribute: " + attribute + " with value " + value);
		append(" %s=\"%s\"", attribute, value);
	}
	
	private void append(String format, Object... args) {
		String formatted = String.format(format, args);
		out.append(formatted);
	}

	private String getName(String localName, String qName) {
		return !"".equals(qName) ? qName : localName;
	}

}