package UMLProfileGeneration.popup.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.epsilon.common.dt.util.LogUtil;
import org.eclipse.epsilon.emc.emf.CachedResourceSet;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import UMLProfileGeneration.UtilityMethods;

public class CreateNEWPapyrusProjectAction implements IObjectActionDelegate {

	private Shell shell;
	private String theSelectedFilePath;
	private String theSelectedFileParentFolder, theDestinationIProjectFolder;
	private IProject theSelectedFileParentIProject, theDestinationIProject;

	/**
	 * Constructor for Action1.
	 */
	public CreateNEWPapyrusProjectAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
		IStructuredSelection theSelectedFile = (IStructuredSelection) targetPart.getSite().getWorkbenchWindow()
				.getSelectionService().getSelection();
		Object firstElement = theSelectedFile.getFirstElement();
		IFile file = (IFile) Platform.getAdapterManager().getAdapter(firstElement, IFile.class);
		theSelectedFileParentFolder = file.getParent().getLocation().toOSString();
		// theProjectFolder = file.getProject().getLocation().toOSString();
		theSelectedFilePath = file.getLocation().toOSString();
		theSelectedFileParentIProject = file.getProject();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {

		UtilityMethods tahh = new UtilityMethods(theSelectedFilePath);

		try {
			 IRunnableWithProgress op = new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) {
					SubMonitor subMonitor = SubMonitor.convert(monitor, 200);
					try {
						theDestinationIProject = tahh.createPluginProject(theSelectedFilePath);
						theDestinationIProjectFolder = theDestinationIProject.getLocation().toOSString();
						subMonitor.setTaskName("Generating the Palette Configuration.");
						tahh.createThePaletteConfiguration(theSelectedFilePath, theDestinationIProjectFolder, theSelectedFileParentIProject);
						subMonitor.split(10);
						subMonitor.setTaskName("Generating the Plugin XML.");
						tahh.createThePluginXml(theSelectedFilePath, theDestinationIProjectFolder);
						subMonitor.split(10);
						subMonitor.setTaskName("Generating the UML Profile.");
						tahh.createTheProfileUmlFile(theSelectedFilePath, theDestinationIProjectFolder, theSelectedFileParentIProject);
						subMonitor.split(30);
						subMonitor.setTaskName("Generating the Project Manifest.");
						tahh.createTheManifestFile(theSelectedFilePath, theDestinationIProjectFolder);
						subMonitor.split(10);
						subMonitor.setTaskName("Generating the Diagram Configuration.");
						tahh.createTheDiagramConfiguration(theSelectedFilePath, theDestinationIProjectFolder, theSelectedFileParentIProject);
						subMonitor.split(10);
						subMonitor.setTaskName("Generating the Element Type Configuration.");
						tahh.createTheElementTypeConfigurations(theSelectedFilePath, theDestinationIProjectFolder, theSelectedFileParentIProject);
						subMonitor.split(10);
						subMonitor.setTaskName("Generating the CSS.");
						tahh.createTheCSSFile(theSelectedFilePath, theDestinationIProjectFolder, theSelectedFileParentIProject);
						subMonitor.split(10);
						subMonitor.setTaskName("Generating the Types Configuration.");
						tahh.createTheTypesConfigurations(theSelectedFilePath, theDestinationIProjectFolder, theSelectedFileParentIProject);
						subMonitor.split(10);
						subMonitor.setTaskName("Generating the UML 2 EMF ETL file.");
						tahh.createTheUml2EmfETLFile(theSelectedFilePath, theDestinationIProjectFolder, theSelectedFileParentIProject);
						subMonitor.split(10);
						subMonitor.setTaskName("Generating Profile related files.");
						tahh.createTheModelProfileNotationFile(theDestinationIProjectFolder);
						subMonitor.split(10);
						subMonitor.setTaskName("Generating Profile related files.");
						tahh.createTheModelProfileDiFile(theDestinationIProjectFolder);
						subMonitor.split(10);
						subMonitor.setTaskName("Generating Build Properties.");
						tahh.createThebuildPropertiesFile(theDestinationIProjectFolder);
						subMonitor.split(10);
						subMonitor.setTaskName("Copying Icons.");
						tahh.copyTheIcons(theSelectedFilePath, theSelectedFileParentIProject.getLocation().toOSString(),
								theDestinationIProjectFolder);
						subMonitor.split(30);
						subMonitor.setTaskName("Copying Shapes.");
						tahh.copyTheShapes(theSelectedFilePath,
								theSelectedFileParentIProject.getLocation().toOSString(), theDestinationIProjectFolder);
						subMonitor.setWorkRemaining(30);
						subMonitor.split(30);
						tahh.refresh(theDestinationIProject);
					} catch (Exception ex) {
						LogUtil.log(ex);
						PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
							public void run() {
								MessageDialog.openError(shell, "Error",
										"An error has occured. Please see the Error Log.");
							}

						});
					} finally {
						CachedResourceSet.getCache().clear();
					}
					//return Status.OK_STATUS;
				}
			};
			 new ProgressMonitorDialog(shell).run(true, true, op);
//			job1.setPriority(Job.SHORT);
	//		job1.schedule();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

}
