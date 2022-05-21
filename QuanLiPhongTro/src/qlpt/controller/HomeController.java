package qlpt.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import qlpt.entity.HoaDonEntity;
import qlpt.entity.NhaTroEntity;
import qlpt.entity.PhongEntity;

@Transactional
@Controller
@RequestMapping("home/")
public class HomeController {
	@Autowired
	SessionFactory factory;
	
	public String mact;
	public int slPhongDaThue=0;
	public int slPhongTrong=0;
	
	@RequestMapping("/index")
	public String index(ModelMap model,HttpSession ss) {
		mact = ss.getAttribute("mact").toString();
		List<PhongEntity> dsPhong=getDSPhong();
		List<PhongEntity> dsPhongTrong=getDSPhongTrong();
		List<HoaDonEntity> dsHoaDon=getDSHoaDonChuaThuTien();
		slPhongDaThue=0;
		slPhongTrong=0;
		thongKeSLPhongTheoTrangThai(dsPhong);
		model.addAttribute("slPhongDaThue", slPhongDaThue);
		model.addAttribute("slPhongTrong", slPhongTrong);
		model.addAttribute("dsPhongTrong", dsPhongTrong);
		model.addAttribute("dsHoaDon", dsHoaDon);
		return "home/index";
	}
	public List<NhaTroEntity> getDSNhaTro() {
		Session session = factory.getCurrentSession();
		String hql = "FROM NhaTroEntity WHERE MACT = :mact";
		Query query = session.createQuery(hql);
		query.setParameter("mact", mact);
		List<NhaTroEntity> list = query.list();
		return list;
	}
	
	public List<PhongEntity> getDSPhong() {
		Session session = factory.getCurrentSession();
		String hql = "FROM PhongEntity where nhatro.chuTro.MACT= :MACT";
		Query query = session.createQuery(hql);
		query.setParameter("MACT", mact);
		List<PhongEntity> list = query.list();
		return list;
	}
	public List<PhongEntity> getDSPhongTrong() {
		Session session = factory.getCurrentSession();
		String hql = "FROM PhongEntity where nhatro.chuTro.MACT= :MACT and trangThai.MATT = 1";
		Query query = session.createQuery(hql);
		query.setParameter("MACT", mact);
		List<PhongEntity> list = query.list();
		return list;
	}
	
	public List<HoaDonEntity> getDSHoaDonChuaThuTien() {
		Session session = factory.getCurrentSession();
		String hql = "FROM HoaDonEntity where TRANGTHAI=0 and hopDong.phong.nhatro.chuTro.MACT= :MACT";
		Query query = session.createQuery(hql);
		query.setParameter("MACT", mact);
		List<HoaDonEntity> list = query.list();
		return list;
	}
	
	public void thongKeSLPhongTheoTrangThai(List<PhongEntity> dsPhong) {
		for(PhongEntity p:dsPhong) {
			if(p.getTrangThai().getMATT()==1) {
				slPhongTrong+=1;
			}
			else {
				slPhongDaThue+=1;
			}
		}
	}
	
}
