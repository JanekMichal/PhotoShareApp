package com.janek.photoShareApp.controllers;

import com.janek.photoShareApp.models.ERole;
import com.janek.photoShareApp.models.User;
import com.janek.photoShareApp.models.UserPage;
import com.janek.photoShareApp.payload.request.UserDataUpdateRequest;
import com.janek.photoShareApp.payload.request.UserPasswordUpdateRequest;
import com.janek.photoShareApp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user/")
public class UserController {

    UserService userService;

    @GetMapping("page_users/{page}")
    public ResponseEntity<List<User>> getUsersPage(@PathVariable("page") int pageNumber, UserPage userPage) {
        return userService.getUsersPage(pageNumber, userPage);
    }

    @GetMapping("search/{name}")
    public ResponseEntity<?> searchForUser(@PathVariable String name) {
        return userService.searchForUser(name);
    }

    //TODO: zobaczyć do czego to jest
    @GetMapping("profile/{userId}")
    public ResponseEntity<User> getUserProfile(@PathVariable("userId") Long userId) {
        return userService.getUserProfile(userId);
    }

//    //TODO: zobaczyć czy to nadal jest potrzebne
//    @PatchMapping(path = "profile/{id}/email/{email}")
//    public ResponseEntity<?> updateEmail(@PathVariable Long id, @PathVariable String email) {
//        return userService.updateEmail(id, email);
//    }

    @Transactional
    @DeleteMapping("delete_someone_else_account/{userId}")
    public ResponseEntity<?> deleteSomeoneElseAccount(@PathVariable("userId") Long userId) {
        return userService.deleteSomeoneElseAccount(userId);
    }

    @Transactional
    @DeleteMapping("delete_own_account/{userId}")
    public ResponseEntity<?> deleteOwnAccount(@PathVariable("userId") Long userId) {
        return userService.deleteOwnAccount(userId);
    }

    @PatchMapping(path = "update_user_data/{id}")
    public ResponseEntity<?> updateUserData(@PathVariable(name = "id") Long userId,
                                            @RequestBody UserDataUpdateRequest userDataUpdateRequest) {
        return userService.updateUserData(userId, userDataUpdateRequest);
    }

    //TODO: nie powinno być id
    @Transactional
    @PatchMapping(path = "change_user_password/{id}")
    public ResponseEntity<?> changeUserPassword(@PathVariable(name = "id") Long userId,
                                                @RequestBody UserPasswordUpdateRequest userPasswordUpdateRequest) {
        return userService.changeUserPassword(userId, userPasswordUpdateRequest);
    }

    @Transactional
    @PatchMapping("give_moderator_role/{id}")
    public ResponseEntity<?> giveModeratorRole(@PathVariable("id") Long userId) {
        return userService.giveRole(userId, ERole.ROLE_MODERATOR);
    }

    @Transactional
    @PatchMapping("give_admin_role/{id}")
    public ResponseEntity<?> giveAdminRole(@PathVariable("id") Long userId) {
        return userService.giveRole(userId, ERole.ROLE_ADMIN);
    }

    @Transactional
    @PatchMapping("give_user_role/{id}")
    public ResponseEntity<?> giveUserRole(@PathVariable("id") Long userId) {
        return userService.giveRole(userId, ERole.ROLE_USER);
    }
}
