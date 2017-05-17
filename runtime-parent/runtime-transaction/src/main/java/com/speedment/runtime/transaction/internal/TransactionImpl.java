/**
 *
 * Copyright (c) 2006-2016, Speedment, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); You may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.speedment.runtime.transaction.internal;

import com.speedment.runtime.core.component.ManagerComponent;
import com.speedment.runtime.core.manager.Manager;
import com.speedment.runtime.transaction.Transaction;
import java.sql.Connection;
import java.sql.SQLException;
import static java.util.Objects.requireNonNull;

/**
 *
 * @author Per Minborg
 */
public class TransactionImpl implements Transaction {

    private final Connection connection;
    private final int transactionIsolation;
    private final ManagerComponent managerComponent;

//    public TransactionImpl(
//        final Connection connection,
//        final ManagerComponent managerComponent
//    ) {
//        this(connection, managerComponent, Connection.TRANSACTION_READ_COMMITTED);
//    }
    public TransactionImpl(
        final Connection connection,
        final ManagerComponent managerComponent,
        final int transactionIsolation
    ) {
        this.connection = requireNonNull(connection);
        switch (transactionIsolation) {
            case Connection.TRANSACTION_READ_COMMITTED:
            case Connection.TRANSACTION_READ_UNCOMMITTED:
            case Connection.TRANSACTION_REPEATABLE_READ:
            case Connection.TRANSACTION_SERIALIZABLE:
                this.transactionIsolation = transactionIsolation;
                break;
            default: {
                throw new IllegalArgumentException("The transactionIsolation value " + transactionIsolation + " is not supported.");
            }

        }
        this.managerComponent = requireNonNull(managerComponent);
    }

    @Override
    public void begin() throws SQLException {
        connection.setAutoCommit(false);
        connection.setTransactionIsolation(transactionIsolation);
    }

    @Override
    public void commit() throws SQLException {
        connection.commit();
        connection.setAutoCommit(true);
    }

    @Override
    public void rollback() throws SQLException {
        connection.rollback();
        connection.setAutoCommit(true);
    }

    @Override 
    public <T, M extends Manager<T>> M manager(Class<M> managerClass) { 
        return managerComponent.create(managerClass, () -> connection);
    } 
// 
//    @Override 
//    public <T, M extends Manager<T>> M managerOf(Class<T> entityClass) { 
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates. 
//    } 
}
