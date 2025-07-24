package com.ebrahimi.azmoon.service;

import com.ebrahimi.azmoon.dto.PageRequestDTO;
import com.ebrahimi.azmoon.dto.course.*;
import com.ebrahimi.azmoon.dto.user.UserDetailDTO;
import com.ebrahimi.azmoon.exception.AlreadyExistException;
import com.ebrahimi.azmoon.exception.BadRequestException;
import com.ebrahimi.azmoon.exception.NotFoundException;
import com.ebrahimi.azmoon.model.Course;
import com.ebrahimi.azmoon.model.RegisterStatus;
import com.ebrahimi.azmoon.model.UserAccount;
import com.ebrahimi.azmoon.model.UserRole;
import com.ebrahimi.azmoon.repository.CourseRepo;
import com.ebrahimi.azmoon.repository.UserAccountRepo;
import com.ebrahimi.azmoon.security.util.SecurityHelper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {
    private final CourseRepo courseRepo;
    private final UserAccountRepo userAccountRepo;

    public CourseService(CourseRepo courseRepo, UserAccountRepo userAccountRepo) {
        this.courseRepo = courseRepo;
        this.userAccountRepo = userAccountRepo;
    }

    @Transactional
    public Boolean addCourse(CreateCourseDTO createCourseDTO) {
        if (courseRepo.existsCourseByCourseName(createCourseDTO.getCourseName())) {
            throw new AlreadyExistException("Course already exists");
        }
        if (createCourseDTO.getStartDate().isAfter(createCourseDTO.getEndDate())) {
            throw new BadRequestException("START-date canNOT be After END-date!");
        }
        Course course = Course.builder()
                .courseName(createCourseDTO.getCourseName())
                .startDate(createCourseDTO.getStartDate())
                .endDate(createCourseDTO.getEndDate())
                .build();
        courseRepo.save(course);
        return Boolean.TRUE;
    }

    public PageCourseDTO findAllCourses(PageRequestDTO pageRequestDTO) {
        Page<Course> courseList = courseRepo
                .findAll(PageRequest.of(pageRequestDTO.getPage(), pageRequestDTO.getSize()));
        List<CourseDTO> data = courseList.stream()
                .map(course -> CourseDTO.builder()
                        .id(course.getId())
                        .courseName(course.getCourseName())
                        .startDate(course.getStartDate())
                        .endDate(course.getEndDate())
                        .build())
                .toList();
        return PageCourseDTO.builder().data(data).total(courseList.getTotalElements()).build();
    }

    @Transactional
    public Boolean patchCourse(PatchCourseDTO patchCourseDTO) {
        Course course = courseRepo.findById(patchCourseDTO.getCourseID()).orElseThrow(()
                -> new NotFoundException("Course not found!"));
        if (patchCourseDTO.getTeacherID() != null) {
            UserAccount maybeTeacher = userAccountRepo
                    .findById(patchCourseDTO.getTeacherID()).orElseThrow(()
                            -> new NotFoundException("Teacher not found!"));
            if (!maybeTeacher.getUserRole().equals(UserRole.TEACHER)) {
                throw new BadRequestException("This User is NOT Teacher!");
            } else if (!maybeTeacher.getRegisterStatus().equals(RegisterStatus.APPROVED)) {
                throw new BadRequestException("This Teacher is Not APPROVED!");
            } else {
                course.setTeacher(maybeTeacher);
            }
        }
        if (patchCourseDTO.getStudentIDs() != null && !patchCourseDTO.getStudentIDs().isEmpty()) {
            List<UserAccount> maybeStudents = patchCourseDTO.getStudentIDs().stream()
                    .map(id -> userAccountRepo.findById(id).orElseThrow(()
                            -> new NotFoundException("Student Not Found!")))
                    .collect(Collectors.toList());
            maybeStudents.forEach(student -> {
                if (!student.getUserRole().equals(UserRole.STUDENT)) {
                    throw new BadRequestException("This User is NOT Student!");
                }
                if (!student.getRegisterStatus().equals(RegisterStatus.APPROVED)) {
                    throw new BadRequestException("This Student is Not APPROVED!");
                }
            });
            course.setStudents(maybeStudents);
        }
        if (patchCourseDTO.getCourseName() != null) {
            course.setCourseName(patchCourseDTO.getCourseName());
        }
        courseRepo.save(course);
        return Boolean.TRUE;
    }

    public CourseDetailDTO findCourseDetailById(Integer id) {
        Course course = courseRepo.findById(id).orElseThrow(()
                -> new NotFoundException("Course not found!"));
        UserAccount teacher = course.getTeacher();
        UserDetailDTO teacherDTO = UserDetailDTO.builder()
                .id(teacher.getId())
                .name(teacher.getName())
                .userRole(teacher.getUserRole())
                .registerStatus(teacher.getRegisterStatus())
                .build();
        List<UserDetailDTO> students = course.getStudents().stream().map(student ->
                UserDetailDTO.builder()
                        .id(student.getId())
                        .name(student.getName())
                        .userRole(student.getUserRole())
                        .registerStatus(student.getRegisterStatus())
                        .build()
        ).collect(Collectors.toList());
        CourseDetailDTO courseDetails = CourseDetailDTO.builder()
                .courseId(course.getId())
                .courseName(course.getCourseName())
                .startDate(course.getStartDate())
                .endDate(course.getEndDate())
                .teacher(teacherDTO)
                .students(students)
                .build();
        return courseDetails;
    }

    public PageCourseDTO findAllTeacherCourses(PageRequestDTO pageRequestDTO) {
        Integer teacherId = SecurityHelper.getCurrentUser().getId();
        Page<Course> courseList = courseRepo
                .findByTeacherId(teacherId, PageRequest.of(pageRequestDTO.getPage(), pageRequestDTO.getSize()));
        List<CourseDTO> data = courseList.stream()
                .map(course -> CourseDTO.builder()
                        .id(course.getId())
                        .courseName(course.getCourseName())
                        .startDate(course.getStartDate())
                        .endDate(course.getEndDate())
                        .build())
                .toList();
        return PageCourseDTO.builder().data(data).total(courseList.getTotalElements()).build();
    }

    public PageCourseDTO findAllStudentCourses(PageRequestDTO pageRequestDTO) {
        Integer studentId = SecurityHelper.getCurrentUser().getId();
//        UserAccount maybeStudent = userAccountRepo.findById(studentId)
//                .orElseThrow(() -> new NotFoundException("User Not Found!"));
//        if (!maybeStudent.getUserRole().equals(UserRole.STUDENT)) {
//            throw new BadRequestException("This Student is Not Student!");
//        }
//        if (maybeStudent.getRegisterStatus().equals(RegisterStatus.WAITING)) {
//            throw new BadRequestException("This Student is Not APPROVED!");
//        }
        Page<Course> coursesByStudentId = courseRepo
                .findByStudentId(studentId, PageRequest.of(pageRequestDTO.getPage(), pageRequestDTO.getSize()));
        List<CourseDTO> data = coursesByStudentId.stream().map(course -> CourseDTO.builder()
                .id(course.getId())
                .courseName(course.getCourseName())
                .startDate(course.getStartDate())
                .endDate(course.getEndDate())
                .build()).toList();
        return PageCourseDTO.builder().data(data).total(coursesByStudentId.getTotalElements()).build();
    }
}
