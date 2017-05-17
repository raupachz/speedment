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

import com.speedment.common.function.sql.SqlBiConsumer;
import com.speedment.common.function.sql.SqlConsumer;
import com.speedment.runtime.config.Dbms;
import com.speedment.runtime.core.component.ManagerComponent;
import com.speedment.runtime.core.component.connectionpool.ConnectionPoolComponent;
import com.speedment.runtime.transaction.Transaction;
import com.speedment.runtime.transaction.TransactionHandler;
import com.speedment.runtime.transaction.TransactionHandler.Builder;
import java.sql.Connection;
import static java.util.Objects.requireNonNull;
import java.util.concurrent.Executor;

/**
 *
 * @author Per Minborg
 */
public class TransactionHandlerBuilderImpl implements TransactionHandler.Builder {

    private final ConnectionPoolComponent connectionPool;
    private final Dbms dbms;
    private final ManagerComponent managerComponent;
    
    private SqlConsumer<Transaction> initializer;
    private SqlConsumer<Transaction> finisher;
    private SqlBiConsumer<Transaction, Throwable> handler;
    private Executor/*Service*/ executor;
    private int transactionIsolation;

    public TransactionHandlerBuilderImpl(
        final ConnectionPoolComponent connectionPool,
        final Dbms dbms,
        final ManagerComponent managerComponent
    ) {
        this.connectionPool = requireNonNull(connectionPool);
        this.dbms = requireNonNull(dbms);
        this.managerComponent = requireNonNull(managerComponent);
        this.initializer = (tx) -> tx.begin();
        this.finisher = (tx) -> tx.commit();
        this.handler = (tx, ex) -> tx.rollback();
        this.executor = Runnable::run; // Run directly in the same thread 
        this.transactionIsolation = Connection.TRANSACTION_READ_COMMITTED;
    }

    @Override
    public Builder withIntializer(SqlConsumer<Transaction> initializer) {
        this.initializer = requireNonNull(initializer);
        return this;
    }

    @Override
    public Builder withFinisher(SqlConsumer<Transaction> finisher) {
        this.finisher = requireNonNull(finisher);
        return this;
    }

    @Override
    public Builder withExeptionHandler(SqlBiConsumer<Transaction, Throwable> handler) {
        this.handler = requireNonNull(handler);
        return this;
    }

    @Override
    public Builder withExecutor(Executor/*Service*/ executor) {
        this.executor = requireNonNull(executor);
        return this;
    }

    @Override
    public Builder withTransactionIsolationReadUncommited() {
        return with(Connection.TRANSACTION_READ_UNCOMMITTED);
    }

    @Override
    public Builder withTransactionIsolationReadCommited() {
        return with(Connection.TRANSACTION_READ_COMMITTED);
    }

    @Override
    public Builder withTransactionIsolationRepeatableRead() {
        return with(Connection.TRANSACTION_REPEATABLE_READ);
    }

    @Override
    public Builder withTransactionIsolationSerializable() {
        return with(Connection.TRANSACTION_SERIALIZABLE);
    }

    private Builder with(int transactionIsolation) {
        this.transactionIsolation = transactionIsolation;
        return this;
    }

    @Override
    public TransactionHandler build() {
        return new TransactionHandlerImpl(
            connectionPool,
            dbms,
            managerComponent,
            initializer,
            finisher,
            handler,
            executor,
            transactionIsolation
        );
    }

}
