package lt.kape1395.jenkins.ditz;

import lt.kape1395.jenkins.ditz.model.Project;

/**
 * Interface for persisting Ditz project data.
 * @author k.petrauskas
 */
public interface DitzProjectDAO {

	/**
	 * Load Ditz project data.
	 * @return Loaded project.
	 * @throws Exception if project cannot be loaded.
	 */
	Project loadProject() throws Exception;
	
	/**
	 * Save project data.
	 * @param project to be saved.
	 * @throws Exception if project cannot be loaded.
	 */
	void saveProject(Project project) throws Exception;

}
