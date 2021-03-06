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
import java.util.List;

/**
 * Representation of an XML document as a tree of named elements.
 * The current implementation of this class handles XML documents,
 * barring these restrictions:
 * <ul>
 * <li>Elements may have attributes, but they are ignored</li>
 * <li>Self-closing elements are not recognized</li>
 * <li>The schema of the document may not be recursive, i.e. an
 * element named <tt>&lt;a&gt;</tt> must not <em>contain</em> another element
 * named <tt>&lt;a&gt;</tt> at any level of nesting</li>
 * <li>Entities and CDATA sections are not supported</li>
 * </ul>
 * An Element can be built from a String by using {@link #parse(String)},
 * and then queried using XPath by creating some {@link XPathExpression}
 * <tt>exp</tt> and calling {@link XPathExpression#evaluate(XmlElement)}.
 *
 */ 
public class XmlElement 
{
	/**
	 * The element's name
	 */
	private /*@NonNull*/ String m_name = "";

	/**
	 * The element's children
	 */
	private /*@NonNull*/ List</*@NonNull*/ XmlElement> m_children;

	/**
	 * Creates an empty XML element
	 */
	XmlElement()
	{
		super();
		m_children = new ArrayList<XmlElement>();
	}

	/**
	 * Creates an XML element with given name
	 * @param name The name
	 */
	public XmlElement(String name)
	{
		this();
		m_name = name;
	}

	/**
	 * Adds a child to this element
	 * @param e The element to add
	 * @return This element
	 */
	public XmlElement addChild(XmlElement e)
	{
		m_children.add(e);
		return this;
	}

	/**
	 * Creates an XML document from a string
	 * @param s The string to read
	 * @return An XML element
	 * @throws XmlParseException If parsing resulted in an error
	 */
	public static /*@NonNull*/ XmlElement parse(String s) throws XmlParseException
	{
		if (s == null)
		{
			throw new XmlParseException("Input string is null");
		}
		s = s.trim();
		if (!s.startsWith("<"))
		{
			return new TextElement(s);
		}
		int closing_index = s.indexOf(">");
		if (closing_index < 0)
		{
			throw new XmlParseException("Closing bracket not found");
		}
		String element_name = s.substring(1, closing_index);
		if (!s.endsWith("</" + element_name + ">"))
		{
			throw new XmlParseException("Closing element " + element_name + " not found");
		}
		XmlElement root = new XmlElement(element_name);
		String inside = s.substring(closing_index + 1, s.length() - (element_name.length() + 3));
		parse(root, inside.trim());
		return root;
	}

	/**
	 * Creates an XML document from a string
	 * @param root The root of the current document
	 * @param s The string to read
	 * @throws XmlParseException If parsing resulted in an error
	 */
	protected static void parse(/*@NonNull*/ XmlElement root, /*@NonNull*/ String s) throws XmlParseException
	{
		while (!s.isEmpty())
		{
			int consumed = 0;
			XmlElement new_element = null;
			if (!s.startsWith("<"))
			{
				int begin_index = s.indexOf("<");
				if (begin_index < 0)
				{
					new_element = new TextElement(s);
					consumed = s.length();
				}
				else
				{
					new_element = new TextElement(s.substring(0, begin_index));
					consumed = begin_index;
				}
			}
			else
			{
				int closing_index = s.indexOf(">");
				if (closing_index < 0)
				{
					throw new XmlParseException("Closing bracket not found");
				}
				String element_name = s.substring(1, closing_index);
				// Find matching closing element name
				int closing_pos = s.indexOf("</" + element_name + ">");
				if (closing_pos < 0)
				{
					throw new XmlParseException("Closing element " + element_name + " not found");
				}
				new_element = new XmlElement(element_name);
				String inside = s.substring(closing_index + 1, closing_pos);
				parse(new_element, inside.trim()); 
				consumed = closing_pos + element_name.length() + 3;
			}
			root.addChild(new_element);
			s = s.substring(consumed).trim();
		}
	}
	
	/**
	 * Gets the children of this element
	 * @return The children
	 */
	public /*@NonNull*/ List<XmlElement> getChildren()
	{
		return m_children;
	}
	
	/**
	 * Gets the element's name
	 * @return The name
	 */
	public /*@NonNull*/ String getName()
	{
		return m_name;
	}

	@Override
	public /*@NonNull*/ String toString()
	{
		StringBuilder out = new StringBuilder();
		out.append("<").append(m_name).append(">");
		for (XmlElement e : m_children)
		{
			out.append(e);
		}
		out.append("</").append(m_name).append(">");
		return out.toString();
	}
	
	/**
	 * Exception denoting an error in the parsing of an XML document
	 */
	public static class XmlParseException extends EmptyException
	{
		/**
		 * Dummy UID
		 */
		private static final long serialVersionUID = 1L;

		public XmlParseException(String message)
		{
			super(message);
		}
	}
	
	/**
	 * Gets the child text of a node 
	 * @return The text, or null if no text was found
	 */
	public /*@Nullable*/ TextElement getTextElement()
	{
		for (XmlElement child : m_children)
		{
			if (child instanceof TextElement)
			{
				return (TextElement) child;
			}
		}
		return null;
	}
	
	@Override
	public int hashCode()
	{
		return m_name.hashCode();
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof XmlElement))
		{
			return false;
		}
		XmlElement xe = (XmlElement) o;
		if (m_children.size() != xe.m_children.size())
		{
			return false;
		}
		return m_children.containsAll(xe.m_children);
	}
}
