package io.github.paulochavesbr.vxnotes.repository;

import java.util.List;
import java.util.Optional;

import io.github.paulochavesbr.vxnotes.model.Note;
import io.vertx.core.Future;

public interface NotesRepository {

	/**
	 * Adds an object to the storage
	 * 
	 * @param note
	 * @return
	 */
	Future<Note> add(Note note);

	/**
	 * Gets all objects from the storage
	 * 
	 * @return
	 */
	Future<List<Note>> findAll();

	/**
	 * Find an object given its ID
	 * 
	 * @param id
	 * @return
	 */
	Future<Optional<Note>> findById(String id);

	Future<Note> update(String id, Note newNote);

	/**
	 * Remove a single object from the storage given its ID
	 * 
	 * @param id
	 * @return
	 */
	Future<Boolean> remove(String id);

	/**
	 * Remove all objects from the storage
	 * 
	 * @return
	 */
	Future<Boolean> removeAll();
}
