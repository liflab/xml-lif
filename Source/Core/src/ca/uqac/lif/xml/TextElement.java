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

public class TextElement extends XmlElement
{
	/**
	 * The text for this element
	 */
	private String m_text;
	
	public TextElement()
	{
		this("");
	}
	
	public TextElement(String s)
	{
		super("CDATA");
		m_text = s;
	}
	
	@Override
	public String toString()
	{
		return m_text;
	}
	
	/**
	 * Gets the text of this element
	 * @return The text
	 */
	public /*@NotNull*/ String getText()
	{
		return m_text;
	}
	
	@Override
	public int hashCode()
	{
		return m_text.hashCode();
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof TextElement))
		{
			return false;
		}
		return m_text.compareTo(((TextElement) o).m_text) == 0;
	}
	
	@Override
	public TextElement getTextElement()
	{
		return this;
	}
}
