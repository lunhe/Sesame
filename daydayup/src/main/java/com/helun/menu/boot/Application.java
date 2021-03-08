package com.helun.menu.boot;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

import com.helun.menu.performance.IMergeSETaskAdapter;
import com.helun.menu.performance.LXXLoginInfo;
import com.helun.menu.performance.LXXResult;
import com.helun.menu.performance.MergeSETaskAdapter;
import com.helun.menu.performance.MergeSETaskFuture;
import com.helun.menu.performance.TestServiceImpl;
import com.helun.menu.performance.TestTaskBOBuilder;

public class Application {
	static int sum = 0  ;

	public static void main(String[] args) throws Exception {
	
		String a = "ab" ;
		String b = "a" + "b" ;
		System.out.println(a == b  );
		System.out.println(a.equals(b));
	}
	
	

	
	public int islandPerimeter(int[][] grid) {
		int statrtRow = 0 ;
		int startCol = 0 ;
		int length = 0 ;
		for(int i = 0 ;i < grid.length ; i ++) {
			for(int j = 0 ; j < grid[i].length ; j++) {
				if (grid[i][j] == 1) {
					statrtRow = i ;
					startCol = j ;
					break ;
				}
			}
		}
		
		if (statrtRow != 0 || startCol != 0) {
			length = countLength(grid, statrtRow, startCol) ;
		}
		
		return length ; 
	}

	public int countLength(int[][] grid , int statrtRow , int startCol) {
		int currentRow = statrtRow ;
		int currentCol = startCol ;
		int length = 1 ;
		while (true) {

			
			// 寻找下一个
			Boolean found = false ;
			int step = 0 ;
			// 右横向查
			if (grid[currentRow][currentCol+1] == 1) {
				currentRow ++ ;
				step = 1 ;
				found = true ;
			}
			// 右斜向查
			if (!found) {
				if (grid[currentRow+1][currentCol+1] == 1) {
					currentCol ++ ;
					currentCol ++ ;
					step = 2 ;
					found = true ;
				}
			}
			// 下纵向查
			if (!found) {
				if (grid[currentRow+1][currentCol] == 1) {
					currentCol ++ ;
					step = 1 ;
					found = true ;
				}
			}
			
			// 左斜向查
			if (!found) {
				if (grid[currentRow+1][currentCol-1] == 1) {
					currentCol ++ ;
					currentCol ++ ;
					step = 2 ;
					found = true ;
				}
			}
			
			// 左横向查
			if (grid[currentRow][currentCol-1] == 1) {
				currentRow ++ ;
				step = 1 ;
				found = true ;
			}
			// 下纵向查
			if (!found) {
				if (grid[currentRow-1][currentCol] == 1) {
					currentCol ++ ;
					step = 1 ;
					found = true ;
				}
			}		
			
			//判断终结
			if (currentCol == startCol && currentRow == statrtRow) {
				break ;
			}
			
			// 计算当前
			
		}
		return length ;
	}
	
	
	
	
	
	
	public void testPipe() throws IOException{
		PipedInputStream inputStream = new PipedInputStream();
		PipedOutputStream outputStream = new PipedOutputStream();

		outputStream.connect(inputStream);

		new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println("------------>");
				while (true) {
					byte[] buffer = new byte[1024];
					try {
						int len = inputStream.read(buffer);
						System.out.println(len + new String(buffer));

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

		new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 10; i++) {
					try {
						Thread.sleep(500);
						outputStream.write("Hello".getBytes());
						outputStream.flush();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						
					}
				}

				while(true) {
					
				}
			}
		}).start();
	}

	public static List<List<Integer>> findCombinations(int[] candidates, Stack<Integer> dValues) {
		List<List<Integer>> retList = new ArrayList<List<Integer>>();
		if (dValues.size() == 0) {
			return retList;
		}

		List<Integer> ret = new ArrayList<Integer>();
		int target = dValues.peek();
		int temp = target;
		for (int i = 0; i < candidates.length; i++) {
			if (candidates[i] > target) {
				continue;
			}
			temp = temp - candidates[i];
			if (temp > 0) {
				dValues.add(temp);
				int[] subCandidates = Arrays.copyOfRange(candidates, i + 1, candidates.length);
				List<List<Integer>> subRetList = findCombinations(subCandidates, dValues);
				for (List<Integer> subRet : subRetList) {
					subRet.add(candidates[i]);
					retList.add(subRet);
				}
			}
			if (temp == 0) {
				ret.add(candidates[i]);
				retList.add(ret);
				break;
			} else {
				temp = target;
			}
		}
		dValues.pop();
		return retList;

	}

	public static void testSocketRSAEncrypt() throws Exception {
		DatagramSocket socket = new DatagramSocket(9999);
		byte[] buffer = new byte[10000];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

		String info = "";
		String key = "";

		// 测试rsa加密登录
		String publicKey = DecryptHandler.readKeyPem("./publicKeyPKCS8.pem");
		info = RSAUtils.encryptByPublicKey("LogON it's me", publicKey);
		InetAddress address = InetAddress.getByName("127.0.0.1");
		DatagramPacket packet2 = new DatagramPacket(info.getBytes(), info.getBytes().length, address, 8888);
		socket.send(packet2);

		// 测试rsa传递密钥

		socket.receive(packet);
		info = new String(buffer, 0, packet.getLength());
		String privateKey = DecryptHandler.readKeyPem("./privateKey.pem");
		key = RSAUtils.decryptByPrivateKey(info, privateKey);

		// 测试密钥aes解密
		socket.receive(packet);
		info = new String(buffer, 0, packet.getLength());
		info = AESUtils.decrypt(info, key);
		System.out.println(info);

		// 测试aes加密
		address = packet.getAddress();
		int port = packet.getPort();
		info = AESUtils.encrypt("Java : " + info, key); //

		byte[] buffer2 = info.getBytes();
		packet2 = new DatagramPacket(buffer2, buffer2.length, address, port);
		socket.send(packet2);

		// 测试对端aes加密
		socket.receive(packet);
		info = new String(buffer, 0, packet.getLength());
		info = AESUtils.decrypt(info, key);
		System.out.println(info);

		socket.close();
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
