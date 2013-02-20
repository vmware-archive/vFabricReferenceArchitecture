package com.vmware.vfra.batch.master;

public class StepExecutionStatus {

	private int runningCount = 0;
	private int successfulCount = 0;
	private int partitionSize = 0;


	public StepExecutionStatus(int runningCount, int successfulCount,
			int partitionSize) {
		super();
		this.runningCount = runningCount;
		this.successfulCount = successfulCount;
		this.partitionSize = partitionSize;
	}

	/**
	 * @return the number of steps that are still running
	 */
	public int getRunningCount() {
		return runningCount;
	}

	/**
	 * @return the number of steps that completed successfully
	 */
	public int getSuccessfulCount() {
		return successfulCount;
	}

	/**
	 * @return the size of the partition, or total number of step executions
	 */
	public int getPartitionSize() {
		return partitionSize;
	}

	@Override
	public String toString() {
		return "StepExecutionStatus [runningCount=" + runningCount
				+ ", successfulCount=" + successfulCount + ", partitionSize="
				+ partitionSize + "]";
	}


}