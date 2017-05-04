package it.diepet.spring.tx.context.impl;

/**
 * The Class TransactionContextLifecycleAwareAdapter.
 */
class TransactionContextLifecycleAwareAdapter implements TransactionContextLifecycleAware {

	/** The transaction context lifecycle. */
	protected TransactionContextLifecycle transactionContextLifecycle;

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diepet.spring.tx.context.impl.TransactionContextLifecycleAware#
	 * setTransactionContextLifecycle(it.diepet.spring.tx.context.impl.
	 * TransactionContextLifecycle)
	 */
	@Override
	public void setTransactionContextLifecycle(TransactionContextLifecycle transactionContextLifecycle) {
		this.transactionContextLifecycle = transactionContextLifecycle;
	}

}
