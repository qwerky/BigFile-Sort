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

import java.io.File;
import java.util.Comparator;

public class ChunkFileComparator implements Comparator<File> {

  @Override
  public int compare(File file1, File file2) {   
    return BigFileSort.getChunkIndex(file1) - BigFileSort.getChunkIndex(file2);
  }

}
