package com.seitenbau.nodedb

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode
@ToString
class Node implements Serializable
{
    Integer id
    String type
    Integer parent
    Collection<NodeProperty> properties = new ArrayList<NodeProperty>()
    Collection<Node> children = new ArrayList<Node>()
}
