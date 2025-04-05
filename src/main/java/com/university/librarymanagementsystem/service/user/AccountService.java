package com.university.librarymanagementsystem.service.user;

import com.university.librarymanagementsystem.dto.user.AccountDTO;

public interface AccountService {
    AccountDTO fetchAccountByID(String user_id);
}
