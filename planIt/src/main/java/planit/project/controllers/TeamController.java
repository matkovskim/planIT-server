package planit.project.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import planit.project.dto.TeamDTO;
import planit.project.dto.TeamMemberDTO;
import planit.project.dto.TeamMemberResponseDTO;
import planit.project.services.TeamService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping(value = "/team", produces = MediaType.APPLICATION_JSON_VALUE)
public class TeamController {

	@Autowired
	private TeamService teamService;

	@PostMapping("/create")
	public ResponseEntity<?> createTeam(@RequestBody TeamDTO createTeamDTO) {

		if (teamService.createTeam(createTeamDTO)) {
			return ResponseEntity.status(200).build();
		}

		return ResponseEntity.status(400).build();

	}

	@PostMapping("/addMember")
	public ResponseEntity<TeamMemberResponseDTO> addUser(@RequestBody TeamMemberDTO teamMemberDTO) {

		TeamMemberResponseDTO response = teamService.addMember(teamMemberDTO);
		if (response != null) {
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		return ResponseEntity.status(400).build();

	}

	@GetMapping("/checkMember")
	public ResponseEntity<TeamMemberResponseDTO> checkUser(@RequestParam String email) {
		TeamMemberResponseDTO response = teamService.checkMember(email);
		if (response != null) {
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		return ResponseEntity.status(400).build();
	}

	@DeleteMapping("/{teamId}")
	public ResponseEntity<?> deleteTeam(@PathVariable Integer teamId) {
		if (teamService.deleteTeam(teamId)) {
			return ResponseEntity.status(200).build();
		}
		return ResponseEntity.status(400).build();
	}
	
	@PutMapping("/{teamId}")
	public ResponseEntity<?> updateTeam(@RequestBody TeamDTO teamDTO, @PathVariable Integer teamId) {
		if(teamService.updateTeam(teamId, teamDTO)) {
			return ResponseEntity.status(200).build();
		}
		
		return ResponseEntity.status(400).build();
	}
	
	@PutMapping("/members/{teamId}")
	public ResponseEntity<?> updateTeamMembers(@RequestBody TeamDTO teamDTO, @PathVariable Integer teamId) {
		if(teamService.updateTeamMembers(teamId, teamDTO)) {
			return ResponseEntity.status(200).build();
		}
		return ResponseEntity.status(400).build();
	}
	

	
}
