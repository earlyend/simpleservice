package au.edu.unsw.soacourse.rest;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class LicenseDao {
	
	public static LicenseBean updateLicense(String licensenumber, String token, String address, String email, String expiryDate){
		Session session = HibernateHelper.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		LicenseBean license = session.get(LicenseBean.class, licensenumber);
		license.setEmail(email);
		license.setAddress(address);
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date tempDate;
		try {
			tempDate = format.parse(expiryDate);
			license.setExpiryDate(new Date(tempDate.getTime()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		session.merge(license);
		t.commit();
		session.close();
		return license;
	}
}
