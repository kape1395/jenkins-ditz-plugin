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
package lt.kape1395.jenkins.ditz;

import java.io.File;
import java.util.logging.Logger;

import lt.kape1395.jenkins.ditz.model.Project;

import hudson.model.AbstractBuild;
import hudson.model.Action;

/**
 * Action, showing general statistics for ditz issues in the middle of the page.
 * This class is also providing a link in the project menu to access details page
 * with all ditz info.
 *
 * @author k.petrauskas
 */
public class DitzPublisherAction implements Action {
    private static Logger log = Logger.getLogger(DitzPublisherAction.class.getName());
    private AbstractBuild<?, ?> owner;
    private Project project;

    /**
     * Constructor.
     * @param owner Last build. It is used to load ditz data.
     */
    public DitzPublisherAction(AbstractBuild<?, ?> owner) {
        this.owner = owner;
    }

    /**
     * Name for the action.
     */
    public String getDisplayName() {
        return Messages.DitzPublisher_ActionName();
    }

    /**
     * Some icon for the action.
     */
    public String getIconFileName() {
        return Messages.DitzPublisher_Icon();
    }

    /**
     * Returns link to the details page.
     */
    public String getUrlName() {
        return "http://karolis.5grupe.lt/";
    }

    /**
     * Returns project data, used in the jelly scripts to display stats to a user.
     * Project data is lazily loaded.
     * @return Project data.
     */
    public Project getProject() {
        if (project == null) {
            try {
                project = loadProject();
            } catch (Exception e) {
                project = new Project();
                project.getIssueStats().reset();
                log.warning("Unable to load project data, using default, exception=" + e);
            }
        }
        return project;
    }

    /**
     * Load project data to be displayed.
     * @return Project data.
     * @throws Exception if project cannot be loaded.
     */
    protected Project loadProject() throws Exception {
        if (owner == null) {
            throw new Exception("No last build exist.");
        }

        File projectFile = new File(owner.getRootDir(), DitzPublisher.DITZ_PROJECT_FILE);
        if (projectFile.exists()) {
            return new XStreamDataSerializer(projectFile).loadProject();
        } else {
            throw new Exception("file does not exist: " + projectFile);
        }
    }
}
