package planit.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import planit.project.dto.ChangeUserDTO;
import planit.project.dto.LoggedResponseDTO;
import planit.project.dto.LoginDTO;
import planit.project.dto.RegisterDTO;
import planit.project.services.UserService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public ResponseEntity<?> addUser(@RequestBody RegisterDTO userDTO) {

		if (userService.registerUser(userDTO) == true) {
			return ResponseEntity.status(200).build();
		}

		return ResponseEntity.status(400).build();

	}

	@PostMapping("/login")
	public ResponseEntity<LoggedResponseDTO> checkUser(@RequestBody LoginDTO loginDTO) {

		LoggedResponseDTO loggedUserDTO = userService.loginUser(loginDTO);
		if (loggedUserDTO != null) {
			return new ResponseEntity<>(loggedUserDTO, HttpStatus.OK);
		}

		return ResponseEntity.status(400).build();

	}

	@PostMapping("/googleLogin")
	public ResponseEntity<?> googleLogin(@RequestBody RegisterDTO registerDTO) {

		if (userService.googleLogin(registerDTO) == true) {
			return ResponseEntity.status(200).build();
		}

		return ResponseEntity.status(400).build();

	}

	@PutMapping("/changeUser")
	public ResponseEntity<LoggedResponseDTO> changeUser(@RequestBody ChangeUserDTO changeUserDTO) {

		if (userService.changeUser(changeUserDTO) != false) {
			return ResponseEntity.status(200).build();
		}

		return ResponseEntity.status(400).build();

	}

}
