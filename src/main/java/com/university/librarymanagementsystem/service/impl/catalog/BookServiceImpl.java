package com.university.librarymanagementsystem.service.impl.catalog;

import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.repository.catalog.BookRepository;
import com.university.librarymanagementsystem.service.catalog.BookService;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;
}
