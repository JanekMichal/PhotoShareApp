package com.janek.photoShareApp.service;

import com.janek.photoShareApp.models.User;
import com.janek.photoShareApp.models.UserPage;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    Page<User> getUsersPage(UserPage userPage);
}
