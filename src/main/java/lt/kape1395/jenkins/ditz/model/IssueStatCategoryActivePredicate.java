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

/**
 * Selects active categories.
 * For example: releases that have new or closed issues.
 *
 * @author k.petrauskas
 */
public class IssueStatCategoryActivePredicate implements Predicate {

	public boolean evaluate(Object object) {
		if (!(object instanceof IssueStatCategory)) {
			return false;
		}
		IssueStats stats = ((IssueStatCategory) object).getIssueStats();
		return stats.getClosedIssues() != 0 || stats.getNewIssues() != 0 || stats.getOpenIssues() != 0;
	}

}
