package au.edu.unsw.soacourse.rest;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class PaymentDao {
	
	public static PaymentBean createPayment(int id, String token, String amount, String date){
		Session session = HibernateHelper.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		NoticeBean notice = session.get(NoticeBean.class, id);
		if(notice.getNoticeId() != id && !notice.getToken().equals(token)){
			System.out.println("wrong");
			session.close();
			return new PaymentBean();
		}
		List<PaymentBean> existingPayments = session.createQuery("from PaymentBean").list();
		for(PaymentBean bean : existingPayments){
			if(bean.getNoticeId() == id){
				session.close();
				return new PaymentBean(); //Change to distinguish later
			}
		}
		PaymentBean payment = new PaymentBean();
		payment.setNoticeId(id);
		payment.setAmount(amount);
		payment.setLocation("http://localhost:8080/LicenseRenewalService/notices/payments/"+token+"/"+id);
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date tempDate;
		try {
			tempDate = format.parse(date);
			payment.setPaidDate(new Date(tempDate.getTime()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		session.persist(payment);
		t.commit();
		session.close();
		return payment;
	}
	
	public static PaymentBean getPayment(int id, String token){
		Session session = HibernateHelper.getSessionFactory().openSession();
		PaymentBean payment = (PaymentBean) session.createQuery("select p from PaymentBean p, NoticeBean n where p.noticeId=:id and n.token=:token")
				.setParameter("id", id).setParameter("token", token).uniqueResult();
		return payment;
	}
}
