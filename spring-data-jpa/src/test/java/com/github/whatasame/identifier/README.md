# Hibernate identifier

JPA gives us two ways to define identifier: assigned and generated. Hibernate implements both of them.

## Assigned identifier

Application can assign identifier to entity before persisting it.

```java
@Entity
@Setter
public class User {

    @Id
    private Long id;
}
```

```java
User user = new User();
user.setId(777L);

entityManager.persist(user);
```

## Generated identifier

When an entity with an identifier defined as generated is persisted, Hibernate will generate the value based on an
associated generation strategy.

```java
@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;
}
```

```java
User user = new User();

entityManager.persist(user); // id will be generated
```

### With assigned value

When `EntityManager` persist entity with an identifier defined as generated but already has an identifier value,
manager determine it as `DETACHED` status throwing exception.

```java
package org.hibernate.event.internal;

public class DefaultPersistEventListener {

    private void persist(PersistEvent event, PersistContext createCache, Object entity) {
    
        // ... 
        
        switch ( entityState( event, entity, entityName, entityEntry ) ) {
            case DETACHED:
                throw new PersistentObjectException( "detached entity passed to persist: "
                        + EventUtil.getLoggableName( event.getEntityName(), entity) );
                        
        // ...
    }
}
```

To avoid this exception, use `EntityManager.merge()` instead of `EntityManager.persist()`. It also determine entity
as `DETACHED` status, but it try to find entity in database first. If entity not found, it will be determined
as `TRANSIENT` status. So it will be persisted.

```java
package org.hibernate.event.internal;

public class DefaultMergeEventListener {

    private void merge(MergeEvent event, MergeContext copiedAlready, Object entity) {
        switch ( entityState( event, entity ) ) {
            case DETACHED:
                entityIsDetached( event, copiedAlready );
                break;
        
        // ...
    }
    
    protected void entityIsDetached(MergeEvent event, MergeContext copyCache) {
        
        // ...
        
        Object id = getDetachedEntityId( event, entity, persister );
        final Object clonedIdentifier = persister.getIdentifierType().deepCopy( id, source.getFactory() );
        final Object result = source.getLoadQueryInfluencers().fromInternalFetchProfile(
                CascadingFetchProfile.MERGE,
                () -> source.get( entityName, clonedIdentifier )
        
        if ( result == null ) { // entity not found in database so treat it as transient
            entityIsTransient( event, copyCache );
            
        // ... 
    }
}
```
