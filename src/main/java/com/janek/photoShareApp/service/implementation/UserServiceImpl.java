package com.janek.photoShareApp.service.implementation;

import com.janek.photoShareApp.models.User;
import com.janek.photoShareApp.models.UserPage;
import com.janek.photoShareApp.repository.UserRepository;
import com.janek.photoShareApp.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<User> getUsersPage(UserPage userPage) {
        Sort sort = Sort.by(userPage.getSortDirection(), userPage.getSortBy());

        Pageable pageable = PageRequest.of(userPage.getPageNumber(),
                userPage.getPageSize(), sort);
        return userRepository.findAll(pageable);
    }
}
