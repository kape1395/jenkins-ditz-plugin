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

import java.util.Collection;
import java.util.Map;

import lt.kape1395.jenkins.ditz.model.Component;
import lt.kape1395.jenkins.ditz.model.Issue;
import lt.kape1395.jenkins.ditz.model.IssueStatCategory;
import lt.kape1395.jenkins.ditz.model.IssueStats;
import lt.kape1395.jenkins.ditz.model.Project;
import lt.kape1395.jenkins.ditz.model.Release;

public class IssueCountCollector implements IssueStatsCollector {
	private Project project;
	private Map<String, IssueStatCategory> releaseStats;
	private Map<String, IssueStatCategory> componentStats;
	
	private IssueCountCollector(Project project) {
		this.project = project;
		for (Release release : project.getReleases()) {
			release.getIssueStats().reset();
			releaseStats.put(release.getName(), release);
		}
		for (Component component : project.getComponents()) {
			component.getIssueStats().reset();
			componentStats.put(component.getName(), component);
		}
	}

	public void collectStatistics(Collection<Issue> issues) {
		IssueStats projectStats = project.getIssueStats();
		for (Issue issue : issues) {
			if (issue.isOpen()) {
				addOpenToCategory(issue.getReleaseName(), releaseStats);
				addOpenToCategory(issue.getComponentName(), componentStats);
				projectStats.setOpenIssues(projectStats.getOpenIssues() + 1);
			}
		}
		projectStats.setNewIssues(projectStats.getOpenIssues());
	}
	
	protected void addOpenToCategory(
			String categoryName,
			Map<String, IssueStatCategory> categories) {
		if (categoryName == null) {
			return;
		}
		IssueStatCategory category = categories.get(categoryName);
		if (category == null) {
			return;
		}
		IssueStats issueStats = category.getIssueStats();
		issueStats.setOpenIssues(issueStats.getOpenIssues() + 1);
		issueStats.setNewIssues(issueStats.getOpenIssues());
	}
}
