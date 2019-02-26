package rit.se.SWEN789.outputgenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import com.opencsv.CSVWriter;

import rit.se.SWEN789.vo.DefectVO;
import rit.se.SWEN789.vo.ProjectVO;

public class CSVOutputGenerator {
	
	private CSVWriter writer;

	public CSVOutputGenerator(Path outputPath) {
		super();
		try {
			this.writer = new CSVWriter(new FileWriter(outputPath.toFile()),',');
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void write(List<ProjectVO> projects) {
		try {
			String header[] = {"Project", "Issue Type", "File Name", "Element","Line No","Activity","Comment" };
			writer.writeNext(header);
			if (projects != null && !projects.isEmpty()) {
				for (ProjectVO project : projects) {
					List<DefectVO> defects = project.getDefects();
					for (DefectVO defect : defects) {
						String entry[] = { project.getName(),defect.getType(), project.getPath().getParent().relativize(defect.getFileVO().getFilePath()).toString(),
								defect.getElementName(), defect.getLineNumber(), null,defect.getComment()};
						writer.writeNext(entry);
					}
					writer.writeNext(new String[]{"","","","","","",""});
				}
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
