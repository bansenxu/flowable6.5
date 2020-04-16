package com.bootdo.modules.flowable.db;

import com.bootdo.ext.servicenode.BaseServiceNode;
import com.bootdo.modules.flowable.dao.SQLExecDao;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;

public class SQLService extends BaseServiceNode implements JavaDelegate {

    /*@Autowired
    private SqlSession sqlSession;*/

    @Autowired
    private SQLExecDao mydao;

    private void init() throws IOException {
        String resource = "org/mybatis/builder/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        SqlSessionFactory factory = builder.build(inputStream);
    }
    private void init2()
    {
        /*DataSource dataSource new DataSource();
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
//        configuration.addMapper(BlogMapper.class);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);*/
    }

    @Override
    public void execute(DelegateExecution execution) {

    }
}
