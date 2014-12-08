package com.techburg.autospring.db.task.abstr;


public abstract class AbstractDBTask {
	public static final int DB_READ_MODE = 0;
	public static final int DB_WRITE_MODE = 1;
	public static final int SCHEDULE_SYNC_MODE = 0;
	public static final int SCHEDULE_ASYNC_MODE = 1;

	private int mReadWriteMode;
	private int mScheduleMode;

	public abstract void execute();

	protected void setDBReadWriteMode(int mode) {
		this.mReadWriteMode = mode;
	}

	public int getDBReadWriteMode() {
		return mReadWriteMode;
	}

	public void setScheduleMode(int scheduleMode) {
		this.mScheduleMode = scheduleMode;
	}

	public int getScheduleMode() {
		return mScheduleMode;
	}
}
