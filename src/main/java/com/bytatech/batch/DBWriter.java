
package com.bytatech.batch;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bytatech.domain.Drivo;
import com.bytatech.repository.DrivoRepository;
import com.bytatech.service.dto.DrivoDTO;
import com.bytatech.service.dto.DrivoData;
import com.bytatech.service.mapper.DrivoMapper;
import com.bytatech.web.rest.DrivoResource;

@Component
public class DBWriter implements ItemWriter<DrivoData> {

	private final Logger log = LoggerFactory.getLogger(DBWriter.class);

	@Autowired
	private DrivoRepository drivoRepo;

	@Autowired
	private DrivoResource drivoResource;

	@Autowired
	private DrivoMapper drivoMapper;

	@Override
	public void write(List<? extends DrivoData> itemDrive) throws Exception {

		for (DrivoData drivodata : itemDrive) {

			System.out.println("...................... data......... "+drivodata);
			Drivo data = new Drivo();

			data.setMobileNo(drivodata.getMobileNo());

			data.setOwnerName(drivodata.getOwnerName());

			data.setRegNo(drivodata.getRegNo());

			data.setVehdecscr(drivodata.getVehdecscr());

			DrivoDTO drivoDTO = drivoMapper.toDto(data);

			drivoDTO = drivoResource.createDrivo(drivoDTO).getBody();

		}

	}
}
