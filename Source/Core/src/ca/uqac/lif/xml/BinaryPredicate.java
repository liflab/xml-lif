package ca.uqac.lif.xml;

public abstract class BinaryPredicate extends Predicate
{
	/**
	 * Left-hand side expression
	 */
	protected String m_left = null;
	
	/**
	 * Right-hand side expression
	 */	
	protected String m_right = null;
	
	/**
	 * Creates an empty binary predicate
	 */
	BinaryPredicate()
	{
		super();
	}
	
	/**
	 * Creates a binary predicate
	 * @param left The left-hand side expression
	 * @param right The right-hand side expression
	 */
	public BinaryPredicate(String left, String right)
	{
		super();
		m_left = left;
		m_right = right;
	}
	
	/**
	 * Gets the value corresponding to the left-hand side of the predicate
	 * @param root The node to evaluate from
	 * @return The value, or null if no value was found
	 */
	protected /*@Nullable*/ TextElement findValue(/*@NonNull*/ XmlElement root)
	{
		XmlElement good_child = null;
		for (XmlElement child : root.getChildren())
		{
			if (m_left.compareTo(child.getName()) == 0)
			{
				good_child = child;
				break;
			}
		}
		if (good_child == null)
		{
			return null;
		}
		return good_child.getTextElement();
	}
}
