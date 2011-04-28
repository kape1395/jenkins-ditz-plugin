package lt.kape1395.jenkins.ditz.model;

/**
 * Represents release in the DITZ data model.
 * @author k.petrauskas
 */
public class Release {
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
