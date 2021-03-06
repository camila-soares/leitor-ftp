package com.microservicemigration.leitorftp.service.file;

import com.microservicemigration.leitorftp.gateway.gson.FileUUIDJson;
import com.microservicemigration.leitorftp.kafka.KafkaRequestSender;
import com.microservicemigration.leitorftp.service.ftp.FileTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.Scanner;

@Service
public class SendFileKafkaService {

    private static final String SISTEMA_1 = "[SISTEMA1]";
    private static final String SISTEMA_2 = "[SISTEMA2]";

    @Value( "${kafka.topictopics1}" )
    private String topictopics1;

    @Value( "${kafka.topictopics2}" )
    private String topictopics2;

    @Autowired
    private KafkaRequestSender kafkaRequestSender;

    public void execute( FileTransfer fileTransfer ) throws FileNotFoundException {

        Scanner in = new Scanner( fileTransfer.getPathLocal() );
        String firstLine = null;
        while( in.hasNextLine() ) {
            firstLine = in.nextLine();
            break;
        }
        in.close();

        String topic = null;
        if(SISTEMA_1.equals( firstLine ) ) {
            topic = topictopics1;
        } else if(SISTEMA_2.equals( firstLine ) ) {
            topic = topictopics2;
        }

        kafkaRequestSender.sendMessage( topic,
                            FileUUIDJson
                            .builder()
                            .uuid( fileTransfer.getUuid() )
                            .build());
    }
}
