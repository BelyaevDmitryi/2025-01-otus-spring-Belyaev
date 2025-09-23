package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookSaveDto {

    @Id
    private String id;

    @NotBlank(message = "The title should not be empty")
    @Size(min = 1, max = 256, message = "Title must contain from 1 to 256 characters")
    private String title;

    @NotNull
    private String authorId;

    @NotNull
    private String genreId;
}
