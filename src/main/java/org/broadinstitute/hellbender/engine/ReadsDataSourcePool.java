package org.broadinstitute.hellbender.engine;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.broadinstitute.hellbender.cmdline.argumentcollections.ReadIndexPair;
import org.broadinstitute.hellbender.exceptions.GATKException;
import org.broadinstitute.hellbender.utils.AutoCloseableReference;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Pool of {@link ReadsDataSource} instances.
 */
public final class ReadsDataSourcePool extends GenericObjectPool<ReadsPathDataSource> implements AutoCloseable {

    public ReadsDataSourcePool(final List<Path> readPaths) {
        super(new Factory(readPaths));
        setWhenExhaustedAction(WHEN_EXHAUSTED_GROW);
    }

    /**
     * Returns a reads-data-source wrapped into a reference that when close returns
     * the source back to the pool.
     * @return never {@code null}.
     */
    public AutoCloseableReference<ReadsPathDataSource> borrowAutoReturn() {
        return AutoCloseableReference.of(borrowObject(), this::returnObject);
    }

    @Override
    public ReadsPathDataSource borrowObject() {
        try {
            return super.borrowObject();
        } catch (Exception e) {
            throw new GATKException(e.getMessage(), e);
        }
    }

    @Override
    public void returnObject(final ReadsPathDataSource dataSource) {
        try {
            super.returnObject(dataSource);
        } catch (Exception e) {
            throw new GATKException(e.getMessage(), e);
        }
    }

    public void close() {
        try {
            super.close();
        } catch (final Exception ex) {
            throw new GATKException("exception when closing the pool", ex);
        }
    }

    private static class Factory extends BasePoolableObjectFactory<ReadsPathDataSource> {

        private final List<ReadIndexPair> paths;

        private Factory(final List<Path> paths) {
            this.paths = paths.stream().map(p -> new ReadIndexPair(new GATKPath(p.toUri().toString()), null)).collect(Collectors.toList());
        }

        @Override
        public ReadsPathDataSource makeObject() {
            return new ReadsPathDataSource(paths);
        }

        @Override
        public void destroyObject(final ReadsPathDataSource dataSource) {
            dataSource.close();
        }

        @Override
        public boolean validateObject(final ReadsPathDataSource dataSource) {
            return !dataSource.isClosed();
        }
    }
}
