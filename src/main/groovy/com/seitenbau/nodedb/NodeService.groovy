package com.seitenbau.nodedb

import javax.sql.DataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct

interface NodeService
{
    List<Node> getAll()

    void clear()

    void save(Node node)
}

@Service
class NodeServiceImpl implements NodeService
{

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
       jdbcTemplate.execute("""
       CREATE TABLE IF NOT EXISTS node (
            id int(11) NOT NULL AUTO_INCREMENT,
            type varchar(20) NOT NULL,
            parent int(11) DEFAULT NULL,
            PRIMARY KEY (id),
            KEY type (type,parent)
       ) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2
       """);
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