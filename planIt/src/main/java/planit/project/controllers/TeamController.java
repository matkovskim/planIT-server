package planit.project.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

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
import planit.project.dto.TeamSyncDTO;
import planit.project.model.ApplicationUser;
import planit.project.model.Team;
import planit.project.model.TeamUserConnection;
import planit.project.model.UserMessage;
import planit.project.services.ChatService;
import planit.project.services.TeamService;
import planit.project.services.UserService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping(value = "/team", produces = MediaType.APPLICATION_JSON_VALUE)
public class TeamController {

	@Autowired
	private TeamService teamService;

	@Autowired
	private UserService userService;

	@Autowired
	private ChatService chatService;

	private DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	@PostMapping("/create")
	public ResponseEntity<Integer> createTeam(@RequestBody TeamDTO createTeamDTO) {

		Integer id = teamService.createTeam(createTeamDTO);
		if (id != null) {
			return new ResponseEntity<>(id, HttpStatus.OK);
		}

		return ResponseEntity.status(400).build();

	}

	@PostMapping("/addMember")
	public ResponseEntity<TeamMemberResponseDTO> addUser(@RequestBody TeamMemberDTO teamMemberDTO) {

		Team team = this.teamService.findByIdAndDeleted((long) teamMemberDTO.getId());

		if (team == null)
			return ResponseEntity.status(400).build();

		ApplicationUser user = this.userService.findByEmail(teamMemberDTO.getEmail());

		if (user == null)
			return ResponseEntity.status(400).build();

		if (teamService.addMember(team, user)) {
			return ResponseEntity.ok(new TeamMemberResponseDTO(user.getEmail(), user.getFirstName(), user.getLastName(),
					user.getColour()));
		}

		return ResponseEntity.status(400).build();

	}

	@GetMapping("/checkMember")
	public ResponseEntity<TeamMemberResponseDTO> checkUser(@RequestParam String email) {

		ApplicationUser user = this.userService.findByEmail(email);

		// TODO: change to 404?
		if (user == null)
			return ResponseEntity.status(400).build();

		return ResponseEntity.ok(
				new TeamMemberResponseDTO(user.getEmail(), user.getFirstName(), user.getLastName(), user.getColour()));
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
		if (teamService.updateTeam(teamId, teamDTO)) {
			return ResponseEntity.status(200).build();
		}

		return ResponseEntity.status(400).build();
	}

	@PutMapping("/members/{teamId}")
	public ResponseEntity<?> updateTeamMembers(@RequestBody TeamDTO teamDTO, @PathVariable Integer teamId) {
		if (teamService.updateTeamMembers(teamId, teamDTO)) {
			return ResponseEntity.status(200).build();
		}
		return ResponseEntity.status(400).build();
	}

	@GetMapping("/sync")
	public ResponseEntity<?> synchronizationData(@RequestParam String email,
			@RequestParam(value = "date", required = false) Long date) {

		System.out.println(email);
		System.out.println(date);

		ApplicationUser user = null;

		if (email != null) {
			user = this.userService.findByEmail(email);
			if (user == null)
				return ResponseEntity.badRequest().build();
		}

		Date dateUserSync = null;
		TeamSyncDTO dto = new TeamSyncDTO();
		// if user sync date exists
		if (date != null) {

			dateUserSync = new Date(date);
			dto.setMessages(this.chatService.syncDateConn(user, dateUserSync));
			dto.setTeams(this.teamService.syncDate(user, dateUserSync));
			dto.setTeamUserConnections(this.teamService.syncDateConn(user, dateUserSync));
			dto.setUsers(new HashSet<>());
			for (TeamUserConnection conn : dto.getTeamUserConnections()) {
				if(!dto.getUsers().contains(conn.getUser()))
					dto.getUsers().add(conn.getUser());
			}

			System.out.println(dto);
			return ResponseEntity.ok(dto);

		}

		dto.setMessages(this.chatService.firstSyncConn(user));
		dto.setTeams(this.teamService.firstSync(user));
		dto.setTeamUserConnections(this.teamService.firstSyncConn(user));
		dto.setUsers(new HashSet<>());
		for (TeamUserConnection conn : dto.getTeamUserConnections()) {
			if(!dto.getUsers().contains(conn.getUser()))
				dto.getUsers().add(conn.getUser());
		}

		System.out.println(dto);
		return ResponseEntity.ok(dto);
	}

	@GetMapping("/init")
	public ResponseEntity<?> init() {
		ApplicationUser user = this.userService.findByEmail("vesnam@gmail.com");
		ApplicationUser maja = this.userService.findByEmail("maja@gmail.com");

		Team team = new Team("Team 1", "Opis", user);
		team = this.teamService.save(team);

		this.teamService.addMember(team, user);
		this.teamService.addMember(team, maja);

		UserMessage message = new UserMessage("CAO", (new Date()).getTime(), maja, team);
		this.chatService.save(message);

		return ResponseEntity.status(200).build();

	}

}
