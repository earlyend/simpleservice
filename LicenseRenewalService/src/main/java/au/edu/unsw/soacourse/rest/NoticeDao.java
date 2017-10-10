package au.edu.unsw.soacourse.rest;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class NoticeDao {
	
	public NoticeDao(){
		
	}
	
	public static NoticeBean createNotice(String licenseNumber, String address, String email){
		Session session = HibernateHelper.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		NoticeBean notice = new NoticeBean();
		notice.setLicenseNumber(licenseNumber);
		notice.setAddress(address);
		notice.setEmail(email);
		notice.setStatus("new");
		notice.setLocation("http://localhost:8080/LicenseRenewalService/notices/"+createUniqueToken());
		session.persist(notice);
		t.commit();
		session.close();
		return notice;
	}
	
	public NoticeBean getNotice(String noticeId){
		Session session = HibernateHelper.getSessionFactory().openSession();
		NoticeBean notice = session.get(NoticeBean.class, noticeId);
		session.close();
		return notice;
	}
	
	@SuppressWarnings("unchecked")
	public static List<NoticeBean> getAllNotices(){
		Session session = HibernateHelper.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		List<NoticeBean> notices = (List<NoticeBean>) session.createQuery("from NoticeBean").list();
		t.commit();
		session.close();
		return notices;
	}
	
	public NoticeBean updateNotice(String noticeId, Map<String, String> fields){
		Session session = HibernateHelper.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		NoticeBean notice = session.get(NoticeBean.class, noticeId);
		if(fields.containsKey("email")){
			notice.setEmail(fields.get("email"));
		}
		if(fields.containsKey("status")){
			notice.setEmail(fields.get("status"));
		}
		if(fields.containsKey("address")){
			notice.setEmail(fields.get("address"));
		}
		session.merge(notice);
		t.commit();
		session.close();
		return notice;
	}
	
	private static String generateToken() {
		char[] chars = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 10; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		String output = sb.toString();
		return output;

	}
	
	@SuppressWarnings("unchecked")
	private static String createUniqueToken() {
		Session session = HibernateHelper.getSessionFactory().openSession();
		Transaction tt = session.beginTransaction();
		String token = generateToken();
		Query urlQuery = session.createQuery("select notice from NoticeBean notice where notice.url = :token").setParameter("token", token);
		List<NoticeBean> notices = (List<NoticeBean>) urlQuery.list();
		while(!notices.isEmpty()) {
			token = generateToken();
			notices = (List<NoticeBean>) urlQuery.list();
		}
		tt.commit();
		session.close();
		return token;
	}
}
