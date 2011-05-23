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
package lt.kape1395.jenkins.ditz.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents DITZ project.
 * @author k.petrauskas
 */
public class Project extends AbstractIssueStatsCategory {
    private String name;
    private List<Component> components;
    private List<Release> releases;
    private List<Issue> issues;

    /**
     * Constructor.
     */
    public Project() {
        components = new LinkedList<Component>();
        releases = new LinkedList<Release>();
        issues = new LinkedList<Issue>();
    }

    /**
     * Sets name for a project.
     * @param name new name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a name of the project.
     * @return name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get project's components.
     * @return list of components.
     */
    public List<Component> getComponents() {
        return components;
    }

    /**
     * Get project's releases.
     * @return list of releases.
     */
    public List<Release> getReleases() {
        return releases;
    }

    /**
     * Get project's issues.
     * @return list of issues.
     */
    public List<Issue> getIssues() {
        return issues;
    }

}
