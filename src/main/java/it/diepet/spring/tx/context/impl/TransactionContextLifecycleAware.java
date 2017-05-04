package it.diepet.spring.tx.context.impl;

/**
 * The Interface TransactionContextLifecycleAware.
 */
interface TransactionContextLifecycleAware {

	/**
	 * Sets the transaction context lifecycle.
	 *
	 * @param transactionContextLifecycle
	 *            the new transaction context lifecycle
	 */
	void setTransactionContextLifecycle(TransactionContextLifecycle transactionContextLifecycle);

}
