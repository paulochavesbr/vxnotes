package io.github.paulochavesbr.vxnotes.repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import io.github.paulochavesbr.vxnotes.model.Note;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.LocalMap;

public class MapNotesRepository implements NotesRepository {

	private final static String NOTES_MAP = "NOTES";
	
	private final Vertx vertx;
	private final AtomicInteger counter = new AtomicInteger();
	
	public MapNotesRepository(Vertx vertx) {
		this.vertx = vertx;
	}
	
	private LocalMap<Integer, JsonObject> getMap() {
		return vertx.sharedData().getLocalMap(NOTES_MAP);
	}
	
	@Override
	public Future<Note> add(Note note) {
		Future<Note> fut = Future.future();
		getMap().put(counter.incrementAndGet(), note.toJson());
		fut.complete(note);
		System.out.println(getMap().size());
		return fut;
	}

	@Override
	public Future<List<Note>> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Future<Optional<Note>> findById() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Future<Note> update(String id, Note newNote) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Future<Boolean> remove(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Future<Boolean> removeAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
