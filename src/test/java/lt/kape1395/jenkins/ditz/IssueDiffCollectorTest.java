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

    @BeforeMethod
    public void beforeMethod() {
        project = createProjectTemplate();
        base = createProjectTemplate();
        issue1 = new Issue("id1", "title1", ":type1", Issue.Status.UNSTARTED.toString(), "r1");
        issue2 = new Issue("id2", "title2", ":type2", Issue.Status.UNSTARTED.toString(), "r1");
        issue3 = new Issue("id3", "title3", ":type3", Issue.Status.UNSTARTED.toString(), "r2", "c1");
    }

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
     *
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
    }

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
    }

    /**
     *
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
    }
}
