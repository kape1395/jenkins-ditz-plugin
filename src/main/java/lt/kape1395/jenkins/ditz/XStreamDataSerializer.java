package lt.kape1395.jenkins.ditz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import lt.kape1395.jenkins.ditz.model.Component;
import lt.kape1395.jenkins.ditz.model.Issue;
import lt.kape1395.jenkins.ditz.model.IssueStats;
import lt.kape1395.jenkins.ditz.model.Project;
import lt.kape1395.jenkins.ditz.model.Release;

import com.thoughtworks.xstream.XStream;

/**
 * Does some persistence of ditz project to a XML document.
 *
 * @author k.petrauskas
 */
public class XStreamDataSerializer implements DitzProjectDAO {
	private File file;
	private XStream xstream;

	/**
	 * Constructor.
	 * @param file File to read from or store to.
	 */
	public XStreamDataSerializer(File file) {
		this.file = file;
		this.xstream = createConfiguredXStream();
	}

	/**
	 * Constructor.
	 * @param file File to read from or store to.
	 * @param xstream Preconfigured XStream.
	 */
	public XStreamDataSerializer(File file, XStream xstream) {
		this.file = file;
		this.xstream = xstream;
	}

	/**
	 * Creates configured xstream.
	 * @return xstream instance.
	 */
	protected XStream createConfiguredXStream() {
		XStream xstream = new XStream();
		xstream.alias("project", Project.class);
		xstream.alias("release", Release.class);
		xstream.alias("component", Component.class);
		xstream.alias("issue", Issue.class);
		xstream.alias("issueStats", IssueStats.class);
		xstream.setMode(XStream.XPATH_RELATIVE_REFERENCES);
		xstream.omitField(IssueStats.class, "stats");
		return xstream;
	}

	/**
	 * {@inheritDoc}
	 */
	public Project loadProject() throws Exception {
		FileInputStream inputStream = new FileInputStream(file);
		Project project = (Project) xstream.fromXML(inputStream);
		inputStream.close();
		return project;
	}

	/**
	 * {@inheritDoc}
	 */
	public void saveProject(Project project) throws Exception {
		FileOutputStream outputStream = new FileOutputStream(file);
		xstream.toXML(project, outputStream);
		outputStream.flush();
		outputStream.close();
	}

}
