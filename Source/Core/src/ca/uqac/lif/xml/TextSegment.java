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
}
