/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import LanguageDao.AddressDao;
import LanguageDao.PasswordProtection;
import LanguageDao.PdfBuilder;
import LanguageDao.TeacherDao;
import cultureLearning.cultureLearning.exception.DuplicateNid;
import cultureLearning.cultureLearning.exception.PasswordNotMatch;
import domain.Address;
import domain.Student;
import domain.Teacher;
import domain.User;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Mwiza
 */
@FacesValidator("nidValidator")
@ManagedBean(name="teacher")
public class TeacherModel implements Validator{
    private Teacher teacher=new Teacher();
    private TeacherDao tdao=new TeacherDao();
   private Address ad=new Address();
   private AddressDao ado=new AddressDao();
   private List<Address>addressList=ado.findAll(Address.class);
    private List<Teacher>teacherList=tdao.findAll(Teacher.class);
     private String addressCode;
     private String phone;
    
    
    public void createTeacher(){
        String hashedPasword=PasswordProtection.hashPassword(teacher.getPassword());
         teacher.setPassword(hashedPasword);
        
        if(tdao.getByEmail(teacher.getEmail())!=null){
            throw  new DuplicateNid("Imeri isanzwe ibamo,koresha iyindi");
        }
        else if(PasswordProtection.verifyPassword(teacher.getCpassword(), teacher.getPassword())){
            Address add=ado.findById(Address.class, addressCode);
            teacher.setAddress(add);
        tdao.create(teacher);
        FacesContext fc=FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Kongeramo mwarimu byagenze neza","Kongeramo mwarimu byagenze neza"));
        }
        
        else {
          throw new PasswordNotMatch("Ijambo ry'ibanga ntabwo risa,ongera ugerageze ");
        }}
    
    
    public void printReport() {
		try {
			List<Teacher> l = tdao.findAll(Teacher.class);
			PdfBuilder pdf = new PdfBuilder();
			pdf.addTitle("Lisiti y'abigisha bose");
			pdf.init(5);
			pdf.addTableHeader(new String[] { "amazina", "email", "telephoni", "irangamuntu", " Aho akomoka" });
			for (User user : l) {
				pdf.addTableCell(user.getFname() + " " + user.getLname());
				
				pdf.addTableCell(user.getEmail());
				pdf.addTableCell(user.getPhone());
				pdf.addTableCell(user.getNid());
                                if(user.getAddress()!=null)
				pdf.addTableCell(user.getAddress().getName());
                                else
                                    pdf.addTableCell("null");
				// pdf.addTableCell(user.getType());
			}
			pdf.download("Raporo y'Abalimu.pdf");
                        System.out.println("AA");
			FacesContext f=FacesContext.getCurrentInstance();
                        f.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"byagenze neza","byagenze neza"));
                        
		} catch (Exception e) {
			FacesContext f=FacesContext.getCurrentInstance();
                        f.addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,"","ntibyagenze neza"));
                        e.printStackTrace();
		}
	}
        
    public void deleteTeacher(Teacher teacher){
      this.tdao.delete(teacher);
      FacesContext fc=FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Gusiba mwarimu byagenze neza","Gusiba mwarimu byagenze neza"));
    }
    public String updateTeacher(Teacher teacher){
       this.teacher=teacher;
       FacesContext fc=FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Guhindura mwarimu byagenze neza","Guhindura mwarimu byagenze neza"));
        return "UpdateTeacher";
    }
    public String updateTeacher(){
       this.tdao.update(teacher);
       FacesContext fc=FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage("Guhindura mwarimu byagenze neza"));
        return "Teacherlist";
    }
    
//    public void validateMobilePhone(FacesContext context, UIComponent comp,
//			Object value) {
//
//		System.out.println("inside validate method");
//
//		String phone = (String) value;
//
//		if (phone.length() <= 13 &&phone.length()>=10) {
//			((UIInput) comp).setValid(false);
//
//			FacesMessage message = new FacesMessage(
//					"Uzuzamo byibura imibare 10");
//			context.addMessage(comp.getClientId(context), message);
//
//		}
//
//	}

     @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
       /// throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    String model = (String) value;

//		if (model.length() <= 13 && model.length() >=10) {
//			FacesMessage msg = new FacesMessage(
//					"Uzuzamo byibura imibare 10");
//			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
//
//			throw new ValidatorException(msg);
                        
                        if (model.isEmpty()) {
			FacesMessage msg = new FacesMessage(
					"Andika imibare y'irangamuntu");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                        

			throw new ValidatorException(msg);
		}
		if(model.length()!=16) {
                    FacesMessage msg = new FacesMessage(
					"irangamuntu ntiyuzure","irangamuntu igomba kugira imibare 16!");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
		}

    
  
  

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public TeacherDao getTdao() {
        return tdao;
    }

    public void setTdao(TeacherDao tdao) {
        this.tdao = tdao;
    }

    public List<Teacher> getTeacherList() {
        return teacherList;
    }

    public void setTeacherList(List<Teacher> teacherList) {
        this.teacherList = teacherList;
    }

    
    public Address getAd() {
        return ad;
    }

    public void setAd(Address ad) {
        this.ad = ad;
    }

    public AddressDao getAdo() {
        return ado;
    }

    public void setAdo(AddressDao ado) {
        this.ado = ado;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    public String getAddressCode() {
        return addressCode;
    }

    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

   

    }
    

