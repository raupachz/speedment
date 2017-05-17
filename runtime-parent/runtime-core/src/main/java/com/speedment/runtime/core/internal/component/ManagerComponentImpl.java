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
package com.speedment.runtime.core.internal.component;

import com.speedment.runtime.core.component.ManagerComponent;
import com.speedment.runtime.core.exception.SpeedmentException;
import com.speedment.runtime.core.manager.Manager;
import java.sql.Connection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Default implementation of the {@link ManagerComponent}-interface.
 *
 * @author Emil Forslund
 */
public final class ManagerComponentImpl implements ManagerComponent {

    private final Map<Class<?>, Manager<?>> managersByEntity;
    private final Map<Class<?>, Function<Supplier<Connection>, Manager<?>>> constructorsByManager;

    public ManagerComponentImpl() {
        managersByEntity = new ConcurrentHashMap<>();
        constructorsByManager = new ConcurrentHashMap<>();
    }

    @Override
    public <ENTITY> void put(Manager<ENTITY> manager) {
        requireNonNull(manager);
        managersByEntity.put(manager.getEntityClass(), manager);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> Manager<E> managerOf(Class<E> entityClass)
        throws SpeedmentException {

        requireNonNull(entityClass);

        @SuppressWarnings("unchecked")
        final Manager<E> manager = (Manager<E>) managersByEntity.get(entityClass);

        if (manager == null) {
            throw new SpeedmentException(
                "No manager exists for " + entityClass
            );
        }

        return manager;
    }

    @Override
    public Stream<Manager<?>> stream() {
        return managersByEntity.values().stream();
    }

    @Override
    public <M> void putConstructor(Class<M> managerClass, Function<Supplier<Connection>, M> constructor) {

        @SuppressWarnings("unchecked")
        Function<Supplier<Connection>, Manager<?>> castedConstructor
            = (Function<Supplier<Connection>, Manager<?>>) ((Function<Supplier<Connection>, ? extends Manager>) constructor);

        constructorsByManager.put(managerClass, castedConstructor);
    }

    @Override
    public <E, M extends Manager<E>> M create(Class<M> managerClass, Supplier<Connection> connectionProvider) {
        final Function<Supplier<Connection>, Manager<?>> constructor = constructorsByManager.get(managerClass);

        @SuppressWarnings("unchecked")
        final M result = (M) constructor.apply(connectionProvider);

        return result;
    }

}
