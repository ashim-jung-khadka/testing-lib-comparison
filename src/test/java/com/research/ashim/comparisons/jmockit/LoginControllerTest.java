package com.research.ashim.comparisons.jmockit;

import com.research.ashim.LoginController;
import com.research.ashim.LoginDao;
import com.research.ashim.LoginService;
import com.research.ashim.UserForm;
import mockit.Expectations;
import mockit.FullVerifications;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class LoginControllerTest {

	@Injectable
	private LoginDao loginDao;

	@Injectable
	private LoginService loginService;

	@Tested
	private LoginController loginController;

	@Test
	public void assertThatNoMethodHasBeenCalled() {
		loginController.login(null);
		new FullVerifications(loginService) {
		};
	}

	@Test
	public void assertTwoMethodsHaveBeenCalled() {
		UserForm userForm = new UserForm();
		userForm.username = "foo";
		new Expectations() {{
			loginService.login(userForm);
			result = true;
			loginService.setCurrentUser("foo");
		}};

		String login = loginController.login(userForm);

		Assert.assertEquals("OK", login);
		new FullVerifications(loginService) {
		};
	}

	@Test
	public void assertOnlyOneMethodHasBeenCalled() {
		UserForm userForm = new UserForm();
		userForm.username = "foo";
		new Expectations() {{
			loginService.login(userForm);
			result = false;
			// no expectation for setCurrentUser
		}};

		String login = loginController.login(userForm);

		Assert.assertEquals("KO", login);
		new FullVerifications(loginService) {
		};
	}

	@Test
	public void mockExceptionThrowing() {
		UserForm userForm = new UserForm();
		new Expectations() {{
			loginService.login(userForm);
			result = new IllegalArgumentException();
			// no expectation for setCurrentUser
		}};

		String login = loginController.login(userForm);

		Assert.assertEquals("ERROR", login);
		new FullVerifications(loginService) {
		};
	}

	@Test
	public void mockAnObjectToPassAround(@Mocked UserForm userForm) {
		new Expectations() {{
			userForm.getUsername();
			result = "foo";
			loginService.login(userForm);
			result = true;
			loginService.setCurrentUser("foo");
		}};

		String login = loginController.login(userForm);

		Assert.assertEquals("OK", login);
		new FullVerifications(loginService) {
		};
		new FullVerifications(userForm) {
		};
	}

	@Test
	public void argumentMatching() {
		UserForm userForm = new UserForm();
		userForm.username = "foo";
		// default matcher
		new Expectations() {{
			loginService.login((UserForm) any);
			result = true;
			// complex matcher
			loginService.setCurrentUser(withArgThat(new BaseMatcher<String>() {
				@Override
				public boolean matches(Object item) {
					return item instanceof String && ((String) item).startsWith("foo");
				}

				@Override
				public void describeTo(Description description) {
					//NOOP
				}
			}));
		}};

		String login = loginController.login(userForm);

		Assert.assertEquals("OK", login);
		new FullVerifications(loginService) {
		};
	}

	@Test
	public void partialMocking() {
		// use partial mock
		LoginService partialLoginService = new LoginService();
		partialLoginService.setLoginDao(loginDao);
		loginController.loginService = partialLoginService;

		UserForm userForm = new UserForm();
		userForm.username = "foo";
		// let service's login use implementation so let's mock DAO call
		new Expectations() {{
			loginDao.login(userForm);
			result = 1;
			// no expectation for loginService.login
			partialLoginService.setCurrentUser("foo");
		}};

		String login = loginController.login(userForm);

		Assert.assertEquals("OK", login);
		// verify mocked call
		new FullVerifications(partialLoginService) {
		};
		new FullVerifications(loginDao) {
		};
	}
}