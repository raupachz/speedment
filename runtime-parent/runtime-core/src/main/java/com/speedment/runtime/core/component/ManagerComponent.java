/**
 *
 * Copyright (c) 2006-2017, Speedment, Inc. All Rights Reserved.
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
package com.speedment.runtime.core.component;

import com.speedment.common.injector.annotation.InjectKey;
import com.speedment.runtime.core.exception.SpeedmentException;
import com.speedment.runtime.core.manager.Manager;
import java.sql.Connection;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * The {@code ManagerComponent} provides the mapping between entities and their
 * corresponding managers. Custom managers may be plugged into the Speedment
 * framework.
 *
 * @author Emil Forslund
 * @since 2.0.0
 */
@InjectKey(ManagerComponent.class)
public interface ManagerComponent {

    /**
     * Puts (associates) a {@link Manager} implementation into the
     * {@code ManagerComponent}. If a previous {@link Manager} was associated
     * with an Entity class, table or interface, that association(s) is/are
     * replaced.
     *
     * @param <E> the entity interface type
     * @param manager to associate
     */
    <E> void put(Manager<E> manager);

    /**
     * Puts (associates) a constructor of a {@link Manager} into the
     * {@code ManagerComponent}. If a previous {@link Manager} constructor was
     * associated with an Entity class, table or interface, that association(s)
     * is/are replaced.
     * <p>
     * The provided constructor is used when dynamic managers are created, for
     * example within transactions.
     *
     * @param <M> the manager type
     * @param constructor to associate
     * @param managerClass to associate
     */
    <M> void putConstructor(Class<M> managerClass, Function<Supplier<Connection>, M> constructor);

    /**
     * Obtains and returns the currently associated {@link Manager}
     * implementation for the given Entity interface Class. If no Manager exists
     * for the given entityClass, a SpeedmentException will be thrown.
     *
     * @param <E> the entity interface type
     * @param entityClass the entity interface {@code Class}
     * @return the currently associated {@link Manager} implementation for the
     * given Entity interface Class
     *
     * @throws SpeedmentException if no Manager exists for the given entityClass
     */
    <E> Manager<E> managerOf(Class<E> entityClass) throws SpeedmentException;

    /**
     * Returns a {@link Stream} of all {@link Manager Managers} associated with
     * this ManagerComponent.
     *
     * @return a {@link Stream} of all {@link Manager Managers} associated with
     * this ManagerComponent
     */
    Stream<Manager<?>> stream();

    /**
     * Creates and returns a new Manager that will be using the connection
     * obtainable via the connection provider.
     *
     * @param <E> entity type
     * @param <M> Manager type
     * @param managerClass the class of the manager to create
     * @param connectionSupplier that will be used to produce a connection
     * @return
     */
    <E, M extends Manager<E>> M create(Class<M> managerClass, Supplier<Connection> connectionSupplier);
}
