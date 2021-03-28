package com.microservicemigration.leitorftp.service.ftp;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.net.ProtocolCommandEvent;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class TransferFTPService {

    @Value( "${ftp.server}" )
    private  String server;
    @Value( "${ftp.port}" )
    private  String port;
    @Value( "${ftp.user}" )
    private  String user;
    @Value( "${ftp.pass}" )
    private String pass;

    @Autowired
    private UploadS3Service s3Service;

    public List<FileTransfer> execute() {
        List<FileTransfer> returnList = new ArrayList<>();
        FTPClient ftpClient = new FTPClient();
        try {
            //configuracao do ftpClient
            ftpClient.addProtocolCommandListener( new ProtocolCommandListener() {@Override public void protocolCommandSent( ProtocolCommandEvent protocolCommandEvent ) { }@Override public void protocolReplyReceived( ProtocolCommandEvent protocolCommandEvent ) { }} );
            ftpClient.connect( server, Integer.parseInt( port ) );
            ftpClient.login( user, pass );
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType( FTP.BINARY_FILE_TYPE );

            //llista os arquivo
            String[] arqs = ftpClient.listNames();
            for( String itemFile: arqs ) {

                String remoteFile1 = itemFile;
                File file = new File( itemFile );
                //baixa o arquivo
                OutputStream outputStream1 = new BufferedOutputStream( new FileOutputStream( file ) );
                boolean success = ftpClient.retrieveFile( remoteFile1, outputStream1 );
                outputStream1.close();
                if(success) {
                    log.info( "Arquivo recebido com sucesso,enviando para o S3... ");
                    FileTransfer fileTransfer = s3Service.execute( itemFile, file );
                    fileTransfer.setPathLocal( file );
                    returnList.add( fileTransfer );
                    log.info( "Arquivo recebido no S3. ");
                    ftpClient.deleteFile( itemFile );
                    log.info( "Arquivo deletado do servidor." );
                }
            }
        }catch( Exception e ) {
                log.error( "Error" + e.getMessage() );
        }finally {
            try {
                if( ftpClient.isConnected() ) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            }catch( IOException ex ) {
                log.error( "Error", ex );
            }
        }
        return returnList;
    }
}
