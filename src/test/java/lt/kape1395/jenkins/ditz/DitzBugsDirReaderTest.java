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

import hudson.FilePath;

import java.io.File;

import lt.kape1395.jenkins.ditz.model.Project;

import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Test case for {@link DitzBugsDirReader}.
 *
 * @author k.petrauskas
 */
public class DitzBugsDirReaderTest {

    /* ***********************************************************************/
    /**
     * Test if loading of DITZ directory works.
     */
    @Test
    public void testReadBugsDir() throws Exception {
        File ditzBugsDir = new File("src/test/resources/bugs-01");
        DitzBugsDirReader ditzBugsDirReader = new DitzBugsDirReader(new FilePath(ditzBugsDir));
        Project project = ditzBugsDirReader.loadProject();

        assertThat(project.getName(), is("x"));
        assertThat(project.getIssues().size(), is(2));
    }

    /* ***********************************************************************/
}
