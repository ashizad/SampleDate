package com.ash.sample.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Ash Izadi
 * <p>This service is to read file.</p>
 */
@Service
public class FileReaderServiceImpl implements FileReaderService{

	private static final Logger log = LoggerFactory.getLogger(FileReaderServiceImpl.class);

	
	@Override
	public Optional<String> readTextFile(String filePath) {
		Optional<String> firstLine = Optional.empty();
		try (Stream<String> lines = Files.lines(Paths.get(filePath) )) {

			// we filter on line1 and stop searching on other lines
			  firstLine = lines
					 .map( line -> {
						 
						 log.debug(" Reading line:" +line);
						 return line.trim(); 
					 })
					 .findFirst();
					
			 

		} catch(NoSuchFileException ex) {
			log.error("There is no file in this path"+ ex);
			throw new RuntimeException("file is not valid");
		}catch (IOException e) {
			log.error("There is an exception once Reading the file:"+ e);
			e.printStackTrace();
			throw new RuntimeException("file is not valid");
		}
		
		return firstLine;
	}

}
































