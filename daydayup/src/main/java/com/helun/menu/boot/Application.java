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

import com.ava.lxx.common.encrypt.AESUtils;
import com.ava.lxx.common.encrypt.RSAUtils;
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
	
		String data = "75 108 62 209 155 232 72 39 133 252 22 27 204 89 128 93 230 255 178 123 236 144 107 152 55 117 73 72 100 141 236 84 79 48 166 105 62 234 69 7 167 214 208 186 177 86 186 134 37 135 230 153 242 187 20 10 96 80 239 78 147 205 65 150 222 206 7 134 245 186 40 110 18 135 116 135 239 166 186 28 202 61 195 81 232 247 146 102 46 120 41 57 129 122 197 87 206 69 54 5 97 98 33 190 65 102 132 234 42 121 225 54 33 64 107 100 68 191 135 176 253 79 66 159 213 114 10 20 91 192 26 78 249 208 55 13 190 127 118 26 157 220 4 5 121 205 24 91 48 148 183 249 100 20 171 231 159 160 85 141 151 146 249 252 207 226 196 144 109 51 4 117 227 148 5 105 169 79 237 38 64 142 138 213 24 103 13 93 46 125 79 108 103 15 83 157 51 49 230 183 245 171 243 42 5 72 131 15 224 96 91 223 141 28 58 217 134 149 105 230 129 199 206 3 6 243 240 78 74 67 231 150 90 195 119 221 224 212 238 169 199 2 1 229 60 246 97 178 32 193 251 222 191 206 111 164 156 109 172 231 123 244 155 221 49 53 108 205 232 208 32 215 17 138 77 210 228 197 28 80 9 174 230 188 207 201 221 187 9 81 235 170 190 159 47 131 12 127 198 254 169 177 39 57 215 91 146 161 59 130 9 0 117 43 31 7 120 72 131 189 220 254 133 204 239 67 74 136 128 82 180 11 57 76 29 155 1 38 237 186 168 108 197 186 88 112 24 95 235 149 246 232 41 116 137 184 96 232 59 118 91 193 247 204 74 84 0 129 187 246 153 179 175 214 188 38 204 221 2 153 98 193 128 80 7 78 36 150 68 167 108 126 2 104 183 0 180 174 85 5 81 145 53 45 87 102 14 109 50 250 85 148 36 95 178 216 198 110 164 167 195 9 105 188 185 242 161 152 136 231 187 220 12 178 46 93 116 110 3 24 192 207 234 108 197 168 187 233 65 146 227 70 59 96 80 94 216 68 50 225 101 25 196 234 192 71 203 71 100 111 25 105 122 167 234 221 199 169 208 190 162 43 178 44 65 155 203 127 227 208 225 56" ;
		StringBuffer buffer = new StringBuffer() ;
		for(String item : data.split(" ")) {
			char bye = (char) Integer.parseInt(item) ;
			buffer.append(String.valueOf(bye)) ;
		}
		System.out.println(buffer.toString());
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
