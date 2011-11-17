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