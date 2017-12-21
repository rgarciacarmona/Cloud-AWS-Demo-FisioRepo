package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Publication;
import models.PublicationRepository;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.Set;

import static play.libs.Json.toJson;

/**
 * The controller keeps all database operations behind the repository, and uses
 * {@link play.libs.concurrent.HttpExecutionContext} to provide access to the
 * {@link play.mvc.Http.Context} methods like {@code request()} and {@code flash()}.
 */
public class PublicationController extends Controller {

    private final FormFactory formFactory;
    private final PublicationRepository publicationRepository;
    private final HttpExecutionContext ec;

    @Inject
    public PublicationController(FormFactory formFactory, PublicationRepository publicationRepository, HttpExecutionContext ec) {
        this.formFactory = formFactory;
        this.publicationRepository = publicationRepository;
        this.ec = ec;
    }

    public CompletionStage<Result> index() {
        return publicationRepository.list().thenApplyAsync(publicationStream -> {
            return ok(views.html.publication.render(publicationStream.collect(Collectors.toList())));
        }, ec.current());
    }

    public CompletionStage<Result> addPublication() {
        Publication publication = formFactory.form(Publication.class).bindFromRequest().get();
        return publicationRepository.add(publication).thenApplyAsync(p -> {
            return redirect(routes.PublicationController.getPublications());
        }, ec.current());
    }

    public CompletionStage<Result> getPublications() {
        return publicationRepository.list().thenApplyAsync(publicationStream -> {
            return ok(toJson(publicationStream.collect(Collectors.toList())));
        }, ec.current());
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> addPublicationJson() {
        JsonNode json = request().body().asJson();
        // read the JsonNode as a Publication
        Publication publication = Json.fromJson(json, Publication.class);
        return publicationRepository.fullAdd(publication).thenApplyAsync(p -> {
            return redirect(routes.PublicationController.getPublications());
        }, ec.current());
    }

    public CompletionStage<Result> searchPublications(String title) {
        return publicationRepository.searchByTitle(title).thenApplyAsync(publicationStream -> {
            return ok(toJson(publicationStream.collect(Collectors.toList())));
        }, ec.current());
    }
}
