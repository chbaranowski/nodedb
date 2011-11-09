package com.seitenbau.nodedb;


import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring/core-context.xml")
public class NodeServiceImplTest
{

    @Autowired
    NodeService nodeService

    @Before
    void setup()
    {
        nodeService.clear()
        nodeService.save(new Node(type:'music', parent: null))
    }

    @Test
    void testGetAll() throws Exception
    {
        def nodes = nodeService.getAll()
        assert nodes == [new Node(id: 1, type: 'music', parent: null)]
    }
}
