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
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

public class BufferedMergeFile {

  private static final int stackSize = 20000;
  
  private BufferedReader reader;
  private Deque<Integer> stack;
  private boolean endOfFile;
  
  public BufferedMergeFile(File chunkFile) throws IOException {
    reader = new BufferedReader(new FileReader(chunkFile));
    stack = new ArrayDeque<Integer>();
    readIntoStack();
  }

  private void readIntoStack() throws IOException {
    String line = null;
    
    int count = 0;
    while (count < stackSize && (line = reader.readLine()) != null) {
      stack.add(Integer.valueOf(line));
      count++;
    }
    endOfFile = count < stackSize;
  }

  public int peek() throws IOException {
    if (isFinished()) {
      throw new IllegalStateException("Peek called after end of file.");
    }
    
    return stack.peek();
  }
  
  public void pop() throws IOException {
    stack.pop();
    
    if (stack.isEmpty()) {
      readIntoStack();
    }
  }
  
  public boolean isFinished() {
    return endOfFile && stack.isEmpty();
  }

  public void close() throws IOException {
    reader.close();
  }
}
