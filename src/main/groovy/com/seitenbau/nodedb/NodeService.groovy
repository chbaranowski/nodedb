package com.seitenbau.nodedb

import javax.sql.DataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface NodeService
{
    List<Node> getAll()

    void clear()

    void save(Node node)
}

@Service
class NodeServiceImpl implements NodeService
{

    def jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource)
    {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Transactional
    List<Node> getAll()
    {
        def nodes = new ArrayList()
        def result = jdbcTemplate.queryForList("select * from node")
        for(row in result)
        {
           def node = new Node(row)
            nodes.add(node)
        }
        return nodes;
    }

    @Transactional
    void clear()
    {
      jdbcTemplate.update("truncate node")
    }

    @Transactional
    void save(Node node)
    {
      jdbcTemplate.update("insert into node (type, parent) values (?, ?)", node.type, node.parent)
    }

}