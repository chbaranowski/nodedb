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
}
