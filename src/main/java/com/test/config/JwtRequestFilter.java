package com.test.config;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.test.entity.TbUser;
import com.test.repository.TbUserRepository;
import com.test.util.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private TbUserRepository tbUserRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String requestTokenHeader = request.getHeader("Authorization");
		if(requestTokenHeader!=null) {
			String username = null;
			String jwtToken = null;
			// JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
			if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
				jwtToken = requestTokenHeader.substring(7);
				try {
					username = jwtTokenUtil.getSubjectToken(jwtToken);
				} catch (IllegalArgumentException e) {
					throw new CustomException("Unable to get JWT Token");
				} catch (ExpiredJwtException e) {
					StringBuilder sb = new StringBuilder();
	                  sb.append("{ ");
	                  sb.append("\"error\": \"Unauthorized\" ");
	                  sb.append("\"message\": \"Unauthorized\"");
	                  sb.append("\"path\": \"")
	                    .append(request.getRequestURL())
	                    .append("\"");
	                  sb.append("} ");

	                  response.setContentType("application/json");
	                  response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  
	                  response.getWriter().write(sb.toString());
	                  return;
				}
			} else {
				throw new CustomException("Unknown JWT Token... (JWT Token must begin with Bearer String)");
			}
			
			// Once we get the token validate it.
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				Optional<TbUser> checkUser = tbUserRepository.findById(username);
				TbUser userDetails = new TbUser();
				if(checkUser.isPresent()) userDetails = checkUser.get();

				// if token is valid configure Spring Security to manually set
				// authentication
				if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, null);
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					// After setting the Authentication in the context, we specify
					// that the current user is authenticated. So it passes the
					// Spring Security Configurations successfully.
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}
		}else {
			SecurityContextHolder.getContext().setAuthentication(null);
		}
		filterChain.doFilter(request, response);
	}
	
}
