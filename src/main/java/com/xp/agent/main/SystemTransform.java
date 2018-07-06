package com.xp.agent.main;

import java.util.List;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class SystemTransform extends CommonTransform {
	public void trans(ClassNode cn) {
		for (MethodNode mn : (List<MethodNode>) cn.methods) {
			if ("<init>".equals(mn.name) || "<clinit>".equals(mn.name)) {
				continue;
			}

			if (!"gc".equals(mn.name)) {
				continue;
			}

			InsnList insns = mn.instructions;
			if (insns.size() == 0) {
				continue;
			}

			InsnList list = new InsnList();
			list.add(getLabelNode());
			list.add(new MethodInsnNode(INVOKESTATIC, "com/xp/agent/core/ThreadInfo", "getStack", "()V", false));
			insns.insert(list);
		}
	}
}
