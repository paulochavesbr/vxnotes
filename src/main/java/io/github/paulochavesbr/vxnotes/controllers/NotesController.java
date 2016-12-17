
package io.github.paulochavesbr.vxnotes.controllers;

import static io.github.paulochavesbr.vxnotes.Constants.CONTENT_TYPE;
import static io.github.paulochavesbr.vxnotes.Constants.MIME_JSON;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.paulochavesbr.vxnotes.model.Note;
import io.github.paulochavesbr.vxnotes.repository.NotesRepository;
import io.vertx.core.Vertx;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonArray;
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
		router.get("/:id").handler(this::getByID);
		router.delete("/:id").handler(this::removeByID);
	}

	public Router getRouter() {
		return router;
	}

	private void getAll(RoutingContext ctx) {
		repository.findAll().setHandler(handler -> {
			if (handler.succeeded()) {
				ctx.response().setStatusCode(200).putHeader(CONTENT_TYPE, MIME_JSON)
						.end(new JsonArray(handler.result()).encode());
			} else {
				internalError(ctx);
			}
		});
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

	private void getByID(RoutingContext ctx) {
		repository.findById(getID(ctx)).setHandler(handler -> {
			if (handler.succeeded()) {
				if (handler.result().isPresent()) {
					sendOk(ctx, handler.result().get().toJson().encode());
				} else {
					notFound(ctx);
				}
			} else {
				internalError(ctx);
			}
		});
	}

	private void removeByID(RoutingContext ctx) {
		repository.remove(getID(ctx)).setHandler(handler -> {
			if (handler.succeeded()) {
				ctx.response().setStatusCode(204).end();
			} else {
				internalError(ctx);
			}
		});
	}

	private String getID(RoutingContext ctx) {
		return Objects.requireNonNull(ctx.request().getParam("id"));
	}
}
