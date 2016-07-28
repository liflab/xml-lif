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

public abstract class BinaryPredicate extends Predicate
{
	/**
	 * Left-hand side expression
	 */
	protected String m_left = null;
	
	/**
	 * Right-hand side expression
	 */	
	protected String m_right = null;
	
	/**
	 * Creates an empty binary predicate
	 */
	BinaryPredicate()
	{
		super();
	}
	
	/**
	 * Creates a binary predicate
	 * @param left The left-hand side expression
	 * @param right The right-hand side expression
	 */
	public BinaryPredicate(String left, String right)
	{
		super();
		m_left = left;
		m_right = right;
	}
	
	/**
	 * Gets the value corresponding to the left-hand side of the predicate
	 * @param root The node to evaluate from
	 * @return The value, or null if no value was found
	 */
	protected /*@Nullable*/ TextElement findValue(/*@NonNull*/ XmlElement root)
	{
		XmlElement good_child = null;
		for (XmlElement child : root.getChildren())
		{
			if (m_left.compareTo(child.getName()) == 0)
			{
				good_child = child;
				break;
			}
		}
		if (good_child == null)
		{
			return null;
		}
		return good_child.getTextElement();
	}
	
	/**
	 * Gets the left part of the predicate
	 * @return The part
	 */
	public String getLeft()
	{
		return m_left;
	}
	
	/**
	 * Sets the left part of the predicate
	 * @param s The string
	 */
	public void setLeft(String s)
	{
		m_left = s;
	}
	
	/**
	 * Gets the right part of the predicate
	 * @return The part
	 */
	public String getRight()
	{
		return m_right;
	}
	
	/**
	 * Sets the right part of the predicate
	 * @param s The string
	 */
	public void setRight(String s)
	{
		m_right = s;
	}
}
