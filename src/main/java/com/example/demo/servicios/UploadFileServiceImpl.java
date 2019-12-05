package com.example.demo.servicios;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

@Service
public class UploadFileServiceImpl implements IUploadFileService {

	private final static String UPLOAD_FOLDER = "uploads";
	
	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(Paths.get(UPLOAD_FOLDER).toFile());
	}

	@Override
	public void init() throws IOException {
		Files.createDirectory(Paths.get(UPLOAD_FOLDER));
	}

}
