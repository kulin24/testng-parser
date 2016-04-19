/* 
 * The MIT License
 * 
 * Copyright (c) 2010 Bruno P. Kinoshita <http://www.kinoshita.eti.br>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.tupilabs.testng.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * TestNG parser.
 * @since 0.1
 */
public class TestNGParser implements Serializable {

	private static final long serialVersionUID = -6714585408222816355L;

	private static final String APACHE_EXT_DTD = "http://apache.org/xml/features/nonvalidating/load-external-dtd";

	/**
	 * Parses the content of an input stream and returns a Suite.
	 * 
	 * @param inputStream the input stream.
	 * @return List of Resulting object.
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public List<Suite> parse(File file) throws ParserException {

		FileInputStream fileInputStream = null;
		final TestNGXmlHandler handler = new TestNGXmlHandler();

		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		try {
			factory.setFeature(APACHE_EXT_DTD, false);
		} catch (ParserConfigurationException e) {
		} catch (SAXNotRecognizedException e) {
		} catch (SAXNotSupportedException e) {
		}

		SAXParser parser = null;
		final List<Suite> suites;

		try {
			fileInputStream = new FileInputStream(file);
			parser = factory.newSAXParser();
			parser.parse(fileInputStream, handler);

			suites = handler.getSuite();
			// Setting file for all the suites
			for (Suite suite : suites) {
				suite.setFile(file.getAbsolutePath());
			}

		} catch (ParserConfigurationException e) {
			throw new ParserException(e);
		} catch (SAXException e) {
			throw new ParserException(e);
		} catch (IOException e) {
			throw new ParserException(e);
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException ioe) {
					// OK, Do nothing
				}
			}
		}
		return suites;
	}

}
