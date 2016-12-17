package io.github.paulochavesbr.vxnotes.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.github.paulochavesbr.vxnotes.model.Note;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class MongoNotesRepository implements NotesRepository {

	private final MongoClient mongo;
	private static final String COLLECTION = "notes";

	public MongoNotesRepository(MongoClient mongo) {
		this.mongo = mongo;
	}

	@Override
	public Future<Note> add(Note note) {
		Future<Note> fut = Future.future();
		mongo.insert(COLLECTION, note.toJson(), handler -> {
			if (handler.succeeded()) {
				note.setId(handler.result());
				fut.complete(note);
			} else {
				fut.fail(handler.cause());
			}
		});
		return fut;
	}

	@Override
	public Future<List<Note>> findAll() {
		Future<List<Note>> fut = Future.future();
		mongo.find(COLLECTION, new JsonObject(), handler -> {
			if (handler.succeeded()) {
				final List<Note> notes = new ArrayList<>();
				handler.result().forEach(note -> notes.add(new Note(note)));
				fut.complete(notes);
			} else {
				fut.fail(handler.cause());
			}
		});
		return fut;
	}

	@Override
	public Future<Optional<Note>> findById(String id) {
		Future<Optional<Note>> fut = Future.future();
		JsonObject query = new JsonObject().put("_id", id);
		mongo.findOne(COLLECTION, query, new JsonObject(), res -> {
			if (res.succeeded()) {
				Note note = res.result() == null ? null : new Note(res.result());
				fut.complete(Optional.ofNullable(note));
			} else {
				fut.fail(res.cause());
			}
		});
		return fut;
	}

	@Override
	public Future<Note> update(String id, Note newNote) {
		return null;
	}

	@Override
	public Future<Boolean> remove(String id) {
		Future<Boolean> fut = Future.future();
		JsonObject query = new JsonObject().put("_id", id);
		mongo.removeDocument(COLLECTION, query, res -> {
			if (res.succeeded()) {
				fut.complete(true);
			} else {
				fut.fail(res.cause());
			}
		});
		return fut;
	}

	@Override
	public Future<Boolean> removeAll() {
		return null;
	}

}
