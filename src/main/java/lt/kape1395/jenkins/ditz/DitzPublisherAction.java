package lt.kape1395.jenkins.ditz;

import lt.kape1395.jenkins.ditz.model.Project;
import lt.kape1395.jenkins.ditz.model.Release;

import hudson.model.Action;

/**
 * 
 * @author k.petrauskas
 */
public class DitzPublisherAction implements Action {
	private Project project;

	public String getDisplayName() {
		return Messages.DitzPublisher_ActionName();
	}

	public String getIconFileName() {
		return Messages.DitzPublisher_Icon();
	}

	public String getUrlName() {
		return "http://karolis.5grupe.lt/";
	}

	
	public Project getProject() {
		if (project == null) {
			project = new Project();
			Release r1 = new Release("r1", ":unreleased");
			r1.getIssueStats().setStats(10, 5, 2);
			project.getReleases().add(r1);
		}
		return project;
	}
}
