package co.develhope.fileUploader.services;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileService {

    @Value("${fileRepositoryFolder}")
    private String fileFolder;

    public String upload(MultipartFile file) throws IOException {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String newFileName = UUID.randomUUID().toString();
        String completeFileName = newFileName + "." + extension;
        File folder = new File(fileFolder);
        if(!folder.exists()) {
            throw new IOException("The folder does not exists :(");
        }
        if(!folder.isDirectory()) {
            throw new IOException("The folder is not a directory :(");
        }
        File finalDestination = new File(fileFolder + "\\" + completeFileName);
        if(finalDestination.exists()){
            throw new IOException("File conflict");
        }
        file.transferTo(finalDestination);
        return completeFileName;
    }


    public byte[] download(String fileName) throws IOException {
        File fileFromRepository = new File(fileFolder + "\\" + fileName);
        if(!fileFromRepository.exists()) {
            throw new IOException("File does not exists");
        }
        return IOUtils.toByteArray(new FileInputStream(fileFromRepository));
    }

}