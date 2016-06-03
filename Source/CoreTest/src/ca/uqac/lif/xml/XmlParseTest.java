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

import ca.uqac.lif.xml.XmlElement.XmlParseException;

public class XmlParseTest
{
	@Test
	public void testEmpty() throws XmlParseException
	{
		XmlElement e = XmlElement.parse("");
		assertNotNull(e);
		assertTrue(e instanceof TextElement);
		TextElement te = (TextElement) e;
		assertTrue(te.getText().isEmpty());
	}

	@Test
	public void testText() throws XmlParseException
	{
		XmlElement e = XmlElement.parse("abc");
		assertNotNull(e);
		assertTrue(e instanceof TextElement);
		TextElement te = (TextElement) e;
		assertEquals(3, te.getText().length());
	}

	@Test
	public void testEmptyElement() throws XmlParseException
	{
		XmlElement e = XmlElement.parse("<a></a>");
		assertNotNull(e);
		assertEquals("a", e.getName());
		assertEquals(0, e.getChildren().size());
	}

	@Test
	public void testEmptyStringElement() throws XmlParseException
	{
		XmlElement e = XmlElement.parse("<a> </a>");
		assertNotNull(e);
		assertEquals("a", e.getName());
		assertEquals(0, e.getChildren().size());
	}

	@Test
	public void testStringElement() throws XmlParseException
	{
		XmlElement e = XmlElement.parse("<a>1</a>");
		assertNotNull(e);
		assertEquals("a", e.getName());
		assertEquals(1, e.getChildren().size());
	}

	@Test
	public void testStringSpaceElementAfter() throws XmlParseException
	{
		XmlElement e = XmlElement.parse("<a>1 </a>");
		assertNotNull(e);
		assertEquals("a", e.getName());
		assertEquals(1, e.getChildren().size());
		XmlElement child = e.getChildren().get(0);
		assertNotNull(child);
		assertTrue(child instanceof TextElement);
		TextElement ct = (TextElement) child;
		assertEquals("1", ct.getText());
	}

	@Test
	public void testStringSpaceElementBefore() throws XmlParseException
	{
		XmlElement e = XmlElement.parse("<a> 1</a>");
		assertNotNull(e);
		assertEquals("a", e.getName());
		assertEquals(1, e.getChildren().size());
		XmlElement child = e.getChildren().get(0);
		assertNotNull(child);
		assertTrue(child instanceof TextElement);
		TextElement ct = (TextElement) child;
		assertEquals("1", ct.getText());
	}

	@Test
	public void testStringElementLong() throws XmlParseException
	{
		XmlElement e = XmlElement.parse("<a>12345</a>");
		assertNotNull(e);
		assertEquals("a", e.getName());
		assertEquals(1, e.getChildren().size());
	}

	@Test
	public void testNested1() throws XmlParseException
	{
		XmlElement e = XmlElement.parse("<a><b></b></a>");
		assertNotNull(e);
		assertEquals("a", e.getName());
		assertEquals(1, e.getChildren().size());
		XmlElement child = e.getChildren().get(0);
		assertNotNull(child);
		assertEquals("b", child.getName());		
	}

	@Test
	public void testNested2() throws XmlParseException
	{
		XmlElement e = XmlElement.parse("<a><b></b><b></b></a>");
		assertNotNull(e);
		assertEquals("a", e.getName());
		assertEquals(2, e.getChildren().size());
		{
			XmlElement child = e.getChildren().get(0);
			assertNotNull(child);
			assertEquals("b", child.getName());
		}
		{
			XmlElement child = e.getChildren().get(1);
			assertNotNull(child);
			assertEquals("b", child.getName());
		}
	}
	
	@Test
	public void testNested3() throws XmlParseException
	{
		XmlElement e = XmlElement.parse("<a>1<b></b></a>");
		assertNotNull(e);
		assertEquals("a", e.getName());
		assertEquals(2, e.getChildren().size());
		{
			XmlElement child = e.getChildren().get(0);
			assertNotNull(child);
			assertTrue(child instanceof TextElement);
			assertEquals("1", ((TextElement) child).getText());
		}
		{
			XmlElement child = e.getChildren().get(1);
			assertNotNull(child);
			assertEquals("b", child.getName());
		}
	}
	
	@Test
	public void testNested5() throws XmlParseException
	{
		XmlElement e = XmlElement.parse("<root><a>1</a></root>");
		assertNotNull(e);
		assertEquals("root", e.getName());
		assertEquals(1, e.getChildren().size());
		{
			XmlElement child = e.getChildren().get(0);
			assertNotNull(child);
			assertEquals("a", child.getName());
		}
	}
	
	@Test
	public void testNested4() throws XmlParseException
	{
		XmlElement e = XmlElement.parse("<a><b></b>1</a>");
		assertNotNull(e);
		assertEquals("a", e.getName());
		assertEquals(2, e.getChildren().size());
		{
			XmlElement child = e.getChildren().get(0);
			assertNotNull(child);
			assertEquals("b", child.getName());
		}
		{
			XmlElement child = e.getChildren().get(1);
			assertNotNull(child);
			assertTrue(child instanceof TextElement);
			assertEquals("1", ((TextElement) child).getText());
		}
	}

	@Test
	public void testMalformed1()
	{
		try
		{
			XmlElement.parse("<");
		}
		catch (XmlParseException ex)
		{
			return;
		}
		fail("Should have thrown an exception");
	}

	@Test
	public void testMalformed2()
	{
		try
		{
			XmlElement.parse(null);
		}
		catch (XmlParseException ex)
		{
			return;
		}
		fail("Should have thrown an exception");
	}

	@Test
	public void testMalformed3()
	{
		try
		{
			XmlElement.parse("<a>");
		}
		catch (XmlParseException ex)
		{
			return;
		}
		fail("Should have thrown an exception");
	}

	@Test
	public void testMalformed4()
	{
		try
		{
			XmlElement.parse("<a>1");
		}
		catch (XmlParseException ex)
		{
			return;
		}
		fail("Should have thrown an exception");
	}

	@Test
	public void testMalformed5()
	{
		try
		{
			XmlElement.parse("<a></b>");
		}
		catch (XmlParseException ex)
		{
			return;
		}
		fail("Should have thrown an exception");
	}
	
	@Test
	public void testMalformed6()
	{
		try
		{
			XmlElement.parse("<a><b></a>");
		}
		catch (XmlParseException ex)
		{
			return;
		}
		fail("Should have thrown an exception");
	}
	
	@Test
	public void testMalformed7()
	{
		try
		{
			XmlElement.parse("<a><b</a>");
		}
		catch (XmlParseException ex)
		{
			return;
		}
		fail("Should have thrown an exception");
	}
	
	@Test
	public void testMultipleRoots()
	{
		try
		{
			XmlElement.parse("<a></a><a></a>");
		}
		catch (XmlParseException ex)
		{
			return;
		}
		fail("Should have thrown an exception");
	}
	
	@Test
	public void testToString() throws XmlParseException
	{
		/* This is a rather "dummy" test, just to make sure that
		 * the lines of toString() are covered by some test.
		 */
		String to_parse = "<a><b>foo</b></a>";
		XmlElement e = XmlElement.parse(to_parse);
		assertEquals(to_parse, e.toString());
	}

}
