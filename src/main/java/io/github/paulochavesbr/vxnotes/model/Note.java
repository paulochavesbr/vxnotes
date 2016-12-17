package io.github.paulochavesbr.vxnotes.model;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class Note {

	public enum Color {
		RED, ORANGE, YELLOW, GREEN, BLUE, PURPLE, BLACK, GRAY, WHITE
	};

	private String id;
	private String title;
	/**
	 * If it's just a text, it must contain only a item {text: "some text"}<br>
	 * It it's a checklist, it must contain an array of items {items: [{text:
	 * string, done: boolean}]}
	 */
	private JsonObject body;
	private Color color;
	private Boolean archived;
	private Boolean deleted;
	/**
	 * An array of tags: ["tag1", "tag2", ...]
	 */
	private List<String> tags;
	private Instant createdAt;
	private Instant updatedAt;

	public Note() {

	}
	
	public Note(Note other) {
		this.id = other.id;
		this.title = other.title;
		this.body = other.body;
		this.color = other.color;
		this.archived = other.archived;
		this.deleted = other.deleted;
		this.tags = other.tags;
		this.createdAt = ZonedDateTime.now(ZoneOffset.UTC).toInstant();
		this.updatedAt = ZonedDateTime.now(ZoneOffset.UTC).toInstant();
	}

	@SuppressWarnings("unchecked")
	public Note(JsonObject note) {
		this(note.getString("_id"),
				note.getString("title"),
				note.getJsonObject("body", new JsonObject()),
				Color.valueOf(note.getString("color", Color.WHITE.name())),
				note.getBoolean("archived", false),
				note.getBoolean("deleted", false),
				note.getJsonArray("tags", new JsonArray()).getList());
	}

	public Note(String id, String title, JsonObject body, Color color, Boolean archived, Boolean deleted,
			List<String> tags) {
		super();
		this.id = id;
		this.title = title;
		this.body = body;
		this.color = color;
		this.archived = archived;
		this.deleted = deleted;
		this.tags = tags;
		this.createdAt = ZonedDateTime.now(ZoneOffset.UTC).toInstant();
		this.updatedAt = ZonedDateTime.now(ZoneOffset.UTC).toInstant();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public JsonObject getBody() {
		return body;
	}

	public void setBody(JsonObject body) {
		this.body = body;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Boolean getArchived() {
		return archived;
	}

	public void setArchived(Boolean archived) {
		this.archived = archived;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}

	public JsonObject toJson() {
		JsonObject obj = new JsonObject();
		if (id != null) {
			obj.put("_id", id);
		}
		obj.put("title", title)
			.put("body", body)
			.put("color", color)
			.put("archived", archived)
			.put("deleted", deleted)
			.put("tags", new JsonArray(tags))
			.put("createdAt", createdAt)
			.put("updatedAt", updatedAt);
		return obj;
	}
}
