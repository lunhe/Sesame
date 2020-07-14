package com.helun.menu.performance;

import java.util.List;
import java.util.Map;

public interface IMergeService {

	public Map<String, LXXResult> doService(List<MergeSETaskBO> mergeSETaskBOs, MergeSETask currentTask,
			Map<Object, Object> context);

	public Map<String, LXXResult> saveService(List<MergeSETaskBO> mergeSETaskBOs, MergeSETask currentTask,
			Map<Object, Object> context);
}
