package com.morenware.tvcrawler.config;

import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

@Import(value={MainDatabaseConfig.class})
@Order(1)
public class MainConfig {




}