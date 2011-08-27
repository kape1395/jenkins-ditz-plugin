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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.collections.Predicate;

import lt.kape1395.jenkins.ditz.model.Component;
import lt.kape1395.jenkins.ditz.model.Issue;
import lt.kape1395.jenkins.ditz.model.IssueOpenPredicate;
import lt.kape1395.jenkins.ditz.model.IssueStatCategory;
import lt.kape1395.jenkins.ditz.model.IssueStats;
import lt.kape1395.jenkins.ditz.model.Project;
import lt.kape1395.jenkins.ditz.model.Release;
import lt.kape1395.jenkins.ditz.model.Issue.StatusChange;

/**
 * Calculates project statistics by comparing ditz project to
 * one from previous successful build.
 *
 * Apart from calculating statistics, status changes are set for
 * all issues. These are used when showing ditz issue details.
 *
 * @author k.petrauskas
 */
public class IssueDiffCollector implements IssueStatsCollector {
    /**
     * Logger for debugging.
     */
    private static Logger log = Logger.getLogger(IssueDiffCollector.class.getName());

    /**
     * Previous project (to compare with).
     */
    private Project baseProject;

    /**
     * Constructor.
     * @param baseProject Previous project/
     */
    public IssueDiffCollector(Project baseProject) {
        if (baseProject != null) {
            this.baseProject = baseProject;
        } else {
            this.baseProject = new Project();
        }
    }

    /**
     * Constructor for the case, when there is no previous build.
     * In this case all open issues will be new as well.
     */
    public IssueDiffCollector() {
        this(null);
    }

    /**
     * {@inheritDoc}
     */
    public void collectStatistics(Project project) {
        Set<String> issueIds = new HashSet<String>();
        Map<String, Issue> sourceIssues = new HashMap<String, Issue>();
        Map<String, Issue> targetIssues = new HashMap<String, Issue>();
        Map<String, IssueStatCategory> releaseStats = resetReleaseStats(project);
        Map<String, IssueStatCategory> componentStats = resetComponentStats(project);
        IssueStatCategory projectStats = resetProjectStats(project);

        for (Issue issue : baseProject.getIssues()) {
            issueIds.add(issue.getId());
            sourceIssues.put(issue.getId(), issue);
        }
        for (Issue issue : project.getIssues()) {
            issueIds.add(issue.getId());
            targetIssues.put(issue.getId(), issue);
        }

        for (String issueId : issueIds) {
            processIssue(
                    project,
                    sourceIssues.get(issueId), targetIssues.get(issueId),
                    projectStats, releaseStats, componentStats);
        }
    }

    /**
     * Reset statistics for all releases.
     * @param project Project to deal with.
     * @return Map of all releases indexed by release names.
     */
    protected Map<String, IssueStatCategory> resetReleaseStats(Project project) {
        Map<String, IssueStatCategory> releaseStats = new HashMap<String, IssueStatCategory>();
        for (Release release : project.getReleases()) {
            release.getIssueStats().reset();
            releaseStats.put(release.getName(), release);
        }
        return releaseStats;
    }

    /**
     * Reset statistics for all components.
     * @param project Project to deal with.
     * @return Map of all components indexed by component names.
     */
    protected Map<String, IssueStatCategory> resetComponentStats(Project project) {
        Map<String, IssueStatCategory> componentStats = new HashMap<String, IssueStatCategory>();
        for (Component component : project.getComponents()) {
            component.getIssueStats().reset();
            componentStats.put(component.getName(), component);
        }
        return componentStats;
    }

    /**
     * Reset project statistics.
     * @param project Project to deal with.
     * @return Project statistics (most general stats).
     */
    protected IssueStatCategory resetProjectStats(Project project) {
        project.getIssueStats().reset();
        return project;
    }

    /**
     * Process an issue by updating required statistics of
     * project, release, component, etc.
     *
     * @param project
     *      Project for which the statistics are being collected.
     * @param sourceIssue
     *      Issue as it was in the previous build.
     *      It can be null, if not existed.
     * @param targetIssue
     *      Issue as is is in the current build.
     *      It can be bull, if was closed or deleted.
     * @param projectStats
     *      Project summary statistics.
     * @param releaseStats
     *      Statistics by release.
     * @param componentStats
     *      Statistics by component.
     */
    protected void processIssue(
            Project project,
            Issue sourceIssue, Issue targetIssue,
            IssueStatCategory projectStats,
            Map<String, IssueStatCategory> releaseStats,
            Map<String, IssueStatCategory> componentStats) {
        Issue issue;
        IssueStats.StatField statField;

        log.fine("processIssue: src=" + sourceIssue + " dst=" + targetIssue);

        
        Predicate issueIsOpen = new IssueOpenPredicate();
        if (targetIssue != null) {
            targetIssue.setStatusChange(StatusChange.UNCHANGED);
        }

        if (issueIsOpen.evaluate(sourceIssue) && issueIsOpen.evaluate(targetIssue)) {
            statField = IssueStats.StatField.OPEN;
            issue = targetIssue;
            issue.setStatusChange(StatusChange.UNCHANGED);
        } else if (issueIsOpen.evaluate(sourceIssue)) {
            statField = IssueStats.StatField.CLOSED;
            issue = sourceIssue;
            if (targetIssue == null) {
                issue.setStatusChange(StatusChange.CLOSED);
                project.getIssues().add(issue);
            } else {
                targetIssue.setStatusChange(StatusChange.CLOSED);
            }
        } else if (issueIsOpen.evaluate(targetIssue)) {
            statField = IssueStats.StatField.NEW;
            issue = targetIssue;
            issue.setStatusChange(StatusChange.NEW);
        } else {
            project.getIssues().remove(targetIssue);
            return; // statistics should not be affected.
        }

        log.fine("processIssue: stat=" + statField + " " + issue);

        addToCategory(issue.getReleaseName(), releaseStats, statField);
        addToCategory(issue.getComponentName(), componentStats, statField);
        projectStats.getIssueStats().increment(statField);
    }

    /**
     * Add issue to a category by incrementing corresponding statistic field.
     * @param categoryName      Name of the category to deal with.
     * @param categories        List of all categories.
     * @param statisticsField   Field to be incremented.
     */
    protected void addToCategory(
            String categoryName,
            Map<String, IssueStatCategory> categories,
            IssueStats.StatField statisticsField) {

        if (categoryName == null) {
            return;
        }

        IssueStatCategory category = categories.get(categoryName);
        if (category == null) {
            return;
        }

        category.getIssueStats().increment(statisticsField);
    }

}
