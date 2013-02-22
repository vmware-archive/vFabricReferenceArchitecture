// Portions Copyright (c) 2013 VMware, Inc. All rights reserved.
/*

 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.vnware.vfra.dev;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.RedirectorElement;

import com.vnware.vfra.dev.command.Expect;
import com.vnware.vfra.dev.command.ExpectCommand;
import com.vnware.vfra.dev.command.Send;

import expectj.ExpectJ;
import expectj.ExpectJException;
import expectj.Spawn;
import expectj.TimeoutException;


/**
 * This task built to interact with a process via the expectJ library. 
 * 
 */
public class ExpectTask extends Task {
    protected boolean failOnError = false;
    protected boolean newEnvironment = false;
    private Long timeout = 20L;
    protected Commandline cmdl = new Commandline();
    private String executable;

    protected RedirectorElement redirectorElement;

    public ArrayList<ExpectCommand> commands = new ArrayList<ExpectCommand>();
    public Expect createExpect() {
    	Expect expect = new Expect();
    	commands.add(expect);
    	return expect;
    }
    public Send createSend() {
    	Send send = new Send();
    	commands.add(send);
    	return send;
    }
    
    private long intrinsicDelay = 1000; 
    public long getIntrinsicDelay() {
		return intrinsicDelay;
	}
	public void setIntrinsicDelay(long intrinsicDelay) {
		this.intrinsicDelay = intrinsicDelay;
	}

	private boolean testload = false;
	public boolean isTestload() {
		return testload;
	}
	public void setTestload(boolean testLoad) {
		this.testload = testLoad;
	}

    /**
     * Set the name of the executable program.
     * @param value the name of the executable program.
     */
    public void setExecutable(String value) {
        this.executable = value;
        cmdl.setExecutable(value);
    }
	
	/**
     * Interact with a process via ExpectJ.
     *
     * @throws BuildException in a number of circumstances:
     */
    public void execute() throws BuildException {
		ExpectJ expectinator = new ExpectJ(timeout);

		ExpectCommand command = null;
		Spawn shell = null;
		try {
			shell = expectinator.spawn(executable);
			Iterator<ExpectCommand> it = commands.iterator();
			while (it.hasNext()) {
				command = it.next();
				command.execute(shell);
				this.log(shell.getCurrentStandardOutContents(), Project.MSG_INFO );
				this.log(shell.getCurrentStandardOutContents(), Project.MSG_ERR);
			}
			shell.expectClose(timeout);
			int exitVal = shell.getExitValue();
			String msg = "  -- command: " + command.getClass().getName() + "   value: " + command.getValue();
			if (exitVal != 0)
				throw new BuildException("[[ Process did not exit within timeout or returned non-zero result ]]" + msg);
		}
		catch (TimeoutException TE) {
			String msg = getStackTrace(TE) + " -- command: " + command.getClass().getName() + "   value: " + command.getValue();
			System.out.println(msg);
			throw new BuildException(msg, TE);
		}
		catch (ExpectJException EJE) {
			String msg = getStackTrace(EJE) + " -- command: " + command.getClass().getName() + "   value: " + command.getValue();
			System.out.println(msg);
			throw new BuildException(msg, EJE);
		}
		catch (IOException IOE) {
			String msg = getStackTrace(IOE) + " -- command: " + command.getClass().getName() + "   value: " + command.getValue();
			System.out.println(msg);
			throw new BuildException(msg, IOE);
		}        
    }

    public static String getStackTrace(Throwable aThrowable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
      }
    
 
}
