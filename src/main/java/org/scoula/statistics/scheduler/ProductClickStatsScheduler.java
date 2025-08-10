package org.scoula.statistics.scheduler;

public interface ProductClickStatsScheduler {
	void sendClickStats();
	void deleteOldLogs();
}
