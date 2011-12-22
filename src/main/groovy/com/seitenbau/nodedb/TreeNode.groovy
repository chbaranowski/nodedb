package com.seitenbau.nodedb

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode
@ToString
class TreeNode implements Serializable
{

    int id

    String name

    int lft

    int rgt

}
