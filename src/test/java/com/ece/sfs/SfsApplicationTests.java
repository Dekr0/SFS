package com.ece.sfs;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
class SfsApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void test() {
		Directory root = new Directory("/", null, Calendar.getInstance().getTime());

		root.addComponent(
				new Directory(
						"home",
						root,
						Calendar.getInstance().getTime()
				)
		);

		FileSystem sys = new FileSystem(root, "User 1");

		assertEquals("Expected Directory", "/home/User 1", sys.getCurrentPath());

		sys.createComponent("test.txt", FileSystem.FileType.FILE);
		sys.createComponent("a", FileSystem.FileType.DIR);
		sys.changeDirectory("a");

		assertEquals("", "/home/User 1/a", sys.getCurrentPath());

		sys.changePrevDirectory();
		assertEquals("", "/home/User 1", sys.getCurrentPath());

		sys.renameComponent("a", "b");

		sys.changeDirectory("b");
		assertEquals("", "/home/User 1/b", sys.getCurrentPath());

		sys.changePrevDirectory();
		assertEquals("", "/home/User 1", sys.getCurrentPath());

		sys.deleteComponent("b");
		sys.changeDirectory("b");
		sys.changeDirectory("test.txt");
	}

}
