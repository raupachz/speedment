package com.speedment.runtime.transaction;

import com.speedment.common.injector.InjectBundle;
import com.speedment.runtime.transaction.internal.TransactionComponentImpl;
import java.util.stream.Stream;

/**
 *
 * @author Per Minborg
 */
public class TransactionBundle implements InjectBundle {

    @Override
    public Stream<Class<?>> injectables() {
        return Stream.of(TransactionComponentImpl.class);
    }

}
