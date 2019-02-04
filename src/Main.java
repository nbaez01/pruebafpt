import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.Scanner;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class Main {
	//Scanner variable = new Scanner(System.in);
	public static void main(String[] args) {
		String server = "ftp.elnazarenovraem.edu.pe";
        int port = 21;
        String user = "elnazare";
        String pass = "ii2Z8)9S!Fc4Rq";
		FTPClient client = new FTPClient();
		try {
			client.connect(InetAddress.getByName(server), port);
			boolean login = client.login(user, pass);
			if (login) {
				client.enterLocalPassiveMode();
				client.setFileType(FTP.BINARY_FILE_TYPE);
				
				downloadFile2(client);
			} else {
				System.out.println("Falló inciar sesión");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
                if (client.isConnected()) {
                	client.logout();
                	client.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
		}
		
	}
	
	public static void subirArchivo(FTPClient client) throws IOException {
		client.changeWorkingDirectory("/public_html");
		int replay = client.getReplyCode();
		System.out.println("Replay "+replay);
		if (FTPReply.isPositiveCompletion(replay)) {
			File file = new File("D:/ftp/poderosa.jpg");
			FileInputStream input = new FileInputStream(file);
			client.enterLocalActiveMode();
			System.out.println("Subió satisfactoriamente el archivo");
			if (!client.storeFile(file.getName(), input)) {
				System.out.println("Subida fallida!");
			}
			input.close();
		}
		boolean logout = client.logout();
		if (logout) {
			System.out.println("Salir del servidor FTP");
		}
	}
	
	public static void downloadFile(FTPClient client) throws IOException{
		String remoteFile1 = "/public_html/pdgimagen.pdf";
        File downloadFile1 = new File("D:/ftp/download/pdfimagen.pdf");
        OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
        boolean success = client.retrieveFile(remoteFile1, outputStream1);
        outputStream1.close();
        if (success) {
            System.out.println("File #1 has been downloaded successfully.");
        }else {
        	System.out.println("Error al descargar archivo");
        }
	}
	
	public static void downloadFile2(FTPClient client) throws IOException{
		String remoteFile2 = "/public_html/pdgimagen2.pdf";
        File downloadFile2 = new File("D:/ftp/download/pdfimagen2.pdf");
        OutputStream outputStream2 = new BufferedOutputStream(new FileOutputStream(downloadFile2));
        InputStream inputStream = client.retrieveFileStream(remoteFile2);
        byte[] bytesArray = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(bytesArray)) != -1) {
            outputStream2.write(bytesArray, 0, bytesRead);
        }

        boolean success = client.completePendingCommand();
        if (success) {
            System.out.println("File #2 has been downloaded successfully.");
        }else {
        	System.out.println("Error al descargar archivo");
        }
        outputStream2.close();
        inputStream.close();
	}

}
