package com.fastcampus.ch3;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;

public class DBConnectionTest2 {
//    @Test
    public void jdbcConnectionTest() throws Exception {
//        // 스키마의 이름(springbasic)이 다른 경우 알맞게 변경 , 대신 이렇게하면 데이터베이스 정보가 바뀔때마다 고치고 컴파일을 다시해야해서 불편해! 그래서 아래와 같은방법으로 한다.
//        직접 객체를 생성하는 방법
//        String DB_URL = "jdbc:mysql://localhost:3306/springbasic?useUnicode=true&characterEncoding=utf8";
//
//        // DB의 userid와 pwd를 알맞게 변경
//        String DB_USER = "oni";
//        String DB_PASSWORD = "1234";
//        String DB_DRIVER = "com.mysql.jdbc.Driver";
//
//        DriverManagerDataSource ds = new DriverManagerDataSource();
//        ds.setDriverClassName(DB_DRIVER);
//        ds.setUrl(DB_URL);
//        ds.setUsername(DB_USER);
//        ds.setPassword(DB_PASSWORD);

        //root-context에다가 접속정보를 주고 getBean으로 얻어온다.
        ApplicationContext ac = new GenericXmlApplicationContext("file:src/main/webapp/WEB-INF/spring/**/root-context.xml");
        DataSource ds = ac.getBean(DataSource.class);

        Connection conn = ds.getConnection(); // 데이터베이스의 연결을 얻는다.

        System.out.println("conn = " + conn);
//        assertTrue(conn!=null);
    }
}