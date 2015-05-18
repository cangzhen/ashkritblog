/*
 * Copyright (c) 2013, OpenCloudDB/MyCAT and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software;Designed and Developed mainly by many Chinese 
 * opensource volunteers. you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License version 2 only, as published by the
 * Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Any questions about this component can be directed to it's project Web address 
 * https://code.google.com/p/opencloudb/.
 *
 */
package sharinglocalpool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author lcz
 */
public final class LocalQueue<T> {
	private final int total;
	private final LinkedList<T> items = new LinkedList<T>();

	public LocalQueue(int capacity) {
		this.total = capacity;
	}

	private int allocateOpts;
	private int recycleOpts;
	
	public int getAllocateOpts() {
		return allocateOpts;
	}

	public int getRecycleOpts() {
		return recycleOpts;
	}
	
	public int size() {
		return this.items.size();
	}

	Collection<T> removeItems(int count) {

		ArrayList<T> removed = new ArrayList<T>();
		Iterator<T> itor = items.iterator();
		while (itor.hasNext()) {
			removed.add(itor.next());
			itor.remove();
			if (removed.size() >= count) {
				break;
			}
		}
		return removed;
	}

	/**
	 * 
	 * @param value
	 * @throws InterruptedException
	 */
	void offer(T value) {
		recycleOpts++;
		this.items.offer(value);
		if (items.size() > total) {
			throw new java.lang.RuntimeException(
					"LocalQueue size exceeded ,cursize:"
							+ items.size());
		}
	}

	T poll() {
		allocateOpts++;
		return items.poll();
	}

}