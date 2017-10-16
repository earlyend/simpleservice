package au.edu.unsw.soacourse.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class NoticeDao {
	
	public NoticeDao(){
		
	}
	
	@SuppressWarnings("unchecked")
	public static List<NoticeBean> createNotices(){
		Session session = HibernateHelper.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		Query licenseQuery = session.createSQLQuery("SELECT licensenumber, address, email FROM licenses WHERE expirydate < DATE_ADD(NOW(), INTERVAL +2 MONTH)");
		List<String> existingNotices = (List<String>) session.createQuery("select l.licenseNumber from NoticeBean l").list();
		List<Object[]> licenses = licenseQuery.list();
		List<NoticeBean> notices = new ArrayList<NoticeBean>();
		System.out.println(existingNotices.size());
		for(Object[] license : licenses){
			boolean existing = false;
			for(String existingNotice : existingNotices){
				if(license[0].toString().equals(existingNotice)){
					existing = true;
					break;
				}
			}
			if(existing){
				continue;
			}
			NoticeBean notice = new NoticeBean();
			notice.setLicenseNumber(license[0].toString());
			notice.setAddress(license[1].toString());
			notice.setEmail(license[2].toString());
			String token = createUniqueToken();
			notice.setToken(token);
			notice.setLocation("new");
			notice.setStatus("new");
			session.persist(notice);
			session.flush();
			notice.setLocation("http://licenserenewalservice-env.2qcm7emnen.ap-southeast-2.elasticbeanstalk.com/notices/"+notice.getToken()+"/"+notice.getNoticeId());
			session.merge(notice);
			notices.add(notice);
		}
		t.commit();
		session.close();
		return notices;
	}
	
	public static NoticeBean getNotice(String token, int id){
		Session session = HibernateHelper.getSessionFactory().openSession();
		NoticeBean notice = session.get(NoticeBean.class, id);
		session.close();
		return notice;
	}
	
	@SuppressWarnings("unchecked")
	public static List<NoticeBean> getAllNotices(){
		Session session = HibernateHelper.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		List<NoticeBean> notices = (List<NoticeBean>) session.createQuery("from NoticeBean").list();
		System.out.println(notices.size());
		t.commit();
		session.close();
		return notices;
	}
	
	public static NoticeBean updateNotice(int noticeId, String token, String email, String address, String status, String rejection){
		Session session = HibernateHelper.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		NoticeBean notice = session.get(NoticeBean.class, noticeId);
		if(!notice.getToken().equals(token)){
			System.out.println("wrong");
			session.close();
			return notice;
		}
		if(!email.isEmpty()){
			notice.setEmail(email);
		}
		if(!address.isEmpty()){
			notice.setAddress(address);
		}
		if(!status.isEmpty()){
			notice.setStatus(status);
		}
		if(!rejection.isEmpty()){
			notice.setRejectionReason(rejection);
		}
		session.merge(notice);
		t.commit();
		session.close();
		return notice;
	}
	
	
	public static boolean deleteNotice(int id, String token){
		Session session = HibernateHelper.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		NoticeBean notice = session.get(NoticeBean.class, id);
		if(!notice.getToken().equals(token)){
			return false;
		}
		List<PaymentBean> payments = (List<PaymentBean>) session.createQuery("select p from PaymentBean p where p.noticeId = :id")
				.setParameter("id", id).list();
		if(payments.size() != 0){
			PaymentBean payment = payments.get(0);
			session.delete(payment);
		}
		session.delete(notice);
		t.commit();
		session.close();
		return true;
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
		Query urlQuery = session.createQuery("select notice from NoticeBean notice where notice.token = :token").setParameter("token", token);
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
