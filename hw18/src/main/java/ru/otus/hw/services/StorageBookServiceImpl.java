package ru.otus.hw.services;

import org.springframework.stereotype.Service;
import ru.otus.hw.dto.StorageInfoDto;

@Service
public class StorageBookServiceImpl implements StorageBookService {

    @Override
    public StorageInfoDto findInfo(Long bookId) {
        return new StorageInfoDto(bookId, "Code", "Number");
    }
}
