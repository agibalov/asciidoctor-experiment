package me.loki2302;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaPackage;
import me.loki2302.testtools.Documentation;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ClassDiagramGeneratorsTest {
    @Rule
    public final Documentation documentation = new Documentation(System.getProperty("snippetsDir"));

    private static boolean shouldIgnore(JavaClass javaClass) {
        if(javaClass.getPackageName().equals("java.lang")) {
            return true;
        }

        if(javaClass.isPrimitive()) {
            return true;
        }

        return false;
    }

    @Test
    public void generatePlantUmlClassDiagram() throws IOException {
        JavaProjectBuilder javaProjectBuilder = new JavaProjectBuilder();
        javaProjectBuilder.addSourceTree(new File("./src/main/java"));

        StringBuilder stringBuilder = new StringBuilder();

        for(JavaClass javaClass : javaProjectBuilder.getClasses()) {
            if(shouldIgnore(javaClass)) {
                continue;
            }

            String classType;
            if(javaClass.isInterface()) {
                classType = "interface";
            } else if(javaClass.isAbstract()) {
                classType = "abstract class";
            } else {
                classType = "class";
            }

            stringBuilder.append(String.format("%s %s", classType, javaClass.getFullyQualifiedName()));

            // extends
            JavaClass superClass = javaClass.getSuperJavaClass();
            if(superClass != null) {
                if(!shouldIgnore(superClass)) {
                    stringBuilder.append(String.format(" extends %s", superClass.getFullyQualifiedName()));
                }
            }

            // implements
            List<JavaClass> interfaces = javaClass.getInterfaces();
            if(!interfaces.isEmpty()) {
                List<String> interfaceNames = interfaces.stream()
                        .filter(i -> !shouldIgnore(i))
                        .map(i -> i.getFullyQualifiedName())
                        .collect(Collectors.toList());
                String implementsString = " implements " + String.join(", ", interfaceNames);
                stringBuilder.append(implementsString);
            }

            stringBuilder.append("\n");
        }

        for(JavaClass javaClass : javaProjectBuilder.getClasses()) {
            if(shouldIgnore(javaClass)) {
                continue;
            }

            // delegation
            List<JavaField> fields = javaClass.getFields();
            for(JavaField field : fields) {
                if(shouldIgnore(field.getType())) {
                    continue;
                }

                stringBuilder.append(String.format("%s -> %s: delegates\n", javaClass.getFullyQualifiedName(), field.getType().getFullyQualifiedName()));
            }
        }

        documentation.text("classDiagram", stringBuilder.toString());
    }

    @Test
    public void generateDotClassDiagram() throws IOException {
        JavaProjectBuilder javaProjectBuilder = new JavaProjectBuilder();
        javaProjectBuilder.addSourceTree(new File("./src/main/java/me/loki2302/"));

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("digraph g { overlap = false; node [shape=rectangle;];\n");

        int clusterIdx = 0;
        for(JavaPackage javaPackage : javaProjectBuilder.getPackages()) {
            stringBuilder.append(String.format("subgraph cluster_%d {\n", clusterIdx++));
            stringBuilder.append(String.format("label=\"%s\";\n", javaPackage.getName()));
            for(JavaClass javaClass : javaPackage.getClasses()) {
                stringBuilder.append(String.format("%s;\n", simpleName(javaClass.getName())));
            }
            stringBuilder.append("}\n");
        }

        for(JavaClass javaClass : javaProjectBuilder.getClasses()) {
            // inheritance
            JavaClass superClass = javaClass.getSuperJavaClass();
            if(superClass != null) {
                if(!shouldIgnore(superClass)) {
                    stringBuilder.append(String.format("%s -> %s;\n", simpleName(javaClass.getName()), simpleName(superClass.getName())));
                }
            }

            for(JavaClass i : javaClass.getInterfaces()) {
                if(shouldIgnore(i)) {
                    continue;
                }

                stringBuilder.append(String.format("%s -> %s;\n", simpleName(javaClass.getName()), simpleName(i.getName())));
            }

            // delegation
            List<JavaField> fields = javaClass.getFields();
            for(JavaField field : fields) {
                if(shouldIgnore(field.getType())) {
                    continue;
                }

                stringBuilder.append(String.format("%s -> %s;\n", simpleName(javaClass.getName()), simpleName(field.getType().getName())));
            }
        }

        stringBuilder.append("}\n");

        documentation.text("classDiagram", stringBuilder.toString());
    }

    private static String simpleName(String n) {
        return n.replace('.', '_');
    }
}
