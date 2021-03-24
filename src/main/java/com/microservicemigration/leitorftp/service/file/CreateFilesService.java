package com.microservicemigration.leitorftp.service.file;

import com.microservicemigration.leitorftp.domain.Files;
import com.microservicemigration.leitorftp.domain.enums.FilesStatus;
import com.microservicemigration.leitorftp.gateway.repositories.FilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CreateFilesService {

    @Autowired
    private FilesRepository filesRepository;

    @Transactional
    public String execute( Files files) {

        files.setStatus( FilesStatus.RECBIDO.name() );
        filesRepository.save( files );
        return files.getId().toString();

    }
}
