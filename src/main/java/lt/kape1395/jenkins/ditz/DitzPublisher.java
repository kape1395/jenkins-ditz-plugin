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
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import lt.kape1395.jenkins.ditz.model.Project;

import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.util.FormValidation;

/**
 * Ditz information publisher.
 * It collects ditz info from the workspace and saves to a separate file
 * under the specific build. This file is then used to get ditz issue
 * statistics and details.
 *
 * @author k.petrauskas
 */
public class DitzPublisher extends Recorder {
    /**
     * Logger.
     */
    private static Logger log = Logger.getLogger(DitzPublisher.class.getName());

    /**
     * Default name for an internal ditz project file.
     */
    public static final String DITZ_PROJECT_FILE = "ditz.diff.xml";

    /**
     * Ditz bugs directory.
     */
    private String bugsDir;

    /* ********************************************************************* */
    /**
     * Constructor.
     * @param bugsDir Ditz bugs directory.
     */
    @DataBoundConstructor
    public DitzPublisher(String bugsDir) {
        this.bugsDir = bugsDir;
    }

    /* ********************************************************************* */
    /**
     * Returns bugs directory, used by this project.
     * @return Ditz bugs directory.
     */
    public String getBugsDir() {
        return bugsDir;
    }

    /* ********************************************************************* */
    /**
     * Perform real action.
     * @param build Current build.
     * @param launcher Launcher for launching something.
     * @param listener Build listener.
     * @return true, always.
     * @throws InterruptedException is not thrown.
     * @throws IOException is not thrown.
     */
    @Override
    public boolean perform(
            AbstractBuild<?, ?> build,
            Launcher launcher,
            BuildListener listener) throws InterruptedException, IOException {
        listener.getLogger().println("DitzPublisher::perform - start");

        FilePath ws = build.getWorkspace();

        try {
            FilePath ditzBugsDir = ws.child(getBugsDir());
            FilePath ditzXmlFile = new FilePath(build.getRootDir()).child(DITZ_PROJECT_FILE);

            if (log.isLoggable(Level.INFO)) {
                log.info("ditzBugsDir=" + ditzBugsDir);
                log.info("ditzXmlFile=" + ditzXmlFile);
            }

            Project project = new DitzBugsDirReader(ditzBugsDir).loadProject();
            IssueStatsCollector statsCollector = null;

            if (build.getPreviousCompletedBuild() != null) {
                File baseDir = build.getPreviousCompletedBuild().getRootDir();
                File baseFile = new File(baseDir, DITZ_PROJECT_FILE);
                if (baseFile.exists()) {
                    log.info("Loading data from the previous build: file=" + baseFile);
                    Project base = new XStreamDataSerializer(baseFile).loadProject();
                    statsCollector = new IssueDiffCollector(base);
                }
            }
            if (statsCollector == null) {
                log.info("Ditz data not found in previous build. Calculating stats without base.");
                statsCollector = new IssueDiffCollector();
            }

            statsCollector.collectStatistics(project);
            new XStreamDataSerializer(new File(ditzXmlFile.toURI())).saveProject(project);

            build.addAction(new DitzPublisherAction(build));
        } catch (Exception e) {
            e.printStackTrace();
        }

        listener.getLogger().println("DitzPublisher::perform - end");
        return true;
    }

    /* ********************************************************************* */
    /**
     * I still don't know, what it is...
     * @return NONE.
     */
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    /* ********************************************************************* */

    /**
     * Get build step descriptor.
     * @return Instance of {@link DescriptorImpl}.
     */
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    /**
     * Build step description.
     */
    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {
        /**
         * Display name.
         * @return Name of the plugin.
         */
        public String getDisplayName() {
            return Messages.DitzPublisher_DisplayName();
        }

        /**
         * Performs on-the-fly validation on the file mask wildcard.
         * Bugs directory should exist.
         * @param project Current project.
         * @param value Bugs directory as a string.
         * @return Validation info.
         * @throws ServletException is not thrown.
         * @throws IOException is not thrown.
         */
        @SuppressWarnings("rawtypes")
        public FormValidation doCheckBugsDir(
                @AncestorInPath AbstractProject project,
                @QueryParameter String value) throws IOException, ServletException {
            FilePath ws = project.getSomeWorkspace();
            if (ws != null) {
                FormValidation formValidation = ws.validateRelativeDirectory(value);
                if (formValidation.kind != FormValidation.Kind.OK) {
                    return formValidation;
                }
                String projectFile = value + "/" + DitzBugsDirReader.DEFAULT_PROJECT_FILE_NAME;
                return ws.validateRelativePath(projectFile, true, true);
            } else {
                return FormValidation.ok();
            }
        }

        /**
         * Applicable to all project types.
         * @param jobType Type of the current job.
         * @return true.
         */
        @SuppressWarnings("rawtypes")
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }
    }

    /* ********************************************************************* */
    /*
     * Returns all provided actions.
     * @param project Current project.
     * @return All provided actions.
     */
    @Override
    public Collection<? extends Action> getProjectActions(AbstractProject<?, ?> project) {
        log.fine("DitzPublisher::getProjectActions()");
        return Collections.singleton(new DitzPublisherAction(project));
    }

}
