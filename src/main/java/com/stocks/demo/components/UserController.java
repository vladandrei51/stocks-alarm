package com.stocks.demo.components;

import com.stocks.demo.model.Alarm;
import com.stocks.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
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

    @GetMapping("/get/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/get/users/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.findUserByEmail(email);
    }

    @GetMapping("/get/alarm/{id}")
    public List<Alarm> getAllAlarms(@PathVariable UUID id) {
        return userService.getAllAlarms(id);
    }

    @PostMapping(path = "/post/user")
    public void addNewUser(@RequestBody @Valid User user) {
        userService.addNewUser(user);
    }

    @PostMapping(path = "/post/alarm")
    public void addNewAlarm(@RequestBody @Valid Alarm alarm) {
        userService.addNewAlarm(alarm);
    }

    @GetMapping("/get/stocks/{stockSymbol}")
    public void getStockFromSearchEndpoint(@PathVariable String stockSymbol) throws IOException {
        userService.getStocksFromSearchEndpoint(stockSymbol);
    }

    @PutMapping(path = "/put/user/{userId}")
    public void updateUser(@PathVariable("userId") UUID userId,
                           @RequestBody User user) {
        userService.updateUser(userId, user);
    }

    @PutMapping(path = "/put/alarm/{alarmId}")
    public void updateAlarm(@PathVariable("alarmId") UUID alarmId,
                            @RequestBody Alarm alarm) {
        userService.updateAlarm(alarmId, alarm);
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
