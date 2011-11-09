package com.seitenbau.nodedb

import org.junit.Test

public class NodeTest
{

    @Test
    void equalsNode()
    {
        def nodeA = new Node(id:1, type:'music')
        def nodeB = new Node(id:1, type:'music', parent: null)
        assert nodeA == nodeB
    }

}
