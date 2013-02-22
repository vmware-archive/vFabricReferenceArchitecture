//  Copyright (c) 2013 VMware, Inc. All rights reserved.
package com.vnware.vfra.dev.command;

import java.io.IOException;

import expectj.Spawn;
import expectj.TimeoutException;

public abstract class ExpectCommand {

	protected String value;	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public abstract void execute(Spawn shell) throws IOException, TimeoutException;
}
