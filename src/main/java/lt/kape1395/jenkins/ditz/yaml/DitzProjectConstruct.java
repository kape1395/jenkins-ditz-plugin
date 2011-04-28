package lt.kape1395.jenkins.ditz.yaml;

import lt.kape1395.jenkins.ditz.model.Project;
import lt.kape1395.jenkins.ditz.model.Release;
import lt.kape1395.jenkins.ditz.model.Component;

import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;

/**
 * Constructs Project object from YAML node.
 * @author k.petrauskas
 */
public class DitzProjectConstruct extends AbstractConstruct {
	public static final String YAML_CLASS = "!ditz.rubyforge.org,2008-03-06/project";
	private DitzComponentConstruct componentConstruct;
	private DitzReleaseConstruct releaseConstruct;
	
	/**
	 * Default constructor, pre-configures all needed constructs.
	 */
	public DitzProjectConstruct() {
		componentConstruct = new DitzComponentConstruct();
		releaseConstruct = new DitzReleaseConstruct();
	}

	/**
	 * Constructor for IoC.
	 * @param componentConstruct Construct for creating components.
	 * @param releaseConstruct	 Construct for creating releases.
	 */
	public DitzProjectConstruct(
			DitzComponentConstruct componentConstruct,
			DitzReleaseConstruct releaseConstruct) {
		this.componentConstruct = componentConstruct;
		this.releaseConstruct = releaseConstruct;
	}
	
	/**
	 * Implementation of Construct.
	 * @param node Node to be parsed. It must be of class MappingNode.
	 * @return issue.
	 */
	public Object construct(Node node) {
		if (!(node instanceof MappingNode)) {
			throw new YAMLException("MappingNode is required to parse an issue.");
		}
		MappingNode mappingNode = (MappingNode) node;
		
		Project project = new Project();
		
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
				project.setName(value);
			} else if (key.equalsIgnoreCase("components")) {
				SequenceNode sequenceNode = (SequenceNode) nodeTuple.getValueNode();
				for (Node componentNode : sequenceNode.getValue()) {
					project.getComponents().add((Component) componentConstruct.construct(componentNode));
				}
			} else if (key.equalsIgnoreCase("releases")) {
				SequenceNode sequenceNode = (SequenceNode) nodeTuple.getValueNode();
				for (Node releaseNode : sequenceNode.getValue()) {
					project.getReleases().add((Release) releaseConstruct.construct(releaseNode));
				}
			}
		}
		
		return project;
	}

}
