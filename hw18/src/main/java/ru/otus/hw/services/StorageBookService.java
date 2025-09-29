package ru.otus.hw.services;

import ru.otus.hw.dto.StorageInfoDto;

public interface StorageBookService {

    StorageInfoDto findInfo(Long bookId);
}

