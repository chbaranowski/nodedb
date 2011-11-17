package com.seitenbau.nodedb

import javax.annotation.PostConstruct
import javax.sql.DataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface NodeService
{
    List<Node> getAll()

    void clear()

    Node save(Node node)
}

@Service
class NodeServiceImpl implements NodeService
{

    @Autowired
    @Qualifier("nodeIncrementer")
    DataFieldMaxValueIncrementer incrementer

    JdbcTemplate jdbcTemplate

    @Autowired
    public void setDataSource(DataSource dataSource)
    {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @PostConstruct
    @Transactional
    def init()
    {
    }

    @Transactional
    List<Node> getAll()
    {
        def result = jdbcTemplate.queryForList("select * from node left join nodeproperty on node.id = nodeproperty.node order by node.id")
        def nodes = [], node
        result.each {row ->
            if (node?.id != row.id)
                nodes += [node = new Node(id: row.id, type: row.type, parent: row.parent)]
            if (row.name)
                node.properties += new NodeProperty(key: row.name, value: row.value)
        }
        return nodes;
    }

    @Transactional
    void clear()
    {
        jdbcTemplate.update("delete from node")
        jdbcTemplate.update("delete from nodeproperty")
        jdbcTemplate.update("delete from node_sequence")
        jdbcTemplate.update("insert into node_sequence values(0)")
    }

    @Transactional
    Node save(Node node)
    {
        def id = incrementer.nextIntValue()
        node.id = id
        jdbcTemplate.update("insert into node (id, type, parent) values (?, ?, ?)", node.id, node.type, node.parent)
        node.properties.each { property ->
            jdbcTemplate.update("insert into nodeproperty (name, value, node) values (?,?,?)",
                    property.key, property.value, node.id)
        }
        return node;
    }

}