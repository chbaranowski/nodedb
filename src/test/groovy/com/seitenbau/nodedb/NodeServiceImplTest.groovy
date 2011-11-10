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

    def simpleNode = new Node(id: 1, type: 'music', parent: null)

    @Before
    void setup()
    {
        nodeService.clear()
        nodeService.save(new Node(type: 'music', parent: null))
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
        def nodes = nodeService.getAll()
        node.id = 2
        assert nodes == [simpleNode, node]
    }

    @Test
    void saveNodeWithProperties() throws Exception
    {
        def node = new Node(type: 'user')
        (1..1000).each {
           node.properties += new NodeProperty(key: 'name', value: 'Rainer')
        }
        nodeService.save(node)

        def startNano = System.nanoTime()
        def startMs = System.currentTimeMillis()
        def nodes = nodeService.getAll()
        println("Time : " + (System.nanoTime() - startNano))
        println("Time MS : " + (System.currentTimeMillis() -startMs))

        node.id = 2
        assert nodes == [simpleNode, node]
    }
}
