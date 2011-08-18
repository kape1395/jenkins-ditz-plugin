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

import java.util.logging.Logger;

/**
 * Aggregated issue statistics.
 * @author k.petrauskas
 */
public class IssueStats {
    /**
     * Logger for debugging.
     */
    private static Logger log = Logger.getLogger(IssueStats.class.getName());

    /**
     * Number of currently open issues.
     */
    private int openIssues;

    /**
     * Number of new issues from the last successful build.
     */
    private int newIssues;

    /**
     * Number of closed issues from the last successful build.
     */
    private int closedIssues;

    /**
     * Constructor. Initializes all to zeros.
     */
    public IssueStats() {
        reset();
    }

    /**
     * Initializes all statistics to zeros.
     */
    public void reset() {
        openIssues = 0;
        newIssues = 0;
        closedIssues = 0;
    }

    /**
     * Increment specified field.
     * @param field Field to be incremented.
     */
    public void increment(StatField field) {
        switch (field) {
        case OPEN:
                openIssues++;
                break;
        case NEW:
                openIssues++;
                newIssues++;
                break;
        case CLOSED:
                closedIssues++;
                break;
        default:
                log.warning("Trying to increment unknown statistic: " + field);
        }
    }

    /**
     * Set some statistic values.
     * @param openIssues    Total open issues per category.
     * @param newIssues     Total new issues per category from the previous build.
     * @param closedIssues  Total closed issues from the previous build.
     */
    public void setStats(int openIssues, int newIssues, int closedIssues) {
        this.openIssues = openIssues;
        this.newIssues = newIssues;
        this.closedIssues = closedIssues;
    }

    /**
     * Get open issues.
     * @return number of open issues.
     * @see #openIssues.
     */
    public int getOpenIssues() {
        return openIssues;
    }

    /**
     * Set statistics.
     * @param openIssues Number of open issues.
     * @see #openIssues.
     */
    public void setOpenIssues(int openIssues) {
        this.openIssues = openIssues;
    }

    /**
     * Get new issues.
     * @return number of new issues.
     * @see #newIssues.
     */
    public int getNewIssues() {
        return newIssues;
    }

    /**
     * Set statistics.
     * @param newIssues Number of new issues.
     * @see #newIssues.
     */
    public void setNewIssues(int newIssues) {
        this.newIssues = newIssues;
    }

    /**
     * Get closed issues.
     * @return number of closed issues.
     * @see #closedIssues.
     */
    public int getClosedIssues() {
        return closedIssues;
    }

    /**
     * Set statistics.
     * @param closedIssues Number of closed issues.
     * @see #closedIssues.
     */
    public void setClosedIssues(int closedIssues) {
        this.closedIssues = closedIssues;
    }

    /**
     * Statistic "fields".
     */
    public static enum StatField {
        /**
         * @see IssueStats#openIssues.
         */
        OPEN,
        /**
         * @see IssueStats#newIssues.
         */
        NEW,
        /**
         * @see IssueStats#closedIssues.
         */
        CLOSED;
    }

}
