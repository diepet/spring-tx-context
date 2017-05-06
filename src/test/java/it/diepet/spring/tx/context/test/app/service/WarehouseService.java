package it.diepet.spring.tx.context.test.app.service;

import it.diepet.spring.tx.context.test.app.error.ApplicationException;

public interface WarehouseService {

	public void removeProductFromContext();

	public void executeRequiresNewMethod();

	void launchCheckedException() throws ApplicationException;

}
