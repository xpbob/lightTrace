package com.xp.agent.main;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public abstract class CommonTransform implements Opcodes{
	public VarInsnNode getVarInsnNode(final int opcode, final int var) {
		return new VarInsnNode(opcode, var);

	}

	public LabelNode getLabelNode() {
		return new LabelNode();
	}

	public MethodInsnNode currentTimeMillis() {
		return new MethodInsnNode(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
	}
	
	public abstract void trans(ClassNode cn);
}
