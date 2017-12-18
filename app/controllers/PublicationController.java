package controllers;

import models.Publication;
import models.PublicationRepository;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

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

    public Result index() {
        return ok(views.html.publication.render());
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
}
