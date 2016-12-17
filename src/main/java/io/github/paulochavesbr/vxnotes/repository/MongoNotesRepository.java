package io.github.paulochavesbr.vxnotes.repository;

import java.util.List;
import java.util.Optional;

import io.github.paulochavesbr.vxnotes.model.Note;
import io.vertx.core.Future;
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
		System.out.println(note);
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
		return null;
	}

	@Override
	public Future<Optional<Note>> findById() {
		return null;
	}

	@Override
	public Future<Note> update(String id, Note newNote) {
		return null;
	}

	@Override
	public Future<Boolean> remove(String id) {
		return null;
	}

	@Override
	public Future<Boolean> removeAll() {
		return null;
	}

}
