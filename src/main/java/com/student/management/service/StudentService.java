package com.student.management.service;

import com.student.management.exception.ResourceNotFoundException;
import com.student.management.model.Student;
import com.student.management.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // Get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Get student by ID
    public Student getStudentById(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            return student.get();
        } else {
            throw new ResourceNotFoundException("Student", "id", id);
        }
    }

    // Get student by email
    public Student getStudentByEmail(String email) {
        Optional<Student> student = studentRepository.findByEmail(email);
        if (student.isPresent()) {
            return student.get();
        } else {
            throw new ResourceNotFoundException("Student", "email", email);
        }
    }

    // Check if email exists
    public boolean emailExists(String email) {
        return studentRepository.existsByEmail(email);
    }

    // Create new student
    public Student createStudent(Student student) {
        // Check if email already exists
        if (emailExists(student.getEmail())) {
            throw new IllegalArgumentException("Email '" + student.getEmail() + "' already exists.");
        }

        return studentRepository.save(student);
    }

    // Update student
    public Student updateStudent(Long id, Student studentDetails) {
        Student student = getStudentById(id);

        // If email is being changed, check if new email exists
        if (!student.getEmail().equals(studentDetails.getEmail()) &&
                emailExists(studentDetails.getEmail())) {
            throw new IllegalArgumentException("Email '" + studentDetails.getEmail() + "' already exists.");
        }

        student.setFirstName(studentDetails.getFirstName());
        student.setLastName(studentDetails.getLastName());
        student.setEmail(studentDetails.getEmail());
        student.setPhoneNumber(studentDetails.getPhoneNumber());
        student.setDateOfBirth(studentDetails.getDateOfBirth());
        student.setAddress(studentDetails.getAddress());
        student.setCourse(studentDetails.getCourse());
        student.setIsActive(studentDetails.getIsActive());

        return studentRepository.save(student);
    }

    // Delete student permanently
    public void deleteStudent(Long id) {
        Student student = getStudentById(id);
        studentRepository.delete(student);
    }

    // Soft delete (mark as inactive)
    public Student deactivateStudent(Long id) {
        Student student = getStudentById(id);
        student.setIsActive(false);
        return studentRepository.save(student);
    }

    // Activate student
    public Student activateStudent(Long id) {
        Student student = getStudentById(id);
        student.setIsActive(true);
        return studentRepository.save(student);
    }

    // Search students by name
    public List<Student> searchStudentsByName(String name) {
        return studentRepository.findByNameContaining(name);
    }

    // Get active students
    public List<Student> getActiveStudents() {
        return studentRepository.findByIsActiveTrue();
    }

    // Get inactive students
    public List<Student> getInactiveStudents() {
        return studentRepository.findByIsActiveFalse();
    }

    // Get students by course
    public List<Student> getStudentsByCourse(String course) {
        return studentRepository.findByCourse(course);
    }
}