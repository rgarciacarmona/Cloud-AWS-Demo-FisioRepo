package models;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Iterator;
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
    public CompletionStage<Publication> add(Publication publication) {
        return supplyAsync(() -> wrap(em -> insert(em, publication)), executionContext);
    }

    @Override
    public CompletionStage<Publication> fullAdd(Publication publication) {
        return supplyAsync(() -> wrap(em -> fullInsert(em, publication)), executionContext);
    }

    @Override
    public CompletionStage<Stream<Publication>> list() {
        return supplyAsync(() -> wrap(em -> list(em)), executionContext);
    }

    @Override
    public CompletionStage<Stream<Publication>> get(Long id) {
        return supplyAsync(() -> wrap(em -> get(em, id)), executionContext);
    }

    @Override
    public CompletionStage<Stream<Publication>> searchByTitle(String title) {
        return supplyAsync(() -> wrap(em -> searchByTitle(em, title)), executionContext);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private Publication insert(EntityManager em, Publication publication) {
        em.persist(publication);
        return publication;
    }

    private Publication fullInsert(EntityManager em, Publication publication) {
        if (publication.authors != null)
            for (Author author: publication.authors) {
                try {
                    Author existingAuthor = em.createQuery("select a from Author a where a.shortName='" + author.shortName + "' " +
                            "and a.fullName='" + author.fullName + "' " +
                            "and a.affiliation='" + author.affiliation + "'", Author.class).getSingleResult();
                    author.id = existingAuthor.id;
                } catch (javax.persistence.NoResultException e) {
                    em.persist(author);
                }
            }
        if (publication.citations != null) {
            for (Citation citation: publication.citations) {
                try {
                    Citation existingCitation = em.createQuery("select c from Citation c where c.author='" + citation.author + "' " +
                            "and c.year=" + citation.year + " " +
                            "and c.name='" + citation.name + "' " +
                            "and c.volume=" + citation.volume + " " +
                            "and c.page=" + citation.page + "", Citation.class).getSingleResult();
                    citation.id = existingCitation.id;
                } catch (javax.persistence.NoResultException e) {
                    em.persist(citation);
                }
            }
        }
        if (publication.keywords != null) {
            for (Keyword keyword : publication.keywords) {
                try {
                    Keyword existingKeyword = em.createQuery("select k from Keyword k where k.name='" + keyword.name + "'", Keyword.class).getSingleResult();
                    keyword.id = existingKeyword.id;
                } catch (javax.persistence.NoResultException e) {
                    em.persist(keyword);
                }
            }
        }
        if (publication.source != null) {
            try {
                Source existingSource = em.createQuery("select s from Source s where s.name='" + publication.source.name + "' " +
                            "and s.ISSN='" + publication.source.ISSN + "'", Source.class).getSingleResult();
                    publication.source.id = existingSource.id;
                } catch (javax.persistence.NoResultException e) {
                    em.persist(publication.source);
                }
        }
        em.persist(publication);
        return publication;
    }

    private Stream<Publication> list(EntityManager em) {
        List<Publication> publications = em.createQuery("select a from Publication a", Publication.class).getResultList();
        return publications.stream();
    }

    private Stream<Publication> get(EntityManager em, Long id) {
        List<Publication> publications = em.createQuery("select p from Publication p where id=" + id, Publication.class).getResultList();
        return publications.stream();
    }

    private Stream<Publication> searchByTitle(EntityManager em, String title) {
        List<Publication> Publications = em.createQuery("select a from Publication a where a.title LIKE '%" + title + "%'", Publication.class).getResultList();
        return Publications.stream();
    }
}
