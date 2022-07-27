package com.distributed.cache.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "cache")
public class Cache {
	@Id
	String cacheName;
	LocalDateTime lastUpdatedDateTime;
	Integer version;
}
