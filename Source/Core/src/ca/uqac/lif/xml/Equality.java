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

import ca.uqac.lif.xml.XPathExpression.XPathParseException;

public class Equality extends BinaryPredicate
{
	/**
	 * The symbol used for equality
	 */
	public static final transient String s_equalSymbol = "=";
	
	public Equality()
	{
		super();
	}
	
	public Equality(String left, String right)
	{
		super(left, right);
	}
	
	public static /*@NonNull*/ Equality parse(/*@NonNull*/ String s) throws XPathParseException
	{
		String[] parts = s.split(s_equalSymbol);
		if (parts.length != 2)
		{
			throw new XPathParseException("Equality must have exactly two members");
		}
		if (parts[0].isEmpty())
		{
			throw new XPathParseException("Equality must have two non-empty members");
		}
		return new Equality(parts[0], parts[1]);
	}
	
	@Override
	public String toString()
	{
		return "[" + m_left + "=" + m_right + "]";
	}
	
	@Override
	public Equality duplicate()
	{
		return new Equality(m_left, m_right);
	}
	
	@Override
	public boolean evaluate(/*@NonNull*/ XmlElement root)
	{
		TextElement el = findValue(root);
		if (el == null)
		{
			return false;
		}
		return m_right.compareTo(el.getText()) == 0;
	}

}
