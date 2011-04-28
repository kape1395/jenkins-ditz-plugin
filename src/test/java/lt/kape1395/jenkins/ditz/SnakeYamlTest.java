package lt.kape1395.jenkins.ditz;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.introspector.GenericProperty;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.Tag;

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

	/* ***********************************************************************/
	/**
	 * 
	 * @throws Exception
	 */
	@Test(enabled = false)
	public void testCustomClassLoader() throws Exception {
		TolerantPropertyUtils propertyUtils = new TolerantPropertyUtils();
		propertyUtils.setAllowReadOnlyProperties(true);
		
		Constructor constructor = new Constructor();
		constructor.addTypeDescription(new TypeDescription(Issue.class, "!ditz.rubyforge.org,2008-03-06/issue"));
		constructor.setPropertyUtils(propertyUtils);

		Yaml yaml = new Yaml(constructor);
		try {
		Object o = yaml.load(new FileInputStream(inFile));
		System.out.println("testCustomClassLoader: o=" + o);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	public static class TolerantPropertyUtils extends PropertyUtils {
		
		public static class FakeProperty extends GenericProperty {

			public FakeProperty(String name, Class<?> aClass, Type aType) {
				super(name, aClass, aType);
			}

			@Override
			public Object get(Object object) {
				return null;
			}

			@Override
			public void set(Object object, Object value) throws Exception {
				// Nothing.
			}
			
		}
		public static class FakeScalar {
			public FakeScalar() {
				System.out.println("FakeScalar::FakeScalar()");
				
			}
			public FakeScalar(String string) {
				System.out.println("FakeScalar::FakeScalar(\"" + string + "\")");
			}
			public FakeScalar(String s1, String s2) {
				System.out.println("FakeScalar::FakeScalar(2)");
			}
			public FakeScalar(String s1, String s2, String s3) {
				System.out.println("FakeScalar::FakeScalar(3)");
			}
			public FakeScalar(String s1, String s2, String s3, String s4) {
				System.out.println("FakeScalar::FakeScalar(4)");
			}
		}

		@Override
		public Set<Property> getProperties(
				Class<? extends Object> type,
				BeanAccess bAccess) throws IntrospectionException {
			System.out.println("TolerantPropertyUtils::getProperties 1");
			return super.getProperties(type, bAccess);
		}

		@Override
		public Set<Property> getProperties(Class<? extends Object> type)
				throws IntrospectionException {
			System.out.println("TolerantPropertyUtils::getProperties 2");
			return super.getProperties(type);
		}

		@Override
		protected Map<String, Property> getPropertiesMap(Class<?> type,	BeanAccess bAccess) throws IntrospectionException {
			System.out.println("TolerantPropertyUtils::getPropertiesMap");
			return super.getPropertiesMap(type, bAccess);
		}

		@Override
		public Property getProperty(Class<? extends Object> type, String name,
				BeanAccess bAccess) throws IntrospectionException {
			System.out.println("TolerantPropertyUtils::getProperty 1, name=" + name);
			return super.getProperty(type, name, bAccess);
		}

		@Override
		public Property getProperty(Class<? extends Object> type, String name)
				throws IntrospectionException {
			System.out.println("TolerantPropertyUtils::getProperty 2, name=" + name + " class=" + type);
			FakeProperty prop = new FakeProperty(name, FakeScalar.class, type);
			return prop;
		}

		@Override
		public void setAllowReadOnlyProperties(boolean allowReadOnlyProperties) {
			System.out.println("TolerantPropertyUtils::setAllowReadOnlyProperties");
			super.setAllowReadOnlyProperties(allowReadOnlyProperties);
		}

		@Override
		protected Set<Property> createPropertySet(
				Class<? extends Object> type,
				BeanAccess bAccess) throws IntrospectionException {
			System.out.println("TolerantPropertyUtils::createPropertySet");
			try {
				return super.createPropertySet(type, bAccess);
			} catch (IntrospectionException e) {
				System.out.println("TolerantPropertyUtils::createPropertySet - exception=" + e);
				throw e;
			}
		}

		@Override
		public void setBeanAccess(BeanAccess beanAccess) {
			System.out.println("TolerantPropertyUtils::setBeanAccess");
			super.setBeanAccess(beanAccess);
		}
		
		
	}
	
	/* ***********************************************************************/
	@Test
	public void testIgnoringConstructor() throws Exception {
		IgnoringConstructor constructor = new IgnoringConstructor();
		constructor.addTypeDescription(new TypeDescription(Issue.class, "!ditz.rubyforge.org,2008-03-06/issue"));
		Yaml yaml = new Yaml(constructor);
		try {
			Object o = yaml.load(new FileInputStream(inFile));
			System.out.println("testIgnoringConstructor: o=" + o);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
    private class IgnoringConstructor extends Constructor {
        private Construct original;

        public IgnoringConstructor() {
            original = this.yamlConstructors.get(null);
            this.yamlConstructors.put(null, new IgnoringConstruct());
        }

        private class IgnoringConstruct extends AbstractConstruct {
            public Object construct(Node node) {
            	System.out.println("IgnoringConstruct::construct," +
            			" nodeId=" + node.getNodeId() +
            			" tag=" + node.getTag() +
            			" nodeClass=" + node.getClass().getName());
                if (node.getTag().startsWith("!ditz.rubyforge.org,2008-03-06/issue")) {
                	node.setTwoStepsConstruction(true);
                	node.setUseClassConstructor(true);
                	
                	if (node instanceof MappingNode) {
                		MappingNode mappingNode = (MappingNode) node;
                		System.out.println("MAPPING - start");
                		for (NodeTuple nodeTuple : mappingNode.getValue()) {
                			System.out.println("NodeTuple: " + nodeTuple);
                		}
                		System.out.println("MAPPING - end");
                	}
                	
                	
                	return new Issue();
                    //return original.construct(node);
                } else {
                	System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXX node=" + node);
                	return null;
                	/*
                    switch (node.getNodeId()) {
                    case scalar:
                        return yamlConstructors.get(Tag.STR).construct(node);
                    case sequence:
                        return yamlConstructors.get(Tag.SEQ).construct(node);
                    case mapping:
                        return yamlConstructors.get(Tag.MAP).construct(node);
                    default:
                        throw new YAMLException("Unexpected node");
                    }
                    */
                }
            }

			@Override
			public void construct2ndStep(Node node, Object data) {
            	System.out.println("IgnoringConstruct::construct2ndStep," +
            			" object=" + data +
            			" nodeId=" + node.getNodeId() +
            			" tag=" + node.getTag());
				//super.construct2ndStep(node, data);
			}
            
        }
    }	
	/* ***********************************************************************/
	public static class Issue {
		public Issue() {
			System.out.println("Issue::Issue()");
		}
		public Issue(String string) {
			System.out.println("Issue::Issue(\"" + string + "\")");
		}
		
	}
}
