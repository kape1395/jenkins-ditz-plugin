package lt.kape1395.jenkins.ditz;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import javax.servlet.ServletException;

import lt.kape1395.jenkins.ditz.model.Project;

import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.util.FormValidation;

/**
 * Ditz information publisher.
 * @author k.petrauskas
 */
public class DitzPublisher extends Recorder {

	private static final String DITZ_PROJECT_FILE = "ditzProject.xml";
	/**
	 * Ditz bugs directory.
	 */
	private String bugsDir;
	
	/* ********************************************************************* */
	/**
	 * Constructor.
	 * @param bugsDir Ditz bugs directory.
	 */
	@DataBoundConstructor
	public DitzPublisher(String bugsDir) {
		this.bugsDir = bugsDir;
	}
	
	/* ********************************************************************* */
	/**
	 * Returns bugs directory, used by this project.
	 * @return Ditz bugs directory.
	 */
	public String getBugsDir() {
		return bugsDir;
	}
	
	/* ********************************************************************* */
	/**
	 * Perform real action.
	 * @param build
	 * @param launcher
	 * @param listener
	 */
	@Override
	public boolean perform(
			AbstractBuild<?, ?> build,
			Launcher launcher,
			BuildListener listener) throws InterruptedException, IOException {
		listener.getLogger().println("DitzPublisher::perform - start");
		
		FilePath ws = build.getProject().getSomeWorkspace();
		
		try {
			File ditzBugsDir = new File(ws.child(getBugsDir()).absolutize().getName());
			File ditzXmlFile = new File(build.getRootDir(), DITZ_PROJECT_FILE);

			// Copy Ditz data from workspace to the build directory.
			Project project = new DitzBugsDirReader(ditzBugsDir).loadProject();
			new XStreamDataSerializer(ditzXmlFile).saveProject(project);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		listener.getLogger().println("DitzPublisher::perform - end");
		return true;
	}

	/* ********************************************************************* */
	/**
	 * I still don't know, what it is...
	 */
	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.NONE;
	}

	/* ********************************************************************* */

	/**
	 * Get build step descriptor.
	 */
	@Override
	public DescriptorImpl getDescriptor() {
		return (DescriptorImpl) super.getDescriptor();
	}

	/**
	 * Build step description.
	 */
    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {
    	/**
    	 * Display name.
    	 */
        public String getDisplayName() {
            return Messages.DitzPublisher_DisplayName();
        }

        /**
         * Performs on-the-fly validation on the file mask wildcard.
         * Bugs directory should exist.
         */
        @SuppressWarnings("unchecked")
        public FormValidation doCheckBugsDir(
        		@AncestorInPath AbstractProject project,
        		@QueryParameter String value) throws IOException, ServletException {
            FilePath ws = project.getSomeWorkspace();
            if (ws != null) {
            	FormValidation formValidation = ws.validateRelativeDirectory(value);
            	if (formValidation.kind != FormValidation.Kind.OK) {
            		return formValidation;
            	}
            	String projectFile = value + "/" + DitzBugsDirReader.DEFAULT_PROJECT_FILE_NAME;
          		return ws.validateRelativePath(projectFile, true, true);
            } else {
            	return FormValidation.ok();
            }
        }
        
        /**
         * Applicable to all project types.
         */
        @SuppressWarnings("unchecked")
		public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }
    }

	/* ********************************************************************* */
	@Override
	public Collection<? extends Action> getProjectActions(AbstractProject<?, ?> project) {
		return Collections.<Action>singleton(new DitzPublisherAction());
	}
}
