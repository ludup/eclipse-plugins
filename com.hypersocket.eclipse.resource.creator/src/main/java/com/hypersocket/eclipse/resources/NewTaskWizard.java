package com.hypersocket.eclipse.resources;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.ClasspathEntry;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.osgi.framework.Bundle;

/**
 * This is a sample new wizard. Its role is to create a new file 
 * resource in the provided container. If the container resource
 * (a folder or a project) is selected in the workspace 
 * when the wizard is opened, it will accept it as the target
 * container. The wizard creates one file with the extension
 * ".java". If a sample multi-page editor (also available
 * as a template) is registered for the same extension, it will
 * be able to open it.
 */

public class NewTaskWizard extends Wizard implements INewWizard {
	private NewTaskWizardPage page;
	private ISelection selection;

	/**
	 * Constructor for NewResourceWizard.
	 */
	public NewTaskWizard() {
		super();
		setNeedsProgressMonitor(true);
	}
	
	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		page = new NewTaskWizardPage(selection);
		addPage(page);
	}

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	public boolean performFinish() {
		
		final IProject project = page.getSelectedProject();
		final String containerName = page.getContainerName();
		final String resourceName = page.getResourceName();
		final String packageName = page.getPackageName();
		
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(project, containerName, resourceName, packageName, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * The worker method. It will find the container, create the
	 * file if missing or just replace its contents, and open
	 * the editor on the newly created file.
	 */

	private void doFinish(
		IProject project,
		String containerName,
		String resourceName,
		String packageName, 
		IProgressMonitor monitor)
		throws CoreException {
		// create a sample file
		monitor.beginTask("Creating package " + packageName, 2);
		
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		if(containerName.startsWith("/")) {
			containerName = containerName.substring(1);
		}
		IResource resource = root.findMember(new Path(containerName));
		if (!resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException("Project \"" + containerName + "\" does not exist.");
		}
		
		IJavaProject javaProject = JavaCore.create(project);
		IClasspathEntry sourcePath = null;
		IClasspathEntry resourcesPath = null;
		for(IClasspathEntry cp : javaProject.getRawClasspath()) {
			if(cp.getEntryKind()==ClasspathEntry.CPE_SOURCE) {
				if(cp.getPath().toString().endsWith("src/main/java")) {
					sourcePath = cp;
				} else if(cp.getPath().toString().endsWith("src/main/resources")) {
					resourcesPath = cp;
				} else if(cp.getPath().toString().endsWith("src")) {
					sourcePath = cp;
				} else if(cp.getPath().toString().endsWith("resources") && !cp.getPath().toString().endsWith("src/test/resources")) {
					resourcesPath = cp;
				}
			}
		}
		
		if(sourcePath==null) {
			throwCoreException("Cannot find source path for project " + containerName);
		}
		
		if(resourcesPath==null) {
			resourcesPath = sourcePath;
		}
		
		IFile sourceFolder = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(sourcePath.getPath().toString()));
		IFile resourceFolder = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(resourcesPath.getPath().toString()));
		
		File newPackage;
		try {
			newPackage = new File(new URI(sourceFolder.getRawLocationURI().toASCIIString() + "/" + packageName.replace('.', java.io.File.separatorChar)));
			newPackage.mkdirs();
			
			File newResources = new File(new URI(resourceFolder.getRawLocationURI().toASCIIString()));
			new File(newResources, "i18n").mkdirs();
			new File(newResources, "tasks").mkdirs();

			copyTask(newPackage, newResources, resourceName, packageName);
		} catch (Exception e1) {
			throwCoreException(e1.getMessage());
		}

		monitor.worked(1);
		monitor.setTaskName("Refreshing project");
		try {
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
		}
		monitor.worked(1);
		
	}
	
	private void copyTask(File newPackage, File newResources, String resourceName, String packageName) throws URISyntaxException, IOException, CoreException {
		
		String _resource = FileHelper.lowerCaseFirst(resourceName);
		
		copyResource("task/src/com/hypersocket/template/task/TemplateTask.java",
				new File(newPackage, resourceName + "Task.java"), resourceName, packageName);
		copyResource("task/src/com/hypersocket/template/task/TemplateTaskRepository.java",
				new File(newPackage, resourceName + "TaskRepository.java"), resourceName, packageName);
		copyResource("task/src/com/hypersocket/template/task/TemplateTaskRepositoryImpl.java",
				new File(newPackage, resourceName + "TaskRepositoryImpl.java"), resourceName, packageName);
		copyResource("task/src/com/hypersocket/template/task/TemplateTaskResult.java",
				new File(newPackage, resourceName + "TaskResult.java"), resourceName, packageName);

		copyResource("task/i18n/__Resource__Task.properties",			
				new File(newResources, "i18n" + File.separator + resourceName + "Task.properties"), resourceName, packageName);
		copyResource("task/tasks/__resource__Task.xml",			
				new File(newResources, "tasks" + File.separator + _resource + "Task.xml"), resourceName, packageName);
		
	}

	private void copyResource(String sourcePath, File target, String resourceName, String packageName) throws URISyntaxException, IOException, CoreException {
	
		Bundle bundle = Platform.getBundle("com.hypersocket.eclipse.resource.creator");
		Path path = new Path(sourcePath);
		URL sourceURL = FileLocator.find(bundle, path, null);
		FileHelper.copyResource(sourceURL, target, resourceName, packageName, "fa-hand-sparkles");
		
	}
	
	private void throwCoreException(String message) throws CoreException {
		IStatus status =
			new Status(IStatus.ERROR, "hypersocket-eclipse-resource-creator", IStatus.OK, message, null);
		throw new CoreException(status);
	}

	/**
	 * We will accept the selection in the workbench to see if
	 * we can initialize from it.
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}