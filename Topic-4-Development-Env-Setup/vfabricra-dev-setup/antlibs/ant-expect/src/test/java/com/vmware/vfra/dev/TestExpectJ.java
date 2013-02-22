// Copyright (c) 2013 VMware, Inc. All rights reserved.
package com.vmware.vfra.dev;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Delete;
import org.junit.Before;
import org.junit.Test;

import expectj.ExpectJ;
import expectj.Spawn;

public class TestExpectJ {

	File temp; 
	@Before
	public void setup() throws Exception {
		temp = new File("scratch");
		if(temp.exists()) {
		  Delete del = new Delete();
		  Project p = new Project();
		  del.setProject(p);
		  del.setDir(temp);
		  del.execute();
		}
		temp.mkdir();
	}
	
//	@Test
	public void setupTest() throws Exception {
		
	}
	
	@Test
	public void testSQLFireWithExpectJ() throws Exception {
		// assumes being run from top level of project
		ExpectJ expectinator = new ExpectJ(5);

		// example of interacting with a shell, then starting another process therein. 
		Spawn shell = expectinator.spawn("/bin/sh");
		shell.send("/bin/ls\n");
		System.out.println("output from bash:" + shell.getCurrentStandardOutContents() + "   --  " + shell.getCurrentStandardErrContents());
//		shell.expect("blahg");
		shell.send("cd scratch\n");
		System.out.println("output from bash:" + shell.getCurrentStandardOutContents() + "   --  " + shell.getCurrentStandardErrContents());

		//		Spawn shell = expectinator.spawn("java -jar src/test/resources/vFabric_SQLFire_103_Installer.jar");
		shell.send("java -jar ../src/test/resources/vFabric_SQLFire_103_Installer.jar\n");
		// Agree to EULA
		shell.expect("continue>");
		shell.send("\n\r");
		shell.expect("continue>");
		shell.send("\n\r");
		shell.expect("continue>");
		shell.send("\n\r");
		shell.expect("continue>");
		shell.send("\n\r");
		shell.expect("continue>");
		shell.send("\n\r");
		shell.expect("continue>");
		shell.send("\n\r");
		shell.expect("continue>");
		shell.send("\n\r");
		shell.expect("continue>");
		shell.send("\n\r");
		shell.expect("continue>");
		shell.send("\n\r");
		shell.expect("continue>");
		shell.send("\n\r");

		// Enter "agree" to accept the terms of the license or "disagree" to abort the installation.
		shell.expect("installation.");
		shell.send("agree\n\r");
		
		// Please specify the vFabric_SQLFire installation directory.
		shell.expect("directory.");
		shell.send( temp.getAbsolutePath() + "\n\r");
		
		// Verify this directory is correct: ... [yes]
		shell.expect("[yes]");
		shell.send("\n\r");
		
		// Unzipping files
		shell.expectClose(-1);
		
//		System.out.println(shell.getCurrentStandardOutContents());
	}
	

	
}
