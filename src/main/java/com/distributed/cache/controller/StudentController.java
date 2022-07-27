package com.distributed.cache.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.distributed.cache.domain.PageableResponse;
import com.distributed.cache.entity.Student;
import com.distributed.cache.service.StudentService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/student")
@Api(value = "APIs for student operations")
public class StudentController {

	@Autowired
	StudentService studentService;

	@PostMapping("/create")
	@ApiOperation(value = "API to create student")
	Student createStudent(@RequestBody Student student) {
		return studentService.createStudent(student);
	}

	@GetMapping("/getStudent/{id}")
	@ApiOperation(value = "API to get student by id")
	Student getStudentById(@PathVariable("id") String id) {
		return studentService.getStudentById(id);
	}

	@GetMapping("/getAllStudent")
	@ApiOperation(value = "API to get student")
	List<Student> getAllStudent() {
		return studentService.getAllStudent();
	}

	@PutMapping("/updateStudent")
	@ApiOperation(value = "API to update student")
	Student updateStudent(@RequestBody Student student) {
		return studentService.updateStudent(student);
	}

	@DeleteMapping("/deleteMapping")
	@ApiOperation(value = "API to delete student")
	String deleteStuident(String id) {
		return studentService.deleteById(id);
	}

	@GetMapping("/getStudent/name/{name}")
	@ApiOperation(value = "API to get student by id")
	List<Student> getStudentByName(@PathVariable("name") String name) {
		return studentService.getStudentByName(name);
	}

	@GetMapping("/getAllByPage")
	@ApiOperation(value = "get all student by page")
	PageableResponse getAllStudent(@RequestBody Pageable pageable) {
		Page<Student> studentPage = studentService.getAllStudentByPage(pageable);
		return PageableResponse.builder().currentPage(studentPage.getNumber()).pageSize(studentPage.getSize())
				.totalPages(studentPage.getTotalPages()).totalElements(studentPage.getTotalElements())
				.results(studentPage.getContent()).build();
	}
	
	@GetMapping("/getBySubject")
	@ApiOperation(value = "getll all students by subject")
	List<Student> getStudentsBySubject(@RequestParam String subject){
		return studentService.getStudentsBySubject(subject);
	}

}
