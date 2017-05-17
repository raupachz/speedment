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
import com.speedment.runtime.transaction.exception.SpeedmentTransactionException;
import java.sql.Connection;
import java.sql.SQLException;
import static java.util.Objects.requireNonNull;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @author Per Minborg
 */
public class TransactionHandlerImpl implements TransactionHandler {

    private final Dbms dbms;
    private final SqlConsumer<Transaction> initializer;
    private final SqlConsumer<Transaction> finisher;
    private final SqlBiConsumer<Transaction, Throwable> handler;
    private final Executor/*Service*/ executor;
    private final int transactionIsolation;
    // 
    private final ConnectionPoolComponent connectionPool;
    private final ManagerComponent managerComponent;

    public TransactionHandlerImpl(
        final ConnectionPoolComponent connectionPool,
        final Dbms dbms,
        final ManagerComponent managerComponent,
        final SqlConsumer<Transaction> initializer,
        final SqlConsumer<Transaction> finisher,
        final SqlBiConsumer<Transaction, Throwable> handler,
        final Executor executor,
        final int transactionIsolation
    ) {
        this.connectionPool = requireNonNull(connectionPool);
        this.dbms = requireNonNull(dbms);
        this.managerComponent = requireNonNull(managerComponent);
        this.initializer = requireNonNull(initializer);
        this.finisher = requireNonNull(finisher);
        this.handler = requireNonNull(handler);
        this.executor = requireNonNull(executor);
        this.transactionIsolation = transactionIsolation;
    }

    @Override
    public void invoke(Consumer<Transaction> action) throws SpeedmentTransactionException /*throws InterruptedException, ExecutionException */ {
        final Connection connection = connectionPool.getConnection(dbms);
        final Transaction tx = new TransactionImpl(connection, managerComponent, transactionIsolation);
        try {
            initializer.accept(tx);
            executor.execute(() -> action.accept(tx));
            finisher.accept(tx);
        } catch (RejectedExecutionException | SQLException ex) {
            try {
                handler.accept(tx, ex);
            } catch (SQLException sqle) {
                /// ignore
            }
            throw new SpeedmentTransactionException("Unable to invoke transaction", ex);
        } finally {
            try {
                connection.close();
            } catch (SQLException sqle) {
                /// ignore
            }
        }
    }

    @Override
    public <R> R invokeAndGet(Function<Transaction, R> mapper) throws SpeedmentTransactionException /*InterruptedException, ExecutionException */ {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates. 
    }

    @Override
    public <R> R invokeAndGet(Function<Transaction, R> mapper, long timeout, TimeUnit timeUnit) throws SpeedmentTransactionException /*InterruptedException, ExecutionException, TimeoutException */ {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates. 
    }

    @Override
    public CompletableFuture<Void> submit(Consumer<Transaction> action) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates. 
    }

    @Override
    public <R> CompletableFuture<R> submitAndGet(Function<Transaction, R> mapper) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates. 
    }

}
