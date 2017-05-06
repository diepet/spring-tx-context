package it.diepet.spring.tx.context.test.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.diepet.spring.tx.context.TransactionContextManager;
import it.diepet.spring.tx.context.test.app.error.ApplicationException;
import it.diepet.spring.tx.context.test.util.StringCollector;

public class WarehouseServiceImpl implements WarehouseService {

	private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseServiceImpl.class);

	@Autowired
	private TransactionContextManager transactionContextManager;

	@Override
	public void removeProductFromContext() {
		LOGGER.debug("[START] removeProductFromContext()");
		transactionContextManager.getTransactionContext().removeAttribute("operation");
		transactionContextManager.getTransactionContext().removeAttribute("productList");
		transactionContextManager.getTransactionContext().removeAttribute("productSet");
		LOGGER.debug("[END] removeProductFromContext()");
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void executeRequiresNewMethod() {
		LOGGER.debug("[START] executeRequiresNewMethod()");
		StringCollector.add("warehouseService.executeRequiresNewMethod()");
		transactionContextManager.getTransactionContext().setAttribute("operation", "executeRequiresNewMethod");
		LOGGER.debug("[END] executeRequiresNewMethod()");
	}

	@Override
	@Transactional(rollbackFor = ApplicationException.class)
	public void launchCheckedException() throws ApplicationException {
		LOGGER.debug("[START] launchCheckedException()");
		StringCollector.add("warehouseService.launchCheckedException()");
		transactionContextManager.getTransactionContext().setAttribute("operation", "launchCheckedException");
		throw new ApplicationException();
	}

}
