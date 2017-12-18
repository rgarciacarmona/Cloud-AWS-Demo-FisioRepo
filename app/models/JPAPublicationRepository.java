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
public class JPAPublicationRepository implements PublicationRepository{

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPAPublicationRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<Publication> add(Publication Publication) {
        return supplyAsync(() -> wrap(em -> insert(em, Publication)), executionContext);
    }

    @Override
    public CompletionStage<Stream<Publication>> list() {
        return supplyAsync(() -> wrap(em -> list(em)), executionContext);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private Publication insert(EntityManager em, Publication Publication) {
        em.persist(Publication);
        return Publication;
    }

    private Stream<Publication> list(EntityManager em) {
        List<Publication> Publications = em.createQuery("select a from Publication a", Publication.class).getResultList();
        return Publications.stream();
    }
}
