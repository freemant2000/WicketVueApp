package com.foo;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import java.io.File;

public class ZipTest {
  public static void main(String[] args) {
    try {
      ZipFile zipFile = new ZipFile("myarchive.zip");
      ZipParameters parameters = new ZipParameters();
      parameters.setFileNameInZip("resume_é.txt"); // Latin character with diacritic mark
      zipFile.addFile(new File("/home/kent/resume_é.txt"), parameters);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}