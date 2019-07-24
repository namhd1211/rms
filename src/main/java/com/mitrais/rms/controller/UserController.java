package com.mitrais.rms.controller;

import com.mitrais.rms.entity.User;
import com.mitrais.rms.repository.RoleRepository;
import com.mitrais.rms.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class UserController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        model.addAttribute("logout", "You have been logged out");
        return "redirect:/login";
    }

    @GetMapping("/user")
    public String listUser(Model model) {
        model.addAttribute("users", userService.getAllUser());
        return "userList";
    }

    @PostMapping("/admin/save")
    public String create(@Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleRepository.findAll());
            return "save";
        }
        if (userService.findByUserName(user.getUserName()) != null) {
            model.addAttribute("existed", "User account is existed");
            return "save";
        }
        userService.save(user);
        return "redirect:/user";
    }

    @GetMapping("/admin/save")
    public String create(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleRepository.findAll());
        return "save";
    }

    @GetMapping("/admin/edit/{userId}")
    public String editUser(@PathVariable Long userId, Model model) {
        User user = userService.findById(userId);
        if (user != null) {
            model.addAttribute("user", user);
        }
        return "edit";
    }

    @GetMapping("/admin/delete/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        userService.delete(userId);
        return "redirect:/user";
    }

    @GetMapping("/403")
    public String error() {
        return "403";
    }
}
