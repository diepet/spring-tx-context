package it.diepet.spring.tx.context;

/**
 * The Interface TransactionContextManager.
 */
public interface TransactionContextManager {

	/**
	 * Gets the transaction context.
	 *
	 * @return the transaction context
	 */
	TransactionContext getTransactionContext();

	/**
	 * Checks if is transaction active.
	 *
	 * @return true, if is transaction active
	 */
	boolean isTransactionActive();

}
