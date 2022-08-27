package com.example.experienceexchange.service;

import com.example.experienceexchange.dto.CommentDto;
import com.example.experienceexchange.dto.CourseDto;
import com.example.experienceexchange.exception.CourseNotFoundException;
import com.example.experienceexchange.exception.UserNotFoundException;
import com.example.experienceexchange.model.Comment;
import com.example.experienceexchange.model.Course;
import com.example.experienceexchange.model.User;
import com.example.experienceexchange.repository.interfaceRepo.ICourseRepository;
import com.example.experienceexchange.repository.interfaceRepo.IUserRepository;
import com.example.experienceexchange.security.JwtUserDetails;
import com.example.experienceexchange.service.interfaceService.ICourseService;
import com.example.experienceexchange.util.date.DateUtil;
import com.example.experienceexchange.util.mapper.CommentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class CourseService implements ICourseService {

    private final ICourseRepository courseRepository;
    private final IUserRepository userRepository;
    private final CommentMapper commentMapper;

    public CourseService(ICourseRepository courseRepository, IUserRepository userRepository, CommentMapper commentMapper) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    public Set<CourseDto> getCoursesByDirection() {
        return null;
    }

    @Override
    public void createCourse(JwtUserDetails userDetails, CourseDto courseDto) {

    }

    @Override
    public CourseDto editCourse(Long id, CourseDto courseDto) {
        return null;
    }

    @Override
    public void deleteCourse(Long id) {

    }

    @Override
    public void subscribeToCourse(Long id, JwtUserDetails userDetails) {

    }
    // TODO : меняется ли product_id ?
    @Transactional
    @Override
    public CommentDto createComment(Long courseId, JwtUserDetails userDetails, CommentDto commentDto) {
        Course course = getCourseById(courseId);
        User user = userRepository.find(userDetails.getId());
        Comment comment = commentMapper.commentDtoToComment(commentDto);
        comment.setCreated(DateUtil.DateTimeNow());
        comment.setAuthor(user);
        course.addComment(comment);
        courseRepository.update(course);
        CommentDto dto = commentMapper.commentToCommentDto(comment);
        return dto;
    }

    private Course getCourseById(Long id) {
        Course course = courseRepository.find(id);
        if (course == null) {
            throw new CourseNotFoundException(id);
        }
        return course;
    }
}
