/*
    xml-lif, manipulate XML elements in Java
    Copyright (C) 2016-2018 Sylvain Hallé

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
	public void testText1() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><a>1</a><a><b>2</b></a></root>");
		XPathExpression xpath = XPathExpression.parse("root/a/text()");
		Collection<XmlElement> result = xpath.evaluate(doc);
		assertNotNull(result);
		assertEquals(1, result.size());
	}

	@Test
	public void testAnyString4() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><a><b>5</b></a><a><b>2</b></a></root>");
		XPathExpression xpath = XPathExpression.parse("root/a");
		String result = xpath.evaluateAnyString(doc);
		assertNotNull(result);
		assertEquals("", result);
	}

	@Test
	public void testAnyNumber4() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><a><b>5</b></a><a><b>2</b></a></root>");
		XPathExpression xpath = XPathExpression.parse("root/a");
		Number result = xpath.evaluateAnyNumber(doc);
		assertNull(result);
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
		XPathExpression xpath = XPathExpression.parse("root/a/text()");
		String result = xpath.evaluateAnyString(doc);
		assertNotNull(result);
	}

	@Test
	public void testAnyString2() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><a>1</a><a>2</a></root>");
		XPathExpression xpath = XPathExpression.parse("root/b/text()");
		String result = xpath.evaluateAnyString(doc);
		assertEquals("", result);
	}

	@Test
	public void testAnyString3() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><a><b></b></a><a><c></c></a></root>");
		XPathExpression xpath = XPathExpression.parse("root/a/text()");
		String result = xpath.evaluateAnyString(doc);
		assertEquals("", result);
	}

	@Test
	public void testAnyNumber1() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><a>1</a><a>2</a></root>");
		XPathExpression xpath = XPathExpression.parse("root/a/text()");
		Number result = xpath.evaluateAnyNumber(doc);
		assertTrue(result.intValue() == 1 || result.intValue() == 2);
	}

	@Test
	public void testAnyNumber2() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><a>1</a><a>2</a></root>");
		XPathExpression xpath = XPathExpression.parse("root/b");
		Number result = xpath.evaluateAnyNumber(doc);
		assertNull(result);
	}

	@Test
	public void testAnyNumber3() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><a><b></b></a><a>foo</a></root>");
		XPathExpression xpath = XPathExpression.parse("root/a/text()");
		Number result = xpath.evaluateAnyNumber(doc);
		assertNull(result);
	}

	@Test
	public void testAnyInt1() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><a>1</a><a>2</a></root>");
		XPathExpression xpath = XPathExpression.parse("root/a/text()");
		int result = xpath.evaluateAnyInt(doc);
		assertTrue(result == 1 || result == 2);
	}

	@Test
	public void testAnyInt2() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><a>1</a><a>xyz</a></root>");
		XPathExpression xpath = XPathExpression.parse("root/a/text()");
		int result = xpath.evaluateAnyInt(doc);
		assertEquals(1, result);
	}

	@Test
	public void testAnyInt3() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><a><b></b></a><a>xyz</a></root>");
		XPathExpression xpath = XPathExpression.parse("root/a/text()");
		int result = xpath.evaluateAnyInt(doc);
		assertEquals(0, result);
	}

	@Test
	public void testAnyFloat1() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><a>1</a><a>2</a></root>");
		XPathExpression xpath = XPathExpression.parse("root/a/text()");
		float result = xpath.evaluateAnyFloat(doc);
		assertTrue(result == 1 || result == 2);
	}

	@Test
	public void testAnyFloat2() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><a>1</a><a>xyz</a></root>");
		XPathExpression xpath = XPathExpression.parse("root/a/text()");
		float result = xpath.evaluateAnyFloat(doc);
		assertTrue(Math.abs(1 - result) < 0.0001);
	}

	@Test
	public void testAnyFloat3() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><a><b></b></a><a>xyz</a></root>");
		XPathExpression xpath = XPathExpression.parse("root/a/text()");
		float result = xpath.evaluateAnyFloat(doc);
		assertTrue(result < 0.0001);
	}

	@Test
	public void testNumbers1() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><a>1</a><a>2</a></root>");
		XPathExpression xpath = XPathExpression.parse("root/a/text()");
		Collection<Number> result = xpath.evaluateAsNumbers(doc);
		assertEquals(2, result.size());
	}

	@Test
	public void testNumbers2() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><a><b></b></a><a>foo</a></root>");
		XPathExpression xpath = XPathExpression.parse("root/a/text()");
		Collection<Number> result = xpath.evaluateAsNumbers(doc);
		assertEquals(0, result.size());
	}

	@Test
	public void testNumbers3() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><a><b></b></a><a>foo</a></root>");
		XPathExpression xpath = XPathExpression.parse("root/a");
		Collection<Number> result = xpath.evaluateAsNumbers(doc);
		assertEquals(0, result.size());
	}

	@Test
	public void testStrings1() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><a>1</a><a>2</a></root>");
		XPathExpression xpath = XPathExpression.parse("root/a/text()");
		Collection<String> result = xpath.evaluateAsStrings(doc);
		assertEquals(2, result.size());
	}

	@Test
	public void testStrings2() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><a><b></b></a><a>foo</a></root>");
		XPathExpression xpath = XPathExpression.parse("root/a/text()");
		Collection<String> result = xpath.evaluateAsStrings(doc);
		assertEquals(1, result.size());
	}

	@Test
	public void testStrings3() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><a><b></b></a><a>foo</a></root>");
		XPathExpression xpath = XPathExpression.parse("root/a");
		Collection<String> result = xpath.evaluateAsStrings(doc);
		assertEquals(0, result.size());
	}

	@Test
	public void testDuplicate1() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><a><b></b></a><a>foo</a></root>");
		XPathExpression xpath = XPathExpression.parse("root/a/text()").duplicate();
		Collection<String> result = xpath.evaluateAsStrings(doc);
		assertEquals(1, result.size());	
	}

	@Test
	public void testDuplicate2() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><foo><bar>0</bar><baz>0</baz></foo><foo><bar>1</bar><baz>0</baz></foo></root>");
		XPathExpression xpath = XPathExpression.parse("root/foo[bar=0]").duplicate();
		Collection<XmlElement> result = xpath.evaluate(doc);
		assertEquals(1, result.size());
		for (XmlElement xe : result)
		{
			assertNotNull(xe);
		}
	}
}
