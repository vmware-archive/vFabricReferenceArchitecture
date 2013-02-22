//  Copyright (c) 2013 VMware, Inc. All rights reserved.
package com.vnware.vfra.dev.command;

import java.io.IOException;

import expectj.Spawn;
import expectj.TimeoutException;

public class Expect extends ExpectCommand {

	private Long timeoutSecs = 5l;
	public Long getTimeoutSecs() {
		return timeoutSecs;
	}
	public void setTimeoutSecs(Long timeoutSecs) {
		this.timeoutSecs = timeoutSecs;
	}

	@Override
	public void execute(Spawn shell) throws IOException, TimeoutException {
		shell.expect(this.value);
	}

}
