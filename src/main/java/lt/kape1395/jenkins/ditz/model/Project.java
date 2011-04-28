package lt.kape1395.jenkins.ditz.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents DITZ project.
 * @author k.petrauskas
 */
public class Project {
	private String name;
	private List<Component> components;
	private List<Release> releases;
	private List<Issue> issues;
	
	/**
	 * Constructor.
	 */
	public Project() {
		components = new LinkedList<Component>();
		releases = new LinkedList<Release>();
		issues = new LinkedList<Issue>();
	}

	/**
	 * Sets name for a project.
	 * @param name new name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns a name of the project.
	 * @return name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get project's components.
	 * @return list of components.
	 */
	public List<Component> getComponents() {
		return components;
	}

	/**
	 * Get project's releases.
	 * @return list of releases.
	 */
	public List<Release> getReleases() {
		return releases;
	}

	/**
	 * Get project's issues.  
	 * @return list of issues.
	 */
	public List<Issue> getIssues() {
		return issues;
	}

}
