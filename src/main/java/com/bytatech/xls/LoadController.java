/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bytatech.xls;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bytatech.domain.Drivo;
import com.bytatech.service.dto.DrivoData;
import com.bytatech.service.dto.DrivoFile;
import com.bytatech.service.mapper.DrivoMapper;
import com.bytatech.web.rest.DrivoResource;

/**
 * @author maya mayabytatech, maya.k.k@lxisoft.com
 */

@RestController
@RequestMapping("/api")
public class LoadController {

	@Autowired
	DrivoResource drivoResource;
	
	@Autowired
	DrivoMapper drivoMapper;
	
	@PostMapping("/import")
	public void mapReapExcelDatatoDB(@RequestParam("file") MultipartFile reapExcelDataFile) throws IOException, URISyntaxException {

		List<Drivo> tempDriveList = new ArrayList<Drivo>();
		
		XSSFWorkbook workbook = new XSSFWorkbook(reapExcelDataFile.getInputStream());
		
		XSSFSheet worksheet = workbook.getSheetAt(0);

		for (int i = 0; i < worksheet.getPhysicalNumberOfRows(); i++) {
			
			Drivo tempDrive = new Drivo();
			XSSFRow row = worksheet.getRow(i);
			tempDrive.setRegNo(row.getCell(0).getStringCellValue());
			tempDrive.setOwnerName(row.getCell(1).getStringCellValue());
			tempDrive.setMobileNo(""+(long)row.getCell(2).getNumericCellValue());
			tempDrive.setVehdecscr(row.getCell(3).getStringCellValue());

			System.out.println(tempDrive);

			drivoResource.createDrivo(drivoMapper.toDto(tempDrive));
			
			tempDriveList.add(tempDrive);

		}
	}
	
	
	@PostMapping("/byte-import")
	public void ExcelDatatoDB(@RequestBody DrivoFile file) throws IOException, URISyntaxException {

		List<Drivo> tempDriveList = new ArrayList<Drivo>();
		
		 InputStream targetStream = new ByteArrayInputStream(file.getFile());
		 
		XSSFWorkbook workbook = new XSSFWorkbook(targetStream);
		
		XSSFSheet worksheet = workbook.getSheetAt(0);

		for (int i = 0; i < worksheet.getPhysicalNumberOfRows(); i++) {
			
			Drivo tempDrive = new Drivo();
			XSSFRow row = worksheet.getRow(i);
			tempDrive.setRegNo(row.getCell(0).getStringCellValue());
			tempDrive.setOwnerName(row.getCell(1).getStringCellValue());
			tempDrive.setMobileNo(""+(long)row.getCell(2).getNumericCellValue());
			tempDrive.setVehdecscr(row.getCell(3).getStringCellValue());

			System.out.println(tempDrive);

			//drivoResource.createDrivo(drivoMapper.toDto(tempDrive));
			
			tempDriveList.add(tempDrive);

		}
	}
}
