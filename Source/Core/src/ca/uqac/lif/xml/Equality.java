package ca.uqac.lif.xml;

import ca.uqac.lif.xml.XPathExpression.XPathParseException;

public class Equality extends BinaryPredicate
{
	/**
	 * The symbol used for equality
	 */
	public static final transient String s_equalSymbol = "=";
	
	Equality()
	{
		super();
	}
	
	Equality(String left, String right)
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
