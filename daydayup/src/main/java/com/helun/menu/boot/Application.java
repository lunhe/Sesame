package com.helun.menu.boot;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.helun.menu.performance.TestVO;
import com.helun.menu.performance.IMergeSETaskAdapter;
import com.helun.menu.performance.LXXLoginInfo;
import com.helun.menu.performance.LXXResult;
import com.helun.menu.performance.MergeSETaskAdapter;
import com.helun.menu.performance.MergeSETaskBOBuilder;
import com.helun.menu.performance.MergeSETaskFuture;
import com.helun.menu.performance.TestServiceImpl;
import com.helun.menu.performance.TestTaskBOBuilder;

public class Application {

	public static void main(String[] args) throws IOException {

//		doMergeTest() ;
//		new GS().show(1,1000L);
//		new GS().show(2,2000L);

		MergeSETaskBOBuilder mergeSETaskBOBuilder = new MergeSETaskBOBuilder();
		mergeSETaskBOBuilder.setVOClass(TestVO.class);
		mergeSETaskBOBuilder.addParams("date", "this is data").addParams("files", null).addParams("testUseTime", 100L)
				.addParams("id", null).addParams("type", 10);
		System.out.println(mergeSETaskBOBuilder.build());
	}

	static class P {
		List<Long> ids = Lists.newArrayList();

		void show(int i) {
			System.out.println("p" + i + ids);
		}
	}

	static class S extends P {
		void show(int i) {
			System.out.println("S" + i);
			super.show(++i);
		}
	}

	static class GS extends S {
		void show(int i, Long id) {
			ids.add(id);
			System.out.println("GS" + i);
			super.show(++i);
		}
	}

	public static void doMergeTest() {
		IMergeSETaskAdapter mergeSETaskAdapter = new MergeSETaskAdapter();
		new TestServiceImpl(mergeSETaskAdapter);

		for (int i = 0; i < 100; i++) {

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("投递" + i);
			new Thread(new Runnable() {
				@Override
				public void run() {
					LXXResult result = testRequest(mergeSETaskAdapter, 10, 10001L);
					System.out.println(Thread.currentThread().getName() + " ==> " + result);
				}
			}).start();
		}
	}

	public static LXXResult testRequest(IMergeSETaskAdapter mergeSETaskAdapter, Integer age, Long classId) {
		TestTaskBOBuilder taskBOBuilder = new TestTaskBOBuilder();
		taskBOBuilder.setAge(age);
		taskBOBuilder.setClassId(classId);
		taskBOBuilder.setId(Thread.currentThread().getId());
		taskBOBuilder.setName(Thread.currentThread().getName());

		LXXLoginInfo loginInfo = new LXXLoginInfo();

		MergeSETaskFuture<Map<String, LXXResult>> future = mergeSETaskAdapter.addTask(loginInfo, taskBOBuilder);

		Long waitTime = 10L;
		LXXResult result = mergeSETaskAdapter.waitResult(future, waitTime, taskBOBuilder.getId().toString());
		return result;
	}

}
