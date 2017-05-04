package it.diepet.spring.tx.context.event;

import org.springframework.context.ApplicationEvent;

import it.diepet.spring.tx.context.TransactionContext;

/**
 * The Class TransactionContextEvent.
 */
public class TransactionContextEvent extends ApplicationEvent {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 740380142532318313L;

	/**
	 * Instantiates a new transaction context event.
	 *
	 * @param source
	 *            the source
	 */
	public TransactionContextEvent(TransactionContext source) {
		super(source);
	}

	/**
	 * Gets the transaction context.
	 *
	 * @return the transaction context
	 */
	public TransactionContext getTransactionContext() {
		return (TransactionContext) this.getSource();
	}

}
