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

import lt.kape1395.jenkins.ditz.model.Release;

import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;

/**
 * Constructs Release object from YAML node.
 * @author k.petrauskas
 */
public class DitzReleaseConstruct extends AbstractConstruct {
	public static final String YAML_CLASS = "!ditz.rubyforge.org,2008-03-06/release";
	
	/**
	 * Implementation of Construct.
	 * @param node Node to be parsed. It must be of class MappingNode.
	 * @return issue.
	 */
	public Object construct(Node node) {
		if (!(node instanceof MappingNode)) {
			throw new YAMLException("MappingNode is required to parse a release.");
		}
		MappingNode mappingNode = (MappingNode) node;
		
		String name = null;
		String statusName = null;
		
		for (NodeTuple nodeTuple : mappingNode.getValue()) {
			if (!(nodeTuple.getKeyNode() instanceof ScalarNode)) {
				continue;
			}
			String key = ((ScalarNode) nodeTuple.getKeyNode()).getValue();
			
			String value = null;
			if (nodeTuple.getValueNode() instanceof ScalarNode) {
				value = ((ScalarNode) nodeTuple.getValueNode()).getValue();
			}
			if ("".equals(value)) {
				value = null;
			}
			
			if (key.equalsIgnoreCase("name")) {
				name = value;
			} else if (key.equalsIgnoreCase("status")) {
				statusName = value;
			}
		}
		
		return new Release(name, statusName);
	}

}
