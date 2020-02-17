package organisation.application.manager.pilote.commun.helper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ZipManagement {

	protected static final Logger LOG = Logger.getLogger(ZipManagement.class);

	public void unzip(String strZipFile) throws IOException {
		try {
			/*
			 * STEP 1 : Create directory with the name of the zip file
			 * 
			 * For e.g. if we are going to extract c:/demo.zip create c:/demo directory
			 * where we can extract all the zip entries
			 * 
			 */
			File fSourceZip = new File(strZipFile);
			String zipPath = strZipFile.substring(0, strZipFile.length() - 4);
			File temp = new File(zipPath);
			temp.mkdir();
			System.out.println(zipPath + " created");

			/*
			 * STEP 2 : Extract entries while creating required sub-directories
			 * 
			 */
			ZipFile zipFile = new ZipFile(fSourceZip);
			Enumeration e = zipFile.entries();

			while (e.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) e.nextElement();
				File destinationFilePath = new File(zipPath, entry.getName());

				// create directories if required.
				destinationFilePath.getParentFile().mkdirs();

				// if the entry is directory, leave it. Otherwise extract it.
				if (entry.isDirectory()) {
					continue;
				} else {
					System.out.println("Extracting " + destinationFilePath);

					/*
					 * Get the InputStream for current entry of the zip file using
					 * 
					 * InputStream getInputStream(Entry entry) method.
					 */
					BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));

					int b;
					byte buffer[] = new byte[1024];

					/*
					 * read the current entry from the zip file, extract it and write the extracted
					 * file.
					 */
					FileOutputStream fos = new FileOutputStream(destinationFilePath);
					BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);

					while ((b = bis.read(buffer, 0, 1024)) != -1) {
						bos.write(buffer, 0, b);
					}

					// flush the output stream and close it.
					bos.flush();
					bos.close();

					// close the input stream.
					bis.close();
				}
			}
			zipFile.close();
		} catch (IOException ioe) {
			System.out.println("IOError :" + ioe);
		}

	}

}
