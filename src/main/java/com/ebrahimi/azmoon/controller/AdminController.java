package com.ebrahimi.azmoon.controller;

import com.ebrahimi.azmoon.dto.PageRequestDTO;
import com.ebrahimi.azmoon.dto.course.CourseDetailDTO;
import com.ebrahimi.azmoon.dto.course.CreateCourseDTO;
import com.ebrahimi.azmoon.dto.course.PageCourseDTO;
import com.ebrahimi.azmoon.dto.course.PatchCourseDTO;
import com.ebrahimi.azmoon.dto.user.PageUserDTO;
import com.ebrahimi.azmoon.dto.user.PatchUserAccountDTO;
import com.ebrahimi.azmoon.dto.user.UserAccountFilterDTO;
import com.ebrahimi.azmoon.model.RegisterStatus;
import com.ebrahimi.azmoon.model.UserRole;
import com.ebrahimi.azmoon.service.CourseService;
import com.ebrahimi.azmoon.service.UserAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAnyRole('ADMIN')")
public class AdminController {
    private final UserAccountService userAccountService;
    private final CourseService courseService;

    public AdminController(UserAccountService userAccountService, CourseService courseService) {
        this.userAccountService = userAccountService;
        this.courseService = courseService;
    }

    @GetMapping("/register-user")
    public ResponseEntity<PageUserDTO> findAllWaitingUsers(@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                                           @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.ok(userAccountService.findAllWaitingUsers(PageRequestDTO.builder().page(page).size(size).build()));
    }

    @PatchMapping("/register-user")
    public ResponseEntity<Boolean> pathRegisterUser(@RequestBody @Validated PatchUserAccountDTO patchUserAccountDTO) {
        Boolean result = userAccountService.changeUserInfo(patchUserAccountDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/users")
    public ResponseEntity<PageUserDTO> getFilteredUsers(@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                                        @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
                                                        @RequestParam(name = "name", required = false) String name,
                                                        @RequestParam(name = "userRole", required = false) UserRole userRole,
                                                        @RequestParam(name = "registerStatus", required = false) RegisterStatus registerStatus) {
        UserAccountFilterDTO userAccountFilterDTO = UserAccountFilterDTO.builder()
                .name(name).userRole(userRole).registerStatus(registerStatus)
                .build();
        return ResponseEntity.ok(userAccountService.getFilteredUsers(userAccountFilterDTO, PageRequestDTO.builder().page(page).size(size).build()));
    }

    @GetMapping("/courses")
    public ResponseEntity<PageCourseDTO> findAllCourses(@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                                        @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.ok(courseService.findAllCourses(PageRequestDTO.builder().page(page).size(size).build()));
    }

    @PostMapping("/add-course")
    public ResponseEntity<Boolean> addCourse(@RequestBody @Validated CreateCourseDTO createCourseDTO) {
        Boolean result = courseService.addCourse(createCourseDTO);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/patch-course")
    public ResponseEntity<Boolean> patchCourse(@RequestBody @Validated PatchCourseDTO patchCourseDTO) {
        boolean result = courseService.patchCourse(patchCourseDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/course-detail/{courseId}")
    public ResponseEntity<CourseDetailDTO> getCourseDetail(@PathVariable("courseId") Integer courseId) {
        return ResponseEntity.ok(courseService.findCourseDetailById(courseId));
    }
}
