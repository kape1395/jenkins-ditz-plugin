package lt.kape1395.jenkins.ditz.model;

/**
 * Represents one issue in DITZ. 
 * @author k.petrauskas
 */
public class Issue {
	
	/**
	 * Unique ID.
	 */
	private String id;
	
	/**
	 * Bug title.
	 */
	private String title;
	
	/**
	 * String representation of issue type.
	 */
	private String typeName;
	
	/**
	 * String representation of issue status.
	 */
	private String statusName;
	
	/**
	 * String representation of issue release.
	 */
	private String releaseName;

	/**
	 * Constructor.
	 * @param id Id.
	 * @param title Title.
	 * @param typeName Type.
	 * @param statusName Status.
	 * @param releaseName Release.
	 */
	public Issue(
			String id, String title, String typeName,
			String statusName, String releaseName) {
		this.id = id;
		this.title = title;
		this.typeName = typeName;
		this.statusName = statusName;
		this.releaseName = releaseName;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getTypeName() {
		return typeName;
	}

	public String getStatusName() {
		return statusName;
	}

	public String getReleaseName() {
		return releaseName;
	}

}
