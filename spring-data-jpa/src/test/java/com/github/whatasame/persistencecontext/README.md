# Persistence context

## What is persistence context?

In document of `EntityManager`, there is a definition of persistence context:

> An EntityManager instance is associated with a persistence context. A persistence context is a set of entity instances
> in which for any persistent entity identity there is a unique entity instance. Within the persistence context, the
> entity instances and their lifecycle are managed. The EntityManager API is used to create and remove persistent entity
> instances, to find entities by their primary key, and to query over entities.

In other words, the persistence context is a **set of unique entities** and is responsible for **managing the
lifecycle of entities**.

### In Spring Framework

Commonly (other type will be discussed soon), each `EntityManager` has its own persistence context associated with a
transaction. Consequently, each transaction creates a new `EntityManager` with an independent persistence context. When
the transaction concludes, both the `EntityManager` and its associated context are terminated.

In a word, different transaction has different `EntityManager` and persistence context. but other type of context can
across multiple transactions.

You can inject `EntityManager` with `@PersistenceContext` annotation.

```java
@PersistenceContext
private EntityManager entityManager;
```

## Persistence context type

There are two type of persistence context in JPA:

* Transaction-scoped persistence context
* Extended persistence context

### Transaction-scoped persistence context

Transaction-scoped persistence context is bounded to a transaction. As soon as the transaction finishes, the entities
present in the persistence context will be flushed into persistent storage. This also implies that, just like when the
transaction finishes, there are no entities remaining in the persistence context.

In a transaction,`EntityManager` checks first whether the entity is in persistence context. If not, it will be loaded
from storage.

### Extended persistence context

An extended persistence context can span **across multiple transactions**. We can persist the entity without the
transaction but cannot flush it without a transaction.

In the stateless session bean, the extended persistence context in one component is completely unaware of any
persistence context of another component.

```java
extendedContext.saveWithoutTransaction(member);

assertAll(
        () ->
                assertThat(extendedContext.find(member.getId()))
                        .isNotNull(), // exists in context so does not query sql
        () ->
                assertThat(transactionContext.find(member.getId()))
                        .isNull()); // but other context does not know so query sql
```

## Manipulating entities whether in transaction or not

In the transaction-scoped context, processes need to be executed within a transaction. Therefore, it flushes the context
to storage when the transaction automatically commits using AOP.

```java
package jakarta.persistence;

public interface EntityManager {

    /**
     * ...
     *
     * @throws TransactionRequiredException if there is no transaction when
     *         invoked on a container-managed entity manager of that is of type 
     *         <code>PersistenceContextType.TRANSACTION</code>
     */
    public void persist(Object entity);


    // Does not require a transaction    
    public <T> T find(Class<T> entityClass, Object primaryKey);
```

On the other hand, in an extended persistence context, processes can be executed without a transaction, but it does not
flush the context to storage. It only exists in the persistence context.

## References

* [JPA/Hibernate Persistence Context - Baeldung](https://www.baeldung.com/jpa-hibernate-persistence-context)
