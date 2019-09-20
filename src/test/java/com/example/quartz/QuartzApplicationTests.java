package com.example.quartz;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuartzApplicationTests {

    @Test
    public void contextLoads() {
    }
    @Test
    public void testGetAllProperties(){
    }

    @Test
    public void testMysqlRowLock() throws SQLException, InterruptedException {


        new Thread(() -> {
            int n = 0;
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jobs?useUnicode=true&characterEncoding=utf-8&autoReconnect=true",
                        "root","670317483Sgy?");
                connection.setAutoCommit(false);
                PreparedStatement preparedStatement1 = connection.prepareStatement("select * from qrtz_locks for update ");
                ResultSet resultSet = preparedStatement1.executeQuery();
                if(resultSet.next()){
                    System.out.println(Thread.currentThread().getName() + "I have selected successfully but I don't commit "+ n);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
//        new Thread(() -> {
//            int n;
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jobs?useUnicode=true&characterEncoding=utf-8&autoReconnect=true",
                        "root","670317483Sgy?");
                connection.setAutoCommit(false);
                PreparedStatement preparedStatement2 = connection.prepareStatement("select * from qrtz_locks for update ");

                ResultSet resultSet = preparedStatement2.executeQuery();
                if(resultSet.next()){
                    System.out.println(Thread.currentThread().getName() + "I am not locked" + "n:");
                }
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
//        }).start();

        Thread.sleep(400000000);

    }

}
