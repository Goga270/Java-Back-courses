package com.example.experienceexchange.repository;

import com.example.experienceexchange.model.Comment;
import com.example.experienceexchange.repository.interfaceRepo.ICommentRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class CommentRepository extends HibernateAbstractDao<Comment, Long> implements ICommentRepository {

    @Override
    public List<Comment> findAllCommentsByCourseId(Long userId) {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<Comment> cq = cb.createQuery(getClassEntity());
        Root<Comment> root = cq.from(getClassEntity());

        cq.select(root).where(cb.equal(root.get("course"), userId));

        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<Comment> findAllCommentsByLessonId(Long userId) {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<Comment> cq = cb.createQuery(getClassEntity());
        Root<Comment> root = cq.from(getClassEntity());

        cq.select(root).where(cb.equal(root.get("lesson"), userId));

        return entityManager.createQuery(cq).getResultList();
    }
}
