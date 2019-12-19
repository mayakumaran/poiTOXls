
package com.bytatech.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.bytatech.service.dto.DrivoData;


@Component
public class Processor implements ItemProcessor<DrivoData, DrivoData> {

	@Override
	public DrivoData process(DrivoData drivo) throws Exception {
		System.out.println("..................");
		return drivo;
	}

}
