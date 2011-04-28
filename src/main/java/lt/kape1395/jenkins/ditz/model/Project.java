package lt.kape1395.jenkins.ditz.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents DITZ project.
 * @author k.petrauskas
 */
public class Project {
	private String name;
	private List<Release> releases;
	private List<Issue> issues;
	
	/**
	 * Constructor.
	 * @param name Name of a DITZ project.
	 */
	public Project(String name) {
		this.name = name;
		releases = new LinkedList<Release>();
		issues = new LinkedList<Issue>();
	}

	/**
	 * Returns a name of the project.
	 * @return name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Add new release.
	 * @param release release to be registered.
	 */
	public void addRelease(Release release) {
		releases.add(release);
	}

	/**
	 * Add new issue.  
	 * @param issue issue to be registered.
	 */
	public void addIssue(Issue issue) {
		issues.add(issue);
	}

}
