package com.helun.menu.performance;

import java.io.IOException;

public class TestApplication {
	public static void main(String[] args) throws IOException {
		MergeSETaskBOBuilder mergeSETaskBOBuilder = new MergeSETaskBOBuilder();
		mergeSETaskBOBuilder.setVOClass(TestVO.class);
		mergeSETaskBOBuilder.addParams("date", "this is data").addParams("files", null).addParams("testUseTime", 100L)
				.addParams("id", null).addParams("type", 10);
		System.out.println(mergeSETaskBOBuilder.build());
	}
}
