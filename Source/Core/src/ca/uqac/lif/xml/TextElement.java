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
}
