package com.student.management.controller;

import com.student.management.model.Student;
import com.student.management.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // ==================== GET ENDPOINTS ====================

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Student> getStudentByEmail(@PathVariable String email) {
        Student student = studentService.getStudentByEmail(email);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Student>> getActiveStudents() {
        List<Student> students = studentService.getActiveStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<Student>> getInactiveStudents() {
        List<Student> students = studentService.getInactiveStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/course/{course}")
    public ResponseEntity<List<Student>> getStudentsByCourse(@PathVariable String course) {
        List<Student> students = studentService.getStudentsByCourse(course);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Student>> searchStudents(@RequestParam String name) {
        List<Student> students = studentService.searchStudentsByName(name);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    // ==================== POST ENDPOINTS ====================

    @PostMapping
    public ResponseEntity<?> createStudent(@Valid @RequestBody Student student) {
        try {
            Student createdStudent = studentService.createStudent(student);
            return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    // ==================== PUT ENDPOINTS ====================

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody Student studentDetails) {
        try {
            Student updatedStudent = studentService.updateStudent(id, studentDetails);
            return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    // ==================== PATCH ENDPOINTS ====================

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Student> deactivateStudent(@PathVariable Long id) {
        Student student = studentService.deactivateStudent(id);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Student> activateStudent(@PathVariable Long id) {
        Student student = studentService.activateStudent(id);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    // ==================== DELETE ENDPOINTS ====================

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // ==================== STATISTICS ====================

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        List<Student> allStudents = studentService.getAllStudents();
        List<Student> activeStudents = studentService.getActiveStudents();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalStudents", allStudents.size());
        stats.put("activeStudents", activeStudents.size());
        stats.put("inactiveStudents", allStudents.size() - activeStudents.size());

        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
}