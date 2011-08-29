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

/**
 * Tests for {@link IssueOpenPredicate}.
 * @author k.petrauskas
 */
public class IssueOpenPredicateTest {

    /**
     * By all statuses. 
     */
    @Test
    public void testEvaluate() {
        Predicate p = new IssueOpenPredicate();
        
        assertThat(p.evaluate(new Issue("a", "b", "c", Status.CLOSED, "d")), is(false));
        assertThat(p.evaluate(new Issue("a", "b", "c", Status.IN_PROGRESS, "d")), is(true));
        assertThat(p.evaluate(new Issue("a", "b", "c", Status.PAUSED, "d")), is(true));
        assertThat(p.evaluate(new Issue("a", "b", "c", Status.UNSTARTED, "d")), is(true));
        assertThat(p.evaluate(new Issue("a", "b", "c", "some", "d")), is(true));
    }

    /**
     * If issue was not closed but removed, it stays with the active status,
     * but statusChange is recorded correctly. To determine, if issue is active,
     * one should look at both statuses.
     */
    @Test
    public void testPreviouslyDeletedIssues() {
        Predicate p = new IssueOpenPredicate();
        
        Issue i;
        i = new Issue("a", "b", "c", Status.CLOSED, "d");
        i.setStatusChange(StatusChange.CLOSED);
        assertThat(p.evaluate(i), is(false));
        
        i = new Issue("a", "b", "c", Status.IN_PROGRESS, "d");
        i.setStatusChange(StatusChange.CLOSED);
        assertThat(p.evaluate(i), is(false));
        
        i = new Issue("a", "b", "c", Status.PAUSED, "d");
        i.setStatusChange(StatusChange.CLOSED);
        assertThat(p.evaluate(i), is(false));
        
        i = new Issue("a", "b", "c", Status.UNSTARTED, "d");
        i.setStatusChange(StatusChange.CLOSED);
        assertThat(p.evaluate(i), is(false));
        
        i = new Issue("a", "b", "c", "some", "d");
        i.setStatusChange(StatusChange.CLOSED);
        assertThat(p.evaluate(i), is(false));
        
        i = new Issue("a", "b", "c", Status.IN_PROGRESS, "d");
        i.setStatusChange(StatusChange.NEW);
        assertThat(p.evaluate(i), is(true));
        
    }

}
