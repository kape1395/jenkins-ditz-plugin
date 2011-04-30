package lt.kape1395.jenkins.ditz.model;

/**
 * Represents component in the DITZ data model.
 * @author k.petrauskas
 */
public class Component extends AbstractIssueStatsCategory {

	/**
	 * Component's name.
	 */
	private String name;

	/**
	 * Constructor.
	 * @param name Name for the component.
	 */
	public Component(String name) {
		this.name = name;
	}

	/**
	 * Returns component name.
	 * @return name.
	 */
	public String getName() {
		return name;
	}

}
