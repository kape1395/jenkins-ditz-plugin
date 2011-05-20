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
 * Aggregated issue statistics.
 * @author k.petrauskas
 */
public class IssueStats {
	
	/**
	 * 
	 */
	public static enum StatField {
		OPEN, NEW, CLOSED;
	}
	
	private int openIssues;
	private int newIssues;
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
	 * 
	 * @param field
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
		}
	}
	
	/**
	 * Set some statistic values.
	 * @param openIssues    Total open issues per category.
	 * @param newIssues     Total new issues per category from the previous build.
	 * @param closedIssues 	Total closed issues from the previous build. 
	 */
	public void setStats(int openIssues, int newIssues, int closedIssues) {
		this.openIssues = openIssues;
		this.newIssues = newIssues;
		this.closedIssues = closedIssues;
	}
	
	public int getOpenIssues() {
		return openIssues;
	}

	public void setOpenIssues(int openIssues) {
		this.openIssues = openIssues;
	}

	public int getNewIssues() {
		return newIssues;
	}

	public void setNewIssues(int newIssues) {
		this.newIssues = newIssues;
	}

	public int getClosedIssues() {
		return closedIssues;
	}

	public void setClosedIssues(int closedIssues) {
		this.closedIssues = closedIssues;
	}

}
