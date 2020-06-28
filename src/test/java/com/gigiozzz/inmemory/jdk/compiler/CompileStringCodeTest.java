/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gigiozzz.inmemory.jdk.compiler;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardLocation;

import org.junit.jupiter.api.Test;

/**
 *
 * @author sportelli
 */
public class CompileStringCodeTest {
	// stesso contenuto di Hello.java
	private static final String SOURCE = "package test;\r\n" + "\r\n"
			+ "import com.gigiozzz.inmemory.jdk.compiler.TestInterface;\r\n"
			+ "import com.google.common.collect.ImmutableList;\r\n" + "import java.util.Map;\r\n" + "\r\n"
			+ "public class HelloSource implements TestInterface {\r\n" + "\r\n"
			+ "    public static void sayHello(String name) {\r\n"
			+ "        System.out.println(\"Hello \" + name);\r\n" + "    }\r\n" + "\r\n"
			+ "    public Object run(Map parameters) {\r\n" + "        if (parameters.containsKey(\"name\")) {\r\n"
			+ "            Hello.sayHello(parameters.get(\"name\").toString());\r\n" + "        }\r\n"
			+ "        ImmutableList.builder();\r\n" + "        System.out.println(\"Hello World system out\");\r\n"
			+ "        System.out.println(\"String function isBlank: \"+ (\"\".isBlank()));\r\n"
			+ "        return \"Hello \" + parameters.get(\"name\");\r\n" + "    }\r\n" + "}";

    
    @Test
    public void test2bis_hello() throws IOException {

        final InMemoryCompiler uCompiler = InMemoryCompiler.javac();
        
		InMemoryCompilation gc = uCompiler
				.compile(Arrays.asList(JavaFileObjectUtils.forSourceString("test.HelloSource", SOURCE)));

        if(InMemoryCompilation.Status.SUCCESS.equals(gc.status())){
            try {
                for(JavaFileObject jfo: gc.generatedFiles()){
                    System.out.println("jfo source:"+jfo);
                    SimpleJavaFileObject k = (SimpleJavaFileObject)jfo;
                    System.out.println("jfo name:"+k.getName()+" "+k.toUri());
					URI uri = InMemoryJavaFileManager.uriForFileObject(StandardLocation.CLASS_OUTPUT, "",
							"test.HelloSource");
                }
            	System.out.println("Compilation OK: "+gc.describeGeneratedSourceFiles());

				final Class<?> hello = gc.loadCompiledClass("test.HelloSource");
                Method mainMethod = hello.getDeclaredMethod("run", Map.class);
				Object instance = hello.getConstructor().newInstance();
                mainMethod.invoke(instance, new Object[]{new HashMap<>()});
                
            } catch(Exception ex){
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        } else {
        	System.out.println("Compilation KO: "+gc.toString());
        	System.out.println(""+gc.describeFailureDiagnostics());
        }
        
        
        assertEquals(true, InMemoryCompilation.Status.SUCCESS.equals(gc.status()));
    }
    
     
}
