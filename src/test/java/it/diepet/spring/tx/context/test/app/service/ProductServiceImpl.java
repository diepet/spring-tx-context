package it.diepet.spring.tx.context.test.app.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import it.diepet.spring.tx.context.TransactionContextManager;
import it.diepet.spring.tx.context.test.app.dao.ProductDAO;
import it.diepet.spring.tx.context.test.app.error.ApplicationException;
import it.diepet.spring.tx.context.test.app.error.ApplicationRuntimeException;
import it.diepet.spring.tx.context.test.app.model.Product;
import it.diepet.spring.tx.context.test.util.StringCollector;

public class ProductServiceImpl implements ProductService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Autowired
	private ProductDAO productDAO;

	@Autowired
	private WarehouseService warehouseService;

	@Autowired
	private TransactionContextManager transactionContextManager;

	@Override
	@Transactional
	public void add(Product product) {
		LOGGER.debug("[START] add()");
		StringCollector.add("productService.add()");
		productDAO.create(product);
		transactionContextManager.getTransactionContext().setAttribute("operation", "add");
		transactionContextManager.getTransactionContext().addListAttribute("productList", product);
		transactionContextManager.getTransactionContext().addSetAttribute("productSet", product);
		LOGGER.debug("[END] add()");
	}

	@Override
	@Transactional
	public List<Product> findAll() {
		LOGGER.debug("[START] findAll()");
		StringCollector.add("productService.findAll()");
		List<Product> result = productDAO.findAll();
		transactionContextManager.getTransactionContext().setAttribute("operation", "findAll");
		LOGGER.debug("[END] findAll()");
		return result;
	}

	@Override
	@Transactional
	public void addTwoProducts(Product p1, Product p2) {
		LOGGER.debug("[START] addTwoProducts()");
		StringCollector.add("productService.addTwoProducts()");
		transactionContextManager.getTransactionContext().setAttribute("operation", "addTwoProducts");
		transactionContextManager.getTransactionContext().addListAttribute("productList", p1);
		transactionContextManager.getTransactionContext().addListAttribute("productList", p2);
		transactionContextManager.getTransactionContext().addSetAttribute("productSet", p1);
		transactionContextManager.getTransactionContext().addSetAttribute("productSet", p2);
		LOGGER.debug("[END] addTwoProducts()");
	}

	@Override
	@Transactional
	public void addProductToContextAndRemoveIt() {
		LOGGER.debug("[START] addProductToContextAndRemoveIt()");
		StringCollector.add("productService.addProductToContextAndRemoveIt()");
		Product p = new Product();
		p.setId(-1l);
		p.setCode("SOME_CODE");
		p.setDescription("SOME_DESCRIPTION");
		transactionContextManager.getTransactionContext().setAttribute("operation", "addSomeAttributesAndRemoveThem");
		transactionContextManager.getTransactionContext().addListAttribute("productList", p);
		transactionContextManager.getTransactionContext().addSetAttribute("productSet", p);
		warehouseService.removeProductFromContext();
		LOGGER.debug("[END] addProductToContextAndRemoveIt()");
	}

	@Override
	public void retrieveContextInANotTransactionalMethod() {
		LOGGER.debug("[START] retrieveContextInANotTransactionalMethod()");
		StringCollector.add("productService.retrieveContextInANotTransactionalMethod()");
		Product p = new Product();
		p.setId(-1l);
		p.setCode("SOME_CODE");
		p.setDescription("SOME_DESCRIPTION");
		transactionContextManager.getTransactionContext().setAttribute("operation", "addSomeAttributesAndRemoveThem");
		transactionContextManager.getTransactionContext().addListAttribute("productList", p);
		transactionContextManager.getTransactionContext().addSetAttribute("productSet", p);
		StringCollector.add("Transaction Context ID: " + transactionContextManager.getTransactionContext().getId());
		StringCollector
				.add("Operation: " + transactionContextManager.getTransactionContext().getAttribute("operation"));
		StringCollector
				.add("Product List: " + transactionContextManager.getTransactionContext().getAttribute("productList"));
		StringCollector
				.add("Product Set: " + transactionContextManager.getTransactionContext().getAttribute("productSet"));
		transactionContextManager.getTransactionContext().removeAttribute("operation");
		transactionContextManager.getTransactionContext().removeAttribute("productList");
		transactionContextManager.getTransactionContext().removeAttribute("productSet");
		StringCollector
				.add("Operation: " + transactionContextManager.getTransactionContext().getAttribute("operation"));
		StringCollector
				.add("Product List: " + transactionContextManager.getTransactionContext().getAttribute("productList"));
		StringCollector
				.add("Product Set: " + transactionContextManager.getTransactionContext().getAttribute("productSet"));
		LOGGER.debug("[END] retrieveContextInANotTransactionalMethod()");
	}

	@Override
	@Transactional
	public void populateContextListAttributeWrong() {
		LOGGER.debug("[START] populateContextListAttributeWrong()");
		transactionContextManager.getTransactionContext().setAttribute("operation", "addSomeAttributesAndRemoveThem");
		transactionContextManager.getTransactionContext().addListAttribute("operation", "something");
		LOGGER.debug("[END] populateContextListAttributeWrong()");
	}

	@Override
	@Transactional
	public void populateContextSetAttributeWrong() {
		LOGGER.debug("[START] populateContextListAttributeWrong()");
		transactionContextManager.getTransactionContext().setAttribute("operation", "addSomeAttributesAndRemoveThem");
		transactionContextManager.getTransactionContext().addSetAttribute("operation", "something");
		LOGGER.debug("[END] populateContextListAttributeWrong()");
	}

	@Override
	@Transactional
	public void launchRuntimeException() {
		LOGGER.debug("[START] launchRuntimeException()");
		StringCollector.add("productService.launchRuntimeException()");
		transactionContextManager.getTransactionContext().setAttribute("operation", "addSomeAttributesAndRemoveThem");
		Product p = new Product();
		p.setId(-1l);
		p.setCode("SOME_CODE");
		p.setDescription("SOME_DESCRIPTION");
		transactionContextManager.getTransactionContext().setAttribute("operation", "addSomeAttributesAndRemoveThem");
		transactionContextManager.getTransactionContext().addListAttribute("productList", p);
		transactionContextManager.getTransactionContext().addSetAttribute("productSet", p);
		throw new ApplicationRuntimeException();
	}

	@Override
	@Transactional
	public void launchTransactionalMethod() {
		LOGGER.debug("[START] launchTransactionalMethod()");
		StringCollector.add("Transaction Active: " + transactionContextManager.isTransactionActive());
		transactionContextManager.getTransactionContext().setAttribute("operation", "launchTransactionalMethod");
		LOGGER.debug("[END] launchTransactionalMethod()");
	}

	@Override
	public void launchNotTransactionalMethod() {
		LOGGER.debug("[START] launchNotTransactionalMethod()");
		StringCollector.add("Transaction Active: " + transactionContextManager.isTransactionActive());
		LOGGER.debug("[END] launchNotTransactionalMethod()");
	}

	@Override
	@Transactional
	public void launchRequiresNewMethod() {
		LOGGER.debug("[START] launchRequiresNewMethod()");
		StringCollector.add("productService.launchRequiresNewMethod()");
		transactionContextManager.getTransactionContext().setAttribute("operation", "launchRequiresNewMethod");
		warehouseService.executeRequiresNewMethod();
		LOGGER.debug("[END] launchRequiresNewMethod()");
	}

	@Override
	@Transactional
	public void launchMethodFailingWhenCommits() {
		LOGGER.debug("[START] launchMethodFailingWhenCommits()");
		StringCollector.add("productService.launchMethodFailingWhenCommits()");
		transactionContextManager.getTransactionContext().setAttribute("operation", "launchMethodFailingWhenCommits");
		try {
			warehouseService.launchCheckedException();
		} catch (ApplicationException e) {
			StringCollector.add("Thrown ApplicationException");
		}
		LOGGER.debug("[END] launchMethodFailingWhenCommits()");
	}

}
