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
import java.util.Collection;
import java.util.logging.Logger;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.functors.AndPredicate;

import lt.kape1395.jenkins.ditz.model.IssueActivePredicate;
import lt.kape1395.jenkins.ditz.model.IssueInReleasePredicate;
import lt.kape1395.jenkins.ditz.model.IssueStatCategoryActivePredicate;
import lt.kape1395.jenkins.ditz.model.Project;
import lt.kape1395.jenkins.ditz.model.Release;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;

/**
 * Action, showing general statistics for ditz issues in the middle of the page.
 * This class is also providing a link in the project menu to access details page
 * with all ditz info.
 *
 * @author k.petrauskas
 */
public class DitzPublisherAction implements Action {
    /**
     * Logger used for debugging.
     */
    private static Logger log = Logger.getLogger(DitzPublisherAction.class.getName());

    /**
     * Link at which the details page will be displayed.
     */
    private static final String URL_NAME = "ditz";

    /**
     * non-null, if this is a build action.
     */
    private AbstractBuild<?, ?> jenkinsBuild;
    
    /**
     * non-null, if this is a project action. 
     */
    private AbstractProject<?, ?> jenkinsProject;

    /**
     * Current ditz project diff.
     */
    private Project project;

    /**
     * Constructor for the build action.
     * @param owner Last build. It is used to load ditz data.
     */
    public DitzPublisherAction(AbstractBuild<?, ?> jenkinsBuild) {
        this.jenkinsBuild = jenkinsBuild;
    }
    
    /**
     * Constructor for the project action.
     * @param jenkinsProject Jenkins project.
     */
    public DitzPublisherAction(AbstractProject<?, ?> jenkinsProject) {
        this.jenkinsProject = jenkinsProject;
    }
    

    /**
     * Name for the action.
     * @return Localized name.
     */
    public String getDisplayName() {
        return Messages.DitzPublisher_ActionName();
    }

    /**
     * Some icon for the action.
     * @return Localized icon file name.
     */
    public String getIconFileName() {
        return Messages.DitzPublisher_Icon();
    }

    /**
     * Returns link to the details page.
     * @return URL of details page.
     */
    public String getUrlName() {
        return URL_NAME;
    }

    /**
     * Returns project data, used in the jelly scripts to display stats to a user.
     * Project data is lazily loaded.
     * @return Project data.
     */
    public Project getProject() {
        if (project == null || jenkinsBuild == null) {
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
        if (jenkinsBuild == null && jenkinsProject == null) {
            throw new Exception("No last build and current project exist.");
        }
        
        AbstractBuild<?, ?> owner;
        if (jenkinsBuild != null) {
            owner = jenkinsBuild;
        } else {
            owner = jenkinsProject.getLastBuild();
        }

        File projectFile = new File(owner.getRootDir(), DitzPublisher.DITZ_PROJECT_FILE);
        if (projectFile.exists()) {
            return new XStreamDataSerializer(projectFile).loadProject();
        } else {
            throw new Exception("file does not exist: " + projectFile);
        }
    }

    /**
     * Returns active releases.
     * @return Releases that should be shown on top of the page.
     */
    @SuppressWarnings("rawtypes")
    public Collection getActiveReleases() {
        return CollectionUtils.select(getProject().getReleases(),
                new IssueStatCategoryActivePredicate());
    }
    
    /**
     * 
     * @param release
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Collection getActiveReleaseIssues(Release release) {
        return CollectionUtils.select(getProject().getIssues(),
                new AndPredicate(new IssueInReleasePredicate(release), new IssueActivePredicate()));
    }
    
    /**
     * 
     * @param release
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Collection getActiveUnassignedIssues() {
    	return getActiveReleaseIssues(null);
    }
    
}
