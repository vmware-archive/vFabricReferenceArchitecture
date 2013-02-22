// Copyright (c) 2013 VMware, Inc. All rights reserved.
package com.vmware.vfra.batch;

import java.util.Date;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

public class JobDateIncrementer implements JobParametersIncrementer {
	
	private static Log logger = LogFactory.getLog(JobDateIncrementer.class);

    public JobParameters getNext(JobParameters parameters) {
    	logger.debug("adding jobTimeStamp...");
    	Date now = Calendar.getInstance().getTime();
    	JobParametersBuilder b =
    			new JobParametersBuilder().addLong("jobTimeStamp", now.getTime()).
    			addDate("jobTimeStampAsDate", now);
        return b.toJobParameters();
    }
}

