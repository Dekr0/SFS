package com.ece.sfs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class SFSApplicationTests {

	private final Directory root;

	@Autowired
	SFSApplicationTests(Directory root) {
		this.root = root;
	}

	@Test
	void contextLoads() {
		assertEquals("/", root.getName());
	}

	@Test
	void testPrivilege() {

		GrantedAuthority INTERNAL = new SimpleGrantedAuthority("INTERNAL");

		User userA = new User(
				"User A",
				"password",
				Collections.singletonList(INTERNAL)
		);

		User userB = new User(
				"User B",
				"password",
				Collections.singletonList(INTERNAL)
		);

		FileSystem fsA = new FileSystem(root, userA.getUsername());
		FileSystem fsB = new FileSystem(root, userB.getUsername());

		assertEquals("/home/User A", fsA.getCurrentPath());
		assertEquals("/home/User B", fsB.getCurrentPath());
		assertTrue(fsA.changePrevDirectory());
		assertEquals("/home", fsA.getCurrentPath());
		assertFalse(fsA.changePrevDirectory());
		assertEquals("/home", fsA.getCurrentPath());
		assertFalse(fsA.changeDirectory("User B"));
		assertEquals("/home", fsA.getCurrentPath());

		assertTrue(fsB.changePrevDirectory());
		assertEquals("/home", fsB.getCurrentPath());
		assertFalse(fsB.changePrevDirectory());
		assertEquals("/home", fsB.getCurrentPath());
		assertFalse(fsB.changeDirectory("User A"));
		assertEquals("/home", fsB.getCurrentPath());

		assertTrue(fsA.changeDirectory("User A"));
		assertTrue(fsB.changeDirectory("User B"));
		assertEquals("/home/User A", fsA.getCurrentPath());
		assertEquals("/home/User B", fsB.getCurrentPath());

		assertTrue(fsA.createComponent("a.txt", FileSystem.FileType.FILE));
		assertTrue(fsA.createComponent("b", FileSystem.FileType.DIR));

		assertFalse(fsA.changeDirectory("a.txt"));
		assertTrue(fsA.changeDirectory("b"));
		assertEquals("/home/User A/b", fsA.getCurrentPath());
		assertTrue(fsA.changePrevDirectory());
		assertEquals("/home/User A", fsA.getCurrentPath());

		assertTrue(fsA.renameComponent("a.txt", "c.txt"));
		assertFalse(fsA.renameComponent("a.txt", "d.txt"));
		assertTrue(fsA.deleteComponent("b"));
		assertFalse(fsA.deleteComponent("b"));
	}

}
