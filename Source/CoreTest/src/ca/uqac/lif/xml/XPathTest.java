package ca.uqac.lif.xml;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;

import ca.uqac.lif.xml.XPathExpression.XPathParseException;
import ca.uqac.lif.xml.XmlElement.XmlParseException;

public class XPathTest
{
	@Test
	public void testSingleEmpty() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<a></a>");
		XPathExpression xpath = XPathExpression.parse("a");
		Collection<XmlElement> result = xpath.evaluate(doc);
		assertEquals(0, result.size());
	}
	
	@Test
	public void testSingleValue() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<a>1</a>");
		XPathExpression xpath = XPathExpression.parse("a");
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
		assertEquals(0, result.size());
	}
	
	@Test
	public void testtwoLevels2() throws XPathParseException, XmlParseException
	{
		XmlElement doc = XmlElement.parse("<root><a>1</a></root>");
		XPathExpression xpath = XPathExpression.parse("root/a");
		Collection<XmlElement> result = xpath.evaluate(doc);
		assertEquals(1, result.size());
		for (XmlElement xe : result)
		{
			assertNotNull(xe);
			assertTrue(xe instanceof TextElement);
		}
	}

}
