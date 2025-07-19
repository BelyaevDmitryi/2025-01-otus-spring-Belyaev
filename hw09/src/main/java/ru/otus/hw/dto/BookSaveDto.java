package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookSaveDto {

    private long id;

    @NotBlank(message = "The title should not be empty")
    @Size(min = 1, max = 254, message = "Max symbols are 254")
    private String title;

    private long authorId;

    private long genreId;
}
