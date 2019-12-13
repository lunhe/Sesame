package com.helun.menu.boot;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import com.helun.menu.api.MergeCEServiceLabel;
import com.helun.menu.api.TaskBOFactory;
import com.helun.menu.model.BaseEntity;
import com.helun.menu.util.FileLoader;

public class Main {
	private static MergeTaskService mergeTaskService ;
	public static void main(String[] args) {
		System.out.println("------------------0");
		mergeTaskService = new MergeTaskService(new TaskBOFactory() {
			
			@Override
			public BaseEntity buid(Long userId, Date applyTime, String... data) {
				// TODO Auto-generated method stub
				return new BaseEntity();
			}
		}, new MergeCEServiceLabel() {
			
			@Override
			public Object servic(Collection<BaseEntity> taskBos) {
				System.out.println("begin  "+Thread.currentThread().getId() + " ======>" );
				Long start = System.currentTimeMillis();
				for(int i = 0 ; i < 10000 ; i++) {
					for(int j = 0 ; j < 10000 ; j++) {
						
					}
				}
				Long end = System.currentTimeMillis();
				System.out.println("finish  "+Thread.currentThread().getId() + " ======> "+ (end-start)+"ms");
				return "0";
			}
		}, 500, 20);
		System.out.println("------------------1");
		for(int i = 0 ; i< 50 ;i ++) {
			demoClient(i);
		}
		System.out.println("------------------2");
		
	}
	
	
	public static void demoClient(int i) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				FutureTask<Object> future = null;
				try {
					future = mergeTaskService.addTask(1000L, new Date(), "test");
					//future.get();
					future.get(5, TimeUnit.SECONDS);
				} catch (Exception e) {
					ArrayBlockingQueue<BaseEntity> queue = mergeTaskService.taskQueueMap.get("1000");
					System.out.println(queue.size());
					System.out.println(mergeTaskService.futureQueueMap);
					e.printStackTrace();
					
					Boolean isCanceled = future.cancel(true);
					if(isCanceled) {
						System.out.println("提交答案超时");
					}else {
						if(future.isDone()) {
							System.out.println("提交答案完成");
						}
					}
				}
				
			}
		}).start();
	}

	public static void testMenu() {
		List<String> menus = FileLoader.readFileByLineOnResources("src/main/resources/menu.json");
		Random random = new Random();
		if (menus.size() > 0) {
			for (int i = 0; i < 3; i++) {
				System.out.println(random.nextInt(menus.size()));
				System.out.println(menus.get(random.nextInt(menus.size())));
			}
		}
	}
}
