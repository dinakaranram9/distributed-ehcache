package com.distributed.cache.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.distributed.cache.entity.QStudent;
import com.distributed.cache.entity.Student;
import com.distributed.cache.repository.StudentRepository;
import com.distributed.cache.service.CacheService;
import com.querydsl.core.types.Predicate;

@Service
public class StudentService {// extends AbstractMongoEventListener<Student>{

	@Autowired
	StudentRepository studentRepository;

	@Autowired
	SequenceGenerator sequenceGenerator;
	

	@Autowired
	MongoTemplate mongoTemplate;
	
	@CacheEvict(value = "user-cache")
	@Transactional
	public Student createStudent(Student student) {
		Student stud = studentRepository.save(student);
		System.out.println(stud.getId());
		return stud;
	}
	
	public Student getStudentById(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		QStudent student = new QStudent("student");
		Predicate predicate = student.id.eq(id);
		System.out.println(studentRepository.findOne(predicate));
		System.out.println(studentRepository.findById(id));
		return mongoTemplate.findOne(query, Student.class);
	}
	
	@Cacheable(value = "user-cache", keyGenerator = "cacheKeyGenerator")
	public List<Student> getAllStudent() {
		System.out.println("not cached");
		return (List<Student>)studentRepository.findAll();
	}

	public Student updateStudent(Student student) {
		return studentRepository.save(student);
	}

	public String deleteById(String id) {
		studentRepository.deleteById(Integer.parseInt(id));
		return "student deleted successfully";
	}

	public List<Student> getStudentByName(String name) {
		QStudent student = new QStudent("student");
		Predicate predicate = student.name.eq(name);
		return (List<Student>) studentRepository.findAll(predicate);
	}

	/**
	 * Sequence generator
	 */

	/*
	 * @Override public void onBeforeConvert(BeforeConvertEvent<Student> event) { //
	 * if (Integer.parseInt(event.getSource().getId()) < 1) {
	 * event.getSource().setId(String.valueOf(sequenceGenerator.generateSequence(
	 * Student.SEQUENCE_NAME))); // } }
	 */

	public Page<Student> getAllStudentByPage(Pageable pageable) {
		Sort sort = Sort.by(Sort.Direction.DESC, "name", "mailId");
		PageRequest pg = PageRequest.of(0, 8, sort);
		return studentRepository.findAll(pg);
	}

	public List<Student> getStudentsBySubject(String subject) {
		return studentRepository.findBySubjectsSubjectName(subject);
	}

}
