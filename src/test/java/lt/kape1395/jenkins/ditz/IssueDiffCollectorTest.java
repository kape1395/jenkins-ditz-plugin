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

import lt.kape1395.jenkins.ditz.model.Component;
import lt.kape1395.jenkins.ditz.model.Issue;
import lt.kape1395.jenkins.ditz.model.Issue.StatusChange;
import lt.kape1395.jenkins.ditz.model.Project;
import lt.kape1395.jenkins.ditz.model.Release;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


/**
 * Test case for {@link IssueDiffCollector}.
 *
 * @author k.petrauskas
 */
public class IssueDiffCollectorTest {
    private Project project;
    private Project base;
    private Issue issue1;
    private Issue issue2;
    private Issue issue3;

    /**
     * Setup tests.
     */
    @BeforeMethod
    public void beforeMethod() {
        project = createProjectTemplate();
        base = createProjectTemplate();
        issue1 = new Issue("id1", "title1", ":type1", Issue.Status.UNSTARTED.toString(), "r1");
        issue2 = new Issue("id2", "title2", ":type2", Issue.Status.UNSTARTED.toString(), "r1");
        issue3 = new Issue("id3", "title3", ":type3", Issue.Status.UNSTARTED.toString(), "r2", "c1");
    }

    /**
     * Creates almost empty project.
     * @return
     */
    public Project createProjectTemplate() {
        Project project = new Project();
        project.setName("a");
        project.getReleases().add(new Release("r1", ":unreleased"));
        project.getReleases().add(new Release("r2", ":unreleased"));
        project.getComponents().add(new Component("c1"));
        project.getComponents().add(new Component("c2"));
        project.getComponents().add(new Component("c3"));
        return project;
    }

    /**
     *  Test the case, when difference is made with no base specified (i.e. from zero).
     *  This situation will always be the case, when this plugin will be invoked first
     *  time on a particular project.
     */
    @Test
    public void testNoBase() throws Exception {
        project.getIssues().add(issue1);
        project.getIssues().add(issue2);
        project.getIssues().add(issue3);

        IssueDiffCollector instance = new IssueDiffCollector();
        instance.collectStatistics(project);

        assertThat(project.getIssueStats().getNewIssues(), is(3));
        assertThat(project.getIssueStats().getOpenIssues(), is(3));
        assertThat(project.getIssueStats().getClosedIssues(), is(0));
        assertThat(project.getReleases().get(0).getIssueStats().getNewIssues(), is(2));
        assertThat(project.getReleases().get(1).getIssueStats().getNewIssues(), is(1));
        assertThat(project.getComponents().get(0).getIssueStats().getNewIssues(), is(1));
        assertThat(project.getComponents().get(1).getIssueStats().getNewIssues(), is(0));
        assertThat(project.getComponents().get(2).getIssueStats().getNewIssues(), is(0));
        
        assertThat(project.getIssues().size(), is(3));
        assertThat(project.getIssues().get(0).getStatusChange(), is(StatusChange.NEW));
        assertThat(project.getIssues().get(1).getStatusChange(), is(StatusChange.NEW));
        assertThat(project.getIssues().get(2).getStatusChange(), is(StatusChange.NEW));
    }

