package it.diepet.spring.tx.context.impl;

import it.diepet.spring.tx.context.TransactionContext;
import it.diepet.spring.tx.context.factory.TransactionContextFactory;

/**
 * The Class TransactionContextFactoryImpl.
 */
public class DefaultTransactionContextFactoryImpl implements TransactionContextFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diepet.spring.tx.context.TransactionContextFactory#createNewInstance()
	 */
	@Override
	public TransactionContext createNewInstance() {
		return new DefaultTransactionContextImpl();
	}

}
