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

public class EmptyException extends Exception
{
	private final String m_message;
	
	/**
	 * Dummy UID
	 */
	private static final long serialVersionUID = 1L;
	
	public EmptyException(String message)
	{
		super();
		m_message = message;
	}
	
	@Override
	public String getMessage()
	{
		return m_message;
	}

}
