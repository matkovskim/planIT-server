package planit.project.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import planit.project.model.ApplicationUser;
import planit.project.model.Label;
import planit.project.repositories.LabelRepository;

@Service
public class LabelService {

	@Autowired
	private LabelRepository labelRepository;
	
	
	public List<Label> firstSync(ApplicationUser user) {
		List<Label> list = this.labelRepository.findLabelFirstTime(false, false, user, false);
		
		if(list!=null)
			return list;

		return new ArrayList<>();

	}

	public List<Label> syncByDate(ApplicationUser user, Date date) {
		
		List<Label> list = this.labelRepository.findLabelSync(date, user);
		if (list != null)
			return list;

		return new ArrayList<>();
	}
	
	public Label save(Label label) {
		return this.labelRepository.save(label);
	}
	

}
