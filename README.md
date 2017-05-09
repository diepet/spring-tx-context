# spring-tx-context [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A Spring Framework plugin for associating a context to a transaction.

During a transaction execution it is possible to add a set of (attribute key, attribute value) pairs to the related context.

After the transaction completion (commit succeeds), the transaction context will be published in order to be managed by its consumers.

This plugin could be useful to collect objects to be consumed only after the transaction commit.

Current last version: 0.9.0

# Requisites

* Java 1.6 or higher
* Spring 3.2 or higher
* [spring-tx-eventdispatcher](https://github.com/diepet/spring-tx-eventdispatcher)

# Configuration

* Configure a transaction manager as explained [here](https://github.com/diepet/spring-tx-eventdispatcher), in order to dispatch transaction lifecycle events.
* Import `META-INF/context-tx-spring-application-context.xml` Spring configuration file.
* Define a new Spring bean inheriting its configuration from `transactionContextManagerAbstract` abstract bean.

Example:

```xml
	<!-- TX Manager configuration by using spring-tx-eventdispatcher -->		
	<bean id="transactionManager" class="it.diepet.spring.tx.eventdispatcher.EventDispatcherJpaTransactionManager">
		<property name="entityManagerFactory" ref="entityManagerFactory"></property>
	</bean>
	
	<!--import spring-tx-context configuration: none instance will be created in the Spring context-->
	<import resource="classpath:META-INF/context-tx-spring-application-context.xml"/>
	
	<!-- Creates the transaction context manager to handle a living transaction context -->
	<bean id="transactionContextManager" parent="transactionContextManagerAbstract" />
```

And that's it. A transaction context will be created for each transaction, and this context will be published when the transaction terminates successfully (after the transaction commit).

# Usage

Inside a transactional method, it is possible to retrieve its transaction context by using the transaction context manager.

```Java
class MyServiceImpl implements MyService {

	@Autowired
	private TransactionContextManager transactionContextManager;

	@Transactional
	void transactionalMethod() { 
		// Retrieves the current transaction context (
		TransactionContext transactionContext = transactionContextManager.getTransactionContext();
		// some db stuff
		....
		// add an attribute having key "hello" and value "world"
		transactionContext.setAttribute("hello", "world");
		
		// add more values to a list stored in an attribute named "someIntegerList"
		transactionContext.addListAttribute("someIntegerList", 10);
		transactionContext.addListAttribute("someIntegerList", 20);
		transactionContext.addListAttribute("someIntegerList", 30);
		
		// add more values to a set stored in an attribute named "someStringSet"
		transactionContext.addSetAttribute("someStringSet", "something1");		
		transactionContext.addSetAttribute("someStringSet", "something1");	
	}

}
```

When a transaction will be terminated successfully, the transaction context will be published. 

More specifically, an application event of this class:

`it.diepet.spring.tx.context.event.TransactionContextEvent`

will be published by using the [Spring application event publisher](http://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/ApplicationEventPublisher.html).

An example of a transaction context consumer for the previous sample code:

```Java

/* ... */
import org.springframework.context.ApplicationListener;
/* ... */

public class SomeTransactionContextListener implements ApplicationListener<TransactionContextEvent> {

	@Override
	public void onApplicationEvent(TransactionContextEvent event) {
		// gets the transaction context published
		TransactionContext transactionContext = event.getTransactionContext();
		
		// stores "world"
		String helloAttribute = (String) transactionContext.getAttribute("hello"); 
		
		// stores a list of integers containing: 10, 20, 30
		List<Integer> someIntegerListAttribute = 
			(List<Integer>) transactionContext.getAttribute("someIntegerList");
		
		// stores a set of strings containing: "something1" and "something2"
		Set<String> someStringSetAttribute = 
			(Set<String>) transactionContext.getAttribute("someStringSet");
		
		/* ... */

	}

}

```

If a transaction will fail (for a rollback or a runtime error), **its transaction context will be destroyed and it will not be published**.

# Furthermore notes

If the transaction context manager is used outside a transaction, the transaction context returned will be a null-safe implementation where the capabilities for adding new attributes will not have any effect and the capabilities for reading an attribute will return null.

```Java
class MyServiceImpl implements MyService {

	@Autowired
	private TransactionContextManager transactionContextManager;

	void notTransactionalMethod() { 
		// Retrieves the current transaction context (
		TransactionContext transactionContext = transactionContextManager.getTransactionContext();

		// no real attribute will be set because the executing method is not transactional
		transactionContext.setAttribute("hello", "world");
		
		// no real attribute will be set because the executing method is not transactional
		transactionContext.addListAttribute("someIntegerList", 10);
		transactionContext.addListAttribute("someIntegerList", 20);
		transactionContext.addListAttribute("someIntegerList", 30);
		
		// no real attribute will be set because the executing method is not transactional
		transactionContext.addSetAttribute("someStringSet", "something1");		
		transactionContext.addSetAttribute("someStringSet", "something1");	
		
		// null will be returned because the executing method is not transactional
		String helloAttribute = (String) transactionContext.getAttribute("hello"); 
		
		// null will be returned because the executing method is not transactional
		List<Integer> someIntegerListAttribute = 
			(List<Integer>) transactionContext.getAttribute("someIntegerList");
		
		// null will be returned because the executing method is not transactional
		Set<String> someStringSetAttribute = 
			(Set<String>) transactionContext.getAttribute("someStringSet");
	}

}
```

# Advanced Topics

The default implementation of a transaction context is:

`it.diepet.spring.tx.context.impl.DefaultTransactionContextImpl`

that implements the interface:

`it.diepet.spring.tx.context.TransactionContext`.

The default implementation stores attributes in a simple `java.util.HashMap<String, Object>`.

A custom implementation of the above interface could be used by implementing a new custom factory for the new transaction context implementation:

```Java

import it.diepet.spring.tx.context.TransactionContext;
import it.diepet.spring.tx.context.factory.TransactionContextFactory

class MyCustomTransactionContextFactoryImpl implements TransactionContextFactory {

	@Override
	public TransactionContext createNewInstance() {
		// creates an instance of a custom implementation of 
		// the it.diepet.spring.tx.context.TransactionContext interface
		return new MyCustomTransactionContextImpl();
	}

}
```

and injecting the custom transaction context factory in the transaction context manager:

```xml
	
	<!-- Creates the transaction context manager to handle a living transaction context -->
	<bean id="transactionContextManager" parent="transactionContextManagerAbstract">
		<property name="transactionContextFactory" ref="myCustomTransactionContextFactory" />
	</bean>
	
	<!-- Creates a custom transaction context factory -->
	<bean id="myCustomTransactionContextFactory" parent="some.package.MyCustomTransactionContextFactoryImpl" />
```
# License

This project is licensed under the terms of the MIT license.
