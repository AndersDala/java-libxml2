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

#include "se_liu_lysator_dahlberg_se_liu_lysator_dahlberg_libxml2_LibXml2Reader.h"
#include "xml2_parse.h"

JNIEXPORT jbyteArray JNICALL
Java_se_liu_lysator_dahlberg_se_liu_lysator_dahlberg_libxml2_LibXml2Reader_libxml2_1parse(
	JNIEnv* env_p, jobject obj, jbyteArray array_p, jobject xmlReader)
{
	// Convert to native types
	jsize lengthInBytes = (*env_p)->GetArrayLength(env_p, array_p);

	char* source_p = malloc(sizeof(char) * (size_t)lengthInBytes);
	(*env_p)->GetByteArrayRegion(env_p, array_p, 0, lengthInBytes, (jbyte*)source_p);

	// Parse using xml2 library
	int parsedLengthInBytes;
	xmlChar* result_p = xml2_parse(source_p, lengthInBytes, &parsedLengthInBytes);

	// Return Java types

	jbyteArray resultArray_p;
	resultArray_p = (*env_p)->NewByteArray(env_p, parsedLengthInBytes);

	if (parsedLengthInBytes > 0) {
		(*env_p)->SetByteArrayRegion(env_p, resultArray_p, 0, parsedLengthInBytes, (jbyte*)result_p);
	}

	printf("Result was:\n%s\n", result_p);
	// Release non-java buffers
	xmlFree(result_p);
	free(source_p);

	return resultArray_p;
}
