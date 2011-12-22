package com.seitenbau.nodedb

import groovy.sql.Sql
import java.sql.Connection
import java.sql.SQLException
import javax.sql.DataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.ConnectionCallback
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface TreeRepository
{
    TreeNode findNodeById(Integer id)

    void clear()

    TreeNode createTreeNode(Integer parentId, String name)

}

@Service
class JdbcTreeRepository implements TreeRepository
{

    @Autowired
    @Qualifier("treeIncrementer")
    DataFieldMaxValueIncrementer incrementer

    JdbcTemplate jdbcTemplate

    @Autowired
    public void setDataSource(DataSource dataSource)
    {
        this.jdbcTemplate = new JdbcTemplate(dataSource)
    }

    @Transactional
    void clear()
    {
        jdbcTemplate.update("delete from tree")
        jdbcTemplate.update("delete from tree_sequence")
        jdbcTemplate.update("insert into tree_sequence values(0)")
    }

    @Transactional
    TreeNode findNodeById(final Integer id)
    {
        return jdbcTemplate.execute(new ConnectionCallback<TreeNode>()
        {
            TreeNode doInConnection(Connection conn) throws SQLException, DataAccessException
            {
                def sql = new Sql(conn)
                def row = sql.firstRow("select * from tree where id=?", [id])
                return new TreeNode(id: row.id, name: row.name, lft: row.lft, rgt: row.rgt)
            }
        })
    }

    @Transactional
    TreeNode createTreeNode(Integer parentId, String name)
    {
        if (parentId)
        {
            def id = incrementer.nextIntValue()
            def parentNode = findNodeById(parentId)
            jdbcTemplate.update("UPDATE tree SET rgt=rgt+2 WHERE rgt >= ?", parentNode.rgt)
            jdbcTemplate.update("UPDATE tree SET lft=lft+2 WHERE lft > ?", parentNode.rgt)
            jdbcTemplate.update("INSERT INTO tree (id, name,lft,rgt) VALUES (?, ?, ?, ?)", id, name, parentNode.rgt, parentNode.rgt + 1)
            return new TreeNode(id: id, name: name, lft: parentNode.rgt, rgt: parentNode.rgt + 1)
        }
        else
        {
            // Save root node
            def id = incrementer.nextIntValue()
            jdbcTemplate.update("INSERT into tree (id, name, lft, rgt) VALUES (?,?,?,?)", id, name, 1, 2)
            return new TreeNode(id: id, name: name, lft: 1, rgt: 2)
        }
    }
}
