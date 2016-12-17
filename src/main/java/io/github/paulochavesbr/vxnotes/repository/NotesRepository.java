package io.github.paulochavesbr.vxnotes.repository;

import java.util.List;
import java.util.Optional;

import io.github.paulochavesbr.vxnotes.model.Note;
import io.vertx.core.Future;

public interface NotesRepository {

	Future<Note> add(Note note);
	
	Future<List<Note>> findAll();
	
	Future<Optional<Note>> findById();
	
	Future<Note> update(String id, Note newNote);
	
	Future<Boolean> remove(String id);
	
	Future<Boolean> removeAll();
}
