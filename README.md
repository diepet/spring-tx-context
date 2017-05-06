# spring-tx-context

A Spring Framework plugin for associating a context to a transaction.

During a transaction execution it is possible to add attributes, or rather a set of (key, value) pairs, to the related context.

After the transaction completion (commit succeeds), the transaction context will be published in order to be managed by its consumers.

This plugin could be useful to collect objects to be consumed only after the transaction commit.

Current last version: 0.9.0

# Requisites

* Java 1.6 or higher
* Spring 3.2 or higher
* [spring-tx-eventdispatcher](https://github.com/diepet/spring-tx-eventdispatcher)

# Configuration

* Configure a transaction manager generating events tracking the transaction lifecycle as explained [here](https://github.com/diepet/spring-tx-eventdispatcher).
* Import `META-INF/transaction-context-manager-application-context.xml` Spring configuration file.
* Define a new Spring bean inheriting its configuration from `transactionContextManagerAbstract` abstract bean.

Example:

```xml
	<!-- TX Manager configuration by using spring-tx-eventdispatcher -->		
	<bean id="transactionManager" class="it.diepet.spring.tx.eventdispatcher.EventDispatcherJpaTransactionManager">
		<property name="entityManagerFactory" ref="entityManagerFactory"></property>
	</bean>
	
	<!--import spring-tx-context configuration: none instance will be created in the Spring context-->
	<import resource="classpath:META-INF/transaction-context-manager-application-context.xml"/>
	
	<!-- Creates the transaction context manager to handle a living transaction context -->
	<bean id="transactionContextManager" parent="transactionContextManagerAbstract" />
```

And that's it. After terminated a transaction successfully, its transaction context will be published. More specifically, an application event of this class:

`it.diepet.spring.tx.context.event.TransactionContextEvent`

will be published by using the [Spring application event publisher](http://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/ApplicationEventPublisher.html).

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
		transactionContext.setListAttribure("someIntegerList", 10);
		transactionContext.setListAttribure("someIntegerList", 10);		
		
	}

}