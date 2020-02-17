package com.amigoscode.demo.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/students")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllStudents() {
        return userService.getAllUsers();
    }

    @PostMapping
    public void addNewStudent(@RequestBody @Valid User user) {
        userService.addNewUser(user);
    }

    @PostMapping
    public void addNewAlarm(@RequestBody @Valid Alarm alarm) {
        userService.addNewAlarm(alarm);
    }


    @PutMapping(path = "{userId}")
    public void updateStudent(@PathVariable("userId") UUID studentId,
                              @RequestBody User user) {
        userService.updateUser(studentId, user);
    }

    @DeleteMapping("{userId}")
    public void deleteStudent(@PathVariable("userId") UUID userId) {
        userService.deleteUser(userId);
    }

    @DeleteMapping("{userId}")
    public void deleteAlarm(@PathVariable("alarmId") UUID alarmId) {
        userService.deleteAlarm(alarmId);
    }

}
