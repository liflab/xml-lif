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

public abstract class Predicate
{
	/**
	 * The symbol indicating the start of a predicate
	 */
	public static transient final String s_startSymbol = "[";
	
	/**
	 * The symbol indicating the end of a predicate
	 */
	public static transient final String s_endSymbol = "]";
	
	public static /*@NonNull*/ Predicate parse(/*@NonNull*/ String s) throws XPathParseException
	{
		if (s.contains(Equality.s_equalSymbol))
		{
			return Equality.parse(s);
		}
		throw new XPathParseException("Could not parse predicate " + s);
	}
	
	public abstract boolean evaluate(/*@NonNull*/ XmlElement root);
	
	/**
	 * Creates a copy of the predicate
	 * @return A copy of the predicate
	 */
	public abstract Predicate duplicate();
}
