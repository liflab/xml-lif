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

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Performs queries on XML documents. The queries are written using the XPath
 * syntax, with the following restrictions:
 * <ul>
 * <li>Transitive children (<tt>//</tt>), parent (<tt>../</tt>) and
 * <tt>sibling</tt> axes are not supported</li>
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
 * <li><tt>abc/def</tt></li>
 * <li><tt>abc[ghi=3]/def/text()</tt></li>
 * <li><tt>abc[ghi=3][q=0]/def[xyz='hello']</tt></li>
 * </ol>
 */ 
public class XPathExpression
{
	/**
	 * The character used to separate segments of a path
	 */
	public static transient final String s_pathSeparator = "/";

	/**
	 * The number formatter used to parse strings into numbers
	 */
	private static transient final NumberFormat s_numberFormat = NumberFormat.getInstance();

	/**
	 * The segments of the path expression
	 */
	/*@NonNull*/ List<Segment> m_segments;

	/**
	 * Creates an XPath expression from a list of segments
	 * @param segments The segments
	 */
	public XPathExpression(/*@NonNull*/ List</*@NonNull*/ Segment> segments)
	{
		super();
		m_segments = segments;
	}
	
	/**
	 * Creates a copy of the current XPath expression
	 * @return A copy
	 */
	public XPathExpression duplicate()
	{
		List<Segment> segments = new ArrayList<Segment>(m_segments.size());
		for (Segment s : m_segments)
		{
			segments.add(s.duplicate());
		}
		return new XPathExpression(segments);
	}

	/**
	 * Parses an XPath expression from a string
	 * @param s The string
	 * @return The resulting expression
	 * @throws XPathParseException If parsing caused an error
	 */
	public static /*@NonNull*/ XPathExpression parse(/*@Nullable*/ String s) throws XPathParseException
	{
		if (s == null)
		{
			throw new XPathParseException("Input string is null");
		}
		List<Segment> segments = getNewList();
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
	public /*@NonNull*/ String evaluateAnyString(/*@NonNull*/ XmlElement root)
	{
		Collection<XmlElement> col = evaluate(root);
		for (XmlElement e : col)
		{
			if (e instanceof TextElement)
			{
				return ((TextElement) e).getText();
			}
		}
		return "";
	}

	/**
	 * Evaluates an XPath expression, and casts its result as a number
	 * @param root The root
	 * @return The result of the expression
	 */
	public /*@Nullable*/ Number evaluateAnyNumber(/*@NonNull*/ XmlElement root)
	{
		Collection<XmlElement> col = evaluate(root);
		for (XmlElement e : col)
		{
			if (e instanceof TextElement)
			{
				String text = ((TextElement) e).getText(); 
				Number n = parseAsNumber(text);
				if (n != null)
				{
					return n;
				}
			}
		}
		return null;
	}

	/**
	 * Evaluates an XPath expression, and casts its result as an
	 * <code>int</code>
	 * @param root The root
	 * @return The result of the expression. 0 is returned if no result
	 * was found.
	 */
	public int evaluateAnyInt(/*@NonNull*/ XmlElement root)
	{
		Number n = evaluateAnyNumber(root);
		if (n == null)
		{
			return 0;
		}
		return n.intValue();
	}

	/**
	 * Evaluates an XPath expression, and casts its result as a
	 * <code>float</code>
	 * @param root The root
	 * @return The result of the expression. 0 is returned if no result
	 * was found.
	 */
	public float evaluateAnyFloat(/*@NonNull*/ XmlElement root)
	{
		Number n = evaluateAnyNumber(root);
		if (n == null)
		{
			return 0;
		}
		return n.floatValue();
	}

	/**
	 * Evaluates an XPath expression, and casts all its results as a string
	 * @param root The root
	 * @return The result of the expression. If an element is not a string,
	 *   it is omitted from the result.
	 */
	public /*@NonNull*/ Collection</*@NonNull*/ String> evaluateAsStrings(/*@NonNull*/ XmlElement root)
	{
		Collection<XmlElement> col = evaluate(root);
		Collection<String> new_col = getNewStringCollection();
		for (XmlElement e : col)
		{
			if (e instanceof TextElement)
			{
				new_col.add(((TextElement) e).getText());
			}
		}
		return new_col;
	}

	/**
	 * Evaluates an XPath expression, and casts all its results as a number
	 * @param root The root
	 * @return The result of the expression. If an element is not a text node,
	 *   or does not parse as a number, it is omitted from the result.
	 */
	public /*@NonNull*/ Collection</*@NonNull*/ Number> evaluateAsNumbers(/*@NonNull*/ XmlElement root)
	{
		Collection<XmlElement> col = evaluate(root);
		Collection<Number> new_col = getNewNumberCollection();
		for (XmlElement e : col)
		{
			if (e instanceof TextElement)
			{
				String text = ((TextElement) e).getText(); 
				Number n = parseAsNumber(text);
				if (n != null)
				{
					new_col.add(n);
				}
			}
		}
		return new_col;
	}

	/**
	 * Evaluates an XPath expression, using some element as the root
	 * @param root The root
	 * @return The result of the expression
	 */
	public /*@NonNull*/ Collection</*@NonNull*/ XmlElement> evaluate(/*@NonNull*/ XmlElement root)
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
		Collection<XmlElement> result = getNewCollection();
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
		List<Segment> new_segments = getNewList();
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

	/**
	 * Gets a new instance of the collection return type.
	 * This is so that we can easily change the actual type of collection
	 * used by the class without doing a find-replace.
	 * @return A new empty collection
	 */
	protected static Collection<XmlElement> getNewCollection()
	{
		return new HashSet<XmlElement>();
	}

	/**
	 * Gets a new instance of the collection string return type.
	 * This is so that we can easily change the actual type of collection
	 * used by the class without doing a find-replace.
	 * @return A new empty collection of strings
	 */
	protected static Collection<String> getNewStringCollection()
	{
		return new HashSet<String>();
	}

	/**
	 * Gets a new instance of the collection number return type.
	 * This is so that we can easily change the actual type of collection
	 * used by the class without doing a find-replace.
	 * @return A new empty collection of numbers
	 */
	protected static Collection<Number> getNewNumberCollection()
	{
		return new HashSet<Number>();
	}

	/**
	 * Gets a new instance of the list return type.
	 * This is so that we can easily change the actual type of collection
	 * used by the class without doing a find-replace.
	 * @return A new empty list
	 */
	protected static List<Segment> getNewList()
	{
		return new ArrayList<Segment>();
	}

	/**
	 * Parses a string as a number
	 * @param s The string
	 * @return The number, or null if no number could be parsed
	 */
	protected static /*@Nullable*/ Number parseAsNumber(/*@NonNull*/ String s)
	{
		try
		{
			return s_numberFormat.parse(s);
		} 
		catch (ParseException e)
		{
			// String does not contain a number
		}
		return null;
	}
}
