package com.xp.agent.main;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

public class Main {

	public static final String SYSTEM = "java.lang.System";

	public static void premain(final String args, Instrumentation inst) {
		agentmain(args, inst);
	}

	public static void agentmain(final String args, Instrumentation inst) {
		inst.addTransformer(new ClassFileTransformer() {

			public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
					ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

				className = className.replace("/", ".");
				if ((className.contains(args) && !className.contains("$")) || SYSTEM.equals(className)) {
					try {
						ClassReader cr;
						cr = new ClassReader(className);
						ClassNode cn = new ClassNode();
						cr.accept(cn, 0);
						if (SYSTEM.equals(className)) {
							SystemTransform at = new SystemTransform();
							at.trans(cn);
						} else {
							CustomTransform at = new CustomTransform();
							at.trans(cn);
						}
						ClassWriter cw = new ClassWriter(0);
						cn.accept(cw);
						byte[] toByte = cw.toByteArray();
						return toByte;
					} catch (IOException e) {
						e.printStackTrace();
					}

				}

				return null;
			}
		}, true);

		Class[] allLoadedClasses = inst.getAllLoadedClasses();
		List<Class> retransformClasses = new ArrayList<Class>(allLoadedClasses.length);
		for (Class clazz : allLoadedClasses) {
			if (inst.isModifiableClass(clazz) && (clazz.getName().contains(args)||clazz.getName().equals(SYSTEM)) && !clazz.getName().contains("$")) {
				retransformClasses.add(clazz);
				System.out.println(clazz.getName());
			}
		}
		try {
			inst.retransformClasses(retransformClasses.toArray(new Class[0]));
		} catch (UnmodifiableClassException e) {
			e.printStackTrace();
		}
	}

}
