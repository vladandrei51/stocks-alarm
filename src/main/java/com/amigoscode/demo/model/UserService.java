package com.amigoscode.demo.model;

import com.amigoscode.demo.EmailValidator;
import com.amigoscode.demo.exception.ApiRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final DataAccessService dataAccessService;
    private final EmailValidator emailValidator;

    @Autowired
    public UserService(DataAccessService dataAccessService,
                       EmailValidator emailValidator) {
        this.dataAccessService = dataAccessService;
        this.emailValidator = emailValidator;
    }

    List<User> getAllUsers() {
        return dataAccessService.selectAllUsers();
    }

    List<Alarm> getAllAlarms(UUID userId) {
        return dataAccessService.selectAllUsersAlarms(userId);
    }

    void addNewUser(User user) {
        addNewUser(null, user);
    }

    void addNewUser(UUID userId, User user) {
        UUID newUserId = Optional.ofNullable(userId)
                .orElse(UUID.randomUUID());

        if (!emailValidator.test(user.getEmail())) {
            throw new ApiRequestException(user.getEmail() + " is not valid");
        }

        if (dataAccessService.isEmailTaken(user.getEmail())) {
            throw new ApiRequestException(user.getEmail() + " is taken");
        }

        dataAccessService.insertUser(newUserId, user);
    }

    void addNewAlarm(UUID alarmId, Alarm alarm) {
        UUID newAlarmId = Optional.ofNullable(alarmId)
                .orElse(UUID.randomUUID());


        dataAccessService.insertAlarm(newAlarmId, alarm);
    }

    void addNewAlarm(Alarm alarm) {
        addNewAlarm(null, alarm);
    }

    public void updateUser(UUID userId, User user) {
        Optional.ofNullable(user.getEmail())
                .ifPresent(email -> {
                    boolean taken = dataAccessService.selectExistsEmail(userId, email);
                    if (!taken) {
                        dataAccessService.updateEmail(userId, email);
                    } else {
                        throw new IllegalStateException("Email already in use: " + user.getEmail());
                    }
                });

        Optional.ofNullable(user.getFirstName())
                .filter(fistName -> !StringUtils.isEmpty(fistName))
                .map(StringUtils::capitalize)
                .ifPresent(firstName -> dataAccessService.updateFirstName(userId, firstName));

        Optional.ofNullable(user.getLastName())
                .filter(lastName -> !StringUtils.isEmpty(lastName))
                .map(StringUtils::capitalize)
                .ifPresent(lastName -> dataAccessService.updateLastName(userId, lastName));
    }

    void deleteUser(UUID userId) {
        dataAccessService.deleteUserById(userId);
    }

    void deleteAlarm(UUID alarmId) {
        dataAccessService.deleteAlarmById(alarmId);
    }
}
