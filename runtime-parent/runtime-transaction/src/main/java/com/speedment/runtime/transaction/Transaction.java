package com.speedment.runtime.transaction;

import com.speedment.runtime.core.manager.Manager;
import java.sql.SQLException;

/**
 *
 * @author Per Minborg
 */
public interface Transaction {

    // Todo:
    //
    // FilmManager.stream() ->
    // AbstractManager.stream() -> 
    // StreamSupplierComponent.stream() -> 
    // SqlStreamSupplierComponentImpl.stream() ->
    // SqlStreamSupplierImpl.stream() ->
    // AbstractDbmsOperationHandler.executeQueryAsync()
    //
    // FilmManager.persist() ->
    // Persister.apply()
    
    // Persister from PersisterComponent.persister()
    // From SqlPersistenceImpl
    // From DbmsOperationHandler
    
    
    // AbstractDbmsOperationHandler needs to know the Supplier<Connection>
    // Add a method DbmsType.getOperationHandler(Supplier<Connection> connectionSupplier)
    
    void begin() throws SQLException;

    void commit() throws SQLException;

    void rollback() throws SQLException;

    public <T, M extends Manager<T>> M manager(Class<M> managerClass);
// 
//    <T, M extends Manager<T>> M managerOf(Class<T> entityClass); 

}
