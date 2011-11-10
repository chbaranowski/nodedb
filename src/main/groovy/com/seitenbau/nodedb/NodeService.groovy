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

    void save(Node node)
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
        jdbcTemplate.execute("""
           CREATE TABLE IF NOT EXISTS node (
                id int(11) NOT NULL AUTO_INCREMENT,
                type varchar(20) NOT NULL,
                parent int(11) DEFAULT NULL,
                PRIMARY KEY (id),
                KEY type (type,parent)
           ) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2
       """)
        jdbcTemplate.execute("""
           CREATE TABLE IF NOT EXISTS `nodeproperty` (
              `name` varchar(20) NOT NULL,
              `node` int(11) NOT NULL,
              `value` varchar(200) DEFAULT NULL,
              KEY `type` (`name`,`node`),
              KEY `node` (`node`)
            ) ENGINE=InnoDB DEFAULT CHARSET=latin1;
       """)
        jdbcTemplate.execute("""
           CREATE TABLE IF NOT EXISTS `node_sequence` (
              `id` int(11) NOT NULL
           ) ENGINE=MyISAM DEFAULT CHARSET=latin1;
       """)
        jdbcTemplate.execute("""
           insert into node_sequence (id) values (0)
       """)
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
                node.properties.add(new NodeProperty(key: row.name, value: row.value))
        }
        return nodes;
    }

    @Transactional
    void clear()
    {
        jdbcTemplate.update("truncate node")
        jdbcTemplate.update("truncate nodeproperty")
        jdbcTemplate.update("truncate node_sequence")
        jdbcTemplate.update("insert into node_sequence (id) values (0)")
    }

    @Transactional
    void save(Node node)
    {
        def id = incrementer.nextIntValue()
        jdbcTemplate.update("insert into node (id, type, parent) values (?, ?, ?)", id, node.type, node.parent)
        node.properties.each { property ->
            jdbcTemplate.update("insert into nodeproperty (name, value, node) values (?,?,?)",
                    property.key, property.value, id)
        }
    }

}