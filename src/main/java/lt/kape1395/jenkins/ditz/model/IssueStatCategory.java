package lt.kape1395.jenkins.ditz.model;

/**
 * Interface, that is implemented by the classes, that
 * categorize issue statistics.
 * @author k.petrauskas
 */
public interface IssueStatCategory {

	/**
	 * Get issue statistics by category, represented by a class,
	 * implementing this interface.
	 * @return Issue statistics.
	 */
	public IssueStats getIssueStats();
}
