package com.seitenbau.nodedb

import org.junit.Test
import javax.sql.DataSource
import org.springframework.beans.factory.annotation.Autowired
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.ContextConfiguration
import static org.junit.Assert.assertNotNull

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/hsql-context.xml")
class HSQLTest {

    @Autowired
    DataSource dataSource;

    @Test
    void dataSource()
    {
       assertNotNull(dataSource)
    }

}
