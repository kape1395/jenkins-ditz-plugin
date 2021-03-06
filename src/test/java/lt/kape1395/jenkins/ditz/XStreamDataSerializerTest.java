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

import lt.kape1395.jenkins.ditz.model.Component;
import lt.kape1395.jenkins.ditz.model.Issue;
import lt.kape1395.jenkins.ditz.model.Project;
import lt.kape1395.jenkins.ditz.model.Release;

import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Test case for {@link DitzBugsDirReader}.
 *
 * @author k.petrauskas
 */
public class XStreamDataSerializerTest {

    /* ***********************************************************************/
    /**
     * Test if loading of DITZ directory works.
     */
    @Test
    public void testSaveAndLoad() throws Exception {
        File file = new File("target/test-lt.kape1395.jenkins.ditz.XStreamDataSerializerTest.testSaveAndLoad-1.xml");
        XStreamDataSerializer serializer = new XStreamDataSerializer(file);

        Project project = new Project();
        project.setName("a");
        project.getReleases().add(new Release("r1", ":unreleased"));
        project.getReleases().add(new Release("r2", ":unreleased"));
        project.getComponents().add(new Component("c1"));
        project.getComponents().add(new Component("c2"));
        project.getComponents().add(new Component("c3"));
        project.getIssues().add(new Issue("id1", "title1", ":type1", "status1", "r1"));
        project.getIssues().add(new Issue("id2", "title2", ":type2", "status2", "r1"));
        project.getIssues().add(new Issue("id3", "title3", ":type3", "status3", "r2", "x"));

        serializer.saveProject(project);
        Project project2 = serializer.loadProject();

        assertThat(project2.getName(), is("a"));
        assertThat(project2.getReleases().size(), is(2));
        assertThat(project2.getComponents().size(), is(3));
        assertThat(project2.getIssues().size(), is(3));
        assertThat(project2.getIssues().get(2).getComponentName(), is("x"));
    }

    /* ***********************************************************************/
}
