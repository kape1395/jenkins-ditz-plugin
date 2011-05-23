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
import java.io.FileInputStream;
import java.io.FilenameFilter;

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
    private File bugsDir;

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
    public DitzBugsDirReader(File bugsDir) {
        this.bugsDir = bugsDir;
        this.yaml = createConfiguredYaml();
    }

    /**
     * This constructor takes pre-configured yaml parser.
     * @param bugsDir Ditz bugs directory.
     * @param yaml Yaml parser, configured to parse ditz data files.
     */
    public DitzBugsDirReader(File bugsDir, Yaml yaml) {
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
        File projectFile = new File(bugsDir, projectFileName);
        Project project = (Project) yaml.load(new FileInputStream(projectFile));

        FilenameFilter filter = new IssueFilenameFilter(projectFileName, issueFileSuffix);
        for (File issueFile : bugsDir.listFiles(filter)) {
            Issue issue = (Issue) yaml.load(new FileInputStream(issueFile));
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
    protected static class IssueFilenameFilter implements FilenameFilter {
        private String projectFileName;
        private String issueFileSuffix;

        /**
         * Constructor.
         * @param projectFileName to be ignored.
         * @param issueFileSuffix to be included.
         */
        public IssueFilenameFilter(String projectFileName, String issueFileSuffix) {
            this.projectFileName = projectFileName;
            this.issueFileSuffix = issueFileSuffix;
        }

        /**
         * Implementation of FilenameFilter.
         * @param dir Directory containing the file.
         * @param name Name of the file to be tested.
         */
        public boolean accept(File dir, String name) {
            return name != null
                    && !name.equals(projectFileName)
                    && name.endsWith(issueFileSuffix);
        }

    }

}
