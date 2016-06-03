package ca.uqac.lif.xml;

import ca.uqac.lif.xml.XPathExpression.XPathParseException;

public class Equality extends Predicate
{
	/**
	 * The symbol used for equality
	 */
	public static final transient String s_equalSymbol = "=";
	
	private String m_left;
	
	private String m_right;
	
	Equality()
	{
		super();
	}
	
	Equality(String left, String right)
	{
		super();
		m_left = left;
		m_right = right;
	}
	
	public static /*@NonNull*/ Equality parse(/*@NonNull*/ String s) throws XPathParseException
	{
		String[] parts = s.split(s_equalSymbol);
		if (parts.length != 2)
		{
			throw new XPathParseException("Equality must have exactly two members");
		}
		if (parts[0].isEmpty() || parts[1].isEmpty())
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
	public boolean evaluate(XmlElement root)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
