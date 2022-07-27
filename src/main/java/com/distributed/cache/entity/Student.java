package com.distributed.cache.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "Student")
public class Student {
	@Transient
    public static final String SEQUENCE_NAME = "student_sequence";
	@Id
	String id;
	String name;
	String mailId;
	Department department;
	List<Subject> subjects;
}
