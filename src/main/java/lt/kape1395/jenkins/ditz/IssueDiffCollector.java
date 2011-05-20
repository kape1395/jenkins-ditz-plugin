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

import lt.kape1395.jenkins.ditz.model.Component;
import lt.kape1395.jenkins.ditz.model.Issue;
import lt.kape1395.jenkins.ditz.model.IssueStatCategory;
import lt.kape1395.jenkins.ditz.model.IssueStats;
import lt.kape1395.jenkins.ditz.model.Project;
import lt.kape1395.jenkins.ditz.model.Release;

/**
 * 
 * @author k.petrauskas
 */
public class IssueDiffCollector implements IssueStatsCollector {
	
	private Project baseProject;
	
	/**
	 * 
	 * @param baseProject
	 */
	public IssueDiffCollector(Project baseProject) {
		if (baseProject != null) {
			this.baseProject = baseProject;
		} else {
			this.baseProject = new Project();
		}
		
	}
	
	/**
	 * 
	 * @param target
	 */
	public IssueDiffCollector() {
		this(null);
	}
	
	/**
	 * 
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
					sourceIssues.get(issueId), targetIssues.get(issueId),
					projectStats, releaseStats, componentStats);
		}
	}
	
	/**
	 * 
	 * @param project
	 * @return
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
	 * 
	 * @param project
	 * @return
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
	 * 
	 * @param project
	 * @return
	 */
	protected IssueStatCategory resetProjectStats(Project project) {
		project.getIssueStats().reset();
		return project;
	}

	/**
	 * 
	 * @param sourceIssue
	 * @param targetIssue
	 */
	protected void processIssue(
			Issue sourceIssue, Issue targetIssue,
			IssueStatCategory projectStats,
			Map<String, IssueStatCategory> releaseStats,
			Map<String, IssueStatCategory> componentStats) {
		Issue issue;
		IssueStats.StatField statField;
		
		if (sourceIssue != null && targetIssue != null) {
			statField = IssueStats.StatField.OPEN;
			issue = targetIssue;
		} else if (sourceIssue != null) {
			statField = IssueStats.StatField.CLOSED;
			issue = sourceIssue;
		} else if (targetIssue != null) {
			statField = IssueStats.StatField.NEW;
			issue = targetIssue;
		} else {
			return;
		}

		addToCategory(issue.getReleaseName(), releaseStats, statField);
		addToCategory(issue.getComponentName(), componentStats, statField);
		projectStats.getIssueStats().increment(statField);
	}

	/**
	 * 
	 * @param categoryName
	 * @param categories
	 * @param statisticsField
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
