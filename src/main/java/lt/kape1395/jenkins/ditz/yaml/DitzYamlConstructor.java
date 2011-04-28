package lt.kape1395.jenkins.ditz.yaml;

import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.constructor.Constructor;

/**
 * Constructor, used to parse DITZ YAML files.
 * @author k.petrauskas
 */
public class DitzYamlConstructor extends Constructor {

	/**
	 * This constructor is here mainly to support IoC and unit tests.
	 * @param classConstruct Construct, to be used to create classes.
	 */
    public DitzYamlConstructor(Construct classConstruct) {
        this.yamlConstructors.put(null, classConstruct);
    }

    /**
     * This constructor configures the object to use predefined
     * constructs for all the currently known YAML classes.
     */
    public DitzYamlConstructor() {
    	DelegatingConstruct construct = new DelegatingConstruct();
    	construct.addConstruct(DitzIssueConstruct.YAML_CLASS, new DitzIssueConstruct());
        this.yamlConstructors.put(null, construct);
    }

}
