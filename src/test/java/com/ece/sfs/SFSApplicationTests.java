package com.ece.sfs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class SFSApplicationTests {

	private final AccessManager accessManager;
	private final UserGroupManager userGroupManager;
	private final Directory root;
	private final GrantedAuthority INTERNAL;
	private final GrantedAuthority EXTERNAL;

	@Autowired
	SFSApplicationTests(
			AccessManager accessManager,
			UserGroupManager userGroupManager,
			Directory root,
			GrantedAuthority INTERNAL,
			GrantedAuthority EXTERNAL
	) {
		this.accessManager = accessManager;
		this.userGroupManager = userGroupManager;
		this.root = root;
		this.INTERNAL = INTERNAL;
		this.EXTERNAL = EXTERNAL;
	}

	@Test
	void contextLoads() {
		assertEquals("/", root.getName());
		assertEquals("INTERNAL", INTERNAL.getAuthority());
		assertEquals("EXTERNAL", EXTERNAL.getAuthority());
	}

	@Test
	void testPrivilege() {
		User userA = new User("userA", "password", Collections.singletonList(INTERNAL));
		User userB = new User("userB", "password", Collections.singletonList(INTERNAL));
		User userC = new User("userC", "password", Collections.singletonList(EXTERNAL));

		userGroupManager.createGroup("Group 1", Collections.singletonList(INTERNAL));
		userGroupManager.createGroup("Group 2", Collections.singletonList(INTERNAL));

		userGroupManager.addUserToGroup(userA.getUsername(), "Users");
		userGroupManager.addUserToGroup(userB.getUsername(), "Users");
		userGroupManager.addUserToGroup(userA.getUsername(), "Group 1");
		userGroupManager.addUserToGroup(userB.getUsername(), "Group 1");
		userGroupManager.addUserToGroup(userC.getUsername(), "Group 2");

		FileSystem session1 = new FileSystem(accessManager, root);
		FileSystem session2 = new FileSystem(accessManager, root);

		session1.loginHome("Group 1", userA.getUsername());
		session2.loginHome("Group 1", userB.getUsername());

		assertTrue(session1.createComponent("a.txt", FileSystem.FileType.FILE));
		assertTrue(session1.createComponent("b", FileSystem.FileType.DIR));
		assertTrue(session1.changeDirectory("b"));
		assertTrue(session1.changeDirectory(".."));
		assertTrue(session1.changeDirectory(".."));
		assertFalse(session1.changeDirectory(".."));

		assertTrue(session2.changeDirectory(".."));
		assertTrue(session2.changeDirectory("userA"));
		assertTrue(session2.changeDirectory("b"));
		assertTrue(session2.changeDirectory(".."));
		assertFalse(session2.renameComponent("a.txt", "c.txt"));
		assertFalse(session2.deleteComponent("a.txt"));
		assertFalse(session2.createComponent("b", FileSystem.FileType.DIR));
		assertFalse(session2.createComponent("c.txt", FileSystem.FileType.FILE));
	}

	@Test
	void testCreateRemoveUserGroup() {
		assertDoesNotThrow(() -> userGroupManager.createGroup("Group 1", List.of(INTERNAL)));
		assertThrows(IllegalArgumentException.class, () -> userGroupManager.createGroup("Group 1", List.of(INTERNAL)));
		assertDoesNotThrow(() -> userGroupManager.deleteGroup("Group 1"));
		assertThrows(IllegalArgumentException.class, () -> userGroupManager.deleteGroup("Group 1"));

		assertDoesNotThrow(() -> userGroupManager.createGroup("Group 2", List.of(INTERNAL)));

		assertDoesNotThrow(() -> userGroupManager.renameGroup("Group 2", "Group 3"));

		assertThrows(IllegalArgumentException.class, () -> userGroupManager.renameGroup("Group 2", "Group 3"));
		assertThrows(IllegalArgumentException.class, () -> userGroupManager.createGroup("Group 3", new ArrayList<>()));
		assertThrows(IllegalArgumentException.class, () -> userGroupManager.deleteGroup("Group 2"));

		assertDoesNotThrow(() -> userGroupManager.createGroup("Group 2", new ArrayList<>()));
	}

	@Test
	void testAddRemoveAuthority() {
		assertDoesNotThrow(() -> userGroupManager.createGroup("Group 1", new ArrayList<>()));
		assertDoesNotThrow(() -> userGroupManager.addGroupAuthority("Group 1", INTERNAL));
		assertThrows(IllegalArgumentException.class, () -> userGroupManager.addGroupAuthority("Group 1", INTERNAL));
		assertDoesNotThrow(() -> userGroupManager.removeGroupAuthority("Group 1", INTERNAL));
		assertThrows(IllegalArgumentException.class, () -> userGroupManager.removeGroupAuthority("Group 1", INTERNAL));
	}

	@Test
	void testAddRemoveUser() {
		User userA = new User("User A", "password", new ArrayList<>(List.of(INTERNAL)));

		assertDoesNotThrow(() -> userGroupManager.createGroup("Group 1", new ArrayList<>(List.of(INTERNAL))));
		assertDoesNotThrow(() -> userGroupManager.addUserToGroup(userA.getUsername(), "Group 1"));

		assertThrows(IllegalArgumentException.class, () -> userGroupManager.addUserToGroup(userA.getUsername(), "Group 1"));
		assertDoesNotThrow(() -> userGroupManager.removeUserFromGroup(userA.getUsername(), "Group 1"));
		assertThrows(IllegalArgumentException.class, () -> userGroupManager.removeUserFromGroup(userA.getUsername(), "Group 1"));
	}
}
