package lt.kape1395.jenkins.ditz;

import java.io.File;
import java.io.FileReader;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.events.Event;


public class SnakeYamlTest {
	private File inFile = new File("src/test/resources/bugs-01/issue-6bc7bb4238a13c55790b73c132e716b74cf65079.yaml");

	
	@BeforeClass
	public void beforeClass() {
	}

	@AfterClass
	public void afterClass() {
	}

	/* ***********************************************************************/
	@Test(enabled = false)
	public void testParse() throws Exception {
		Yaml yaml = new Yaml();
		
		System.out.println("testParse: start");
		for (Event event : yaml.parse(new FileReader(inFile))) {
			System.out.println("testParse: event=" + event);
		}
		System.out.println("testParse: end");
	}

}
