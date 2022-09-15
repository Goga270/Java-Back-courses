package com.example.experienceexchange.repository.interfaceRepo;

import com.example.experienceexchange.model.Comment;

import java.util.List;

public interface ICommentRepository extends GenericDao<Comment, Long> {

    List<Comment> findAllCommentsByCourseId(Long userId);

    List<Comment> findAllCommentsByLessonId(Long userId);
}
