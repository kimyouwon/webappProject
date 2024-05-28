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
import java.util.regex.Pattern;

@Controller
@RequiredArgsConstructor
public class RegisterController {
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

        // 비밀번호 검증
        Pattern passwordPattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,15}$");
        if (!passwordPattern.matcher(userDto.getPassword()).matches()) {
            model.addAttribute("error", "비밀번호는 8자 이상 15자 이하의 영문, 숫자, 특수문자를 포함해야 합니다.");
            return "createacc_page";
        }

        // 닉네임 검증
        Pattern nicknamePattern = Pattern.compile("^[가-힣a-zA-Z0-9]{3,10}$");
        if (!nicknamePattern.matcher(userDto.getNickname()).matches()) {
            model.addAttribute("error", "닉네임은 영문/한글, 숫자로 이루어져야 하며 3자 이상 10자 이하이어야 합니다.");
            return "createacc_page";
        }

        // DB 중복 체크
        if (userService.existsByLoginId(userDto.getLoginId())) {
            model.addAttribute("errorLoginId", "이미 사용중인 로그인 ID입니다. 다른 ID를 선택해 주세요.");
            return "createacc_page";
        }
        if (userService.existsByNickname(userDto.getNickname())) {
            model.addAttribute("errorNickname", "이미 사용중인 닉네임입니다. 다른 닉네임을 선택해 주세요.");
            return "createacc_page";
        }

        try {
            userService.postUser(userDto);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "회원가입 중 오류가 발생했습니다.");
            return "createacc_page";
        }

        return "redirect:/home/login";
    }

}
