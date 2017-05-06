# spring-tx-context

A Spring Framework plugin for associating a context to a transaction.

During a transaction execution it is possible to add attributes to the related context.

After the transaction completion (commit succeeds), the transaction context will be published in order to be managed by its consumers.

Current last version: 0.9.0

# Requisites

* Java 1.6 or higher
* Spring 3.2 or higher
* [spring-tx-eventdispatcher](https://github.com/diepet/spring-tx-eventdispatcher)



