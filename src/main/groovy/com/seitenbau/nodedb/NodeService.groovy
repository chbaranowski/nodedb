package com.seitenbau.nodedb

import groovy.sql.Sql
import java.sql.Connection
import java.sql.SQLException
import javax.annotation.PostConstruct
import javax.sql.DataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.ConnectionCallback
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
class JdbcNodeService implements NodeService
{

    @Autowired
    @Qualifier("nodeIncrementer")
    DataFieldMaxValueIncrementer incrementer

    JdbcTemplate jdbcTemplate

    @PostConstruct
    void init()
    {
    }

    @Autowired
    public void setDataSource(DataSource dataSource)
    {
        this.jdbcTemplate = new JdbcTemplate(dataSource)
    }

    @Transactional
    List<Node> getAll()
    {
        return jdbcTemplate.execute(new ConnectionCallback<List<Node>>()
        {
            List<Node> doInConnection(Connection con) throws SQLException, DataAccessException
            {
                def sql = new Sql(con)
                def nodes = [], node
                sql.eachRow("""
                         select id, type, name, value from node
                                   left join nodeproperty on
                                    node.id = nodeproperty.node
                                   order by node.id """, { row ->
                    def id = row.id
                    def name = row.name
                    if (node?.id != id)
                        nodes.add(node = new Node(id: id, type: row.type))
                    if (name)
                        node.properties.add(new NodeProperty(key: name, value: row.value))
                })
                return nodes
            }
        })
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