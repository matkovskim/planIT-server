package planit.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import planit.project.dto.CreateTeamDTO;
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
	public ResponseEntity<?> createTeam(@RequestBody CreateTeamDTO createTeamDTO) {

		if (teamService.createTeam(createTeamDTO) == true) {
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

}
