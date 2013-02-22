//  Copyright (c) 2013 VMware, Inc. All rights reserved.
package com.vnware.vfra.dev.command;

import java.io.IOException;

import expectj.Spawn;
import expectj.TimeoutException;

public class Send extends ExpectCommand {

	@Override
	public void execute(Spawn shell) throws IOException, TimeoutException {
		shell.send(this.value);
	}


}
