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
	<!-- TX Configuration -->		
	<bean id="transactionManager" class="it.diepet.spring.tx.eventdispatcher.EventDispatcherJpaTransactionManager">
		<property name="entityManagerFactory" ref="entityManagerFactory"></property>
	</bean>
	
	<!--import spring-tx-context configuration: no any instance will be created in the Spring context-->
	<import resource="classpath:META-INF/transaction-context-manager-application-context.xml"/>
	
	<!-- Creates the transaction context manager to handle a living transaction context -->
	<bean id="transactionContextManager" parent="transactionContextManagerAbstract" />
```







