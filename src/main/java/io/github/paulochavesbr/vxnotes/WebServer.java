package io.github.paulochavesbr.vxnotes;

import static io.vertx.core.http.HttpMethod.DELETE;
import static io.vertx.core.http.HttpMethod.GET;
import static io.vertx.core.http.HttpMethod.OPTIONS;
import static io.vertx.core.http.HttpMethod.PATCH;
import static io.vertx.core.http.HttpMethod.POST;
import static io.vertx.core.http.HttpMethod.PUT;

import java.util.Arrays;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

public class WebServer extends AbstractVerticle {

	private static final Logger LOG = LoggerFactory.getLogger(WebServer.class);
	private static final int PORT = 8080;

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		final MongoClient mongo = MongoClient.createShared(vertx, config());
		Router router = Router.router(vertx);

		enableCORS(router);
		addRoutes(router);

		HttpServerOptions options = new HttpServerOptions().setLogActivity(true);

		vertx.createHttpServer(options).requestHandler(router::accept).listen(config().getInteger("http.port", PORT),
				result -> {
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
		CorsHandler cors = CorsHandler.create("*");
		cors.allowedHeaders(new HashSet<>(
				Arrays.asList("x-requested-with", "Access-Control-Allow-Origin", "origin", "Content-Type", "accept")));
		cors.allowedMethods(new HashSet<>(Arrays.asList(GET, POST, PUT, PATCH, DELETE, OPTIONS)));

		router.route().handler(cors);
		router.route().handler(BodyHandler.create());
	}
}
