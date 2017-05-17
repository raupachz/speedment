package com.speedment.runtime.transaction.internal;

import com.speedment.common.injector.annotation.Inject;
import com.speedment.runtime.config.Dbms;
import com.speedment.runtime.config.identifier.DbmsIdentifier;
import com.speedment.runtime.core.component.ManagerComponent;
import com.speedment.runtime.core.component.ProjectComponent;
import com.speedment.runtime.core.component.connectionpool.ConnectionPoolComponent;
import com.speedment.runtime.transaction.TransactionComponent;
import com.speedment.runtime.transaction.TransactionHandler;
import java.util.Objects;
import static java.util.Objects.requireNonNull;

/**
 *
 * @author Per Minborg
 */
public class TransactionComponentImpl implements TransactionComponent {

    @Inject
    private ProjectComponent projectComponent;
    @Inject
    private ConnectionPoolComponent connectionPoolComponent;
    @Inject
    private ManagerComponent managerComponent;

    public TransactionHandler.Builder builder() {
        return projectComponent
            .getProject()
            .dbmses()
            .findFirst()
            .map(this::builder)
            .orElseThrow(() -> new IllegalStateException("builder() called while there are several dbmses in the project. Please specify which dbms to use."));
    }

    @Override
    public TransactionHandler.Builder builder(DbmsIdentifier<?> dbmsIdentifier) {
        return projectComponent
            .getProject()
            .dbmses()
            .filter(dbms -> Objects.equals(dbms.getId(), dbmsIdentifier.getDbmsName()))
            .findAny()
            .map(
                dbms -> new TransactionHandlerBuilderImpl(
                    requireNonNull(connectionPoolComponent),
                    requireNonNull(dbms),
                    requireNonNull(managerComponent)
                )
            )
            .orElseThrow(
                () -> new IllegalArgumentException("There is no dbms with the id " + dbmsIdentifier.getDbmsName())
            );

    }

    private TransactionHandler.Builder builder(Dbms dbms) {
        return new TransactionHandlerBuilderImpl(
            requireNonNull(connectionPoolComponent),
            requireNonNull(dbms),
            requireNonNull(managerComponent)
        );
    }

}
