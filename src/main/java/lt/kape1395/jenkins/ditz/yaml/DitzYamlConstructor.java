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

import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.constructor.Constructor;

/**
 * Constructor, used to parse DITZ YAML files.
 * @author k.petrauskas
 */
public class DitzYamlConstructor extends Constructor {

	/**
	 * This constructor is here mainly to support IoC and unit tests.
	 * @param classConstruct Construct, to be used to create classes.
	 */
    public DitzYamlConstructor(Construct classConstruct) {
        this.yamlConstructors.put(null, classConstruct);
    }

    /**
     * This constructor configures the object to use predefined
     * constructs for all the currently known YAML classes.
     */
    public DitzYamlConstructor() {
    	DelegatingConstruct construct = new DelegatingConstruct();
    	construct.addConstruct(DitzIssueConstruct.YAML_CLASS, new DitzIssueConstruct());
        this.yamlConstructors.put(null, construct);
    }

}
