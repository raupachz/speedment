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
package com.speedment.runtime.transaction;

import com.speedment.common.function.sql.SqlBiConsumer;
import com.speedment.common.function.sql.SqlConsumer;
import com.speedment.runtime.transaction.exception.SpeedmentTransactionException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @author Per Minborg
 */
public interface TransactionHandler {

    void invoke(Consumer<Transaction> action) throws SpeedmentTransactionException /*throws InterruptedException, ExecutionException*/;

    <R> R invokeAndGet(Function<Transaction, R> mapper) throws SpeedmentTransactionException  /* throws InterruptedException, ExecutionException*/;

    <R> R invokeAndGet(Function<Transaction, R> mapper, long timeout, TimeUnit timeUnit) throws SpeedmentTransactionException  /* throws InterruptedException, ExecutionException, TimeoutException*/;

    CompletableFuture<Void> submit(Consumer<Transaction> action);

    <R> CompletableFuture<R> submitAndGet(Function<Transaction, R> mapper);

    interface Builder /*extends TransactionHandler*/ {

        // Default is (tx) -> tx.begin(); 
        Builder withIntializer(SqlConsumer<Transaction> iniitializer);

        // Default is (tx) -> tx.commit(); 
        Builder withFinisher(SqlConsumer<Transaction> finisher);

        // Default is (tx, ex) -> tx.rollback(); 
        Builder withExeptionHandler(SqlBiConsumer<Transaction, Throwable> handler);

        // Default is in the same thread 
        Builder withExecutor(Executor/*Service*/ executor);

        Builder withTransactionIsolationReadUncommited();

        Builder withTransactionIsolationReadCommited();

        Builder withTransactionIsolationRepeatableRead();

        Builder withTransactionIsolationSerializable();

        TransactionHandler build();

//        // REMOVE THESE?? -> 
//        @Override 
//        public default void invoke(Consumer<Transaction> action) throws InterruptedException, ExecutionException { 
//            build().invoke(action); 
//        } 
// 
//        @Override 
//        public default <R> R invoke(Function<Transaction, R> mapper) throws InterruptedException, ExecutionException { 
//            return build().invoke(mapper); 
//        } 
// 
//        @Override 
//        public default <R> R invoke(Function<Transaction, R> mapper, long timeout, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException { 
//            return build().invoke(mapper, timeout, timeUnit); 
//        } 
// 
//        @Override 
//        public default CompletableFuture<Void> submit(Consumer<Transaction> action) { 
//            return build().submit(action); 
//        } 
// 
//        @Override 
//        public default <R> CompletableFuture<R> submit(Function<Transaction, R> mapper) { 
//            return build().submit(mapper); 
//        } 
    }

}
