package controllers;

import models.FullPublication;
import models.FullPublicationRepository;
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
public class FullPublicationController extends Controller {

    private final FormFactory formFactory;
    private final FullPublicationRepository fullPublicationRepository;
    private final HttpExecutionContext ec;

    @Inject
    public FullPublicationController(FormFactory formFactory, FullPublicationRepository fullPublicationRepository, HttpExecutionContext ec) {
        this.formFactory = formFactory;
        this.fullPublicationRepository = fullPublicationRepository;
        this.ec = ec;
    }

    public Result index() {
        return ok(views.html.index.render());
    }

    public CompletionStage<Result> addFullPublication() {
        FullPublication fullPublication = formFactory.form(FullPublication.class).bindFromRequest().get();
        return fullPublicationRepository.add(fullPublication).thenApplyAsync(p -> {
            return redirect(routes.FullPublicationController.index());
        }, ec.current());
    }

    public CompletionStage<Result> getFullPublications() {
        return fullPublicationRepository.list().thenApplyAsync(fullPublicationStream -> {
            return ok(toJson(fullPublicationStream.collect(Collectors.toList())));
        }, ec.current());
    }

    public CompletionStage<Result> searchFullPublications(String keyword) {
        return fullPublicationRepository.searchByKeyword(keyword).thenApplyAsync(fullPublicationStream -> {
            return ok(toJson(fullPublicationStream.collect(Collectors.toList())));
        }, ec.current());
    }
}
