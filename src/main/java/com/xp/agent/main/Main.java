package com.xp.agent.main;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import static com.xp.common.CommonString.NOCLASS;
import static com.xp.common.CommonString.SYSTEM;

public class Main {

    public static void premain(final String args, Instrumentation inst) {
        agentmain(args, inst);
    }

    public static void agentmain(final String args, Instrumentation inst) {

        inst.addTransformer(new ClassFileTransformer() {

            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

                className = className.replace("/", ".");
                if (!className.contains("$") && (isSpecialClass(className) || isReTransformClass(className, args))) {

                    ClassReader cr;
                    cr = new ClassReader(classfileBuffer);
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


                }

                return null;
            }
        }, true);

        Class[] allLoadedClasses = inst.getAllLoadedClasses();
        List<Class> retransformClasses = new ArrayList<Class>(allLoadedClasses.length);
        for (Class clazz : allLoadedClasses) {
            String name = clazz.getName();
            if (isSpecialClass(name) || isReTransformClass(name, args)) {
                if (inst.isModifiableClass(clazz) && !name.contains("$")) {
                    retransformClasses.add(clazz);
                }
            }
        }
        try {
            inst.retransformClasses(retransformClasses.toArray(new Class[0]));
        } catch (UnmodifiableClassException e) {
            e.printStackTrace();
        }
    }

    public static boolean isSpecialClass(String className) {
        return SYSTEM.equals(className);
    }

    public static boolean isReTransformClass(String className, String args) {
        if (NOCLASS.equals(args)) {
            return false;
        }

        if (className.contains(args)) {
            return true;
        }

        return false;
    }

}
