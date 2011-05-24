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
import java.io.FileFilter;
import java.io.Serializable;

import hudson.FilePath;

import org.yaml.snakeyaml.Yaml;

import lt.kape1395.jenkins.ditz.model.Issue;
import lt.kape1395.jenkins.ditz.model.Project;
import lt.kape1395.jenkins.ditz.yaml.DelegatingConstruct;
import lt.kape1395.jenkins.ditz.yaml.DitzIssueConstruct;
import lt.kape1395.jenkins.ditz.yaml.DitzProjectConstruct;
import lt.kape1395.jenkins.ditz.yaml.DitzYamlConstructor;

/**
 * Reads DITZ bugs directory, parses all data files
 * and constructs data model, representing the data.
 * The method {{@link #readBugsDir(File)} is the entry point here.
 *
 * @author k.petrauskas
 */
public class DitzBugsDirReader implements DitzProjectDAO {

    /**
     * Default name of the ditz project file.
     */
    public static final String DEFAULT_PROJECT_FILE_NAME = "project.yaml";

    /**
     * Ditz "bugs" directory.
     */
    private FilePath bugsDir;

    /**
     * Yaml parser.
     */
    private Yaml yaml;

    /**
     * Name of the project file in the DITZ bugs directory.
     * Default is "project.yaml".
     */
    private String projectFileName = DEFAULT_PROJECT_FILE_NAME;

    /**
     * Suffix of the issue files in the DITZ bugs directory.
     * Default is ".yaml".
     */
    private String issueFileSuffix = ".yaml";

    /**
     * This constructor configures yaml parser internally.
     * @param bugsDir Ditz bugs directory.
     */
    public DitzBugsDirReader(FilePath bugsDir) {
        this.bugsDir = bugsDir;
        this.yaml = createConfiguredYaml();
    }

    /**
     * This constructor takes pre-configured yaml parser.
     * @param bugsDir Ditz bugs directory.
     * @param yaml Yaml parser, configured to parse ditz data files.
     */
    public DitzBugsDirReader(FilePath bugsDir, Yaml yaml) {
        this.bugsDir = bugsDir;
        this.yaml = yaml;
    }

    /**
     * Create and configure Yaml parser.
     * This is invoked if the parser is not supplied to this object via constructor.
     * @return Configured Yaml instance.
     */
    protected Yaml createConfiguredYaml() {
        DitzIssueConstruct ditzIssueConstruct = new DitzIssueConstruct();
        DitzProjectConstruct ditzProjectConstruct = new DitzProjectConstruct();

        DelegatingConstruct delegatingConstruct = new DelegatingConstruct();
        delegatingConstruct.addConstruct(DitzIssueConstruct.YAML_CLASS, ditzIssueConstruct);
        delegatingConstruct.addConstruct(DitzProjectConstruct.YAML_CLASS, ditzProjectConstruct);

        return new Yaml(new DitzYamlConstructor(delegatingConstruct));
    }

    /**
     * Read bugs directory.
     * {@inheritDoc}
     */
    public Project loadProject() throws Exception {
        FilePath projectFile = bugsDir.child(projectFileName);
        Project project = (Project) yaml.load(projectFile.read());

        FileFilter filter = new IssueFileFilter(projectFileName, issueFileSuffix);
        for (FilePath issueFile : bugsDir.list(filter)) {
            Issue issue = (Issue) yaml.load(issueFile.read());
            project.getIssues().add(issue);
        }
        return project;
    }

    /**
     * Not implemented.
     * {@inheritDoc}
     * @throws Exception always.
     */
    public void saveProject(Project project) throws Exception {
        throw new Exception("Operation saveProject is not supported by " + getClass().getName());
    }

    /**
     * Filters files that are issues.
     */
    protected static class IssueFileFilter implements FileFilter, Serializable {
        /**
         * Required for serializable.
         */
        private static final long serialVersionUID = -579137861545379815L;
        /**
         * Basename fo the project file.
         */
        private String projectFileName;
        /**
         * Suffix for the issue files.
         */
        private String issueFileSuffix;

        /**
         * Constructor.
         * @param projectFileName to be ignored.
         * @param issueFileSuffix to be included.
         */
        public IssueFileFilter(String projectFileName, String issueFileSuffix) {
            this.projectFileName = projectFileName;
            this.issueFileSuffix = issueFileSuffix;
        }

        /**
         * Implementation of FilenameFilter.
         * @param file File to be tested.
         * @return true for all issue files.
         */
        public boolean accept(File file) {
            if (file == null) {
                return false;
            }

            String name = file.getName();
            return name != null
                    && !name.equals(projectFileName)
                    && name.endsWith(issueFileSuffix);
        }

    }

}
