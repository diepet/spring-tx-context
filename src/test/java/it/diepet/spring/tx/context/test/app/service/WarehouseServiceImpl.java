package it.diepet.spring.tx.context.test.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.diepet.spring.tx.context.TransactionContextManager;

public class WarehouseServiceImpl implements WarehouseService {

	private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseServiceImpl.class);

	@Autowired
	private TransactionContextManager transactionContextManager;

	@Override
	public void removeProductFromContext() {
		LOGGER.debug("[START] warehouseService.removeProductFromContext()");
		transactionContextManager.getTransactionContext().removeAttribute("operation");
		transactionContextManager.getTransactionContext().removeAttribute("productList");
		transactionContextManager.getTransactionContext().removeAttribute("productSet");
		LOGGER.debug("[END] warehouseService.removeProductFromContext()");
	}

}
