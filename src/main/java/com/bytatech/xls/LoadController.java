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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bytatech.service.dto.DrivoData;

/**
 * @author maya mayabytatech, maya.k.k@lxisoft.com
 */

@RestController
@RequestMapping("/api")
public class LoadController {

	@PostMapping("/import")
	public void mapReapExcelDatatoDB(@RequestParam("file") MultipartFile reapExcelDataFile) throws IOException {

		List<DrivoData> tempDriveList = new ArrayList<DrivoData>();
		XSSFWorkbook workbook = new XSSFWorkbook(reapExcelDataFile.getInputStream());
		XSSFSheet worksheet = workbook.getSheetAt(0);

		for (int i = 0; i < worksheet.getPhysicalNumberOfRows(); i++) {
			
			DrivoData tempDrive = new DrivoData();
			XSSFRow row = worksheet.getRow(i);
			tempDrive.setRegNo(row.getCell(0).getStringCellValue());
			tempDrive.setOwnerName(row.getCell(1).getStringCellValue());
			tempDrive.setMobileNo(""+(long)row.getCell(2).getNumericCellValue());
			tempDrive.setVehdecscr(row.getCell(3).getStringCellValue());

			System.out.println(tempDrive);

			tempDriveList.add(tempDrive);

		}
	}
}
