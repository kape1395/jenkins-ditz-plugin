/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package lt.kape1395.jenkins.ditz.yaml;

import java.util.Map;
import java.util.HashMap;

import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;

/**
 * Creates objects, that are known to this implementation,
 * ignoring all the rest. Unknown nodes are returned as "VOID" string.
 *
 * @author k.petrauskas
 */
public class DelegatingConstruct extends AbstractConstruct {
    /**
     * Constructs by DITZ YAML class name.
     */
    private Map<String, Construct> constructs;

    /**
     * Constructor.
     */
    public DelegatingConstruct() {
        constructs = new HashMap<String, Construct>();
    }

    /**
     * Use this to register all constructors for considered classes.
     * @param yamlClass Yaml class.
     * @param construct Construct to be used for the class.
     */
    public void addConstruct(String yamlClass, Construct construct) {
        constructs.put(yamlClass, construct);
    }

    /**
     * Creates objects, that are known to this implementation,
     * ignoring all the rest. Unknown nodes are returned as "VOID" string.
     * @param node Node to be parsed.
     * @return Constructed object or "VOID" string.
     */
    public Object construct(Node node) {
        Construct target = constructs.get(node.getTag().getValue());
        if (node instanceof MappingNode && target != null) {
            return target.construct(node);
        } else {
            return "VOID";
        }
    }

}
