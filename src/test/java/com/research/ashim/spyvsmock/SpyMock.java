package com.research.ashim.spyvsmock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SpyMock {

	@Mock
	private List<String> mockList;

	@Spy
	private List<String> spyList = new ArrayList<>();

	@Test
	public void testMockList() {
		//by default, calling the methods of mock object will do nothing
		mockList.add("test");
		assertNull(mockList.get(0));
	}

	@Test
	public void testSpyList() {
		//spy object will call the real method when not stub
		spyList.add("test");
		assertEquals("test", spyList.get(0));
	}

	@Test
	public void testMockWithStub() {
		//try stubbing a method
		String expected = "Mock 100";
		when(mockList.get(100)).thenReturn(expected);

		assertEquals(expected, mockList.get(100));
	}

	@Test
	public void testSpyWithStub() {
		//stubbing a spy method will result the same as the mock object
		String expected = "Spy 100";
		//take note of using doReturn instead of when
		doReturn(expected).when(spyList).get(100);

		assertEquals(expected, spyList.get(100));
	}
}