//package com.stocks.demo;
//
//import com.stocks.demo.components.DataAccessService;
//import com.stocks.demo.model.Alarm;
//import com.stocks.demo.model.User;
//import com.stocks.demo.scheduler.AlarmsScheduler;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.util.UUID;
//
////@RunWith(SpringJUnit4ClassRunner.class)
////@Import(DataAccessService.class)
////@SpringBootTest
//public class JdbcTemplateTest {
//
//    @Autowired
//    DataAccessService dataAccessService;
//
////    @Test
//    public void test__CRUD__User() {
//        User user = new User(UUID.randomUUID(), "Michael", "Schmitz", "michaleschmitz@gmail.com", "cantbreakme");
//        int preInsertionCount = dataAccessService.selectAllUsers().size();
//        dataAccessService.insertUser(UUID.randomUUID(), user);
//        User addedUser = dataAccessService.selectAllUsers().get(preInsertionCount);
//        int postInsertionCount = dataAccessService.selectAllUsers().size();
//
//        //assert insertions
//        Assert.assertEquals(preInsertionCount, postInsertionCount - 1);
//
//        //asserts update
//        dataAccessService.updateEmail(addedUser.getUserId(), "schmitzmichael@yahoo.com");
//        Assert.assertEquals(dataAccessService.selectAllUsers().get(postInsertionCount - 1).getEmail(), "schmitzmichael@yahoo.com");
//
//        dataAccessService.updateFirstName(addedUser.getUserId(), "Mike");
//        Assert.assertEquals(dataAccessService.selectAllUsers().get(postInsertionCount - 1).getFirstName(), "Mike");
//
//        dataAccessService.updateLastName(addedUser.getUserId(), "Samuel");
//        Assert.assertEquals(dataAccessService.selectAllUsers().get(postInsertionCount - 1).getLastName(), "Samuel");
//
//        dataAccessService.deleteUserById(addedUser.getUserId());
//        int postDeletionCount = dataAccessService.selectAllUsers().size();
//
//        //asserts deletion
//        Assert.assertEquals(preInsertionCount, postDeletionCount);
//    }
//
////    @Test
//    public void test__CRUD_API__Alarm() {
//        User userFromDB = dataAccessService.selectAllUsers().get(0);
//        Alarm alarm = new Alarm(null, userFromDB.getUserId(), "W", 100, 1, 0, 0, true);
//        dataAccessService.insertAlarm(UUID.randomUUID(), alarm);
//        int postInsertionCount = dataAccessService.selectAllAlarms().size();
//        Alarm addedAlarm = dataAccessService.selectAllAlarms().get(postInsertionCount - 1);
//
//        //asserts current stock price is successfully updating via API
//        Assert.assertNotEquals(0, addedAlarm.getCurrentStockPrice());
//
//        dataAccessService.deleteAlarmById(addedAlarm.getAlarmId());
//        int postDeletionCount = dataAccessService.selectAllAlarms().size();
//
//        Assert.assertEquals(postInsertionCount, postDeletionCount + 1);
//    }
//
//
////    @Test
//    public void test__SATISFIED_ALARM__Alarm() {
//        User userFromDB = dataAccessService.selectAllUsers().get(0);
//        Alarm alarm = new Alarm(null, userFromDB.getUserId(), "W", 0, 1, 0, 0, true);
//        dataAccessService.insertAlarm(UUID.randomUUID(), alarm);
//        int postInsertionCount = dataAccessService.selectAllAlarms().size();
//        Alarm addedAlarm = dataAccessService.selectAllAlarms().get(postInsertionCount - 1);
//
//        //asserts the fact that the alarm target has benn reached;
//        Assert.assertFalse(AlarmsScheduler.isAlarmSatisfied(addedAlarm));
//
//        //cleanup
//        dataAccessService.deleteAlarmById(addedAlarm.getAlarmId());
//    }
//
//
//}
