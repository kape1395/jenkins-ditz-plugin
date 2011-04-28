package lt.kape1395.jenkins.ditz;

import java.io.File;

import lt.kape1395.jenkins.ditz.model.Project;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Test case for {@link DitzBugsDirReader}.
 *
 * @author k.petrauskas
 */
public class DitzBugsDirReaderTest {
	private DitzBugsDirReader ditzBugsDirReader;
	
	@BeforeMethod
	public void beforeMethod() {
		ditzBugsDirReader = new DitzBugsDirReader();
	}

	/* ***********************************************************************/
	/**
	 * Test if loading of DITZ directory works.
	 */
	@Test
	public void testReadBugsDir() throws Exception {
		Project project = ditzBugsDirReader.readBugsDir(new File("src/test/resources/bugs-01"));

		assertThat(project.getName(), is("x"));
		assertThat(project.getIssues().size(), is(2));
	}

	/* ***********************************************************************/
}
