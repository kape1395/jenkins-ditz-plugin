package lt.kape1395.jenkins.ditz.model;

/**
 * Base implementation for {@link IssueStats}.
 * @author k.petrauskas
 */
public class AbstractIssueStatsCategory {

	/**
	 * Issue statistics per project.
	 */
	private IssueStats issueStats;
	
	/**
	 * Constructor.
	 * Initializes issue statistics.
	 */
	public AbstractIssueStatsCategory() {
		issueStats = new IssueStats();
	}

	/**
	 * Returns issue statistics.
	 * @return statistics.
	 */
	public IssueStats getIssueStats() {
		return issueStats;
	}

	/**
	 * Set issue statistics.
	 * @param issueStats New statistics.
	 */
	public void setIssueStats(IssueStats issueStats) {
		if (issueStats == null) {
			this.issueStats.reset();
		} else {
			this.issueStats = issueStats;
		}
	}

}
