package rit.se.SWEN789.astparser.process;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import rit.se.SWEN789.astparser.parser.ClassParser;
import rit.se.SWEN789.astparser.parser.CodeParser;
import rit.se.SWEN789.astparser.parser.InnerClassVisitor;
import rit.se.SWEN789.astparser.vo.ClassBean;
import rit.se.SWEN789.astparser.vo.MethodBean;
import rit.se.SWEN789.astparser.vo.PackageBean;


public class FolderToJavaProjectConverter {
	
	public static void main(String[] args) {
		try {
			List<ClassBean> beans = extractActivityClasses("C:\\MyPc\\RIT\\Sem2\\RA\\SWEN789\\Testing-apps\\EverMemo");
			List<MethodBean> mb = (List<MethodBean>) beans.get(0).getMethods();
			List<MethodBean> s = (List<MethodBean>) mb.get(0).getMethodCalls();
			for (MethodBean methodBean : s) {
				System.out.println(methodBean.getName());
			}
		} catch (CoreException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public static ArrayList<ClassBean> extractClasses(String pPath) throws CoreException, IOException {
        ArrayList<ClassBean> system = new ArrayList<>();
        ArrayList<PackageBean> packages = FolderToJavaProjectConverter.convert(pPath);

        // Create vector of all the classes in the system
        for (PackageBean packageBean : packages) {
            for (ClassBean classBean : packageBean.getClasses()) {
                system.add(classBean);
            }
        }

        return system;
    }
    
    public static ArrayList<ClassBean> extractActivityClasses(String pPath) throws CoreException, IOException {
        ArrayList<ClassBean> system = new ArrayList<>();
        ArrayList<PackageBean> packages = FolderToJavaProjectConverter.convert(pPath);

        // Create vector of all the classes in the system
        for (PackageBean packageBean : packages) {
            for (ClassBean classBean : packageBean.getClasses()) {
            	if(classBean.getName().toLowerCase().contains("activity"))
            		system.add(classBean);
            }
        }

        return system;
    }

    public static ArrayList<PackageBean> convert(String pPath) throws CoreException, IOException {
        File projectDirectory = new File(pPath);
        CodeParser codeParser = new CodeParser();
        ArrayList<PackageBean> packages = new ArrayList<>();

        if (projectDirectory.isDirectory()) {
            for (File subDir : projectDirectory.listFiles()) {

                if (subDir.isDirectory()) {
                    ArrayList<File> javaFiles = FolderToJavaProjectConverter.listJavaFiles(subDir);

                    if (javaFiles.size() > 0) {
                        for (File javaFile : javaFiles) {
                            System.out.println(javaFile.getAbsolutePath());
                            CompilationUnit parsed = codeParser.createParser(FileUtilities.readFile(javaFile.getAbsolutePath()));
                            
                            if (parsed.types().size() == 0) 
                            	continue;
                            
                            TypeDeclaration typeDeclaration = (TypeDeclaration) parsed.types().get(0);

                            ArrayList<String> imports = new ArrayList<>();

                            for (Object importedResource : parsed.imports()) {
                                imports.add(importedResource.toString());
                            }

                            if (!FolderToJavaProjectConverter.isAlreadyCreated(
                                    parsed.getPackage().getName().getFullyQualifiedName(), packages)) {

                                PackageBean packageBean = new PackageBean();
                                packageBean.setName(parsed.getPackage().getName().getFullyQualifiedName());

                                ClassBean classBean = ClassParser.parse(typeDeclaration, packageBean.getName(),
                                        imports);
                                classBean.setPathToClass(javaFile.getAbsolutePath());

                                InnerClassVisitor innerClassVisitor = new InnerClassVisitor();
                                typeDeclaration.accept(innerClassVisitor);

                                Collection<TypeDeclaration> innerTypes = innerClassVisitor.getInnerClasses();
                                Collection<ClassBean> innerClasses = new ArrayList<>();
                                for (TypeDeclaration innerType : innerTypes) {
                                    //TypeDeclaration innerClassDeclaration = (TypeDeclaration) parsed.types().get(i);
                                    ClassBean innerClass = ClassParser.parse(innerType,
                                            packageBean.getName(), imports);

                                    for (int i = 0; i < innerType.modifiers().size(); i++) {
                                        if (("" + innerType.modifiers().get(i)).equals("static")) {
                                            innerClass.setStatic(true);
                                        }
                                    }

                                    innerClasses.add(innerClass);
                                }

                                classBean.setInnerClasses(innerClasses);
                                packageBean.addClass(classBean);
                                packages.add(packageBean);

                            } else {
                                PackageBean packageBean = FolderToJavaProjectConverter.getPackageByName(
                                        parsed.getPackage().getName().getFullyQualifiedName(), packages);

                                ClassBean classBean = ClassParser.parse(typeDeclaration, packageBean.getName(),
                                        imports);
                                classBean.setPathToClass(javaFile.getAbsolutePath());

                                InnerClassVisitor innerClassVisitor = new InnerClassVisitor();
                                typeDeclaration.accept(innerClassVisitor);

                                Collection<TypeDeclaration> innerTypes = innerClassVisitor.getInnerClasses();
                                Collection<ClassBean> innerClasses = new ArrayList<>();
                                for (TypeDeclaration innerType : innerTypes) {
                                    //TypeDeclaration innerClassDeclaration = (TypeDeclaration) parsed.types().get(i);
                                    ClassBean innerClass = ClassParser.parse(innerType,
                                            packageBean.getName(), imports);

                                    for (int i = 0; i < innerType.modifiers().size(); i++) {
                                        if (("" + innerType.modifiers().get(i)).equals("static")) {
                                            innerClass.setStatic(true);
                                        }
                                    }

                                    innerClasses.add(innerClass);
                                }

                                classBean.setInnerClasses(innerClasses);
                                packageBean.addClass(classBean);
                            }

                        }
                    }

                }

            }

        }

        for (PackageBean pb : packages) {
            String textualContent = "";
            for (ClassBean cb : pb.getClasses()) {
                textualContent += cb.getTextContent();
            }
            pb.setTextContent(textualContent);
        }

        return packages;
    }

    private static ArrayList<File> listJavaFiles(File pDirectory) {
        ArrayList<File> javaFiles = new ArrayList<>();
        File[] fList = pDirectory.listFiles();

        if (fList != null) {
            for (File file : fList) {
                if (file.isFile()) {
                    if (file.getName().contains(".java")) {
                        javaFiles.add(file);
                    }
                } else if (file.isDirectory()) {
                    File directory = new File(file.getAbsolutePath());
                    javaFiles.addAll(listJavaFiles(directory));
                }
            }
        }
        return javaFiles;
    }

    private static boolean isAlreadyCreated(String pPackageName, ArrayList<PackageBean> pPackages) {
        for (PackageBean pb : pPackages) {
            if (pb.getName().equals(pPackageName)) {
                return true;
            }
        }

        return false;
    }

    private static PackageBean getPackageByName(String pPackageName, ArrayList<PackageBean> pPackages) {
        for (PackageBean pb : pPackages) {
            if (pb.getName().equals(pPackageName)) {
                return pb;
            }
        }
        return null;
    }
}
