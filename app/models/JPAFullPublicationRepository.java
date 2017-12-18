package models;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
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
public class JPAFullPublicationRepository implements FullPublicationRepository{

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPAFullPublicationRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<FullPublication> add(FullPublication FullPublication) {
        return supplyAsync(() -> wrap(em -> insert(em, FullPublication)), executionContext);
    }

    @Override
    public CompletionStage<Stream<FullPublication>> list() {
        return supplyAsync(() -> wrap(em -> list(em)), executionContext);
    }

    @Override
    public CompletionStage<Stream<FullPublication>> searchByKeyword(String keyword) {
        return supplyAsync(() -> wrap(em -> searchByKeyword(em, keyword)), executionContext);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private FullPublication insert(EntityManager em, FullPublication FullPublication) {
        em.persist(FullPublication);
        return FullPublication;
    }

    private Stream<FullPublication> list(EntityManager em) {
        List<FullPublication> FullPublications = em.createQuery("select a from FullPublication a", FullPublication.class).getResultList();
        return FullPublications.stream();
    }

    private Stream<FullPublication> searchByKeyword(EntityManager em, String keyword) {
        List<FullPublication> FullPublications = em.createQuery("select a from FullPublication a where a.keywords LIKE '%" + keyword + "%'", FullPublication.class).getResultList();
        return FullPublications.stream();
    }


}
