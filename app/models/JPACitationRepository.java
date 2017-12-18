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
public class JPACitationRepository implements CitationRepository{

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPACitationRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<Citation> add(Citation Citation) {
        return supplyAsync(() -> wrap(em -> insert(em, Citation)), executionContext);
    }

    @Override
    public CompletionStage<Stream<Citation>> list() {
        return supplyAsync(() -> wrap(em -> list(em)), executionContext);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private Citation insert(EntityManager em, Citation Citation) {
        em.persist(Citation);
        return Citation;
    }

    private Stream<Citation> list(EntityManager em) {
        List<Citation> Citations = em.createQuery("select a from Citation a", Citation.class).getResultList();
        return Citations.stream();
    }
}
