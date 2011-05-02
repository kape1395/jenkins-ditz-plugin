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

import lt.kape1395.jenkins.ditz.model.Project;
import lt.kape1395.jenkins.ditz.model.Release;

import hudson.model.Action;

/**
 * 
 * @author k.petrauskas
 */
public class DitzPublisherAction implements Action {
	private Project project;

	public String getDisplayName() {
		return Messages.DitzPublisher_ActionName();
	}

	public String getIconFileName() {
		return Messages.DitzPublisher_Icon();
	}

	public String getUrlName() {
		return "http://karolis.5grupe.lt/";
	}

	
	public Project getProject() {
		if (project == null) {
			project = new Project();
			Release r1 = new Release("r1", ":unreleased");
			r1.getIssueStats().setStats(10, 5, 2);
			project.getReleases().add(r1);
		}
		return project;
	}
}
