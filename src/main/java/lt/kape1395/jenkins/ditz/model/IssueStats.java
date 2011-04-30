package lt.kape1395.jenkins.ditz.model;

/**
 * Aggregated issue statistics.
 * @author k.petrauskas
 */
public class IssueStats {
	
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
