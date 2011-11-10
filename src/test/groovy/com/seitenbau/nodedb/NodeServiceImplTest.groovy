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

    def simpleNode = new Node(type: 'music', parent: null)

    @Before
    void setup()
    {
        nodeService.clear()
        nodeService.save(simpleNode)
    }

    @Test
    void getAll() throws Exception
    {
        def nodes = nodeService.getAll()
        assert nodes == [simpleNode]
    }

    @Test
    void saveNodeWithProperty() throws Exception
    {
        def node = new Node(type: 'user', properties: [new NodeProperty(key: 'name', value: 'Rainer')])
        nodeService.save(node)
        assert nodeService.getAll() == [simpleNode, node]
    }

    @Test
    void saveNodeWithProperties() throws Exception
    {
        def node = new Node(type: 'user')
        (1..1000).each {
           node.properties += new NodeProperty(key: 'name', value: 'Rainer')
        }
        nodeService.save(node)
        assert nodeService.getAll() == [simpleNode, node]
    }
}
