package com.morenware.tvcrawler.vertx.util;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 *
 * Adapted from https://github.com/vert-x3/vertx-examples/blob/master/web-examples/src/main/java/io/vertx/example/util/Runner.java
 */
public class Runner {

	private static final String APP_DIR = "tv-crawler";
	private static final String JAVA_DIR = APP_DIR + "/src/main/java/";

	public static void runClusteredExample(Class clazz) {
		runExample(JAVA_DIR, clazz, new VertxOptions().setClustered(true), null);
	}

	public static void runExample(Class clazz) {
		runExample(JAVA_DIR, clazz, new VertxOptions().setClustered(false), null);
	}

	public static void runExample(Class clazz, DeploymentOptions options) {
		runExample(JAVA_DIR, clazz, new VertxOptions().setClustered(false), options);
	}

	public static void runExample(String appDir, Class clazz, VertxOptions options, DeploymentOptions
			deploymentOptions) {
		runExample(appDir + clazz.getPackage().getName().replace(".", "/"), clazz.getName(), options, deploymentOptions);
	}


	public static void runScript(String prefix, String scriptName, VertxOptions options) {
		File file = new File(scriptName);
		String dirPart = file.getParent();
		String scriptDir = prefix + dirPart;
		runExample(scriptDir, scriptDir + "/" + file.getName(), options, null);
	}

	public static void runExample(String appDir, String verticleID, VertxOptions options, DeploymentOptions deploymentOptions) {
		if (options == null) {
			// Default parameter
			options = new VertxOptions();
		}
		// Smart cwd detection

		// Based on the current directory (.) and the desired directory (baseDir), we try to compute the vertx.cwd
		// directory:
		try {
			// We need to use the canonical file. Without the file name is .
			File current = new File(".").getCanonicalFile();
			if (appDir.startsWith(current.getName()) && !appDir.equals(current.getName())) {
				appDir = appDir.substring(current.getName().length() + 1);
			}
		} catch (IOException e) {
			// Ignore it.
		}

		System.setProperty("vertx.cwd", appDir);
		Consumer<Vertx> runner = vertx -> {
			try {
				if (deploymentOptions != null) {
					vertx.deployVerticle(verticleID, deploymentOptions);
				} else {
					vertx.deployVerticle(verticleID);
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		};
		if (options.isClustered()) {
			Vertx.clusteredVertx(options, res -> {
				if (res.succeeded()) {
					Vertx vertx = res.result();
					runner.accept(vertx);
				} else {
					res.cause().printStackTrace();
				}
			});
		} else {
			Vertx vertx = Vertx.vertx(options);
			runner.accept(vertx);
		}
	}
}
