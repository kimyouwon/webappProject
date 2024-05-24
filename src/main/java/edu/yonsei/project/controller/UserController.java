package edu.yonsei.project.controller;

import edu.yonsei.project.dto.UserDto;
import edu.yonsei.project.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/home/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "createacc_page";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @PostMapping("/home/register")
    public String registerUser(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "createacc_page";
        }
        try {
            userService.postUser(userDto);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "이미 사용중인 로그인 ID입니다. 다른 ID를 선택해 주세요.");
            return "createacc_page";
        }
        return "redirect:/home/login";
    }
}
