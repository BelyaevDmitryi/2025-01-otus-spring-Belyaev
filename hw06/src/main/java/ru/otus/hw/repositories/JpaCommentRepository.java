package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaCommentRepository implements CommentRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public JpaCommentRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Comment> findById(long id) {
        Comment comment = entityManager.find(Comment.class, id);
        return Optional.ofNullable(comment);
    }

    @Override
    public List<Comment> findByBookId(long bookId) {
        TypedQuery<Comment> query = entityManager.createQuery(
                "select bc from Comment bc where bc.book.id = :book_id",
                Comment.class);
        query.setParameter("book_id", bookId);
        return query.getResultList();
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == 0) {
            return insert(comment);
        }
        return update(comment);
    }

    @Override
    public void deleteById(long id) {
        try {
            Comment comment = entityManager.find(Comment.class, id);
            entityManager.remove(comment);
        } catch (NullPointerException e) {
            throw new NullPointerException("Comment with id " + id + " not found");
        }
    }

    private Comment insert(Comment comment) {
        entityManager.persist(comment);
        return comment;
    }

    private Comment update(Comment comment) {
        return entityManager.merge(comment);
    }
}
