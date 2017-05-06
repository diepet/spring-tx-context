package it.diepet.spring.tx.context.test.app.service;

import java.util.List;

import it.diepet.spring.tx.context.test.app.model.Product;

public interface ProductService {

	void add(Product product);

	List<Product> findAll();

	void addTwoProducts(Product p1, Product p2);

	void addProductToContextAndRemoveIt();

	void retrieveContextInANotTransactionalMethod();

	void populateContextListAttributeWrong();

	void populateContextSetAttributeWrong();

	void launchRuntimeException();

	void launchTransactionalMethod();

	void launchNotTransactionalMethod();

}
