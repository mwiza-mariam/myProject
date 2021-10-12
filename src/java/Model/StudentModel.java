/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import LanguageDao.AddressDao;
import LanguageDao.PasswordProtection;
import LanguageDao.PdfBuilder;
import LanguageDao.StudentDao;
import cultureLearning.cultureLearning.exception.DuplicateNid;
import cultureLearning.cultureLearning.exception.PasswordNotMatch;
import domain.Address;
import domain.Student;
import domain.User;
import encryptionClass.Bcrypt;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;


/**
 *
 * @author Mwiza
 */@ManagedBean(name="student")
 @SessionScoped
public class StudentModel {
   
     private Student student=new Student();
     private StudentDao sdao=new StudentDao();
     private List<Student>listStudent=sdao.findAll(Student.class);
     private Address addr=new Address();
     private  AddressDao addDao=new AddressDao();
    
     private List<Address> listCountry=addDao.findAll(Address.class);
     private String addressCode;

     private Bcrypt bcrypt=new Bcrypt();
     private PasswordProtection ps=new PasswordProtection();
     
     public void createStudent(){
         String hashedPasword=PasswordProtection.hashPassword(student.getPassword());
        
        student.setPassword(hashedPasword);
        if (sdao.getByEmail(student.getEmail())!=null) {
			throw new DuplicateNid("Imeri isanzwe ibamo,koresha iyindi");
		}
        else if(PasswordProtection.verifyPassword(student.getCpassword(), student.getPassword())) {
         Address ad=addDao.findById(Address.class, addressCode);
         
        student.setAddress(ad);
        
         sdao.create(student);
          FacesContext fc=FacesContext.getCurrentInstance();
         fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Murakoze gufungura konti","bagenze neza !"));
		
        
        }
        else {
			throw new PasswordNotMatch("Ijambo ry'ibanga ntabwo risa,ongera ugerageze ");
		}
        
     }
     public void deleteStudent(Student student){
         this.sdao.delete(student);
          FacesContext fc=FacesContext.getCurrentInstance();
         fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Yasibitse","Yasibitse  !"));
     }
     public void updateStudent(Student student){
         this.student=student;
          FacesContext fc=FacesContext.getCurrentInstance();
         fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Byagenze neza","Yasibitse  !"));
     }
     public void updateStudent(){
         this.sdao.update(student);
          FacesContext fc=FacesContext.getCurrentInstance();
         fc.addMessage(null, new FacesMessage("Byavuguruwe neza  !"));
     }
     
   

    public Address getAddr() {
        return addr;
    }

    public void setAddr(Address addr) {
        this.addr = addr;
    }

    public AddressDao getAddDao() {
        return addDao;
    }

    public void setAddDao(AddressDao addDao) {
        this.addDao = addDao;
    }
     
     
     
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public StudentDao getSdao() {
        return sdao;
    }

    public void setSdao(StudentDao sdao) {
        this.sdao = sdao;
    }

    public List<Student> getListStudent() {
        System.out.println("Student Size: "+listStudent.size());
        return listStudent;
    }

    public void setListStudent(List<Student> listStudent) {
        this.listStudent = listStudent;
    }

    public List<Address> getListCountry() {
        return listCountry;
    }

    public void setListCountry(List<Address> listCountry) {
        this.listCountry = listCountry;
    }

    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }

    public String getAddressCode() {
        return addressCode;
    }

    public Bcrypt getBcrypt() {
        return bcrypt;
    }

    public void setBcrypt(Bcrypt bcrypt) {
        this.bcrypt = bcrypt;
    }

    public PasswordProtection getPs() {
        return ps;
    }

    public void setPs(PasswordProtection ps) {
        this.ps = ps;
    }

	public void printReport() {
		try {
			List<Student> l = sdao.findAll(Student.class);
			PdfBuilder pdf = new PdfBuilder();
			pdf.addTitle("Lisiti y'abiga bose");
			pdf.init(5);
			pdf.addTableHeader(new String[] { "amazina", "email", "telephoni", "irangamuntu", " Aho akomoka" });
			for (User user : l) {
				pdf.addTableCell(user.getFname() + " " + user.getLname());
				// pdf.addTableCell(user.getLname());
				pdf.addTableCell(user.getEmail());
				pdf.addTableCell(user.getPhone());
				pdf.addTableCell(user.getNid());
                                if(user.getAddress()!=null)
				pdf.addTableCell(user.getAddress().getName());
                                else
                                    pdf.addTableCell("null");
				// pdf.addTableCell(user.getType());
			}
			pdf.download("Report.pdf");
                        System.out.println("AA");
			FacesContext f=FacesContext.getCurrentInstance();
                        f.addMessage(null, new FacesMessage("byagenze neza"));
                        
		} catch (Exception e) {
			FacesContext f=FacesContext.getCurrentInstance();
                        f.addMessage(null, new FacesMessage(" ntibyagenze neza"));
                        e.printStackTrace();
		}
	}
        
        
        
    
}
