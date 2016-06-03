/*
    xml-lif, manipulate XML elements in Java
    Copyright (C) 2016 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.xml;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;

import ca.uqac.lif.xml.XPathExpression.XPathParseException;
import ca.uqac.lif.xml.XmlElement.XmlParseException;

public class XPathTest
{
	@Test
	public void testEmpty() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<a></a>");
		XPathExpression xpath = XPathExpression.parse("");
		Collection<XmlElement> result = xpath.evaluate(doc);
		assertEquals(0, result.size());
	}
	
	@Test
	public void testSingleEmptyText() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<a></a>");
		XPathExpression xpath = XPathExpression.parse("a/text()");
		Collection<XmlElement> result = xpath.evaluate(doc);
		assertEquals(0, result.size());
	}
	
	@Test
	public void testSingle() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<a></a>");
		XPathExpression xpath = XPathExpression.parse("a");
		Collection<XmlElement> result = xpath.evaluate(doc);
		assertEquals(1, result.size());
	}

	
	@Test
	public void testSingleValue() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<a>1</a>");
		XPathExpression xpath = XPathExpression.parse("a");
		Collection<XmlElement> result = xpath.evaluate(doc);
		assertEquals(1, result.size());
	}
	
	@Test
	public void testSingleValueText() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<a>1</a>");
		XPathExpression xpath = XPathExpression.parse("a/text()");
		Collection<XmlElement> result = xpath.evaluate(doc);
		assertEquals(1, result.size());
		for (XmlElement xe : result)
		{
			assertNotNull(xe);
			assertTrue(xe instanceof TextElement);
		}
	}
	
	@Test
	public void testtwoLevels1() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><a></a></root>");
		XPathExpression xpath = XPathExpression.parse("root/a");
		Collection<XmlElement> result = xpath.evaluate(doc);
		assertEquals(1, result.size());
	}
	
	@Test
	public void testtwoLevels2() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><a>1</a></root>");
		XPathExpression xpath = XPathExpression.parse("root/a/text()");
		Collection<XmlElement> result = xpath.evaluate(doc);
		assertEquals(1, result.size());
		for (XmlElement xe : result)
		{
			assertNotNull(xe);
			assertTrue(xe instanceof TextElement);
		}
	}
	
	@Test
	public void testMultipleValues1() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><a>1</a><a>2</a></root>");
		XPathExpression xpath = XPathExpression.parse("root/a");
		Collection<XmlElement> result = xpath.evaluate(doc);
		assertEquals(2, result.size());
		for (XmlElement xe : result)
		{
			assertNotNull(xe);
			assertTrue(xe instanceof XmlElement);
		}
	}
	
	@Test
	public void testMultipleValues2() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><a>1</a><b>123</b><a>2</a></root>");
		XPathExpression xpath = XPathExpression.parse("root/a");
		Collection<XmlElement> result = xpath.evaluate(doc);
		assertEquals(2, result.size());
		for (XmlElement xe : result)
		{
			assertNotNull(xe);
			assertTrue(xe instanceof XmlElement);
		}
	}
	
	@Test
	public void testPredicate1() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><foo><bar>0</bar><baz>0</baz></foo><foo><bar>1</bar><baz>0</baz></foo></root>");
		XPathExpression xpath = XPathExpression.parse("root/foo[bar=0]");
		Collection<XmlElement> result = xpath.evaluate(doc);
		assertEquals(1, result.size());
		for (XmlElement xe : result)
		{
			assertNotNull(xe);
		}
	}
	
	@Test
	public void testPredicate2() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><foo><bar>0</bar><baz>0</baz></foo><foo><bar>1</bar><baz>0</baz></foo></root>");
		XPathExpression xpath = XPathExpression.parse("root/foo[zzz=0]");
		Collection<XmlElement> result = xpath.evaluate(doc);
		assertEquals(0, result.size());
	}
	
	@Test
	public void testPredicate3() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><foo><bar>0</bar><baz>0</baz></foo><foo><bar>1</bar><baz>0</baz></foo></root>");
		XPathExpression xpath = XPathExpression.parse("root/foo[bar=0][baz=0]");
		Collection<XmlElement> result = xpath.evaluate(doc);
		assertEquals(1, result.size());
		for (XmlElement xe : result)
		{
			assertNotNull(xe);
		}
	}
	
	@Test
	public void testPredicate4() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><foo><bar>0</bar><baz>0</baz></foo><foo><bar>1</bar><baz>0</baz></foo></root>");
		XPathExpression xpath = XPathExpression.parse("root/foo[bar=0][baz=6]");
		Collection<XmlElement> result = xpath.evaluate(doc);
		assertEquals(0, result.size());
	}

	@Test
	public void testAny1() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><a>1</a><a>2</a></root>");
		XPathExpression xpath = XPathExpression.parse("root/a/text()");
		XmlElement result = xpath.evaluateAny(doc);
		assertNotNull(result);
		assertTrue(result instanceof TextElement);
	}
	
	@Test
	public void testAny2() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><a>1</a><a>2</a></root>");
		XPathExpression xpath = XPathExpression.parse("root/b");
		XmlElement result = xpath.evaluateAny(doc);
		assertNull(result);
	}
	
	@Test
	public void testAnyString1() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><a>1</a><a>2</a></root>");
		XPathExpression xpath = XPathExpression.parse("root/a");
		String result = xpath.evaluateString(doc);
		assertNotNull(result);
	}
	
	@Test
	public void testAnyString2() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><a>1</a><a>2</a></root>");
		XPathExpression xpath = XPathExpression.parse("root/b");
		String result = xpath.evaluateString(doc);
		assertEquals("", result);
	}

}
