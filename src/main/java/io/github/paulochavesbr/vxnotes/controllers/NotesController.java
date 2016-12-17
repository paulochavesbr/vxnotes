
package io.github.paulochavesbr.vxnotes.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.paulochavesbr.vxnotes.model.Note;
import io.github.paulochavesbr.vxnotes.repository.NotesRepository;
import io.vertx.core.Vertx;
import io.vertx.core.json.DecodeException;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class NotesController extends Controller {

	private static final Logger LOG = LoggerFactory.getLogger(NotesController.class);
	
	private final Vertx vertx;
	private final Router router;
	private final NotesRepository repository;

	public NotesController(Vertx vertx, NotesRepository repository) {
		this.vertx = vertx;
		this.router = Router.router(this.vertx);
		this.repository = repository;

		router.get("/").handler(this::getAll);
		router.post("/").handler(this::add);
	}

	public Router getRouter() {
		return router;
	}

	private void getAll(RoutingContext ctx) {
		ctx.response().end("List notes");
	}

	private void add(RoutingContext ctx) {
		try {
			Note note = new Note(ctx.getBodyAsJson());
			repository.add(note).setHandler(handler -> {
				if (handler.succeeded()) {
					created(ctx, note.toJson().toString());
				} else {
					internalError(ctx);
				}
			});
		} catch (DecodeException e) {
			LOG.info(e.getMessage());
			badRequest(ctx);
		}
	}
}
