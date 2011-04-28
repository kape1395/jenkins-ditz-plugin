package lt.kape1395.jenkins.ditz.yaml;

import java.util.Map;
import java.util.HashMap;

import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;

/**
 * Creates objects, that are known to this implementation, 
 * ignoring all the rest. Unknown nodes are returned as "VOID" string.
 *
 * @author k.petrauskas
 */
public class DelegatingConstruct extends AbstractConstruct {
	/**
	 * Constructs by DITZ YAML class name.
	 */
	private Map<String, Construct> constructs;

	/**
	 * Constructor.
	 */
	public DelegatingConstruct() {
		constructs = new HashMap<String, Construct>();
	}
	
	/**
	 * Use this to register all constructors for considered classes.
	 * @param yamlClass
	 * @param construct
	 */
	public void addConstruct(String yamlClass, Construct construct) {
		constructs.put(yamlClass, construct);
	}
	
	/**
	 * Creates objects, that are known to this implementation, 
	 * ignoring all the rest. Unknown nodes are returned as "VOID" string.
	 * @param node Node to be parsed.
	 * @return Constructed object or "VOID" string. 
	 */
	public Object construct(Node node) {
		Construct target = constructs.get(node.getTag().getValue());
		if (node instanceof MappingNode && target != null) {
			return target.construct(node);
		} else {
			return "VOID";
		}
	}

}
