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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class Creator {

  private static Random random = new Random();
  
  public static void main(String[] args) throws IOException {
    String bigfile = "C:/tmp/bigfile/bigfile.txt";
    PrintWriter writer = new PrintWriter(bigfile);
    
    //        2147483647
    int size = 100000000;
    for (int i=0; i<size; i++) {
      writer.println(random.nextInt());
    }
    writer.close();
  }

}
