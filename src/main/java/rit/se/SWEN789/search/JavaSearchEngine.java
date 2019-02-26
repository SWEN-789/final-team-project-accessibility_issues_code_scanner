package rit.se.SWEN789.search;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;

public class JavaSearchEngine {
	
	
	public JavaSearchEngine() {
	}

	public void search() throws JavaModelException{
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject("EverMemo");
		IJavaProject javaProject = JavaCore.create(project);
		IPath path = new Path("/EverMemo/src/main/java");
//		IPath path = new Path("/EverMemo/app/src/main/java");
		IPackageFragment javaPackage = javaProject.findPackageFragment(path);
		IPackageFragment[] packages = {javaPackage};
		SearchPattern pattern = SearchPattern.createPattern("*Activity*",
				IJavaSearchConstants.CLASS, IJavaSearchConstants.DECLARATIONS,
				SearchPattern.R_EXACT_MATCH);
		
		IJavaSearchScope scope = SearchEngine.createJavaSearchScope(packages);
		
		SearchRequestor requestor = new SearchRequestor() {
			private int count;
			public void acceptSearchMatch(SearchMatch match) {
				System.out.println(match.getElement());
				System.out.println("count: "+(++count));
			}
		};
		
		SearchEngine searchEngine = new SearchEngine();
		try {
			searchEngine.search(pattern,
					new SearchParticipant[] { SearchEngine
							.getDefaultSearchParticipant() }, scope,
					requestor, null);
		} catch (CoreException e) {
			System.out.println("exception");
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String[] args) {
		JavaSearchEngine engine = new JavaSearchEngine();
		try {
			engine.search();
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
