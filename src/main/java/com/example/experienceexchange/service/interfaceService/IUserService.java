package com.example.experienceexchange.service.interfaceService;

import com.example.experienceexchange.dto.*;
import com.example.experienceexchange.security.JwtUserDetails;

import java.util.List;

public interface IUserService {

    UserDto getAccount(JwtUserDetails userDetails);

    List<CourseDto> getCreatedCoursesByUser(JwtUserDetails userDetails);

    List<LessonDto> getLessonsSubscribedByUser(JwtUserDetails userDetails);

    List<CourseDto> getCoursesSubscribedByUser(JwtUserDetails userDetails);

    List<LessonDto> getCreatedLessonsByUser(JwtUserDetails userDetails);

    UserDto editAccount(JwtUserDetails userDetails, UserDto userDto);

    void changePassword(JwtUserDetails userDetails, NewPasswordDto passwordDto);

    void changeEmail(JwtUserDetails jwtUserDetails, NewEmailDto newEmailDto);

    List<PaymentDto> getPayments(JwtUserDetails userDetails);
}
