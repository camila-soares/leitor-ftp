package com.microservicemigration.leitorftp.service;

import com.microservicemigration.leitorftp.domain.Files;
import com.microservicemigration.leitorftp.service.file.CreateFilesService;
import com.microservicemigration.leitorftp.service.file.SendFileKafkaService;
import com.microservicemigration.leitorftp.service.ftp.FileTransfer;
import com.microservicemigration.leitorftp.service.ftp.TransferFTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;

@Service
public class TransferFileService {

    @Autowired
    private TransferFTPService transferFTPService;

    @Autowired
    private CreateFilesService filesService;

    @Autowired
    private SendFileKafkaService sendFileKafkaService;

    @Scheduled(fixedRate = 100*600)
    public void execute() throws FileNotFoundException {
       List< FileTransfer > files = transferFTPService.execute();

       for( FileTransfer fileTransfer : files ) {
           var uuid = filesService.execute( Files.builder()
                                    .path( fileTransfer.getPath() )
                                    .build() );

           fileTransfer.setUuid( uuid );
           sendFileKafkaService.execute( fileTransfer );
       }

    }
}
