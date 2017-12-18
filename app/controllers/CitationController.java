package controllers;

import models.Citation;
import models.CitationRepository;
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
public class CitationController extends Controller {

    private final FormFactory formFactory;
    private final CitationRepository citationRepository;
    private final HttpExecutionContext ec;

    @Inject
    public CitationController(FormFactory formFactory, CitationRepository citationRepository, HttpExecutionContext ec) {
        this.formFactory = formFactory;
        this.citationRepository = citationRepository;
        this.ec = ec;
    }

    public Result index() {
        return ok(views.html.citation.render());
    }

    public CompletionStage<Result> addCitation() {
        Citation citation = formFactory.form(Citation.class).bindFromRequest().get();
        return citationRepository.add(citation).thenApplyAsync(p -> {
            return redirect(routes.CitationController.getCitations());
        }, ec.current());
    }

    public CompletionStage<Result> getCitations() {
        return citationRepository.list().thenApplyAsync(citationStream -> {
            return ok(toJson(citationStream.collect(Collectors.toList())));
        }, ec.current());
    }
}
