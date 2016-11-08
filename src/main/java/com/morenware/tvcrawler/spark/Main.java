package com.morenware.tvcrawler.spark;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.staticFiles;
import static spark.debug.DebugScreen.*;
/**
 * Created by david on 07/11/2016.
 */
public class Main {

    public static void main(String[] args) {

        port(4567);
        staticFiles.location("/public");
        staticFiles.expireTime(600L);
        enableDebugScreen();

        get("/hello", (req, res) -> "Hello World");
    }
}
