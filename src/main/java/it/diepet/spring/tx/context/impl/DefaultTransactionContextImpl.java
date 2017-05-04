package it.diepet.spring.tx.context.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import it.diepet.spring.tx.context.TransactionContext;

/**
 * The Class TransactionContextImpl.
 */
public class DefaultTransactionContextImpl implements TransactionContext {

	/** The context map. */
	private Map<String, Object> contextMap = new HashMap<String, Object>();

	/** The id. */
	private String id;

	/**
	 * Instantiates a new default transaction context impl.
	 */
	DefaultTransactionContextImpl() {
		super();
		this.id = UUID.randomUUID().toString();
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
		this.contextMap.put(attributeName, attributeValue);
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
		return this.contextMap.get(attributeName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.diepet.spring.tx.context.TransactionContext#getId()
	 */
	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public void removeAttribute(String attributeName) {
		this.contextMap.remove(attributeName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.diepet.spring.tx.context.TransactionContext#addListAttribute(java
	 * .lang.String, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> void addListAttribute(String attributeName, T attributeValue) {
		Object contextAttributeValue = this.contextMap.get(attributeName);
		List<T> attributeValueList = null;
		if (contextAttributeValue == null) {
			attributeValueList = new LinkedList<T>();
			this.contextMap.put(attributeName, attributeValueList);
		} else if (contextAttributeValue instanceof List) {
			attributeValueList = (List<T>) contextAttributeValue;
		} else {
			throw new UnsupportedOperationException();
		}
		attributeValueList.add(attributeValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diepet.spring.tx.context.TransactionContext#addSetAttribute(java.lang.
	 * String, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> void addSetAttribute(String attributeName, T attributeValue) {
		Object contextAttributeValue = this.contextMap.get(attributeName);
		Set<T> attributeValueSet = null;
		if (contextAttributeValue == null) {
			attributeValueSet = new HashSet<T>();
			this.contextMap.put(attributeName, attributeValueSet);
		} else if (contextAttributeValue instanceof Set) {
			attributeValueSet = (Set<T>) contextAttributeValue;
		} else {
			throw new UnsupportedOperationException();
		}
		attributeValueSet.add(attributeValue);
	}

}
