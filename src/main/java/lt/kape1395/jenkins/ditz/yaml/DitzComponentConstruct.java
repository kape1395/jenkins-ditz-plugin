package lt.kape1395.jenkins.ditz.yaml;

import lt.kape1395.jenkins.ditz.model.Component;

import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;

/**
 * Constructs Component object from YAML node.
 * @author k.petrauskas
 */
public class DitzComponentConstruct extends AbstractConstruct {
	public static final String YAML_CLASS = "!ditz.rubyforge.org,2008-03-06/component";
	
	/**
	 * Implementation of Construct.
	 * @param node Node to be parsed. It must be of class MappingNode.
	 * @return issue.
	 */
	public Object construct(Node node) {
		if (!(node instanceof MappingNode)) {
			throw new YAMLException("MappingNode is required to parse a component.");
		}
		MappingNode mappingNode = (MappingNode) node;
		
		String name = null;
		
		for (NodeTuple nodeTuple : mappingNode.getValue()) {
			if (!(nodeTuple.getKeyNode() instanceof ScalarNode)) {
				continue;
			}
			String key = ((ScalarNode) nodeTuple.getKeyNode()).getValue();
			
			String value = null;
			if (nodeTuple.getValueNode() instanceof ScalarNode) {
				value = ((ScalarNode) nodeTuple.getValueNode()).getValue();
			}
			if ("".equals(value)) {
				value = null;
			}
			
			if (key.equalsIgnoreCase("name")) {
				name = value;
			}
		}
		
		return new Component(name);
	}

}
