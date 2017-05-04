package it.diepet.spring.tx.context;

public interface TransactionContext {

	/**
	 * Sets the attribute.
	 *
	 * @param attributeName
	 *            the attribute name
	 * @param attributeValue
	 *            the attribute value
	 */
	void setAttribute(String attributeName, Object attributeValue);

	/**
	 * Adds an item to a list attribute.
	 *
	 * @param attributeName
	 *            the attribute name
	 * @param attributeValue
	 *            the attribute value
	 */
	<T> void addListAttribute(String attributeName, T attributeValue);

	/**
	 * Adds an item to a set attribute.
	 *
	 * @param <T>
	 *            the generic type
	 * @param attributeName
	 *            the attribute name
	 * @param attributeValue
	 *            the attribute value
	 */
	<T> void addSetAttribute(String attributeName, T attributeValue);

	/**
	 * Gets the attribute.
	 *
	 * @param attributeName
	 *            the attribute name
	 * @return the attribute
	 */
	Object getAttribute(String attributeName);

	/**
	 * Removes the attribute.
	 *
	 * @param attributeName
	 *            the attribute name
	 */
	void removeAttribute(String attributeName);

	/**
	 * Gets the transaction context id.
	 *
	 * @return the id
	 */
	String getId();

}
