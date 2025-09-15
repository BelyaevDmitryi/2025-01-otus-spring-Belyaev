package ru.otus.hw.models.migrate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book_migrate")
public class BookMigrate {

    @Column(name = "id")
    private long id;

    @Id
    @Column(name = "mongo_id")
    private String mongoId;

}
