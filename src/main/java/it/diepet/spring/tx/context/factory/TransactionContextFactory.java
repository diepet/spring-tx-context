package it.diepet.spring.tx.context.factory;

import it.diepet.spring.tx.context.TransactionContext;

/**
 * A factory for creating TransactionContext objects.
 */
public interface TransactionContextFactory {

	TransactionContext createNewInstance();

}
