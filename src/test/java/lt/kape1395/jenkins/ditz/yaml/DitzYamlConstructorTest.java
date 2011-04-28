package lt.kape1395.jenkins.ditz.yaml;

import java.io.File;
import java.io.FileInputStream;

import lt.kape1395.jenkins.ditz.model.Issue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.yaml.snakeyaml.Yaml;

/**
 * Test case for DitzYamlConstructor and related classes.
 * @author k.petrauskas
 */
public class DitzYamlConstructorTest {
	private Yaml yaml;
	private DitzYamlConstructor constructor;
	private DelegatingConstruct construct;
	
	@BeforeMethod
	public void beforeMethod() {
		construct = new DelegatingConstruct();
		constructor = new DitzYamlConstructor(construct);
		yaml = new Yaml(constructor);
	}

	/* ***********************************************************************/
	@SuppressWarnings("unchecked")
	@Test
	public void testConstructIssue() throws Exception {
		DitzIssueConstruct ditzIssueConstruct = new DitzIssueConstruct();
		construct.addConstruct(DitzIssueConstruct.YAML_CLASS, ditzIssueConstruct);
		
		File inFile1 = new File("src/test/resources/bugs-01/issue-6bc7bb4238a13c55790b73c132e716b74cf65079.yaml");
		File inFile2 = new File("src/test/resources/bugs-01/issue-b028be7dbeee9870faf416f2361f94f7e762b82e.yaml");
		File inFile3 = new File("src/test/resources/bugs-01/project.yaml");
		Object object1;
		Object object2;
		Object object3;
		try {
			object1 = yaml.load(new FileInputStream(inFile1));
			object2 = yaml.load(new FileInputStream(inFile2));
			object3 = yaml.load(new FileInputStream(inFile3));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		assertThat(object1, allOf(notNullValue(), instanceOf(Issue.class)));
		assertThat(object2, allOf(notNullValue(), instanceOf(Issue.class)));
		assertThat(object3, allOf(notNullValue(), is("VOID")));
		
		Issue issue;
		
		issue = (Issue) object1;
		assertThat(issue.getId(), is("6bc7bb4238a13c55790b73c132e716b74cf65079"));
		assertThat(issue.getTitle(), is("noo"));
		assertThat(issue.getTypeName(), is(":bugfix"));
		assertThat(issue.getStatusName(), is(":unstarted"));
		assertThat(issue.getReleaseName(), nullValue());
		
		issue = (Issue) object2;
		assertThat(issue.getId(), is("b028be7dbeee9870faf416f2361f94f7e762b82e"));
		assertThat(issue.getTitle(), is("Bug in release"));
		assertThat(issue.getTypeName(), is(":task"));
		assertThat(issue.getStatusName(), is(":unstarted"));
		assertThat(issue.getReleaseName(), is("1.0.0"));
		
		System.out.println("testParse: end");
	}

}
