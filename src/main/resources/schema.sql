CREATE TABLE node (
  id int,
  type varchar NOT NULL,
  parent int DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE nodeproperty (
  name varchar NOT NULL,
  node int NOT NULL,
  value varchar DEFAULT NULL
);

create table node_sequence (id identity);
insert into node_sequence values(0);