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

public class TextSegment extends Segment
{
	/**
	 * The expression used to denote a text segment
	 */
	public static transient final String s_expression = "text()";
	
	@Override
	public String toString()
	{
		return s_expression;
	}
	
	@Override
	public TextSegment duplicate()
	{
		return new TextSegment();
	}
}
