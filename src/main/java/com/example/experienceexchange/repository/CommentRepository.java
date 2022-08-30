package com.example.experienceexchange.repository;

import com.example.experienceexchange.model.Comment;
import com.example.experienceexchange.repository.interfaceRepo.ICommentRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CommentRepository extends HibernateAbstractDao<Comment, Long> implements ICommentRepository {
    //
}
