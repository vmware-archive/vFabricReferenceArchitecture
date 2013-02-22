//  Copyright (c) 2013 VMware, Inc. All rights reserved.

package com.vmware.vfra.dev;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.launch.Launcher;
import org.apache.tools.ant.taskdefs.Delete;
import org.junit.Before;
import org.junit.Test;

import com.vnware.vfra.dev.ExpectTask;
import com.vnware.vfra.dev.command.Expect;
import com.vnware.vfra.dev.command.Send;

public class ExpectTaskTest {

	File temp = new File("scratch");

	@Before
	public void setup() throws Exception {
		temp = new File("scratch");
		if (temp.exists()) {
			Delete del = new Delete();
			Project p = new Project();
			del.setProject(p);
			del.setDir(temp);
			del.execute();
		}
		temp.mkdir();
	}

	// Agree to EULA
	public String[] script = { "expect::continue>", // 1
			"send::\r\n",
			"expect::continue>", // 2
			"send::\r\n",
			"expect::continue>", // 3
			"send::\r\n",
			"expect::continue>", // 4
			"send::\r\n",
			"expect::continue>", // 5
			"send::\r\n",
			"expect::continue>", // 6
			"send::\r\n",
			"expect::continue>", // 7
			"send::\r\n",
			"expect::continue>", // 8
			"send::\r\n",
			"expect::continue>", // 9
			"send::\r\n",
			"expect::continue>", // 10
			"send::\r\n",

			// Enter "agree" to accept the terms of the license or "disagree" to
			// abort the installation.
			"expect::installation.", "send::agree\r\n",

			// Please specify the vFabric_SQLFire installation directory.
			"expect::directory.", "send::" + temp.getAbsolutePath() + "\r\n",

			// Verify this directory is correct: ... [yes]
			"expect::[yes]", "send::\r\n", "expect::finished."
			// Unzipping files .. wait for default timeout
	};

	String delimiter = "::";

	public void loadScript(ExpectTask task) {
		int i = 0;
		System.out.println("script was:" + script.toString());
		for (String s : script) {
			i++;
			String[] commandset = s.split(delimiter);
			if (commandset.length == 2) {
				if ("expect".equals(commandset[0].trim())) {
					Expect expect = new Expect();
					expect.setValue(commandset[1]);
					task.commands.add(expect);
				}
				if ("send".equals(commandset[0].trim())) {
					Send send = new Send();
					send.setValue(commandset[1]);
					task.commands.add(send);
				}
			} else {
				throw new RuntimeException(
						"Command #"
								+ i
								+ " in the command line '"
								+ commandset[0]
								+ "' did not have the format of <command type>::<entry>");
			}
		}
	}

	// this works...
	@Test
	public void testSQLFireWithExpectTask() throws Exception {
		// assumes being run from top level of project
		ExpectTask etask = new ExpectTask();
		Project p = new Project();
		p.setBaseDir(temp);
		etask.setExecutable("java -jar src/test/resources/vFabric_SQLFire_103_Installer.jar");
		loadScript(etask);
		etask.execute();
	}

}
