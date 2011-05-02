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

/**
 * Represents release in the DITZ data model.
 * @author k.petrauskas
 */
public class Release extends AbstractIssueStatsCategory {
	/**
	 * Name of a release.
	 */
	private String name;
	/**
	 * String representation of the release status.
	 */
	private String statusName;

	/**
	 * Constructor.
	 * @param name Name of the release.
	 * @param statusName Status of the release as string.
	 */
	public Release(String name, String statusName) {
		this.name = name;
		this.statusName = statusName;
	}

	/**
	 * Get release name.
	 * @return name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get release status as string.
	 * @return status, e.g. ":unreleased".
	 */
	public String getStatusName() {
		return statusName;
	}

}
