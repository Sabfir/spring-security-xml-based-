package ua.opinta.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {
	public static final Logger logger = LoggerFactory.getLogger(MainController.class);

	@RequestMapping(value = { "/", "/main" }, method = RequestMethod.GET)
	public String mainPage(ModelMap model) {
		System.out.println("main page");

		model.addAttribute("message", "Welcome to the Spring security (xml)!");

		return "main";
	}

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public String userPage(ModelMap model) {
		System.out.println("user");

		model.addAttribute("user", getAuthenticatedUserName());
		
		return "user";
	}

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String adminPage(ModelMap model) {
		System.out.println("admin");
		
		model.addAttribute("user", getAuthenticatedUserName());
		
		return "admin";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}

		/*
		 * return "redirect:/login?logout";//You can redirect wherever you want,
		 * but generally it's a good idea to show login screen again.
		 */
		return "login";
	}

	@RequestMapping(value = "/login")
	public ModelAndView login(@RequestParam(value = "error", required = false) String error) {
		System.out.println("login");
		ModelAndView model = new ModelAndView();
		if (error != null) {
			System.out.println("login error");

			model.addObject("error", "Invalid username or password!");
		}

		model.setViewName("login");

		return model;
	}
	
	@RequestMapping(value="/accessDenied", method=RequestMethod.GET)
	public ModelAndView accessDenied(Principal user) {
		ModelAndView model = new ModelAndView();
		
		if (user != null) {
			model.addObject("errorMessage", user.getName() + " you have no rights to this page!");
		} else {
			model.addObject("errorMessage", "You have no rights to this page!");
		}
		
		model.setViewName("accessDenied");
		
		return model;
	}
	
	private String getAuthenticatedUserName() {
		Object authenticatedUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (authenticatedUser instanceof String) {
			return (String) authenticatedUser;
		} else {
			return ((User) authenticatedUser).getUsername();
		}
	}
}
