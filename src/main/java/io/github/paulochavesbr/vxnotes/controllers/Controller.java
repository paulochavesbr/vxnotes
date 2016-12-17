package io.github.paulochavesbr.vxnotes.controllers;

import io.vertx.ext.web.RoutingContext;

public abstract class Controller {

	protected void created(RoutingContext ctx, String content) {
		ctx.response().setStatusCode(201).putHeader("Content-Type", "application/json").end(content);
	}

	protected void notFound(RoutingContext ctx) {
		ctx.response().setStatusCode(404).end();
	}

	protected void badRequest(RoutingContext ctx) {
		ctx.response().setStatusCode(400).end();
	}

	protected void internalError(RoutingContext ctx) {
		ctx.response().setStatusCode(500).end();
	}
}
