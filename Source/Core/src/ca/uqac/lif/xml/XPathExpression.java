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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
* Performs queries on XML documents. The queries are written using the XPath
* syntax, with the following restrictions:
* <ul>
* <li>Transitive children (<tt>//</tt>) and parent (<tt>../</tt>) axes are
* not supported</li>
* <li>Attributes (<tt>@att</tt>) are not supported</li>
* <li>The only operator allowed in a predicate is equality between a path
* and a constant</li>
* </ul>
* Normal usage involves instantiating an expression from a String using
* the {@link #parse(String)} method, and then querying a document using the
* {@link #evaluate(XmlElement)} method.
* <p>
* Examples of valid queries:
* <ol>
* <li><tt>/abc/def</tt></li>
* <li><tt>/abc[ghi=3]/def/text()</tt></li>
* <li><tt>/abc[ghi=3][q=0]/def[xyz='hello']</tt></li>
* </ol>
*/ 
public class XPathExpression
{
	/**
	 * The character used to separate segments of a path
	 */
	public static transient final String s_pathSeparator = "/";
	
	/**
	 * The segments of the path expression
	 */
	List<Segment> m_segments;
	
	/**
	 * Creates an XPath expression from a list of segments
	 * @param segments The segments
	 */
	XPathExpression(ArrayList<Segment> segments)
	{
		super();
		m_segments = segments;
	}
	
	/**
	 * Parses an XPath expression from a string
	 * @param s The string
	 * @return The resulting expression
	 * @throws XPathParseException If parsing caused an error
	 */
	public static /*@NonNull*/ XPathExpression parse(String s) throws XPathParseException
	{
		if (s == null)
		{
			throw new XPathParseException("Input string is null");
		}
		ArrayList<Segment> segments = new ArrayList<Segment>();
		String[] parts = s.split(s_pathSeparator);
		for (String part : parts)
		{
			Segment seg = Segment.parse(part);
			segments.add(seg);
		}
		return new XPathExpression(segments);
	}
	
	/**
	 * Evaluates an XPath expression, and returns a single element 
	 * @param root The root
	 * @return The result of the expression. If the expression returns multiple
	 * elements, the method picks only one and returns it. If the expression
	 * returns no element, the method returns null.
	 */
	public /*@Nullable*/ XmlElement evaluateAny(/*@NonNull*/ XmlElement root)
	{
		Collection<XmlElement> col = evaluate(root);
		for (XmlElement e : col)
		{
			return e;
		}
		return null;
	}
	
	/**
	 * Evaluates an XPath expression, and casts its result as a string
	 * @param root The root
	 * @return The result of the expression
	 */
	public /*@NonNull*/ String evaluateString(/*@NonNull*/ XmlElement root)
	{
		XmlElement e = evaluateAny(root);
		if (e == null || !(e instanceof TextElement))
		{
			return "";
		}
		return ((TextElement) e).getText();
	}
	
	/**
	 * Evaluates an XPath expression, using some element as the root
	 * @param root The root
	 * @return The result of the expression
	 */
	public /*@NonNull*/ Collection<XmlElement> evaluate(/*@NonNull*/ XmlElement root)
	{
		return evaluate(m_segments, root);
	}
	
	/**
	 * Evaluates an XPath expression, using some element as the root and a
	 * list of segments
	 * @param segments The list of segments
	 * @param root The root
	 * @return The result of the expression
	 */
	protected static /*@NonNull*/ Collection<XmlElement> evaluate(/*@NonNull*/ List<Segment> segments, /*@NonNull*/ XmlElement root)
	{
		ArrayList<XmlElement> result = new ArrayList<XmlElement>();
		if (segments.isEmpty())
		{
			return result;
		}
		Segment first_segment = segments.get(0);
		if (first_segment instanceof TextSegment)
		{
			if (root instanceof TextElement)
			{
				result.add(root);
			}
			return result;
		}
		if (first_segment.getElementName().compareTo(root.getName()) != 0)
		{
			return result;
		}
		Collection<Predicate> predicates = first_segment.getPredicates();
		for (Predicate p : predicates)
		{
			if (!p.evaluate(root))
			{
				// Predicate returns false: stop considering this branch
				return result;
			}
		}
		// This segment is OK; remove it and continue evaluation with every
		// child of the root
		if (segments.size() == 1)
		{
			result.add(root);
			return result;
		}
		List<Segment> new_segments = new ArrayList<Segment>();
		new_segments.addAll(segments);
		new_segments.remove(0);
		if (!new_segments.isEmpty())
		{
			List<XmlElement> children = root.getChildren();
			for (XmlElement child : children)
			{
				/*@NonNull*/ Collection<XmlElement> child_result = evaluate(new_segments, child);
				result.addAll(child_result);
			}
		}
		else
		{
			result.add(root);
		}
		return result;
	}
	
	/**
	 * Gets the segments of this XPath expression
	 * @return The list of segments
	 */
	public /*@NonNull*/ List<Segment> getSegments()
	{
		return m_segments;
	}

	/**
	 * Exception denoting an error in the parsing of an XPath expression
	 */
	public static class XPathParseException extends EmptyException
	{
		/**
		 * Dummy UID
		 */
		private static final long serialVersionUID = 1L;

		public XPathParseException(String message)
		{
			super(message);
		}
	}
	
	@Override
	public String toString()
	{
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < m_segments.size(); i++)
		{
			if (i > 0)
			{
				out.append(s_pathSeparator);
			}
			out.append(m_segments.get(i).toString());
		}
		return out.toString();
	}

}
