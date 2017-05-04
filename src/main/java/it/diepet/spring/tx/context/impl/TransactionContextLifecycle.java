package it.diepet.spring.tx.context.impl;

import it.diepet.spring.tx.context.TransactionContext;
import it.diepet.spring.tx.context.TransactionContextManager;

/**
 * The Interface TransactionContextLifecycle.
 */
interface TransactionContextLifecycle extends TransactionContextManager {

	/**
	 * Creates the transaction context.
	 *
	 * @return the transaction context
	 */
	public TransactionContext createTransactionContext();

	/**
	 * Destroy transaction context.
	 */
	public void destroyTransactionContext();
}
