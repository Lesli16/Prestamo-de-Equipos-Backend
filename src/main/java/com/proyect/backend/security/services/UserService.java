package com.proyect.backend.security.services;

import com.proyect.backend.dtos.ForgotPasswordDto;
import com.proyect.backend.exceptions.GeneralException;
import com.proyect.backend.security.entities.User;
import com.proyect.backend.security.jwt.JwtProvider;
import com.proyect.backend.security.respositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;

    @Value("${reset.password.url}")
    private String resetPasswordUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public Optional<User> getByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public void updateUserInfo(User user){
        userRepository.save(user);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public User getUserDetails() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();
        return getByUserName(userName).get();
    }

    public void sendForgotPasswordEmail(String email) throws GeneralException, MessagingException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new GeneralException("No se encontró el email: " + email));

        String resetPasswordToken = jwtProvider.generateResetPasswordToken(email);

        user.setResetPasswordToken(resetPasswordToken);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        Context context = new Context();
        Map<String, Object> model = new HashMap<>();
        model.put("url", resetPasswordUrl + resetPasswordToken);
        context.setVariables(model);

        String htmlText = templateEngine.process("forgot-password", context);
        helper.setFrom(fromEmail);
        helper.setSubject("Restablecer contraseña");
        helper.setTo(email);
        helper.setText(htmlText, true);
        javaMailSender.send(message);
        userRepository.save(user);
    }

    public void resetPassword(ForgotPasswordDto forgotPasswordDto) throws GeneralException {
        String email = jwtProvider.getUserNameFromToken(forgotPasswordDto.getResetPasswordToken());
        User user = userRepository.findByEmail(email).orElseThrow(() -> new GeneralException("No se encontró el usuario con email: " + email));

        String encodedNewPassword = passwordEncoder.encode(forgotPasswordDto.getNewPassword());

        user.setResetPasswordToken(null);
        user.setPassword(encodedNewPassword);
        userRepository.save(user);
    }

    public boolean validateResetTokenPassword(String resetTokenPassword) {
        return jwtProvider.validateToken(resetTokenPassword);
    }

    public void changePassword(String newPassword) throws GeneralException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();
        User user = userRepository.findByUserName(userName).orElseThrow(()-> new GeneralException("No se encontró el nombre de usuario: " + userName));
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);
        userRepository.save(user);
    }
}
