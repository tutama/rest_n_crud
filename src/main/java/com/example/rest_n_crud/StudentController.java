package com.example.rest_n_crud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class StudentController {

	@Autowired
	private StudentService studentService;

	@Autowired
	private StudentRepository studentRepository;

	// http://localhost:8080/student
	@GetMapping("/student")
	public Student getStudent() {
		return new Student("Thesa", "Utama");
	}

	@GetMapping("/students")
	public List<Student> getStudents(){
		List<Student> students = new ArrayList<>();
		students.add(new Student("Thesa", "Utama"));
		students.add(new Student("Lukas", "Asmi"));
		students.add(new Student("Michael", "Bony"));
		students.add(new Student("Sukri", "Jadul"));
		students.add(new Student("Nana", "Utama"));
		return students;
	}

	// http://localhost:8080/student/1
	// @PathVariable annotation
	@GetMapping("/student/{firstName}/{lastName}/")
	public Student studentPathVariable(@PathVariable("firstName") String firstName,
									   @PathVariable("lastName") String lastName) {
		return new Student(firstName, lastName);
	}

	// build rest API to handle query parameters
	// http://localhost:8080/student/query?firstName=Thesa&lastName=Utama
	@GetMapping("/student/query")
	public Student studentQueryParam(
			@RequestParam(name = "firstName") String firstName,
			@RequestParam(name = "lastName") String lastName) {
		return new Student(firstName, lastName);
	}

	// build rest API to handle post with JSON query body
	// http://localhost:8080/student/postquery
	//	{
	//		"firstName": "Thesa",
	//		"lastName": "Utama"
	//	}
	@PostMapping(value = "/student/postquery", consumes = "application/json", produces = "application/json")
	public Student updatePerson(@RequestBody Student student) {
		Student _student = studentRepository
				.save(new Student(student.getFirstName(), student.getLastName()));
		return _student;
	}

	@GetMapping("/list-students")
	public ResponseEntity<List<Student>> getAllStudents(
			@RequestParam(required = false) String firstName,
			@RequestParam(required = false) String lastName) {
		try {
			List<Student> students = new ArrayList<Student>();

			if (firstName != null) {
				studentRepository.findByFirstName(firstName).forEach(students::add);
			} else if (firstName != null) {
				studentRepository.findByLastName(firstName).forEach(students::add);
			} else {
				studentRepository.findAll().forEach(students::add);
			}

			if (students.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(students, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(value = "/update-student/{id}", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Student> updateStudent(@PathVariable("id") int id, @RequestBody Student student) {
		Optional<Student> studentData = studentRepository.findById(id);

		if (studentData.isPresent()) {
			Student _student = studentData.get();
			_student.setFirstName(student.getFirstName());
			_student.setLastName(student.getLastName());
			return new ResponseEntity<>(studentRepository.save(_student), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/delete-student/{id}")
	public ResponseEntity<HttpStatus> deleteStudent(@PathVariable("id") int id) {
		try {
			studentRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
