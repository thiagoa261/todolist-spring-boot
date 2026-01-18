package com.thiagoaio.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.thiagoaio.todolist.user.IUserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, 
        HttpServletResponse response, 
        FilterChain filterChain
    ) throws ServletException, IOException {
        var servletPath = request.getServletPath();

        if (servletPath.contains("/tasks/")) {
            var authorization = request.getHeader("Authorization");
            byte[] authDecoded = Base64.getDecoder().decode(authorization.substring("Basic".length()).trim());

            String[] credentials = new String(authDecoded).split(":");
            String username = credentials[0];
            String password = credentials[1];

            var user = this.userRepository.findByUsername(username);
            if (user == null) response.sendError(401, "Erro ao autenticar usuário");
            
            var passValidation = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
            if (passValidation.verified) {
                request.setAttribute("userId", user.getId());
                filterChain.doFilter(request, response);
            } 
            else {
                response.sendError(401, "Erro ao autenticar usuário");
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
