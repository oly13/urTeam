package urteam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import urteam.community.Community;
import urteam.community.CommunityRepository;
import urteam.event.Event;
import urteam.event.EventRepository;
import urteam.user.User;
import urteam.user.UserRepository;

@Controller
public class urteamController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private EventRepository eventRepo;

	@Autowired
	private CommunityRepository communityRepo;

	@PostConstruct
	public void init() throws ParseException {

		for (int i = 0; i < 10; i++) {
			String name = "Usuario" + i;
			String surname = "apellido" + i;
			String nickname = "user" + surname.substring(1, 3) + i;
			String password = "123456";
			String email = name + surname + i + "@urteam.com";
			String bio = "Lorem ipsum dolor sit amet, " + "consectetur adipiscing elit, "
					+ "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. "
					+ "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris "
					+ "nisi ut aliquip ex ea commodo consequat. "
					+ "Duis aute irure dolor in reprehenderit in voluptate velit "
					+ "esse cillum dolore eu fugiat nulla pariatur. "
					+ "Excepteur sint occaecat cupidatat non proident, "
					+ "sunt in culpa qui officia deserunt mollit anim id est laborum. "
					+ "Lorem ipsum dolor sit amet, consectetur adipiscing elit, "
					+ "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
			String score = "9999";
			String city = "Madrid";
			String country = "España";
			userRepo.save(new User(name, surname, nickname, password, email, bio, score, city, country));

		}

		for (int i = 0; i < 10; i++) {
			String name = String.valueOf(i);
			String sport = String.valueOf(i);
			double price = i;
			String info = String.valueOf(i);
			String place = String.valueOf(i);

			Date start_date = new SimpleDateFormat("dd/MM/yyyy").parse("02/11/2017");
			Date end_date = new SimpleDateFormat("dd/MM/yyyy").parse("02/11/2017");

			Calendar cal = toCalendar(start_date);

			Event event = new Event(name, sport, price, info, place, start_date, end_date);
			event.setDay_date(cal.get(Calendar.DAY_OF_MONTH));
			event.setMonth_date(cal.get(Calendar.MONTH));
			event.setYear_date(cal.get(Calendar.YEAR));

			eventRepo.save(event);
		}

		for (int i = 0; i < 10; i++) {
			String name = String.valueOf(i);
			String info = String.valueOf(i);
			communityRepo.save(new Community(name, info, "Running"));
		}
	}

	@RequestMapping("/")
	public String index(Model model) {

		return "index";
	}

	

	@RequestMapping("image/upload")
	public String uploadImageFile(Model model, @RequestParam("file") MultipartFile file, String action) {

		String fileName = "test.jpeg";

		if (!file.isEmpty()) {
			try {
				File filesFolder = new File("imgs");
				if (!filesFolder.exists()) {
					filesFolder.mkdirs();
				}
				File uploadedFile = new File(filesFolder.getAbsolutePath(), fileName);
				file.transferTo(uploadedFile);

				return "index";

			} catch (Exception e) {
				model.addAttribute("fileName", fileName);
				model.addAttribute("error", e.getClass().getName() + ":" + e.getMessage());
				return "index";

			}
		} else {
			model.addAttribute("error", "The file is empty");
			return "index";
		}

	}

	@RequestMapping("/image/{fileName}")
	public void handleFileDownload(@PathVariable String fileName, HttpServletResponse res)
			throws FileNotFoundException, IOException {

		File file = new File("imgs", fileName + ".jpg");

		if (file.exists()) {
			res.setContentType("image/jpeg");
			res.setContentLength(new Long(file.length()).intValue());
			FileCopyUtils.copy(new FileInputStream(file), res.getOutputStream());
		} else {
			res.sendError(404, "File" + fileName + "(" + file.getAbsolutePath() + ") does not exist");
		}
	}

	private static Calendar toCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}
}
