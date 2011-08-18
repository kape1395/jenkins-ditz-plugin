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

import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;

/**
 * Checks if issue is assigned to the specified release.
 * @author k.petrauskas
 */
public class IssueInReleasePredicate implements Predicate {
	private Release release;
	
	/**
	 * Constructor. 
	 * @param release Release to find issues assigned to.
	 * 		If release is null, unassigned predicates are returned.
	 */
	public IssueInReleasePredicate(Release release) {
		this.release = release;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean evaluate(Object object) {
		if (object == null || !(object instanceof Issue)) {
			return false;
		}
		Issue issue = (Issue) object;
		if (release != null) {
			return release.getName().equals(issue.getReleaseName());
		} else {
			return StringUtils.isEmpty(issue.getReleaseName());
		}
	}

}
