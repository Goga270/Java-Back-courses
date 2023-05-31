package com.example.experienceexchange.controller;


import com.example.experienceexchange.dto.CourseDto;
import com.example.experienceexchange.dto.LessonOnCourseDto;
import com.example.experienceexchange.dto.PaymentDto;
import com.example.experienceexchange.dto.SkillDto;
import com.example.experienceexchange.model.Skill;
import com.example.experienceexchange.repository.filter.SearchCriteria;
import com.example.experienceexchange.security.JwtUserDetails;
import com.example.experienceexchange.service.interfaceService.ICourseService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Работа с курсами
 */
@Validated
@RestController
@RequestMapping("/courses")
public class CourseController {

    private final ICourseService courseService;

    public CourseController(ICourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * Найти курсы по заданными фильтрам
     * @param filters Параметры фильтрации
     * @return Найденные курсы по фильтрам
     */
    @JsonView({CourseDto.Details.class})
    @PostMapping("")
    public List<CourseDto> getCoursesByFilter(@RequestBody @Valid List<SearchCriteria> filters) {
        return courseService.getCourses(filters);
    }

    /**
     * Найти курс по идентификатору
     * @param courseId Идентификатор курса
     * @return Найденный курс
     */
    @JsonView({CourseDto.Details.class})
    @GetMapping("/{id}")
    public CourseDto getCourse(@PathVariable("id") Long courseId) {
        return courseService.getCourse(courseId);
    }

    /**
     * Найти все навыки
     * @return Найденные навыки
     */
    @GetMapping("/skills")
    public List<SkillDto> getSkills() {
        return courseService.getSkills();
    }

    /**
     * Найти определенный урок на определенном курсе
     * @param userDetails Информация о пользователе
     * @param courseId Идентификатор курса
     * @param lessonId Идентификатор урока
     * @return
     */
    @JsonView({LessonOnCourseDto.DetailsForSubscribe.class})
    @GetMapping("/{courseId}/{lessonId}")
    public LessonOnCourseDto getLessonOnCourse(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @PathVariable Long courseId,
            @PathVariable Long lessonId) {
        return courseService.getLessonOnCourse(userDetails, courseId, lessonId);
    }

    /**
     * Вывести расписание уроков
     * @param userDetails Информация о пользователе
     * @return Расписание уроков
     */
    @JsonView(LessonOnCourseDto.DetailsForTimetable.class)
    @GetMapping("/my-schedule-courses")
    public List<LessonOnCourseDto> getSchedule(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return courseService.getSchedule(userDetails);
    }

    /**
     * Вывести все уроки на курсе
     * @param courseId Идентификатор курса
     * @return Найденные уроки
     */
    @JsonView(LessonOnCourseDto.DetailsForTimetable.class)
    @GetMapping("/get-lessons-on-course")
    public List<LessonOnCourseDto> getlessonOnCourse(@RequestParam("id") Long courseId) {
        return courseService.getlessonsByCourse(courseId);
    }

    /**
     * Вывести расписание на определенном курсе
     * @param userDetails Информация о пользователе
     * @param courseId Идентификатор курса
     * @return Найденные уроки на курсе
     */
    @JsonView(LessonOnCourseDto.DetailsForTimetable.class)
    @GetMapping("/{id}/my-schedule-course")
    public List<LessonOnCourseDto> getScheduleByCourse(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @PathVariable("id") Long courseId) {
        return courseService.getScheduleByCourse(userDetails, courseId);
    }

    /**
     * Создать новый курс
     * @param userDetails Информация о пользователе
     * @param courseDto Информация о курсе
     * @return Новый курс
     */
    @PostMapping("/new-course")
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDto createCourse(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @RequestBody @Validated(CourseDto.Create.class) CourseDto courseDto) {
        return courseService.createCourse(userDetails, courseDto);
    }

    /**
     * Создать новый урок на определенном курсе
     * @param userDetails Информация о пользователе
     * @param courseId Идентификатор курса
     * @param lesson Информация об уроке
     * @return Новый урок
     */
    @PostMapping("/{id}/settings/new-lesson")
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDto createLesson(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @PathVariable("id") Long courseId,
            @RequestBody @Validated({LessonOnCourseDto.Create.class}) LessonOnCourseDto lesson) {
        return courseService.createLessonOnCourse(userDetails, courseId, lesson);
    }

    /**
     * Редактировать курс
     * @param userDetails Информация о пользователе
     * @param id Идентификатор курса
     * @param courseDto Редактированные данные о курсе
     * @return От редактируемый курс
     */
    @PutMapping("/{id}/settings")
    public CourseDto editCourse(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @PathVariable Long id,
            @RequestBody @Validated(value = CourseDto.Edit.class) CourseDto courseDto) {
        return courseService.editCourse(userDetails, id, courseDto);
    }

    /**
     * Перезапустить курс
     * @param userDetails Информация о пользователе
     * @param courseDto Информация о курсе
     * @return От редактировнный курс
     */
    @PutMapping("/restart-course")
    public CourseDto restartCourse(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @RequestBody @Validated(value = CourseDto.Edit.class) CourseDto courseDto) {
        return courseService.restartCourse(userDetails, courseDto);
    }

    /**
     * Подписаться на курс
     * @param userDetails Информация о пользователе
     * @param courseId Идентификатор курса
     * @param paymentDto Данные оплаты
     * @return Данные оплаты
     */
    @JsonView({PaymentDto.CreateCourse.class})
    @PostMapping("/{id}/subscribe")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentDto subscribeToCourse(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @PathVariable("id") Long courseId,
            @RequestBody @Validated(PaymentDto.CreateCourse.class) PaymentDto paymentDto) {
        return courseService.subscribeToCourse(userDetails, paymentDto, courseId);
    }

    /**
     * Удалить курс
     * @param userDetails Информация о пользователе
     * @param courseId Идентификтор курса
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCourse(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @PathVariable("id") Long courseId) {
        courseService.deleteCourse(userDetails, courseId);
    }
}
