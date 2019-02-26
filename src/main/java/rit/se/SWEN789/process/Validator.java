package rit.se.SWEN789.process;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import rit.se.SWEN789.rules.ContentDescriptionRule;
import rit.se.SWEN789.rules.TextSizeRule;
import rit.se.SWEN789.rules.TouchTargetSizeRule;
import rit.se.SWEN789.rules.TouchTargetSpacingRule;
import rit.se.SWEN789.rules.ContrastRule;
import rit.se.SWEN789.vo.ProjectVO;
import rit.se.SWEN789.vo.XMLFileVO;

public class Validator{
	
	public static void validate(ProjectVO project){
		System.out.println("Validating......"+project.getName());
		List<XMLFileVO> xmlFileVOs = project.getXmlFiles();
		for (XMLFileVO xmlFileVO : xmlFileVOs) {
			System.out.println("Validating......"+xmlFileVO.getName());
			Element root = xmlFileVO.getDocument().getDocumentElement();
			applyRules(root,xmlFileVO,project);
			project.getDefects().addAll(xmlFileVO.getDefects());
		}
	}

	//this method takes a pointer to current element which is being validated, and it gets recursively called if the element has child nodes
	private static void applyRules(Element currentElement, XMLFileVO xmlFile, ProjectVO project) {
		//Rules
		ContentDescriptionRule.applyRule(currentElement, xmlFile, project);
		TouchTargetSizeRule.applyRule(currentElement, xmlFile, project);
		TextSizeRule.applyRule(currentElement, xmlFile, project);
		ContrastRule.applyRule(currentElement, xmlFile, project);
		//TouchTargetSpacingRule.applyRule(currentElement, xmlFile, project);
		
		//checking for child nodes
		NodeList children = currentElement.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child instanceof Element) {
				applyRules((Element) child, xmlFile,project);
			}
		}
	}
}
