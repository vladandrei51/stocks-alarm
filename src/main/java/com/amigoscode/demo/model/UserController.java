package com.amigoscode.demo.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get/users/")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/get/alarms/{id}")
    public List<Alarm> getAllAlarms(@PathVariable UUID id) {
        return userService.getAllAlarms(id);
    }

    @PostMapping(path = "/post/user", consumes = "application/json", produces = "application/json")
    public void addNewUser(@RequestBody @Valid User user) {
        userService.addNewUser(user);
    }

    @PostMapping(path = "/post/alarm", consumes = "application/json", produces = "application/json")
    public void addNewAlarm(@RequestBody @Valid Alarm alarm) {
        userService.addNewAlarm(alarm);
    }

    @GetMapping("/get/stocks/{stockSymbol}")
    public void getStockFromSearchEndpoint(@PathVariable String stockSymbol) {
        userService.getStocksFromSearchEndpoint(stockSymbol);
    }

    @PutMapping(path = "/put/{userId}")
    public void updateUser(@PathVariable("userId") UUID userId,
                           @RequestBody User user) {
        userService.updateUser(userId, user);
    }

    @DeleteMapping("/delete/user/{userId}")
    public void deleteUser(@PathVariable("userId") UUID userId) {
        userService.deleteUser(userId);
    }

    @DeleteMapping("/delete/alarm/{alarmId}")
    public void deleteAlarm(@PathVariable("alarmId") UUID alarmId) {
        userService.deleteAlarm(alarmId);
    }

}
