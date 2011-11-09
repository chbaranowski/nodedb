package com.seitenbau.nodedb;


import org.databene.contiperf.PerfTest
import org.databene.contiperf.Required
import org.databene.contiperf.junit.ContiPerfRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@PerfTest(invocations = 10)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring/core-context.xml")
public class NodeServiceImplTest
{

    @Rule
    public ContiPerfRule contiPerf = new ContiPerfRule();

    @Autowired
    NodeService nodeService

    @Before
    void setup()
    {
        nodeService.clear()
        nodeService.save(new Node(type:'music', parent: null))
    }

    @Test
    @Required(max = 200, average = 150)
    void testGetAll() throws Exception
    {
        def nodes = nodeService.getAll()
        assert nodes == [new Node(id: 1, type: 'music', parent: null)]
    }
}
