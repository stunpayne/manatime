package com.slate.common;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import javax.inject.Inject;

public class FileUtil {

  private static final String TAG = FileUtil.class.getSimpleName();
  private static final Gson gson = new Gson();

  /**
   * Reads the contents of an entire file and returns the contents as a string
   *
   * @param context  the context from which to read the file
   * @param fileName the name of the file to read from
   * @return the file content string
   */
  public static <T> T readFile(Context context, String fileName, TypeToken<T> typeToken) {
    try {
      FileInputStream fis = context.openFileInput(fileName);
      InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
      BufferedReader bufferedReader = new BufferedReader(isr);
      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        sb.append(line).append("\n");
      }
      return gson.fromJson(sb.toString(), typeToken.getType());
    } catch (IOException e) {
      Log.e(TAG, "Error occurred while reading file " + fileName, e);
      return null;
    }
  }

  /**
   * Writes the given contents to the given file in Internal Storagew
   *
   * @param context  the context from which to read the file
   * @param filename the name of the file to write to
   * @param object   the object to write to the file
   */
  public static boolean writeObjectToFile(Context context, String filename, Object object) {
    try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
      fos.write(gson.toJson(object).getBytes());
      return true;
    } catch (IOException e) {
      Log.d(TAG, "onCreate: ", e);
      return false;
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
      fos.write(fileContents.getBytes());
    } catch (IOException e) {
      Log.d(TAG, "onCreate: ", e);
    }
  }
}
