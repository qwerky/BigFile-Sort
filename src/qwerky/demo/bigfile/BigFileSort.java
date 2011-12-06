/**
 *  Copyright (C) 2011  Neil Taylor (https://github.com/qwerky/)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
 package qwerky.demo.bigfile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class BigFileSort {

  private static int chunkSize = 100000000;
  private static File chunksDir = new File("C:/tmp/bigfile/");
  
  public static void main(String[] args) throws IOException {
    String bigfile = "C:/tmp/bigfile/bigfile.txt";
    
    //Break into chunks
    long start = System.currentTimeMillis();
    BufferedReader reader = new BufferedReader(new FileReader(bigfile));
    int count = 0;
    int chunk = 1;
    String line = null;
    int[] ints = new int[chunkSize];
    while ((line = reader.readLine()) != null) {
      int value = Integer.parseInt(line);
      ints[count] = value;
      count++;
      
      if (count == chunkSize) {
        dumpToFile(ints, chunk);
        count = 0;
        chunk++;
      }
    }
    chunk--;
    reader.close();
    long time = System.currentTimeMillis() - start;
    System.out.println("Wrote " + chunk + " files with size " + chunkSize + " in " + time + "ms");

    //Merge the chunks
    start = System.currentTimeMillis();
    File[] chunkFiles = getChunkFiles();
    while (chunkFiles.length > 1) {
      File chunkFile1 = chunkFiles[0];
      File chunkFile2 = chunkFiles[1];
      int newChunkNumber = 1 + getChunkIndex(chunkFiles[chunkFiles.length-1]);
      File newChunkFile = new File(chunksDir, "chunk" + newChunkNumber + ".txt");
      merge(chunkFile1, chunkFile2, newChunkFile);
      chunkFile1.delete();
      chunkFile2.delete();
      chunkFiles = getChunkFiles();
    }
    File finished = new File(chunksDir, "chunk" + getChunkIndex(chunkFiles[chunkFiles.length-1]) + ".txt");
    finished.renameTo(new File(chunksDir, "sorted.txt"));
    time = System.currentTimeMillis() - start;
    System.out.println("Merged chunks in " + time + "ms");
  }

  private static File[] getChunkFiles() {
    File[] chunkFiles = chunksDir.listFiles(new ChunkFileFilter());
    Arrays.sort(chunkFiles, new ChunkFileComparator());
    return chunkFiles;
  }

  private static void dumpToFile(int[] ints, int chunk) throws IOException {
    String chunkFile = "C:/tmp/bigfile/chunk" + chunk + ".txt";
    long start = System.currentTimeMillis();
    Arrays.sort(ints);
    long time = System.currentTimeMillis() - start;
    System.out.println("Arrays.sort() took " + time + "ms to sort " + ints.length);
    PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(chunkFile)));
    boolean firstLine = true;
    for (int i : ints) {
      if (!firstLine) writer.println();
      writer.print(i);
      firstLine = false;
    }
    writer.close();
  }
  
  private static void merge(File chunkFile1, File chunkFile2, File target) throws IOException {
    PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(target)));
    BufferedMergeFile buffer1 = new BufferedMergeFile(chunkFile1);
    BufferedMergeFile buffer2 = new BufferedMergeFile(chunkFile2);

    boolean mergeComplete = false;
    boolean firstLine = true;
    while (!mergeComplete) {
      if (!buffer1.isFinished()) {
        int i1 = buffer1.peek();
        if (!buffer2.isFinished()) {
          int i2 = buffer2.peek();
          if (i1 < i2) {
            buffer1.pop();
            if (!firstLine) writer.println();
            writer.print(i1);
          } else {
            buffer2.pop();
            if (!firstLine) writer.println();
            writer.print(i2);
          }
        } else {
          buffer1.pop();
          if (!firstLine) writer.println();
          writer.print(i1);
        }
      } else {
        if (!buffer2.isFinished()) {
          int i2 = buffer2.peek();
          buffer2.pop();
          if (!firstLine) writer.println();
          writer.print(i2);
        } else {
          mergeComplete = true;
        }
      }
      firstLine = false;
    }
    buffer1.close();
    buffer2.close();
    writer.close();
  }

  public static int getChunkIndex(File file) {
    int dotpos = file.getName().indexOf(".");
    return Integer.parseInt(file.getName().substring(5, dotpos));
  }

}
