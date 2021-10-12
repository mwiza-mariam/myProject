/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import LanguageDao.AddressDao;
import LanguageDao.AdminDao;
import LanguageDao.AuthenticationService;
import LanguageDao.CourseDao;
import LanguageDao.NotificationDao;
import LanguageDao.PasswordProtection;
import LanguageDao.TranslationDao;
import LanguageDao.UserDao;

import cultureLearning.cultureLearning.exception.InvalidMailException;
import domain.Address;
import domain.Admin;
import domain.Student;
import domain.Teacher;
import domain.User;
import encryptionClass.Bcrypt;

import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mwiza
 */
@ManagedBean
public class UserModel {
    
	private UserDao userDao=new UserDao();
        private User user=new User();
        private Admin admin=new Admin();
	private AdminDao adao=new AdminDao();
	private AddressDao address=new AddressDao();
	
	
	private Address add=new Address();
	private String translationText = "Igisubizo";
	private String translationResult = "Igisubizo";
	
	private TranslationDao translator=new TranslationDao();
	
	private NotificationDao notifier=new NotificationDao();
	@Temporal(TemporalType.DATE)
	private Date date = new Date();

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	private Bcrypt bcrypt;
	private String code;
	private String password;
	private String npassword;
	private String oldPassword;
	private String cpassword;
	private String email;
	private String salt;
	private List<User> listofUsers;
	private List<Address> listofCountries=address.findAll(Address.class);
	private String addresscode;
	private List<User> listofTeacher;
FacesContext fc=FacesContext.getCurrentInstance();


	
	private CourseDao courseService=new CourseDao();

	


    @PostConstruct
    public void init(){
        User loggedInUser = (User) AuthenticationService.getSession().getAttribute("s_user");
        user.setEmail(loggedInUser.getEmail());
    }
    public String addUser() {
		try {
			System.out.println(add.getCode());
			Address ad = address.findById(Address.class,addresscode);

			admin.setAddress(ad);
			String passwordHashed = PasswordProtection.hashPassword(admin.getPassword());
			admin.setPassword(passwordHashed);
			adao.create(admin);

			 
                         fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Murakaza neza","Gufungura konti byagenze neza!!"));
                        
			return "/Login.xhtml";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
                        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Mwihangane","Gufungura konti nti byagenze neza!!"));
			e.getMessage();
		}
                return "";
	}

	//@Command("login")
	//@NotifyChange("listofUsers")
	public String loginfunction() {
		try {

			if (user.getEmail().length() > 0 && user.getPassword().length() > 0) {

				User sessionUser = userDao.userExist(user.getEmail(), user.getPassword());
                                        
				if (null != sessionUser) {
					//Session.getCurrent().setAttribute("s_user", sessionUser);
					if (sessionUser instanceof Admin) {
						 fc.addMessage(null, new FacesMessage("Murakaza neza!!"));
						return "/index.xhtml";
					} else if (sessionUser instanceof Student) {
						 fc.addMessage(null, new FacesMessage("Murakaza neza!!"));
						return("/Learner.xhtml");
					}

					else if (sessionUser instanceof Teacher) {
						 fc.addMessage(null, new FacesMessage("Murakaza Neza!!"));
						return("/TeacherDashboard.xhtml");
					} else {
                                             fc.addMessage(null, new FacesMessage("Mwihangane ntabwo tubashije kumenya uwo uriwe!"));
						
						return("User.xhtml");
					}

				} else {
                                     fc.addMessage(null, new FacesMessage("imeri cyangwa ijambobanga byanditse nabi!"));
					
				}
			} else {
                               fc.addMessage(null, new FacesMessage("Wanditse nabi imeri yawe!"));
				

			}
		} catch (InvalidMailException e) {
                   
			e.getMessage();
                        
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
                         fc.addMessage(null, new FacesMessage("Internal Error!"));
			
		}
                return "";
	}


