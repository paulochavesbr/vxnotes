package io.github.paulochavesbr.vxnotes;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.paulochavesbr.vxnotes.model.Note.Color;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class NotesApiIT {

	private final static int PORT = 8484;
	private final static String HOST = "127.0.0.1";
	private final static JsonObject DB = new JsonObject()
			.put("connection_string", "mongodb://127.0.0.1:37017/test-vxnotes").put("db_name", "test-vxnotes");
	private final static String STORAGE = "mongo";

	private static Vertx vertx;

	@BeforeClass
	public static void before(TestContext ctx) {
		JsonObject config = new JsonObject();
		config.put("http.port", PORT);
		config.put("mongo", DB);
		config.put("storage", STORAGE);
		DeploymentOptions options = new DeploymentOptions().setConfig(config);

		vertx = Vertx.vertx();
		vertx.deployVerticle(new WebServer(), options, ctx.asyncAssertSuccess());
	}

	@AfterClass
	public static void after(TestContext ctx) {
		vertx.close(ctx.asyncAssertSuccess());
	}

	@Test(timeout = 2000l)
	public void testGetByID(TestContext ctx) {
		HttpClient client = vertx.createHttpClient();
		HttpClientRequest req = client.get(PORT, HOST, "/api/notes/111");
		Async async = ctx.async();
		req.handler(response -> response.bodyHandler(body -> {
			JsonObject note = body.toJsonObject();
			ctx.assertEquals(200, response.statusCode());
			ctx.assertEquals("First Note", note.getString("title"));
			ctx.assertEquals(Color.GREEN, Color.valueOf(note.getString("color")));
			client.close();
			async.complete();
		}));
		req.end();
	}

	@Test(timeout = 2000l)
	public void endpointNotFound(TestContext ctx) {
		HttpClient client = vertx.createHttpClient();
		HttpClientRequest req = client.get(PORT, HOST, "/api/nootes/111");
		Async async = ctx.async();
		req.handler(response -> {
			ctx.assertEquals(404, response.statusCode());
			client.close();
			async.complete();
		});
		req.end();
	}
}
