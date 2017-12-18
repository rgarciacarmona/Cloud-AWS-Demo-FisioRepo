package models;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Provide JPA operations running inside of a thread pool sized to the connection pool
 */
public class JPASourceRepository implements SourceRepository{

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPASourceRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<Source> add(Source Source) {
        return supplyAsync(() -> wrap(em -> insert(em, Source)), executionContext);
    }

    @Override
    public CompletionStage<Stream<Source>> list() {
        return supplyAsync(() -> wrap(em -> list(em)), executionContext);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private Source insert(EntityManager em, Source Source) {
        em.persist(Source);
        return Source;
    }

    private Stream<Source> list(EntityManager em) {
        List<Source> Sources = em.createQuery("select a from Source a", Source.class).getResultList();
        return Sources.stream();
    }
}
