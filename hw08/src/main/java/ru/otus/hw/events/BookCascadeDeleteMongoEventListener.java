package ru.otus.hw.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import ru.otus.hw.exceptions.MappingException;
import ru.otus.hw.repositories.CommentRepository;

import java.lang.reflect.Field;

import static org.jline.style.Styler.getSource;

@Component
@RequiredArgsConstructor
public class BookCascadeDeleteMongoEventListener extends AbstractMongoEventListener<Object> {

    private final CommentRepository commentRepository;

    private MongoOperations mongoOperations;

    public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
        ReflectionUtils.makeAccessible(field);

        if (field.isAnnotationPresent(DBRef.class) &&
                field.isAnnotationPresent(CascadeDelete.class)) {

            Object fieldValue = field.get(getSource());
            FieldCallback callback = null;
            if (fieldValue != null) {
                callback = new FieldCallback();
                ReflectionUtils.doWithFields(fieldValue.getClass(), callback);
            }
            if (callback != null && !callback.isIdFound()) {
                throw new MappingException("Cannot perform cascade delete on child object without id set");
            }
            mongoOperations.remove(fieldValue);
        }
    }

    @Bean
    public BookCascadeDeleteMongoEventListener bookCascadeDeleteMongoEventListener() {
        return new BookCascadeDeleteMongoEventListener(commentRepository);
    }

    @Getter
    public static class FieldCallback implements ReflectionUtils.FieldCallback {
        private boolean idFound;

        public void doWith(Field field) throws IllegalArgumentException {
            ReflectionUtils.makeAccessible(field);

            if (field.isAnnotationPresent(Id.class)) {
                idFound = true;
            }
        }
    }
}
