package ca.uqac.lif.xml;

import java.util.ArrayList;
import java.util.Collection;

import ca.uqac.lif.xml.XPathExpression.XPathParseException;

class Segment
{
	/**
	 * The predicates (if any) associated to this segment
	 */
	private Collection</*@NonNull*/ Predicate> m_predicates;
	
	/**
	 * The element name corresponding to this segment
	 */
	private String m_elementName = "";
	
	Segment()
	{
		super();
		m_predicates = null;
	}
	
	Segment(/*@NonNull*/ String element_name, /*@NonNull*/ Collection<Predicate> predicates)
	{
		super();
		m_elementName = element_name;
		m_predicates = predicates;
	}
	
	public static /*@NonNull*/ Segment parse(/*@NonNull*/ String s) throws XPathParseException
	{
		String element_name = "";
		int start_index = s.indexOf(Predicate.s_startSymbol);
		if (start_index > 0)
		{
			element_name = s.substring(0, start_index);
			if (element_name.contains(Predicate.s_endSymbol))
			{
				throw new XPathParseException("Element name contains " + Predicate.s_endSymbol);
			}
			s = s.substring(start_index).trim();
		}
		else if (start_index < 0)
		{
			element_name = s;
			if (element_name.contains(Predicate.s_endSymbol))
			{
				throw new XPathParseException("Element name contains " + Predicate.s_endSymbol);
			}
			s = "";
		}
		Collection<Predicate> predicates = new ArrayList<Predicate>();
		while (!s.isEmpty())
		{
			if (!s.startsWith(Predicate.s_startSymbol))
			{
				throw new XPathParseException("Predicate does not start with " + Predicate.s_startSymbol);
			}
			int closing_symbol_index = s.indexOf(Predicate.s_endSymbol);
			if (closing_symbol_index < 0)
			{
				throw new XPathParseException("Predicate does not end with " + Predicate.s_endSymbol);
			}
			String content_string = s.substring(1, closing_symbol_index).trim();
			Predicate pred = Predicate.parse(content_string);
			predicates.add(pred);
			s = s.substring(closing_symbol_index + 1).trim();
		}
		return new Segment(element_name, predicates);
	}
	
	/**
	 * Returns the element name corresponding to this segment
	 * @return The element name
	 */
	public /*@NonNull*/ String getElementName()
	{
		return m_elementName;
	}
	
	/**
	 * Gets the collection of predicates associated to this segment
	 * @return The predicates
	 */
	public /*@NonNull*/ Collection<Predicate> getPredicates()
	{
		return m_predicates;
	}
}
