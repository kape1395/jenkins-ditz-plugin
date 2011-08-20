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

import lt.kape1395.jenkins.ditz.model.Issue.Status;
import lt.kape1395.jenkins.ditz.model.Issue.StatusChange;
import org.apache.commons.collections.Predicate;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class IssueActivePredicateTest {

    @Test
    public void testEvaluate() {
        Predicate p = new IssueActivePredicate();
        Issue i1 = new Issue("a", "b", "c", Status.CLOSED, "d");
        Issue i2 = new Issue("a", "b", "c", Status.IN_PROGRESS, "d");
        Issue i3 = new Issue("a", "b", "c", Status.PAUSED, "d");
        Issue i4 = new Issue("a", "b", "c", Status.UNSTARTED, "d");
        Issue i5 = new Issue("a", "b", "c", "some", "d");
        assertThat(p.evaluate(i1), is(false));
        assertThat(p.evaluate(i2), is(true));
        assertThat(p.evaluate(i3), is(true));
        assertThat(p.evaluate(i4), is(true));
        assertThat(p.evaluate(i5), is(true));   // unknown statuses are considered active.
        
        i1.setStatusChange(null);   // Null is the same as unchanged
        i2.setStatusChange(null);
        i3.setStatusChange(null);
        i4.setStatusChange(null);
        i5.setStatusChange(null);
        assertThat(p.evaluate(i1), is(false));
        assertThat(p.evaluate(i2), is(true));
        assertThat(p.evaluate(i3), is(true));
        assertThat(p.evaluate(i4), is(true));
        assertThat(p.evaluate(i5), is(true));   // unknown statuses are considered active.
        
        i1.setStatusChange(StatusChange.UNCHANGED);
        i2.setStatusChange(StatusChange.UNCHANGED);
        i3.setStatusChange(StatusChange.UNCHANGED);
        i4.setStatusChange(StatusChange.UNCHANGED);
        i5.setStatusChange(StatusChange.UNCHANGED);
        assertThat(p.evaluate(i1), is(false));
        assertThat(p.evaluate(i2), is(true));
        assertThat(p.evaluate(i3), is(true));
        assertThat(p.evaluate(i4), is(true));
        assertThat(p.evaluate(i5), is(true));   // unknown statuses are considered active.
        
        i1.setStatusChange(StatusChange.CLOSED);
        i2.setStatusChange(StatusChange.CLOSED);
        i3.setStatusChange(StatusChange.CLOSED);
        i4.setStatusChange(StatusChange.CLOSED);
        i5.setStatusChange(StatusChange.CLOSED);
        assertThat(p.evaluate(i1), is(true));
        assertThat(p.evaluate(i2), is(true));
        assertThat(p.evaluate(i3), is(true));
        assertThat(p.evaluate(i4), is(true));
        assertThat(p.evaluate(i5), is(true));
        
        i1.setStatusChange(StatusChange.NEW);
        i2.setStatusChange(StatusChange.NEW);
        i3.setStatusChange(StatusChange.NEW);
        i4.setStatusChange(StatusChange.NEW);
        i5.setStatusChange(StatusChange.NEW);
        assertThat(p.evaluate(i1), is(true));
        assertThat(p.evaluate(i2), is(true));
        assertThat(p.evaluate(i3), is(true));
        assertThat(p.evaluate(i4), is(true));
        assertThat(p.evaluate(i5), is(true));
        
        i1.setStatusChange(StatusChange.MODIFIED);
        i2.setStatusChange(StatusChange.MODIFIED);
        i3.setStatusChange(StatusChange.MODIFIED);
        i4.setStatusChange(StatusChange.MODIFIED);
        i5.setStatusChange(StatusChange.MODIFIED);
        assertThat(p.evaluate(i1), is(true));
        assertThat(p.evaluate(i2), is(true));
        assertThat(p.evaluate(i3), is(true));
        assertThat(p.evaluate(i4), is(true));
        assertThat(p.evaluate(i5), is(true));
        
    }

}
