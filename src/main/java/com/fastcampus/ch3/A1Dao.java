package com.fastcampus.ch3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class A1Dao {
    @Autowired
    DataSource ds;



    public int insert(int key, int value) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
//            conn = ds.getConnection();
            conn = DataSourceUtils.getConnection(ds);
            System.out.println("conn = " + conn); //txmanager를 안쓰면 다른 커넥션객체를 쓰게된다.
            pstmt = conn.prepareStatement("insert into a1 values(?,?)");
            pstmt.setInt(1,key);
            pstmt.setInt(2,value);

            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; //에러 발생시 다시 던져주어야 commit이 아니라 rollback한다!
        } finally {
//            close(conn, pstmt);
            close(pstmt);
            DataSourceUtils.releaseConnection(conn, ds);
        }
    }

    private void close(AutoCloseable... acs) {
        for(AutoCloseable ac :acs)
            try { if(ac!=null) ac.close(); } catch(Exception e) { e.printStackTrace(); }
    }

    public void deleteAll() throws Exception {
//        Connection conn = DataSourceUtils.getConnection(ds);
        Connection conn = ds.getConnection();
        System.out.println(conn);
        String sql = "delete from a1";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.executeUpdate();
        close(pstmt);
    }
}
