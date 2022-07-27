package com.distributed.cache.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.distributed.cache.entity.Student;

@Repository
public interface StudentRepository extends MongoRepository<Student, Integer>, QuerydslPredicateExecutor<Student> {

	@Query("{_id:?0}")
	Student findById(String id);
	
	List<Student> findBySubjectsSubjectName(String name);
}
