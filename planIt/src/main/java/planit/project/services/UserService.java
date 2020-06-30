package planit.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import planit.project.dto.ChangeUserDTO;
import planit.project.dto.LoggedResponseDTO;
import planit.project.dto.LoginDTO;
import planit.project.dto.RegisterDTO;
import planit.project.model.ApplicationUser;
import planit.project.repositories.ApplicationUserRepository;
import planit.project.utils.PasswordHash;

@Service
public class UserService {

	@Autowired
	private ApplicationUserRepository applicationUserRepository;

	public boolean registerUser(RegisterDTO userDTO) {
		ApplicationUser applicationUser = applicationUserRepository.findByEmail(userDTO.getEmail());
		if (applicationUser != null) {
			return false;
		}

		ApplicationUser newUser = new ApplicationUser(userDTO);
		String salt = PasswordHash.generateSalt();
		String password = PasswordHash.hashPassword(userDTO.getPassword(), salt);

		newUser.setSalt(salt);
		newUser.setPassword(password);

		if (applicationUserRepository.save(newUser) != null) {
			return true;
		}

		return false;
	}

	public LoggedResponseDTO loginUser(LoginDTO loginDTO) {

		ApplicationUser user = applicationUserRepository.findByEmail(loginDTO.getEmail());
		if (user == null) {
			return null;
		}

		String hashedPassword = PasswordHash.hashPassword(loginDTO.getPassword(), user.getSalt());
		if (hashedPassword.equals(user.getPassword())) {
			return new LoggedResponseDTO(user.getEmail(), user.getFirstName(), user.getLastName(), user.getColour(), user.getFirebaseId());
		}
		return null;
	}

	public boolean changeUser(ChangeUserDTO changeUserDTO) {
		ApplicationUser applicationUser = applicationUserRepository.findByEmail(changeUserDTO.getEmail());
		if (applicationUser == null) {
			return false;
		}
		applicationUser.setFirstName(changeUserDTO.getFirstName());
		applicationUser.setLastName(changeUserDTO.getLastName());
		ApplicationUser savedUser = applicationUserRepository.save(applicationUser);
		if (savedUser != null) {
			return true;
		}
		return false;
	}

	public boolean googleLogin(RegisterDTO userDTO) {
		ApplicationUser applicationUser = applicationUserRepository.findByEmail(userDTO.getEmail());
		if (applicationUser != null) {
			return true;
		}

		ApplicationUser newUser = new ApplicationUser(userDTO);

		if (applicationUserRepository.save(newUser) != null) {
			return true;
		}

		return false;
	}

}