	public void changepassword() {
		try {
			userDao.updatePassword(user.getEmail(), oldPassword, npassword, cpassword);
			
			fc.addMessage(null, new FacesMessage("Byagenze neza!"));
		} catch (Exception e) {
			// TODO: handle exception
			
			fc.addMessage(null, new FacesMessage("guhindura password ntibyagenze neza!mmwongere mugerageza" + e.getMessage()));
                        e.printStackTrace();
		}

	}


	public void forgotpassword() {
		try {
			User u = userDao.getByEmail(getEmail());
                        System.out.println(u);
			if (u == null) {
                            fc.addMessage(null, new FacesMessage("Iyo imeri ntibaho!mushyiremo iyindi "));
				
				return;
			}
			String password = getAlphaNumericString(6);
			String passwordHashed = PasswordProtection.hashPassword(password);

			u.setPassword(passwordHashed);
			userDao.update(u);
			notifier.sendEmail(u.getEmail(), "Reset Password", "Ijambobanga ryawe niri: " + password);
			
                         fc.addMessage(null, new FacesMessage("byagenze neza "));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
                        fc.addMessage(null, new FacesMessage(" ntibyagenze neza!mmwongere mugerageza" + e.getMessage()));
		}
	}

	String getAlphaNumericString(int n) {
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
		StringBuilder sb = new StringBuilder(n);

		for (int i = 0; i < n; i++) {
			int index = (int) (AlphaNumericString.length() * Math.random());
			sb.append(AlphaNumericString.charAt(index));
		}

		return sb.toString();
	}

	
//	public String resetLearnerPassword(String email) {
//		try {
//			userDao.getByEmail(email);
//			String randomPassword = RandomStringUtils.randomAlphanumeric(10);
//			user.setPassword(randomPassword);
//			userDao.update(user);
//			
//			fc.addMessage(null, new FacesMessage("ijambo banga ryawe rwahinduwe !reba imeri yawe." ));
//                        return randomPassword;
//
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.getStackTrace();
//			return "guhindura ijambobanga ntibyakunze mwongere mugerageza";
//
//		}
//
//	}

//	@Command("search")
//	@NotifyChange("listofUsers")
	public void search() {
		try {
			userDao.search(user);
			/* Clients.showNotification("found "); */
			System.out.println(email);
		} catch (Exception e) {
			// TODO: handle exception
			 fc.addMessage(null, new FacesMessage(" ntibyagenze neza!mmwongere mugerageza" + e.getMessage()));
			e.printStackTrace();
		}
	}

	//@Command("logout")
	public String Logout() {
		// service.logout();
		System.out.println("Logout");
		return "index.xhtml";
	}

	
	public void updateUser( ) {
		try {
			userDao.update(user);
			 fc.addMessage(null, new FacesMessage(" Ivugurura Ryagenze neza" ));
		} catch (Exception e) {
			// TODO: handle exception
			 fc.addMessage(null, new FacesMessage(" Ivugurura ntiryagenze neza!mmwongere mugerageza" + e.getMessage()));
			e.printStackTrace();
		}

	}

	//@Command("learner")
	public void displayLearner() {
		userDao.listOfLearner();
	}

	//@Command("delete")
	//@NotifyChange("listofUsers")
	public void deleteUser() {
		try {

			userDao.delete(user);
			 fc.addMessage(null, new FacesMessage(" gusiba Byagenze neza" ));
		} catch (Exception e) {
			// TODO: handle exception
                     fc.addMessage(null, new FacesMessage(" Gusiba ntibyagenze neza!mmwongere mugerageza" + e.getMessage()));
			
		}

	}


	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	

	public Bcrypt getBcrypt() {
		return bcrypt;
	}

	

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getTranslationText() {
		return translationText;
	}

	public void setTranslationResult(String translationResult) {
		this.translationResult = translationResult;
	}

