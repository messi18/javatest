package zip;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by malance on 14-8-31.
 */
public class ZipStreamTest {
    private String inputBase = "/Users/malance/app/test/", outputBase = "/Users/malance/app/test/output/";
    private String file1 = "a.txt", file2 = "b.txt", outputFile = "test.zip";
    private String content1 = "this is file1 a.txt", content2 = "this is file2 b.txt";

    @Before
    public void init() throws IOException {
        Files.write(Paths.get(inputBase + file1), content1.getBytes());
        Files.write(Paths.get(inputBase + file2), content2.getBytes());
    }

    @After
    public void clear() throws IOException {
        Files.deleteIfExists(Paths.get(outputBase+file1));
        Files.deleteIfExists(Paths.get(outputBase+file2));
        Files.deleteIfExists(Paths.get(outputBase+outputFile));
    }

    @Test
    public void zipMultiFiles() throws Exception{
        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outputBase+outputFile))) {
            zipit(out,inputBase,file1);
            zipit(out,inputBase,file2);
        }

        assertTrue(Files.exists(Paths.get(outputBase+outputFile)));

        try (ZipInputStream in = new ZipInputStream(new FileInputStream(outputBase+outputFile))){
            ZipEntry entry = null;
            while((entry = in.getNextEntry()) != null) {
                System.out.println("got entry: "+ entry.getName());
                Files.copy(in,Paths.get(outputBase+entry.getName()));
            }
        }

        assertTrue(Files.exists(Paths.get(outputBase+file1)));
        assertEquals(content1, new String(Files.readAllBytes(Paths.get(outputBase + file1))));
        assertTrue(Files.exists(Paths.get(outputBase + file2)));
        assertEquals(content2, new String(Files.readAllBytes(Paths.get(outputBase + file2))));
    }

    static void zipit(ZipOutputStream out, String baseFolder, String fileName) throws IOException {
        ZipEntry zipEntry = new ZipEntry(fileName);
        out.putNextEntry(zipEntry);
        Files.copy(Paths.get(baseFolder+fileName),out);
        out.closeEntry();
    }
}
