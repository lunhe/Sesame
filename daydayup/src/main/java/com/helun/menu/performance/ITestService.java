package com.helun.menu.performance;

import java.util.List;
import java.util.Map;

public interface ITestService {
	
	public Map<String, LXXResult> saveTest(List<MergeSETaskBO> mergeSETaskBOs) ;
}
