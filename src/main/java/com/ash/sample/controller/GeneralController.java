package com.ash.sample.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ash.sample.service.DateService;

/**
 * @author Ash Izadi
 *
 */
@RestController
@RequestMapping("/")
public class GeneralController {

	private static final Logger log = LoggerFactory.getLogger(GeneralController.class);

	@Autowired
	DateService dateService;
	//sample request:
	//curl localhost:8080/file/path?path=K:%5CAshkan%5CProjects%5Cbitbucket%5CSuncorp-SampleDate%5Csrc%5Cmain%5Cresources%5Csample.txt

	@RequestMapping(value = "file/path", method = RequestMethod.GET)
	public String loadFile(@RequestParam(value = "path", required = false) String path) {
		log.debug("Controller with this path: file/path is looking for correspondence service");
		Optional<String> text = dateService.proccessFile(path);

		return text.get();
	}
	
	//sample request:
	//curl localhost:8080/input?input=20+11+1980,+28+10+1990
	@RequestMapping(value ="input", method = RequestMethod.GET)
	public String standardInput(@RequestParam (value = "input", required = false) String input) {
		
		log.debug("Controller with this path: /input is looking for correspondence service");
		Optional<String> text = dateService.processInput(input);

		return text.get();
	}

}
