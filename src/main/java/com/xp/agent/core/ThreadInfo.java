package com.xp.agent.core;

public class ThreadInfo {
	public static void getStack() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		for (StackTraceElement element : stackTrace) {
			System.out.println(element);
		}
	}

}
