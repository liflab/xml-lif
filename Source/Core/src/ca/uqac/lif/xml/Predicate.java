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
}
