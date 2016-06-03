package ca.uqac.lif.xml;

public class XmlParseException extends Exception
{
	private final String m_message;
	
	/**
	 * Dummy UID
	 */
	private static final long serialVersionUID = 1L;
	
	public XmlParseException(String message)
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
