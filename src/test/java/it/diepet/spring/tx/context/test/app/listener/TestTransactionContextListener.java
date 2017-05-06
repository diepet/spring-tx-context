package it.diepet.spring.tx.context.test.app.listener;

import java.util.List;
import java.util.Set;

import org.springframework.context.ApplicationListener;

import it.diepet.spring.tx.context.TransactionContext;
import it.diepet.spring.tx.context.event.TransactionContextEvent;
import it.diepet.spring.tx.context.test.app.model.Product;
import it.diepet.spring.tx.context.test.util.StringCollector;

public class TestTransactionContextListener implements ApplicationListener<TransactionContextEvent> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.context.ApplicationListener#onApplicationEvent(org.
	 * springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(TransactionContextEvent event) {
		TransactionContext transactionContext = event.getTransactionContext();
		// add tx context id
		StringCollector.add("Transaction Context ID: " + transactionContext.getId());
		StringCollector.add("Operation: " + transactionContext.getAttribute("operation"));
		// add product list
		List<Product> productList = (List<Product>) transactionContext.getAttribute("productList");
		if (productList != null) {
			for (Product product : productList) {
				StringCollector.add("Added product to list: " + product.getDescription());
			}
		}
		// add result list size set
		Set<Product> productSet = (Set<Product>) transactionContext.getAttribute("productSet");
		if (productSet != null) {
			StringCollector.add("Product set size " + productSet.size());
		}

	}

}
