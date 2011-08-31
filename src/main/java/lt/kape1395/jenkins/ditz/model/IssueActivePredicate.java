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

import lt.kape1395.jenkins.ditz.model.Issue.StatusChange;

import org.apache.commons.collections.Predicate;

/**
 * Checks if issue was active in this build (created or closed).
 * @author k.petrauskas
 */
public class IssueActivePredicate implements Predicate {

    /**
     * Used to check, if issue is open.
     */
    private static Predicate issueIsOpen = new IssueOpenPredicate();

    /**
     * {@inheritDoc}
     */
    public boolean evaluate(Object object) {
        if (!(object instanceof Issue)) {
            return false;
        }
        Issue issue = (Issue) object;
        Issue.StatusChange ss = issue.getStatusChange();
        return (ss != null && ss != StatusChange.UNCHANGED) || issueIsOpen.evaluate(issue);
    }

}
