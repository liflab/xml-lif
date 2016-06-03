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

import org.junit.Test;

import ca.uqac.lif.xml.XPathExpression.XPathParseException;

public class XPathParseTest
{
	@Test
	public void testEmpty() throws XPathParseException
	{
		XPathExpression xpe = XPathExpression.parse("");
		assertNotNull(xpe);
		assertEquals(1, xpe.getSegments().size());
		Segment seg = xpe.getSegments().get(0);
		assertNotNull(seg);
		assertEquals("", seg.getElementName());
	}
	
	@Test
	public void testSingleElement() throws XPathParseException
	{
		XPathExpression xpe = XPathExpression.parse("a");
		assertNotNull(xpe);
		assertEquals(1, xpe.getSegments().size());
		Segment seg = xpe.getSegments().get(0);
		assertNotNull(seg);
		assertEquals("a", seg.getElementName());
	}
	
	@Test
	public void testTwoElements() throws XPathParseException
	{
		XPathExpression xpe = XPathExpression.parse("foo/bar");
		assertNotNull(xpe);
		assertEquals(2, xpe.getSegments().size());
		{
			Segment seg = xpe.getSegments().get(0);
			assertNotNull(seg);
			assertEquals("foo", seg.getElementName());
		}
		{
			Segment seg = xpe.getSegments().get(1);
			assertNotNull(seg);
			assertEquals("bar", seg.getElementName());
		}
	}
	
	@Test
	public void testElementPredicate() throws XPathParseException
	{
		XPathExpression xpe = XPathExpression.parse("foo[a=0]");
		assertNotNull(xpe);
		assertEquals(1, xpe.getSegments().size());
		{
			Segment seg = xpe.getSegments().get(0);
			assertNotNull(seg);
			assertEquals("foo", seg.getElementName());
			assertEquals(1, seg.getPredicates().size());
			for (Predicate p : seg.getPredicates())
			{
				assertNotNull(p);
			}
		}
	}
	
	@Test
	public void testSinglePredicate() throws XPathParseException
	{
		XPathExpression xpe = XPathExpression.parse("[a=0]");
		assertNotNull(xpe);
		assertEquals(1, xpe.getSegments().size());
		Segment seg = xpe.getSegments().get(0);
		assertNotNull(seg);
		assertEquals("", seg.getElementName());
		assertEquals(1, seg.getPredicates().size());
		for (Predicate p : seg.getPredicates())
		{
			assertNotNull(p);
		}
	}
	
	@Test
	public void testTwoPredicates() throws XPathParseException
	{
		XPathExpression xpe = XPathExpression.parse("[a=0][b=1]");
		assertNotNull(xpe);
		assertEquals(1, xpe.getSegments().size());
		Segment seg = xpe.getSegments().get(0);
		assertNotNull(seg);
		assertEquals("", seg.getElementName());
		assertEquals(2, seg.getPredicates().size());
		for (Predicate p : seg.getPredicates())
		{
			assertNotNull(p);
		}
	}
	
	@Test
	public void testTwoPredicatesSpace() throws XPathParseException
	{
		XPathExpression xpe = XPathExpression.parse("[a=0] [b=1]");
		assertNotNull(xpe);
		assertEquals(1, xpe.getSegments().size());
		Segment seg = xpe.getSegments().get(0);
		assertNotNull(seg);
		assertEquals("", seg.getElementName());
		assertEquals(2, seg.getPredicates().size());
		for (Predicate p : seg.getPredicates())
		{
			assertNotNull(p);
		}
	}
	
	@Test
	public void testTextSegment1() throws XPathParseException
	{
		XPathExpression xpe = XPathExpression.parse("text()");
		assertNotNull(xpe);
		assertEquals(1, xpe.getSegments().size());
		Segment seg = xpe.getSegments().get(0);
		assertNotNull(seg);
		assertTrue(seg instanceof TextSegment);
	}
	
	@Test
	public void testTextSegment2() throws XPathParseException
	{
		XPathExpression xpe = XPathExpression.parse("foo/text()");
		assertNotNull(xpe);
		assertEquals(2, xpe.getSegments().size());
		Segment seg = xpe.getSegments().get(1);
		assertNotNull(seg);
		assertTrue(seg instanceof TextSegment);
	}
	
	@Test
	public void testNull()
	{
		try
		{
			XPathExpression.parse(null);
		}
		catch (XPathParseException ex)
		{
			return;
		}
		fail("Should throw an exception");
	}
	
	@Test
	public void testMalformed1()
	{
		try
		{
			XPathExpression.parse("[");
		}
		catch (XPathParseException ex)
		{
			return;
		}
		fail("Should throw an exception");		
	}
	
	@Test
	public void testMalformed2()
	{
		try
		{
			XPathExpression.parse("a[");
		}
		catch (XPathParseException ex)
		{
			return;
		}
		fail("Should throw an exception");		
	}
	
	@Test
	public void testMalformed3()
	{
		try
		{
			XPathExpression.parse("[]");
		}
		catch (XPathParseException ex)
		{
			return;
		}
		fail("Should throw an exception");		
	}
	
	@Test
	public void testMalformed4()
	{
		try
		{
			XPathExpression.parse("a]");
		}
		catch (XPathParseException ex)
		{
			return;
		}
		fail("Should throw an exception");		
	}
	
	@Test
	public void testMalformed5()
	{
		try
		{
			XPathExpression.parse("abc[a=1]][b=0]");
		}
		catch (XPathParseException ex)
		{
			return;
		}
		fail("Should throw an exception");		
	}
	
	@Test
	public void testMalformed6()
	{
		try
		{
			XPathExpression.parse("ab]cd[foo=0]");
		}
		catch (XPathParseException ex)
		{
			return;
		}
		fail("Should throw an exception");		
	}
	
	@Test
	public void testMalformedEquality1()
	{
		try
		{
			XPathExpression.parse("a[=]");
		}
		catch (XPathParseException ex)
		{
			return;
		}
		fail("Should throw an exception");		
	}
	
	@Test
	public void testMalformedEquality2()
	{
		try
		{
			XPathExpression.parse("a[=c]");
		}
		catch (XPathParseException ex)
		{
			return;
		}
		fail("Should throw an exception");		
	}
	
	@Test
	public void testMalformedEquality3()
	{
		try
		{
			XPathExpression.parse("a[c=]");
		}
		catch (XPathParseException ex)
		{
			return;
		}
		fail("Should throw an exception");		
	}


	
	@Test
	public void testToString() throws XPathParseException
	{
		/* This is a rather "dummy" test, just to make sure that
		 * the lines of toString() are covered by some test.
		 */
		String to_parse = "a/b[foo=bar]";
		XPathExpression e = XPathExpression.parse(to_parse);
		assertEquals(to_parse, e.toString());
	}
}
