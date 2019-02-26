package rit.se.SWEN789.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import rit.se.SWEN789.vo.XMLFileVO;

public class LayoutParser {
	
	private DocumentBuilder builder;
	
	public LayoutParser() {
		super();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		try {
			this.builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

/*	public void parse(ProjectVO projectVO){
		System.out.println("Parsing layout of project "+projectVO.getName());
		List<Path> layoutFolders; //= projectVO.getLayoutFolders();
		for (Path folder : layoutFolders) {
			try(DirectoryStream<Path> entries = Files.newDirectoryStream(folder,"*.xml")){
				for (Path file : entries) {
					XMLFileVO xmlFile = new XMLFileVO();
					xmlFile.setName(file.getFileName().toString());
					System.out.println("XML file name: "+xmlFile.getName());
					xmlFile.setLayoutFolder(file);
					projectVO.getXmlFiles().add(xmlFile);
//					Document doc = builder.parse(file.toFile());
					Document doc = PositionalXMLReader.readXML(Files.newInputStream(file));
					Element root = doc.getDocumentElement();
					validate(root, xmlFile);
					projectVO.getDefects().addAll(xmlFile.getDefects());
				}
			} catch (IOException | SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}*/
	
	public XMLFileVO parse(Path file){
		System.out.println(file.getFileName().toString());
		XMLFileVO xmlFile = new XMLFileVO();
		xmlFile.setName(file.getFileName().toString());
		System.out.println("XML file name: "+xmlFile.getName());
		xmlFile.setFilePath(file);
		Document doc=null;
		try {
			doc = PositionalXMLReader.readXML(Files.newInputStream(file));
		} catch (IOException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(doc!=null){
			xmlFile.setDocument(doc);
			return xmlFile;
		}else{
			return null;
		}
		
		
	}
	
	public static Map<String, String> getAttributesOfElement(Element element){
		Map<String,String> attrMap = new HashMap<>();
		NamedNodeMap attributes = element.getAttributes();
		for(int i=0;i<attributes.getLength();i++){
			Node attr = attributes.item(i);
			attrMap.put(attr.getNodeName(), attr.getNodeValue());
		}
		return attrMap;
	}
}
