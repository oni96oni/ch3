package com.fastcampus.ch3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TxService {

    @Autowired A1Dao a1Dao;
    @Autowired B1Dao b1Dao;

    public void insertA1WithoutTx() throws Exception {
        a1Dao.insert(1, 100);
        a1Dao.insert(1, 200);
    }

    @Transactional(rollbackFor = Exception.class) // 이 매개변수가 있어야 롤백이 되서 입력자체가 안된다!
//    @Transactional // RuntimeException, Error만 rollback 해준다! 그외의 경우에는 매개변수를 넣어주어야해
    public void insertA1WithTxFail() throws Exception {
        a1Dao.insert(1, 100); // 성공
//        throw new Exception();
        a1Dao.insert(1, 200); // 실패
    }

    @Transactional
    public void insertA1WithTxSuccess() throws Exception {
        a1Dao.insert(1, 100);
        a1Dao.insert(2, 200);
    }
}
