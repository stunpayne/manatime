package com.slate.common;

import android.content.Context;
import android.util.Log;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FileUtil {

  private static final String TAG = FileUtil.class.getSimpleName();

  /**
   * Reads the contents of an entire file and returns the contents as a string
   *
   * @param context  the context from which to read the file
   * @param filename the name of the file to read from
   * @return the file content string
   */
  public static String readFile(Context context, String filename) {
    try {
      FileInputStream fis = context.openFileInput(filename);
      InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
      BufferedReader bufferedReader = new BufferedReader(isr);
      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        sb.append(line).append("\n");
      }
      return sb.toString();
    } catch (IOException e) {
      return "";
    }
  }

  /**
   * Writes the given contents to the given file in Internal Storage
   *
   * @param context      the context from which to read the file
   * @param filename     the name of the file to write to
   * @param fileContents the String to write to the file
   */
  public static void writeToFile(Context context, String filename, String fileContents) {
    try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
      try {
        fos.write(fileContents.getBytes());
      } catch (IOException e) {
        Log.d(TAG, "onCreate: ", e);
      }
    } catch (FileNotFoundException e) {
      Log.d(TAG, "onCreate: ", e);
    } catch (IOException e) {
      Log.d(TAG, "onCreate: ", e);
    }
  }
}
