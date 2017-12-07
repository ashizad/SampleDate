package com.ash.sample.service;

import java.io.IOException;
import java.util.Optional;

/**
 * @author AshI01
 *
 */
public interface FileReaderService {

	
	/**
	 * <p> To be able reading file from user,<br/>
	 * The string should be on line one of this file, otherwise it won't process
	 * </p>
	 * @param filePath: user provide the path of file
	 * @return : DD MM YYYY, DD MM YYYY
	 * @throws IOException
	 */
	Optional<String> readTextFile(String filePath)  throws IOException;
	
	
	
}
