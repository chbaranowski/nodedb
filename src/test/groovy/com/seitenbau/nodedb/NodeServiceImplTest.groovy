package com.seitenbau.nodedb;


import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.util.StopWatch

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(["classpath:/spring/core-context.xml", "classpath:spring/hsql-context.xml"])
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
        assert nodeService.getAll() == [simpleNode]
    }

    @Test
    void save_NodeWithProperty() throws Exception
    {
        def nodeWithProperty = new Node(type: 'user',
                properties: [new NodeProperty(key: 'name', value: 'Rainer')])
        nodeService.save(nodeWithProperty)
        assert nodeService.getAll() == [simpleNode, nodeWithProperty]
    }

    @Test
    void save_NodeWithProperties() throws Exception
    {
        def nodeWithProperties = new Node(type: 'user')
        (1..1000).each {
            nodeWithProperties.properties += new NodeProperty(key: 'name', value: 'Rainer')
        }
        nodeService.save(nodeWithProperties)
        assert nodeService.getAll() == [simpleNode, nodeWithProperties]
    }

    @Test
    void getAll_ManyNodes_ManuelPerformanceTest() throws Exception
    {
        StopWatch stopWatch = new StopWatch()
        def nodes = [simpleNode]
        (1..1000).each {
            nodes += nodeService.save(new Node(type: 'music'))
        }
        (1..1000).each {
            nodes += nodeService.save(new Node(type: 'photo'))
        }

        def actualNodes

        def count = 100
        (1..count).each {
            stopWatch.start(it.toString())
            actualNodes = nodeService.getAll()
            stopWatch.stop()
        }

        println(stopWatch.prettyPrint())
        println(stopWatch.totalTimeMillis / count)
        assert actualNodes == nodes
    }
}
