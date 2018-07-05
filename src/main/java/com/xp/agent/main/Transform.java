package com.xp.agent.main;

import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class Transform implements Opcodes {

	public VarInsnNode getVarInsnNode(final int opcode, final int var) {
		return new VarInsnNode(opcode, var);

	}

	public LabelNode getLabelNode() {
		return new LabelNode();
	}

	public MethodInsnNode currentTimeMillis() {
		return new MethodInsnNode(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
	}

	public void trans(ClassNode cn) {
		for (MethodNode mn : (List<MethodNode>) cn.methods) {
			if ("<init>".equals(mn.name) || "<clinit>".equals(mn.name)) {
				continue;
			}
			InsnList insns = mn.instructions;
			if (insns.size() == 0) {
				continue;
			}

			FieldInsnNode out = new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
			MethodInsnNode println = new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "println",
					"(Ljava/lang/String;)V", false);

			VarInsnNode var = new VarInsnNode(LSTORE, mn.maxLocals);
			mn.maxLocals += 2;
			insns.insert(var);
			insns.insert(currentTimeMillis());
			insns.insert(getLabelNode());

			Iterator<AbstractInsnNode> j = insns.iterator();

			while (j.hasNext()) {
				AbstractInsnNode in = j.next();

				int op = in.getOpcode();
				if ((op >= Opcodes.IRETURN && op <= Opcodes.RETURN)) {

					AbstractInsnNode previous = in.getPrevious();
					InsnList list = new InsnList();
					VarInsnNode varEnd = new VarInsnNode(LSTORE, mn.maxLocals);
					mn.maxLocals += 2;
					list.add(getLabelNode());
					list.add(currentTimeMillis());
					list.add(varEnd);
					list.add(getLabelNode());
					list.add(out);
					list.add(new TypeInsnNode(NEW, "java/lang/StringBuilder"));
					list.add(new InsnNode(DUP));
					list.add(new VarInsnNode(ALOAD, 0));
					list.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;",
							false));
					list.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/Class", "getName", "()Ljava/lang/String;",
							false));
					list.add(new MethodInsnNode(INVOKESTATIC, "java/lang/String", "valueOf",
							"(Ljava/lang/Object;)Ljava/lang/String;", false));
					list.add(new MethodInsnNode(INVOKESPECIAL, "java/lang/StringBuilder", "<init>",
							"(Ljava/lang/String;)V", false));
					list.add(new LdcInsnNode(" "));
					list.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
							"(Ljava/lang/String;)Ljava/lang/StringBuilder;", false));
					list.add(new LdcInsnNode(mn.name));
					list.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
							"(Ljava/lang/String;)Ljava/lang/StringBuilder;", false));
					list.add(new LdcInsnNode(" "));
					list.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
							"(Ljava/lang/String;)Ljava/lang/StringBuilder;", false));
					list.add(new LdcInsnNode("cost "));
					list.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
							"(Ljava/lang/String;)Ljava/lang/StringBuilder;", false));

					list.add(getVarInsnNode(LLOAD, varEnd.var));
					list.add(getVarInsnNode(LLOAD, var.var));
					list.add(new InsnNode(LSUB));
					list.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
							"(J)Ljava/lang/StringBuilder;", false));
					list.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString",
							"()Ljava/lang/String;", false));
					list.add(println);
					list.add(getLabelNode());
					insns.insert(previous, list);

				}
			}
			mn.maxStack += 6;
		}
	}

}
