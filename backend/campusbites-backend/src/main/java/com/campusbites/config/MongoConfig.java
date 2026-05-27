package com.campusbites.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Enables @CreatedDate and @LastModifiedDate on all MongoDB documents.
 * Required by User, Product, Cart, and Order models.
 */
@Configuration
@EnableMongoAuditing
public class MongoConfig {}