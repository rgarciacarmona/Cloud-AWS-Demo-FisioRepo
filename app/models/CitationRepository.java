package models;

import com.google.inject.ImplementedBy;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

/**
 * This interface provides a non-blocking API for possibly blocking operations.
 */
@ImplementedBy(JPACitationRepository.class)
public interface CitationRepository {

    CompletionStage<Citation> add(Citation citation);

    CompletionStage<Stream<Citation>> list();
}