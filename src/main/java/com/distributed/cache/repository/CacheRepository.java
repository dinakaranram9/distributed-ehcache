package com.distributed.cache.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.distributed.cache.entity.Cache;

@Repository
public interface CacheRepository extends MongoRepository<Cache, String>{

}
