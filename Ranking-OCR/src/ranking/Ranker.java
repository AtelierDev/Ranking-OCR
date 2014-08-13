/*
 * Copyright (C) 2014 Nils Ryter
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ranking;

/**
 * Common interface of the rankers.
 * <p>
 * @author Nils Ryter
 */
public interface Ranker {

    /**
     * Compare the documents.
     * <p>
     * @param original Original document
     * @param comparative Document to compare to original
     * @return Error rate in percent
     */
    public double compare(String original, String comparative);
}
