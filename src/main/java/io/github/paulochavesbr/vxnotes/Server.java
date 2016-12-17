package io.github.paulochavesbr.vxnotes;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

public class Server extends AbstractVerticle {

	private static final Logger LOG = LoggerFactory.getLogger(Server.class);
	private static final String HOST = "0.0.0.0";
	private static final int PORT = 8080;

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		final MongoClient mongo = MongoClient.createShared(vertx, config());
		Router router = Router.router(vertx);

		enableCORS(router);
		addRoutes(router);

		HttpServerOptions options = new HttpServerOptions().setLogActivity(true);

		vertx.createHttpServer(options).requestHandler(router::accept).listen(config().getInteger("http.port", PORT),
				HOST, result -> {
					if (result.succeeded()) {
						LOG.info("The server is listening on port {}", PORT);
						startFuture.complete();
					} else {
						LOG.error("Failed to start the HTTP server", result.cause());
						startFuture.fail(result.cause());
					}
				});
	}

	private void addRoutes(Router router) {
		router.get("/hello").handler(ctx -> {
			ctx.response().end("Hello Vert.x3!!");
		});
	}

	private void enableCORS(Router router) {
		// CORS support
		Set<String> allowHeaders = new HashSet<>();
		allowHeaders.add("x-requested-with");
		allowHeaders.add("Access-Control-Allow-Origin");
		allowHeaders.add("origin");
		allowHeaders.add("Content-Type");
		allowHeaders.add("accept");
		Set<HttpMethod> allowMethods = new HashSet<>();
		allowMethods.add(HttpMethod.GET);
		allowMethods.add(HttpMethod.POST);
		allowMethods.add(HttpMethod.DELETE);
		allowMethods.add(HttpMethod.PATCH);
		allowMethods.add(HttpMethod.PUT);

		router.route().handler(BodyHandler.create());
		router.route().handler(CorsHandler.create("*").allowedHeaders(allowHeaders).allowedMethods(allowMethods));
	}
}
