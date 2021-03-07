package ru.vasiljev.aa.util;

import lombok.experimental.UtilityClass;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import ru.geekbrains.java4.lesson6.db.dao.ProductsMapper;

import java.io.IOException;
import java.io.InputStream;

@UtilityClass
public class DbUtils {
    private static String resource = "mybatis-config.xml";
    public static ProductsMapper getProductsMapper() {
        SqlSessionFactory sqlSessionFactory;
        InputStream is = null;
        try {
            is = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession session = sqlSessionFactory.openSession(true);

        return session.getMapper(ProductsMapper.class);
    }
}