    /**
     * Compare two equal projects.
     *
     * @throws Exception
     */
    @Test
    public void testNoDifferences() throws Exception {
        base.getIssues().add(issue1);
        base.getIssues().add(issue2);
        base.getIssues().add(issue3);
        project.getIssues().add(issue1);
        project.getIssues().add(issue2);
        project.getIssues().add(issue3);

        IssueDiffCollector instance = new IssueDiffCollector(base);
        instance.collectStatistics(project);

        assertThat(project.getIssueStats().getNewIssues(), is(0));
        assertThat(project.getIssueStats().getOpenIssues(), is(3));
        assertThat(project.getIssueStats().getClosedIssues(), is(0));
        assertThat(project.getReleases().get(0).getIssueStats().getNewIssues(), is(0));
        assertThat(project.getReleases().get(0).getIssueStats().getOpenIssues(), is(2));
        assertThat(project.getReleases().get(0).getIssueStats().getClosedIssues(), is(0));
        assertThat(project.getReleases().get(1).getIssueStats().getNewIssues(), is(0));
        assertThat(project.getReleases().get(1).getIssueStats().getOpenIssues(), is(1));
        assertThat(project.getReleases().get(1).getIssueStats().getClosedIssues(), is(0));
        assertThat(project.getComponents().get(0).getIssueStats().getOpenIssues(), is(1));
        assertThat(project.getComponents().get(1).getIssueStats().getOpenIssues(), is(0));
        assertThat(project.getComponents().get(2).getIssueStats().getOpenIssues(), is(0));
        
        assertThat(project.getIssues().size(), is(3));
        assertThat(project.getIssues().get(0).getStatusChange(), is(StatusChange.UNCHANGED));
        assertThat(project.getIssues().get(1).getStatusChange(), is(StatusChange.UNCHANGED));
        assertThat(project.getIssues().get(2).getStatusChange(), is(StatusChange.UNCHANGED));
    }

    /**
     * Test differences with one fixed, one new and one active but unchanged issues.
     * @throws Exception
     */
    @Test
    public void testAllTypes() throws Exception {
        base.getIssues().add(issue1);
        base.getIssues().add(issue2);
        project.getIssues().add(issue2);
        project.getIssues().add(issue3);

        IssueDiffCollector instance = new IssueDiffCollector(base);
        instance.collectStatistics(project);

        assertThat(project.getIssueStats().getNewIssues(), is(1));
        assertThat(project.getIssueStats().getOpenIssues(), is(2));
        assertThat(project.getIssueStats().getClosedIssues(), is(1));

        assertThat(project.getReleases().get(0).getIssueStats().getNewIssues(), is(0));
        assertThat(project.getReleases().get(0).getIssueStats().getOpenIssues(), is(1));
        assertThat(project.getReleases().get(0).getIssueStats().getClosedIssues(), is(1));

        assertThat(project.getReleases().get(1).getIssueStats().getNewIssues(), is(1));
        assertThat(project.getReleases().get(1).getIssueStats().getOpenIssues(), is(1));
        assertThat(project.getReleases().get(1).getIssueStats().getClosedIssues(), is(0));

        assertThat(project.getComponents().get(0).getIssueStats().getNewIssues(), is(1));
        assertThat(project.getComponents().get(0).getIssueStats().getOpenIssues(), is(1));
        assertThat(project.getComponents().get(0).getIssueStats().getClosedIssues(), is(0));

        assertThat(project.getIssues().size(), is(3));
        assertThat(issue1.getStatusChange(), is(StatusChange.CLOSED));
        assertThat(issue2.getStatusChange(), is(StatusChange.UNCHANGED));
        assertThat(issue3.getStatusChange(), is(StatusChange.NEW));
    }

    /**
     * Check is old non-open issues are removed from the target project.
     *
     * @throws Exception
     */
    @Test
    public void testIfAllUnusedIssuesRemoved() throws Exception {
        Issue issue1a = new Issue("id1", "title1", ":type1", Issue.Status.CLOSED.toString(), "r1");
        Issue issue2a = new Issue("id2", "title2", ":type2", Issue.Status.CLOSED.toString(), "r1");
        base.getIssues().add(issue1a);
        base.getIssues().add(issue2a);
        base.getIssues().add(issue3);
        project.getIssues().add(issue2a);
        project.getIssues().add(issue3);

        IssueDiffCollector instance = new IssueDiffCollector(base);
        instance.collectStatistics(project);

        assertThat(project.getIssues().size(), is(1));
        assertThat(issue3.getStatusChange(), is(StatusChange.UNCHANGED));
    }
}
