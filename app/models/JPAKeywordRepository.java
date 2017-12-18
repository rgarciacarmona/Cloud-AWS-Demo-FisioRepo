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
public class JPAKeywordRepository implements KeywordRepository{

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPAKeywordRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<Keyword> add(Keyword Keyword) {
        return supplyAsync(() -> wrap(em -> insert(em, Keyword)), executionContext);
    }

    @Override
    public CompletionStage<Stream<Keyword>> list() {
        return supplyAsync(() -> wrap(em -> list(em)), executionContext);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private Keyword insert(EntityManager em, Keyword Keyword) {
        em.persist(Keyword);
        return Keyword;
    }

    private Stream<Keyword> list(EntityManager em) {
        List<Keyword> Keywords = em.createQuery("select a from Keyword a", Keyword.class).getResultList();
        return Keywords.stream();
    }
}
