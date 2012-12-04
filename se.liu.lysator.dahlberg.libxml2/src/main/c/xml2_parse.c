/*
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
 *  Created on: Dec 2, 2012
 *      Author: Anders Dahlberg
 */

#include "xml2_parse.h"

#include <stdio.h>
#include <libxml/parser.h>
#include <libxml/tree.h>
#include <libxml/xinclude.h>

#define MY_ENCODING "UTF-8"

xmlChar*
xml2_parse(
		const char* content,
		int lengthInBytes,
		int* parsedLengthInBytes_p)
{
	/*
	 * this initialize the library and check potential ABI mismatches
	 * between the version it was compiled for and the actual shared
	 * library used.
	 */
	LIBXML_TEST_VERSION

	/*
	 * The document being in memory, it have no base per RFC 2396,
	 * and the "noname.xml" argument will serve as its base.
	 *
	 * TODO original name here somehow?
	 */
	xmlDocPtr doc;
	doc = xmlReadMemory(content, lengthInBytes, "noname.xml", MY_ENCODING, 0);
	if (doc == NULL) {
		fprintf(stderr, "Failed to parse document\n");
		// TODO gracefully report error back to Java code
		return NULL;
	}

	// Process any xincludes in the document
	if (xmlXIncludeProcessFlags(doc, XML_PARSE_XINCLUDE) < 0) {
		fprintf(stderr, "XInclude processing failed\n");
		return NULL;
	}

	xmlChar* parsedContents_p;
	xmlDocDumpFormatMemory(doc, &parsedContents_p, parsedLengthInBytes_p, 1);

	/*
	 * Free associated memory.
	 */
	xmlFreeDoc(doc);

	return parsedContents_p;
}
