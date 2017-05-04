package it.diepet.spring.tx.context;

/**
 * The Interface TransactionContextManager.
 */
public interface TransactionContextManager {

	/**
	 * Creates the transaction context.
	 *
	 * @return the transaction context
	 */
	TransactionContext createTransactionContext();

	/**
	 * Gets the transaction context.
	 *
	 * @return the transaction context
	 */
	TransactionContext getTransactionContext();

	/**
	 * Destroy transaction context.
	 */
	void destroyTransactionContext();

	/**
	 * Checks if is transaction active.
	 *
	 * @return true, if is transaction active
	 */
	boolean isTransactionActive();

}
