package lt.kape1395.jenkins.ditz.model;

/**
 * Represents release in the DITZ data model.
 * @author k.petrauskas
 */
public class Release {
	private String name;
	private String status;
	
	public Release(String name, String status) {
		this.name = name;
		this.status = status;
	}
	
	public String getName() {
		return name;
	}
	
	public String getStatus() {
		return status;
	}
	
}
