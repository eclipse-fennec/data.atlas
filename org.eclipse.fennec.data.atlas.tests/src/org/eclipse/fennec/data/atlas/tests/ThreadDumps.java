/**
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Data In Motion Consulting - initial implementation
 */
package org.eclipse.fennec.data.atlas.tests;


import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;

/**
 * Thread dumps for wait-timeouts: a test that gives up waiting for the
 * framework appends one to its failure, so a hang on a CI runner (where
 * nobody can attach jstack) shows who holds which lock.
 */
final class ThreadDumps {

	private ThreadDumps() {
	}

	/** All live threads with their stacks, held monitors and locks. */
	static String dump() {
		StringBuilder out = new StringBuilder("\n--- thread dump ---\n");
		for (ThreadInfo info : ManagementFactory.getThreadMXBean().dumpAllThreads(true, true)) {
			out.append('"').append(info.getThreadName()).append("\" ").append(info.getThreadState());
			if (info.getLockName() != null) {
				out.append(" on ").append(info.getLockName());
			}
			if (info.getLockOwnerName() != null) {
				out.append(" owned by \"").append(info.getLockOwnerName()).append('"');
			}
			out.append('\n');
			for (StackTraceElement frame : info.getStackTrace()) {
				out.append("\tat ").append(frame).append('\n');
			}
			for (var monitor : info.getLockedMonitors()) {
				out.append("\t- locked ").append(monitor).append(" at ").append(monitor.getLockedStackFrame())
						.append('\n');
			}
			for (var synchronizer : info.getLockedSynchronizers()) {
				out.append("\t- locked ").append(synchronizer).append('\n');
			}
		}
		long[] deadlocked = ManagementFactory.getThreadMXBean().findDeadlockedThreads();
		if (deadlocked != null) {
			out.append("DEADLOCKED thread ids: ").append(java.util.Arrays.toString(deadlocked)).append('\n');
		}
		return out.toString();
	}
}
