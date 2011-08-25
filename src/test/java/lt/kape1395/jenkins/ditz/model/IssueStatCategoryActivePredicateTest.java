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

import org.testng.annotations.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Test cases for {@link IssueStatCategory}.
 * @author k.petrauskas
 */
public class IssueStatCategoryActivePredicateTest {

    /**
     * Test all cases.
     */
    @Test
    public void testEvaluate() {
        IssueStatCategoryActivePredicate p = new IssueStatCategoryActivePredicate();
        IssueStatCategory c = mock(IssueStatCategory.class);
        IssueStats s = new IssueStats();
        when(c.getIssueStats()).thenReturn(s);

        s.setStats(0, 0, 0); // open, new, closed
        assertThat(p.evaluate(c), is(false));

        s.setStats(1, 0, 0);
        assertThat(p.evaluate(c), is(true));

        s.setStats(0, 1, 0);
        assertThat(p.evaluate(c), is(true));

        s.setStats(0, 0, 1);
        assertThat(p.evaluate(c), is(true));

        s.setStats(1, 1, 1);
        assertThat(p.evaluate(c), is(true));

        s.setStats(0, 1, 1);
        assertThat(p.evaluate(c), is(true));
        
        assertThat(p.evaluate(new Object()), is(false));
        assertThat(p.evaluate(null), is(false));
    }
}
