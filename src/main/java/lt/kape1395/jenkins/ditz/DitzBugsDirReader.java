package lt.kape1395.jenkins.ditz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;

import org.yaml.snakeyaml.Yaml;

import lt.kape1395.jenkins.ditz.model.Issue;
import lt.kape1395.jenkins.ditz.model.Project;
import lt.kape1395.jenkins.ditz.yaml.DelegatingConstruct;
import lt.kape1395.jenkins.ditz.yaml.DitzIssueConstruct;
import lt.kape1395.jenkins.ditz.yaml.DitzProjectConstruct;
import lt.kape1395.jenkins.ditz.yaml.DitzYamlConstructor;

/**
 * Reads DITZ bugs directory, parses all data files
 * and constructs data model, representing the data.
 * The method {{@link #readBugsDir(File)} is the entry point here.
 *
 * @author k.petrauskas
 */
public class DitzBugsDirReader {
	
	/**
	 * Yaml parser.
	 */
	private Yaml yaml;
	
	/**
	 * Name of the project file in the DITZ bugs directory.
	 * Default is "project.yaml".
	 */
	private String projectFileName = "project.yaml";
	
	/**
	 * Suffix of the issue files in the DITZ bugs directory.
	 * Default is ".yaml".
	 */
	private String issueFileSuffix = ".yaml";

	/**
	 * This constructor configures yaml parser internally.
	 */
	public DitzBugsDirReader() {
		DitzIssueConstruct ditzIssueConstruct = new DitzIssueConstruct();
		DitzProjectConstruct ditzProjectConstruct = new DitzProjectConstruct();
		
		DelegatingConstruct delegatingConstruct = new DelegatingConstruct();
		delegatingConstruct.addConstruct(DitzIssueConstruct.YAML_CLASS, ditzIssueConstruct);
		delegatingConstruct.addConstruct(DitzProjectConstruct.YAML_CLASS, ditzProjectConstruct);
		
		yaml = new Yaml(new DitzYamlConstructor(delegatingConstruct));
	}
	
	/**
	 * This constructor takes pre-configured yaml parser.
	 * @param yaml Yaml parser, configured to parse ditz data files.
	 */
	public DitzBugsDirReader(Yaml yaml) {
		this.yaml = yaml;
	}
	
	/**
	 * Read bugs directory.
	 *
	 * @param bugsDir Bugs directory.
	 * @return Project.
	 * @throws Exception if something goes wrong.
	 */
	public Project readBugsDir(File bugsDir) throws Exception {
		File projectFile = new File(bugsDir, projectFileName);
		Project project = (Project) yaml.load(new FileInputStream(projectFile));
		
		FilenameFilter filter = new IssueFilenameFilter(projectFileName, issueFileSuffix);
		for (File issueFile : bugsDir.listFiles(filter)) {
			Issue issue = (Issue) yaml.load(new FileInputStream(issueFile));
			project.getIssues().add(issue);
		}
		return project;
	}

	/**
	 * Filters files that are issues.
	 */
	protected static class IssueFilenameFilter implements FilenameFilter {
		private String projectFileName;
		private String issueFileSuffix;
		
		/**
		 * Constructor.
		 * @param projectFileName to be ignored.
		 * @param issueFileSuffix to be included.
		 */
		public IssueFilenameFilter(String projectFileName, String issueFileSuffix) {
			this.projectFileName = projectFileName;
			this.issueFileSuffix = issueFileSuffix;
		}

		/**
		 * Implementation of FilenameFilter.
		 * @param dir Directory containing the file.
		 * @param name Name of the file to be tested.
		 */
		public boolean accept(File dir, String name) {
			return name != null
					&& !name.equals(projectFileName)
					&& name.endsWith(issueFileSuffix);
		}
		
	}
}
