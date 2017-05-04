package it.diepet.spring.tx.context.impl;

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;

import it.diepet.spring.tx.context.TransactionContext;
import it.diepet.spring.tx.context.TransactionContextManager;
import it.diepet.spring.tx.context.event.TransactionContextEvent;
import it.diepet.spring.tx.context.factory.TransactionContextFactory;
import it.diepet.spring.tx.eventdispatcher.event.BeginTransactionEvent;
import it.diepet.spring.tx.eventdispatcher.event.CommitTransactionEvent;
import it.diepet.spring.tx.eventdispatcher.event.RollbackTransactionEvent;
import it.diepet.spring.tx.eventdispatcher.event.failure.TransactionErrorEvent;

/**
 * The Class DefaultTransactionContextManagerImpl.
 */
public class DefaultTransactionContextManagerImpl implements TransactionContextManager, TransactionContextLifecycle {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTransactionContextManagerImpl.class);

	/** The Constant NULL_TRANSACTION_CONTEXT. */
	private static final TransactionContext NULL_TRANSACTION_CONTEXT = new NullTransactionContextImpl();

	/** The thread local tx context stack. */
	private ThreadLocal<Stack<TransactionContext>> threadLocalTxContextStack = new ThreadLocal<Stack<TransactionContext>>();

	/** The transaction context factory. */
	private TransactionContextFactory transactionContextFactory = new DefaultTransactionContextFactoryImpl();

	/** The begin event listener. */
	private BeginTransactionEventListener beginEventListener;

	/** The commit event listener. */
	private CommitTransactionEventListener commitEventListener;

	/** The rollback event listener. */
	private RollbackTransactionEventListener rollbackEventListener;

	/** The transaction error event listener. */
	private TransactionErrorEventListener transactionErrorEventListener;

	/**
	 * BeginTransactionEventListener: creates a new transaction context.
	 */
	public static class BeginTransactionEventListener extends TransactionContextLifecycleAwareAdapter
			implements ApplicationListener<BeginTransactionEvent> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.springframework.context.ApplicationListener#onApplicationEvent(
		 * org.springframework.context.ApplicationEvent)
		 */
		@Override
		public void onApplicationEvent(final BeginTransactionEvent beginTransactionEvent) {
			LOGGER.debug("[START] onApplicationEvent(BeginTransactionEvent)");
			// transaction began: create its context from the manager instance
			transactionContextLifecycle.createTransactionContext();
			LOGGER.debug("[END] onApplicationEvent(BeginTransactionEvent)");
		}

	}

	/**
	 * CommitTransactionEventListener: creates a new transaction context.
	 */
	public static class CommitTransactionEventListener extends TransactionContextLifecycleAwareAdapter
			implements ApplicationListener<CommitTransactionEvent>, ApplicationEventPublisherAware {

		/** The application event publisher. */
		private ApplicationEventPublisher applicationEventPublisher;

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.springframework.context.ApplicationListener#onApplicationEvent(
		 * org.springframework.context.ApplicationEvent)
		 */
		@Override
		public void onApplicationEvent(final CommitTransactionEvent commitTransactionEvent) {
			LOGGER.debug("[START] onApplicationEvent(CommitTransactionEvent)");
			// store current transaction context instance
			final TransactionContext transactionContext = transactionContextLifecycle.getTransactionContext();
			// transaction completed at commit: destroy its context from the
			// manager
			// instance
			transactionContextLifecycle.destroyTransactionContext();
			// transaction committed: publish an event having its context in
			// order
			// to consume it
			applicationEventPublisher.publishEvent(new TransactionContextEvent(transactionContext));
			LOGGER.debug("[END] onApplicationEvent(CommitTransactionEvent)");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.springframework.context.ApplicationEventPublisherAware#
		 * setApplicationEventPublisher(org.springframework.context.
		 * ApplicationEventPublisher)
		 */
		@Override
		public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
			this.applicationEventPublisher = applicationEventPublisher;
		}

	}

	/**
	 * TransactionErrorEventListener: creates a new transaction context.
	 */
	@SuppressWarnings("rawtypes")
	public static class TransactionErrorEventListener extends TransactionContextLifecycleAwareAdapter
			implements ApplicationListener<TransactionErrorEvent> {

		@Override
		public void onApplicationEvent(final TransactionErrorEvent transactionErrorEvent) {
			LOGGER.debug("[START] onApplicationEvent(TransactionErrorEvent)");
			// transaction rollbacked: destroy its context from the manager
			// instance
			transactionContextLifecycle.destroyTransactionContext();
			LOGGER.debug("[END] onApplicationEvent(TransactionErrorEvent)");
		}

	}

	/**
	 * RollbackTransactionEventListener: creates a new transaction context.
	 */
	public static class RollbackTransactionEventListener extends TransactionContextLifecycleAwareAdapter
			implements ApplicationListener<RollbackTransactionEvent> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.springframework.context.ApplicationListener#onApplicationEvent(
		 * org.springframework.context.ApplicationEvent)
		 */
		@Override
		public void onApplicationEvent(final RollbackTransactionEvent rollbackTransactionEvent) {
			LOGGER.debug("[START] onApplicationEvent(RollbackTransactionEvent)");
			// transaction rollbacked: destroy its context from the manager
			// instance
			transactionContextLifecycle.destroyTransactionContext();
			LOGGER.debug("[END] onApplicationEvent(RollbackTransactionEvent)");
		}

	}

	/**
	 * Creates the transaction context.
	 *
	 * @return the transaction context
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.diepet.spring.tx.context.TransactionContextManager#
	 * createTransactionContext()
	 */
	@Override
	public TransactionContext createTransactionContext() {
		final Stack<TransactionContext> stack = getThreadLocalTxContextStack();
		final TransactionContext transactionContext = transactionContextFactory.createNewInstance();
		stack.push(transactionContext);
		LOGGER.debug("Created transaction context having ID {} [thread stack size: {}]", transactionContext.getId(),
				stack.size());
		return transactionContext;
	}

	/**
	 * Gets the transaction context.
	 *
	 * @return the transaction context
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.diepet.spring.tx.context.TransactionContextManager#
	 * getTransactionContext ()
	 */
	@Override
	public TransactionContext getTransactionContext() {
		final Stack<TransactionContext> stack = getThreadLocalTxContextStack();
		if (stack.isEmpty()) {
			return NULL_TRANSACTION_CONTEXT;
		}
		return stack.peek();
	}

	/**
	 * Destroy transaction context.
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.diepet.spring.tx.context.TransactionContextManager#
	 * destroyTransactionContext()
	 */
	@Override
	public void destroyTransactionContext() {
		final Stack<TransactionContext> stack = getThreadLocalTxContextStack();
		if (!stack.isEmpty()) {
			final TransactionContext transactionContext = stack.pop();
			LOGGER.debug("Destroyed transaction context having ID {} [thread stack size: {}]",
					transactionContext.getId(), stack.size());
		}
	}

	/**
	 * Checks if is transaction active.
	 *
	 * @return true, if is transaction active
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.diepet.spring.tx.context.TransactionContextManager#
	 * isTransactionActive ()
	 */
	@Override
	public boolean isTransactionActive() {
		final Stack<TransactionContext> stack = getThreadLocalTxContextStack();
		return !stack.isEmpty();
	}

	/**
	 * Inits the transaction listeners.
	 */
	public void init() {
		this.beginEventListener.setTransactionContextLifecycle(this);
		this.commitEventListener.setTransactionContextLifecycle(this);
		this.rollbackEventListener.setTransactionContextLifecycle(this);
		this.transactionErrorEventListener.setTransactionContextLifecycle(this);
	}

	/**
	 * Sets the transaction context factory.
	 *
	 * @param transactionContextFactory
	 *            the new transaction context factory
	 */
	public void setTransactionContextFactory(TransactionContextFactory transactionContextFactory) {
		this.transactionContextFactory = transactionContextFactory;
	}

	/**
	 * Sets the begin event listener.
	 *
	 * @param beginEventListener
	 *            the new begin event listener
	 */
	public void setBeginEventListener(final BeginTransactionEventListener beginEventListener) {
		this.beginEventListener = beginEventListener;
	}

	/**
	 * Sets the commit event listener.
	 *
	 * @param commitEventListener
	 *            the new commit event listener
	 */
	public void setCommitEventListener(final CommitTransactionEventListener commitEventListener) {
		this.commitEventListener = commitEventListener;
	}

	/**
	 * Sets the rollback event listener.
	 *
	 * @param rollbackEventListener
	 *            the new rollback event listener
	 */
	public void setRollbackEventListener(final RollbackTransactionEventListener rollbackEventListener) {
		this.rollbackEventListener = rollbackEventListener;
	}

	/**
	 * Sets the transaction error event listener.
	 *
	 * @param transactionErrorEventListener
	 *            the new transaction error event listener
	 */
	public void setTransactionErrorEventListener(final TransactionErrorEventListener transactionErrorEventListener) {
		this.transactionErrorEventListener = transactionErrorEventListener;
	}

	/**
	 * Gets the thread local tx context stack.
	 *
	 * @return the thread local tx context stack
	 */
	private Stack<TransactionContext> getThreadLocalTxContextStack() {
		Stack<TransactionContext> stack = this.threadLocalTxContextStack.get();
		if (stack == null) {
			stack = new Stack<TransactionContext>();
			this.threadLocalTxContextStack.set(stack);
		}
		return stack;
	}

}
