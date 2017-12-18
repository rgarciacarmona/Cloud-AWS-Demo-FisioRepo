package models;

import com.google.inject.ImplementedBy;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

/**
 * This interface provides a non-blocking API for possibly blocking operations.
 */
@ImplementedBy(JPAFullPublicationRepository.class)
public interface FullPublicationRepository {

    CompletionStage<FullPublication> add(FullPublication citation);

    CompletionStage<Stream<FullPublication>> list();

    CompletionStage<Stream<FullPublication>> searchByKeyword(String keyword);
}