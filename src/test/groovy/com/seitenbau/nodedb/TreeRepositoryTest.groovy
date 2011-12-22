package com.seitenbau.nodedb;


import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(["classpath:/spring/core-context.xml", "classpath:spring/hsql-context.xml"])
class TreeRepositoryTest
{

    @Autowired
    TreeRepository treeRepository

    @Before
    void setUp()
    {
        treeRepository.clear()
    }

    @Test
    void saveNode()
    {
        def treeNode = treeRepository.createTreeNode(null, "Root Folder")
        def node = treeRepository.findNodeById(treeNode.id)
        assert node == treeNode
    }

    @Test
    void saveNodeWithChild()
    {
        def saeugetiereNode = treeRepository.createTreeNode(null, "Säugetier")
        def primatenNode = treeRepository.createTreeNode(saeugetiereNode.id, "Primaten")
        def halbaffenNode = treeRepository.createTreeNode(primatenNode.id, "Halbaffen")
        def affenNode = treeRepository.createTreeNode(primatenNode.id, "Affen")
        def nagetierNode = treeRepository.createTreeNode(saeugetiereNode.id, "Nagetier")

        saeugetiereNode = treeRepository.findNodeById(saeugetiereNode.id)
        assert saeugetiereNode.lft == 1 && saeugetiereNode.rgt == 10

        primatenNode = treeRepository.findNodeById(primatenNode.id)
        assert primatenNode.lft == 2 && primatenNode.rgt == 7

        halbaffenNode = treeRepository.findNodeById(halbaffenNode.id)
        assert halbaffenNode.lft == 3 && halbaffenNode.rgt == 4

        affenNode = treeRepository.findNodeById(affenNode.id)
        assert affenNode.lft == 5 && affenNode.rgt == 6

        nagetierNode = treeRepository.findNodeById(nagetierNode.id)
        assert nagetierNode.lft == 8 && nagetierNode.rgt == 9
    }

}
