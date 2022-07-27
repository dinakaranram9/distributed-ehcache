package com.distributed.cache.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.distributed.cache.entity.Cache;
import com.distributed.cache.entity.Student;
import com.distributed.cache.service.CacheService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/cache")
@Api(value = "APIs for cache operations")
public class CacheController {

	@Autowired
	CacheService cacheService;

	@PostMapping
	@ApiOperation(value = "API to create cache")
	Cache createStudent(@RequestBody Cache cache) {
		return cacheService.addCache(cache);
	}

}
