package app.controller.auth;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import app.security.jwt.AuthResponse;
import app.security.jwt.LoginRequest;
import app.security.jwt.UserLoginService;
import app.security.jwt.AuthResponse.Status;

@RestController
@RequestMapping("/api/auth")
public class LoginRestController {

	@Autowired
	private UserLoginService userService;
	@Operation(summary = "Login")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Login successful", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content) })
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(
			@CookieValue(name = "accessToken", required = false) String accessToken,
			@CookieValue(name = "refreshToken", required = false) String refreshToken,
			@RequestBody LoginRequest loginRequest) {
		
		return userService.login(loginRequest, accessToken, refreshToken);
	}
	@Operation(summary = "Logout")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Logout successful", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content)})
	@PostMapping("/logout")
	public ResponseEntity<AuthResponse> logOut(HttpServletRequest request, HttpServletResponse response) {

		return ResponseEntity.ok(new AuthResponse(Status.SUCCESS, userService.logout(request, response)));
	}
}