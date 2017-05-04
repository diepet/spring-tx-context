package it.diepet.spring.tx.context.test.app.dao;

import it.diepet.spring.tx.context.test.app.model.Product;
import it.diepet.spring.tx.eventdispatcher.test.app.dao.AbstractJpaDAO;

public class ProductDAO extends AbstractJpaDAO<Product> {
	public ProductDAO() {
		setClazz(Product.class);
	}
}
