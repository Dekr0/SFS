package com.ece.sfs;

import com.ece.sfs.access.AccessManager;
import com.ece.sfs.group.UserGroupManager;
import com.ece.sfs.core.Directory;
import com.ece.sfs.core.FileSystem;
import io.vavr.control.Either;
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
	private final GrantedAuthority adminAuthority;
	private final GrantedAuthority userAuthority;

	@Autowired
	SFSApplicationTests(
			AccessManager accessManager,
			UserGroupManager userGroupManager,
			Directory root,
			GrantedAuthority adminAuthority,
			GrantedAuthority userAuthority
	) {
		this.accessManager = accessManager;
		this.userGroupManager = userGroupManager;
		this.root = root;
		this.adminAuthority = adminAuthority;
		this.userAuthority = userAuthority;
	}

	@Test
	void contextLoads() {
		assertEquals("/", root.getName());
		assertEquals("Admin", adminAuthority.getAuthority());
		assertEquals("User", userAuthority.getAuthority());
	}

	@Test
	void testPrivilege() {
		User userA = new User("userA", "password", Collections.singletonList(adminAuthority));
		User userB = new User("userB", "password", Collections.singletonList(adminAuthority));
		User userC = new User("userC", "password", Collections.singletonList(userAuthority));

		userGroupManager.createGroup("Group 1", Collections.singletonList(adminAuthority));
		userGroupManager.createGroup("Group 2", Collections.singletonList(adminAuthority));

		userGroupManager.addUserToGroup(userA.getUsername(), "Users");
		userGroupManager.addUserToGroup(userB.getUsername(), "Users");
		userGroupManager.addUserToGroup(userA.getUsername(), "Group 1");
		userGroupManager.addUserToGroup(userB.getUsername(), "Group 1");
		userGroupManager.addUserToGroup(userC.getUsername(), "Group 2");

		FileSystem session1 = new FileSystem(accessManager, root);
		FileSystem session2 = new FileSystem(accessManager, root);

		session1.loginHome("Group 1", userA.getUsername());
		session2.loginHome("Group 1", userB.getUsername());

		Either<Boolean, String> r;

		r = session1.createComponent("a.txt", FileSystem.FileType.FILE);
		assertTrue(r.isLeft() && r.getLeft());
		r = session1.createComponent("b", FileSystem.FileType.DIR);
		assertTrue(r.isLeft() && r.getLeft());

		r = session1.changeDirectory("b");
		assertTrue(r.isLeft() && r.getLeft());
		assertEquals("/home/userA/b", session1.getCurrentPath());

		r = session1.changeDirectory("..");
		assertTrue(r.isLeft() && r.getLeft());
		assertEquals("/home/userA", session1.getCurrentPath());

		r = session1.changeDirectory("..");
		assertTrue(r.isLeft() && r.getLeft());
		assertEquals("/home", session1.getCurrentPath());

		r = session1.changeDirectory("..");
		assertTrue(r.isRight() &&
				r.get().compareTo("You do not have access rights to this directory") == 0);
		assertEquals("/home", session1.getCurrentPath());

		r = session1.changeDirectory("userA");
		assertTrue(r.isLeft() && r.getLeft());
		assertEquals("/home/userA", session1.getCurrentPath());


		r = session2.changeDirectory("..");
		assertTrue(r.isLeft() && r.getLeft());
		assertEquals("/home", session2.getCurrentPath());

		r = session2.changeDirectory("userA");
		assertTrue(r.isLeft() && r.getLeft());
		assertEquals("/home/userA", session2.getCurrentPath());

		r = session2.changeDirectory("b");
		assertTrue(r.isLeft() && r.getLeft());
		assertEquals("/home/userA/b", session2.getCurrentPath());

		r = session2.changeDirectory("..");
		assertTrue(r.isLeft() && r.getLeft());

		r = session2.renameComponent("a.txt", "c.txt");
		assertTrue(r.isRight() &&
				r.get().compareTo("You do not have permission to rename " + "a.txt") == 0);

		r = session2.deleteComponent("a.txt");
		assertTrue(r.isRight() &&
				r.get().compareTo("You do not have permission to delete this component") == 0);

		r = session2.createComponent("b", FileSystem.FileType.DIR);
		assertTrue(r.isRight() &&
				r.get().compareTo("Component " + "b" + " already exists") == 0);

		r = session2.createComponent("c.txt", FileSystem.FileType.FILE);
		assertTrue(r.isRight() &&
				r.get().compareTo("You do not have permission to create a component in this directory") == 0);

		session2.ls();

		r = session1.createComponent("c.txt", FileSystem.FileType.FILE);
		assertTrue(r.isLeft() && r.getLeft());

		session2.ls();
	}

	@Test
	void testCreateRemoveUserGroup() {
		assertDoesNotThrow(() -> userGroupManager.createGroup("Group 1", List.of(adminAuthority)));
		assertThrows(IllegalArgumentException.class, () -> userGroupManager.createGroup("Group 1", List.of(adminAuthority)));
		assertDoesNotThrow(() -> userGroupManager.deleteGroup("Group 1"));
		assertThrows(IllegalArgumentException.class, () -> userGroupManager.deleteGroup("Group 1"));

		assertDoesNotThrow(() -> userGroupManager.createGroup("Group 2", List.of(adminAuthority)));

		assertDoesNotThrow(() -> userGroupManager.renameGroup("Group 2", "Group 3"));

		assertThrows(IllegalArgumentException.class, () -> userGroupManager.renameGroup("Group 2", "Group 3"));
		assertThrows(IllegalArgumentException.class, () -> userGroupManager.createGroup("Group 3", new ArrayList<>()));
		assertThrows(IllegalArgumentException.class, () -> userGroupManager.deleteGroup("Group 2"));

		assertDoesNotThrow(() -> userGroupManager.createGroup("Group 2", new ArrayList<>()));
	}

	@Test
	void testAddRemoveAuthority() {
		assertDoesNotThrow(() -> userGroupManager.createGroup("Group 1", new ArrayList<>()));
		assertDoesNotThrow(() -> userGroupManager.addGroupAuthority("Group 1", adminAuthority));
		assertThrows(IllegalArgumentException.class, () -> userGroupManager.addGroupAuthority("Group 1", adminAuthority));
		assertDoesNotThrow(() -> userGroupManager.removeGroupAuthority("Group 1", adminAuthority));
		assertThrows(IllegalArgumentException.class, () -> userGroupManager.removeGroupAuthority("Group 1", adminAuthority));
	}

	@Test
	void testAddRemoveUser() {
		User userA = new User("User A", "password", new ArrayList<>(List.of(adminAuthority)));

		assertDoesNotThrow(() -> userGroupManager.createGroup("Group 1", new ArrayList<>(List.of(adminAuthority))));
		assertDoesNotThrow(() -> userGroupManager.addUserToGroup(userA.getUsername(), "Group 1"));

		assertThrows(IllegalArgumentException.class, () -> userGroupManager.addUserToGroup(userA.getUsername(), "Group 1"));
		assertDoesNotThrow(() -> userGroupManager.removeUserFromGroup(userA.getUsername(), "Group 1"));
		assertThrows(IllegalArgumentException.class, () -> userGroupManager.removeUserFromGroup(userA.getUsername(), "Group 1"));
	}
}
