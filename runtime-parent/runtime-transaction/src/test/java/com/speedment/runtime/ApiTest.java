package com.speedment.runtime;

import com.speedment.runtime.config.identifier.TableIdentifier;
import com.speedment.runtime.core.Speedment;
import com.speedment.runtime.core.exception.SpeedmentException;
import com.speedment.runtime.core.manager.Manager;
import com.speedment.runtime.core.manager.Persister;
import com.speedment.runtime.core.manager.Remover;
import com.speedment.runtime.core.manager.Updater;
import com.speedment.runtime.field.Field;
import com.speedment.runtime.transaction.TransactionComponent;
import com.speedment.runtime.transaction.TransactionHandler;
import java.util.stream.Stream;
import org.junit.Test;

/**
 *
 * @author Per Minborg
 */
public class ApiTest {

    //@Test
    public void demo() {
        Speedment app = null;

        StringManager originalStringManager = app.getOrThrow(StringManager.class);

        TransactionComponent txComponent = app.getOrThrow(TransactionComponent.class);
        TransactionHandler txHandler = txComponent.transactionHandler();

        txHandler.invoke(tx -> {
            StringManager strings = tx.manager(StringManager.class);
        });

    }

    private class StringManager implements Manager<String> {

        @Override
        public TableIdentifier<String> getTableIdentifier() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Class<String> getEntityClass() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Stream<Field<String>> fields() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Stream<Field<String>> primaryKeyFields() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Stream<String> stream() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String persist(String entity) throws SpeedmentException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Persister<String> persister() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String update(String entity) throws SpeedmentException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Updater<String> updater() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String remove(String entity) throws SpeedmentException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Remover<String> remover() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }

}
