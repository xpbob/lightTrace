package com.xp.console;

import java.io.IOException;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import static com.xp.common.CommonString.*;

public class Main {

	public static void main(String[] args)
			throws AttachNotSupportedException, IOException, AgentLoadException, AgentInitializationException {

		String agentPath = args[0];
		String pid = null;
		String arg = null;
		try {
			pid = args[1];
			if (pid == null) {
				System.out.println("input pid");
				return;
			}
		} catch(Exception e) {
			System.out.println("input pid");
			return;
		}

		try {
			arg = args[2];
			if (arg == null) {
				System.out.println("input arg");
				return;
			}
		} catch(Exception e) {
			arg=NOCLASS;

		}
		
		
		final String jarName = "trace-0.0.1-SNAPSHOT-agent.jar";

		VirtualMachine vm;
		try {
			vm = VirtualMachine.attach(pid);
			vm.loadAgent(agentPath + "/" + jarName,arg);
			vm.detach();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

}
