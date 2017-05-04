package it.diepet.spring.tx.context.impl;

import it.diepet.spring.tx.context.TransactionContext;

/**
 * An empty implementation of TransactionContext for avoiding
 * NullPointerException(s).
 */
public class NullTransactionContextImpl implements TransactionContext {

	/**
	 * Instantiates a new default transaction context impl.
	 */
	NullTransactionContextImpl() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.diepet.spring.tx.context.TransactionContext#setAttribute(java.lang
	 * .String, java.lang.Object)
	 */
	@Override
	public void setAttribute(String attributeName, Object attributeValue) {
		// EMPTY IMPLEMENTATION
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.diepet.spring.tx.context.TransactionContext#getAttribute(java.lang
	 * .String)
	 */
	@Override
	public Object getAttribute(String attributeName) {
		// EMPTY IMPLEMENTATION
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.diepet.spring.tx.context.TransactionContext#getId()
	 */
	@Override
	public String getId() {
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.diepet.spring.tx.context.TransactionContext#addListAttribute(java
	 * .lang.String, java.lang.Object)
	 */
	@Override
	public <T> void addListAttribute(String attributeName, T attributeValue) {
		// EMPTY IMPLEMENTATION
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diepet.spring.tx.context.TransactionContext#addSetAttribute(java.lang.
	 * String, java.lang.Object)
	 */
	@Override
	public <T> void addSetAttribute(String attributeName, T attributeValue) {
		// EMPTY IMPLEMENTATION
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.diepet.spring.tx.context.TransactionContext#removeAttribute(java.
	 * lang.String)
	 */
	@Override
	public void removeAttribute(String attributeName) {
		// EMPTY IMPLEMENTATION
	}

}
