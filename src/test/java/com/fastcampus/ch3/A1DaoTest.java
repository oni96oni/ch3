package com.fastcampus.ch3;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/**/root-context.xml"})
public class A1DaoTest {

    @Autowired
    A1Dao a1Dao;

    @Autowired
    DataSource ds;

    //root-context에다가 bean등록을 해준뒤에는 이렇게 할 수 있다.
    @Autowired
    DataSourceTransactionManager tm;
    @Test
    public void insertTest() throws Exception{
        // tx manager를 생성
//        PlatformTransactionManager tm = new DataSourceTransactionManager(ds);
        TransactionStatus status = tm.getTransaction(new DefaultTransactionDefinition());
        // tx 시작
        try {
            a1Dao.deleteAll();
            a1Dao.insert(1, 100);
            a1Dao.insert(2, 200);
            tm.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            tm.rollback(status);
        } finally {
        }
    }
}