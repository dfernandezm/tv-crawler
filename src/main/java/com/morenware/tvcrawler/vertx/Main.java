package com.morenware.tvcrawler.vertx;

import com.morenware.tvcrawler.vertx.example.ProductHandler;
import com.morenware.tvcrawler.vertx.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * Created by david on 07/11/2016.
 */
public class Main extends AbstractVerticle {

    private Logger log = LoggerFactory.getLogger(Main.class.getName());

    @Override
    public void start() {
        setup();
    }

    // Convenience method so you can run it in your IDE
    public static void main(String[] args) {
        Runner.runExample(Main.class);
    }

    private void setup() {
        ProductHandler productHandler = new ProductHandler();
        productHandler.setUpInitialData();

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.get("/products/:productID").handler(productHandler::handleGetProduct);
        router.put("/products/:productID").handler(productHandler::handleAddProduct);
        router.get("/products").handler(productHandler::handleListProducts);

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
        log.info("Started server in port 8080...");

    }
}