	public void setBcrypt(Bcrypt bcrypt) {
		this.bcrypt = bcrypt;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public String getCpassword() {
		return cpassword;
	}

	public void setCpassword(String cpassword) {
		this.cpassword = cpassword;
	}

	public String getNpassword() {
		return npassword;
	}

	public void setNpassword(String npassword) {
		this.npassword = npassword;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public void setTranslationText(String text) {
		this.translationText = text;
	}

	public String getTranslationResult() {
		return translationResult;
	}

	
//	public void translate() {
//		System.out.println("Translating");
//		if (translationText == null || translationText == "")
//			return;
//		System.out.println("Translating");
//		try {
//			this.translationResult = translator.translateKinyarwandaToEnglish(translationText);
//		} catch (Exception e) {
//			e.printStackTrace();
//			this.translationResult = "Ntibishoboye kuboneka";
//		}
//	}

	
        
//
//	@Command("printReport")
//	public void printReportTeacher() {
//		try {
//			List<User> l = service.listOfTeacher();
//			PdfBuilder pdf = new PdfBuilder();
//			pdf.addTitle("Lisiti y'abigisha  bose");
//			pdf.init(5);
//			pdf.addTableHeader(new String[] { "amazina", "imeri","telephone", "irangamuntu", " Igihugu Akomokamo" });
//			for (User user : l) {
//				pdf.addTableCell(user.getFname() + " " + user.getLname());
//				// pdf.addTableCell(user.getLname());
//				pdf.addTableCell(user.getEmail());
//				pdf.addTableCell(user.getPhone());
//				pdf.addTableCell(user.getNid());
//				pdf.addTableCell(user.getAddress().getName());
//				// pdf.addTableCell(user.getType());
//			}
//			pdf.run();
//			Clients.showNotification("byagenze neza");
//		} catch (Exception e) {
//			Clients.showNotification(e.getMessage());
//		}
//	}
//
//	@Command("printButtonAll")
//	public void printReportAllUsers() {
//		try {
//			List<User> l = service.findAllUser();
//			PdfBuilder pdf = new PdfBuilder();
//			pdf.addTitle("Lisiti y'abakoresha urubuga bose");
//			pdf.init(6);
//			pdf.addTableHeader(new String[] { "izina rya mbere", "izina rya nyuma", "email", "irangamuntu", " Address",
//					"ubwoko bwa abakoresha" });
//			for (User user : l) {
//				pdf.addTableCell(user.getFname());
//				pdf.addTableCell(user.getLname());
//				pdf.addTableCell(user.getEmail());
//				pdf.addTableCell(user.getNid());
//				pdf.addTableCell(user.getAddress().getName());
//				// pdf.addTableCell(user.getType());
//			}
//			pdf.run();
//			Clients.showNotification("byagenze neza");
//		} catch (Exception e) {
//			Clients.showNotification(e.getMessage());
//		}
//	}
//
//	
//
//	

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public AddressDao getAddress() {
        return address;
    }

    public void setAddress(AddressDao address) {
        this.address = address;
    }

    public Address getAdd() {
        return add;
    }

    public void setAdd(Address add) {
        this.add = add;
    }

    public TranslationDao getTranslator() {
        return translator;
    }

    public void setTranslator(TranslationDao translator) {
        this.translator = translator;
    }

    public NotificationDao getNotifier() {
        return notifier;
    }

    public void setNotifier(NotificationDao notifier) {
        this.notifier = notifier;
    }

    public List<User> getListofUsers() {
        return listofUsers;
    }

    public void setListofUsers(List<User> listofUsers) {
        this.listofUsers = listofUsers;
    }

    public List<Address> getListofCountries() {
        return listofCountries;
    }

    public void setListofCountries(List<Address> listofCountries) {
        this.listofCountries = listofCountries;
    }

    

    public List<User> getListofTeacher() {
        return listofTeacher;
    }

    public void setListofTeacher(List<User> listofTeacher) {
        this.listofTeacher = listofTeacher;
    }

    public CourseDao getCourseService() {
        return courseService;
    }

    public void setCourseService(CourseDao courseService) {
        this.courseService = courseService;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public AdminDao getAdao() {
        return adao;
    }

    public void setAdao(AdminDao adao) {
        this.adao = adao;
    }

    public String getAddresscode() {
        return addresscode;
    }

    public void setAddresscode(String addresscode) {
        this.addresscode = addresscode;
    }

    

    
	
}


