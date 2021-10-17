package com.collabsphere.springbatchdemo.processor;

import com.xpagesbeast.lego.model.csv.LegoPart;
import org.springframework.batch.item.ItemProcessor;

public class LegoPartProcessor implements ItemProcessor<LegoPart, LegoPart>{

	@Override
	public LegoPart process(LegoPart item) throws Exception {
		System.out.println("Processing " + item.getName());
		return item;
	}
	

}
