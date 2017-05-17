/**
 *
 * @author Per Minborg
 */
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

import com.speedment.common.injector.annotation.InjectKey;

import com.speedment.runtime.config.Dbms;
import com.speedment.runtime.config.identifier.DbmsIdentifier;
import static java.util.Objects.requireNonNull;

/**
 *
 * @author Per Minborg
 */
@InjectKey(TransactionComponent.class)
public interface TransactionComponent {

    /**
     * Creates and returns a new default TransactionHandler for the single
     * database in the current Project.
     *
     * @return a new default TransactionHandler for the single database in the
     * current Project
     * @throws IllegalStateException if there are several databases in the
     * current project.
     */
    default TransactionHandler transactionHandler() {
        return builder().build();
    }

    /**
     * Creates and returns a new default TransactionHandler for the given
     * database in the current Project.
     *
     * @param dbmsIdentifier the dbms identifier the TransactionHandler shall
     * use
     * @return a new default TransactionHandler for the given database in the
     * current Project
     * @throws IllegalArgumentException if the given database is not a part of
     * the current project.
     */
    default TransactionHandler transactionHandler(DbmsIdentifier<?> dbmsIdentifier) {
        return builder(requireNonNull(dbmsIdentifier)).build();
    }

    /**
     * Creates and returns a new default TransactionHandler Builder for the
     * single database in the current Project.
     *
     * @return a new default TransactionHandler Builder for the single database
     * in the current Project
     * @throws IllegalStateException if there are several databases in the
     * current project.
     */
    TransactionHandler.Builder builder();

    /**
     * Creates and returns a new default TransactionHandler Builder for the
     * given database in the current Project.
     *
     * @param dbmsIdentifier the dbms identifier the TransactionHandler shall
     * use
     * @return a new default TransactionHandler Builder for the given database
     * in the current Project
     * @throws IllegalArgumentException if the given database is not a part of
     * the current project.
     */
    TransactionHandler.Builder builder(DbmsIdentifier<?> dbmsIdentifier);

}
