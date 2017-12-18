package controllers;

import models.Author;
import models.AuthorRepository;
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
public class AuthorController extends Controller {

    private final FormFactory formFactory;
    private final AuthorRepository authorRepository;
    private final HttpExecutionContext ec;

    @Inject
    public AuthorController(FormFactory formFactory, AuthorRepository authorRepository, HttpExecutionContext ec) {
        this.formFactory = formFactory;
        this.authorRepository = authorRepository;
        this.ec = ec;
    }

    public Result index() {
        return ok(views.html.index.render());
    }

    public CompletionStage<Result> addAuthor() {
        Author author = formFactory.form(Author.class).bindFromRequest().get();
        return authorRepository.add(author).thenApplyAsync(p -> {
            return redirect(routes.AuthorController.index());
        }, ec.current());
    }

    public CompletionStage<Result> getAuthors() {
        return authorRepository.list().thenApplyAsync(authorStream -> {
            return ok(toJson(authorStream.collect(Collectors.toList())));
        }, ec.current());
    }
}
